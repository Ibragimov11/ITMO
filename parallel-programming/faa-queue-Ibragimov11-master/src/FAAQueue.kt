package mpp.faaqueue

import kotlinx.atomicfu.*

class FAAQueue<E> {
    private val head: AtomicRef<Segment> // Head pointer, similarly to the Michael-Scott queue (but the first node is _not_ sentinel)
    private val tail: AtomicRef<Segment> // Tail pointer, similarly to the Michael-Scott queue

    init {
        val firstNode = Segment()
        head = atomic(firstNode)
        tail = atomic(firstNode)
    }

    /**
     * Adds the specified element [element] to the queue.
     */
    fun enqueue(element: E) {
        while (true) {
            val curTail = tail.value
            val i = curTail.enqIdx.getAndIncrement()

            if (i >= SEGMENT_SIZE) {
                val next = Segment().apply {
                    put(0, element)
                }

                if (curTail.next.compareAndSet(null, next)) {
                    tail.compareAndSet(curTail, next)
                    return
                } else {
                    tail.compareAndSet(curTail, curTail.next.value!!)
                }
            } else if (curTail.cas(i, null, element)) {
                return
            }
        }
    }

    /**
     * Retrieves the first element from the queue and returns it;
     * returns `null` if the queue is empty.
     */
    @Suppress("UNCHECKED_CAST")
    fun dequeue(): E? {
        while (true) {
            val curHead = head.value
            val i = curHead.deqIdx.getAndIncrement()

            if (i >= SEGMENT_SIZE) {
                val next = curHead.next.value ?: return null
                head.compareAndSet(curHead, next)
            } else if (!curHead.cas(i, null, BROKEN)) {
                return curHead.get(i) as E?
            }
        }
    }

    /**
     * Returns `true` if this queue is empty, or `false` otherwise.
     */
    val isEmpty: Boolean
        get() {
            while (true) {
                val curHead = head.value
                val deqIdx = curHead.deqIdx.value
                val enqIdx = curHead.enqIdx.value

                if (!(deqIdx < enqIdx && deqIdx < SEGMENT_SIZE)) {
                    val next = curHead.next.value ?: return true
                    head.compareAndSet(curHead, next)
                } else {
                    return false
                }
            }
        }

    companion object {
        object BROKEN
    }
}

private class Segment {
    val next: AtomicRef<Segment?> = atomic(null)
    val elements = atomicArrayOfNulls<Any>(SEGMENT_SIZE)

    val enqIdx = atomic(0)
    val deqIdx = atomic(0)

    fun get(i: Int) = elements[i].value
    fun cas(i: Int, expect: Any?, update: Any?) = elements[i].compareAndSet(expect, update)
    fun put(i: Int, value: Any?) {
        elements[i].value = value
    }
}

const val SEGMENT_SIZE = 2 // DO NOT CHANGE, IMPORTANT FOR TESTS
