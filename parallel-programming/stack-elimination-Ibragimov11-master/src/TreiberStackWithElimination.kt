package mpp.stackWithElimination

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlin.random.Random

class TreiberStackWithElimination<E> {
    private val top = atomic<Node<E>?>(null)
    private val eliminationArray = atomicArrayOfNulls<Any?>(ELIMINATION_ARRAY_SIZE)

    /**
     * Adds the specified element [x] to the stack.
     */
    fun push(x: E) {
        val i = randomIndex()
        val cell = eliminationArray[i]
        val v = cell.value

        if (v == null && cell.compareAndSet(null, x)) {
            repeat(ATTEMPTS_NUMBER) {
                if (cell.compareAndSet(DONE, null)) {
                    return@push
                }
            }

            if (!cell.compareAndSet(x, null)) {
                return
            }
        }

        while (true) {
            val node = Node(x, top.value)
            if (top.compareAndSet(node.next, node))
                return
        }
    }

    /**
     * Retrieves the first element from the stack
     * and returns it; returns `null` if the stack
     * is empty.
     */
    @Suppress("UNCHECKED_CAST")
    fun pop(): E? {
        val i = randomIndex()
        val cell = eliminationArray[i]

        repeat(ATTEMPTS_NUMBER) {
            val v = cell.value
            if (v != null && v != DONE && cell.compareAndSet(v, DONE)) {
                return v as E
            }

        }

        while (true) {
            val node = top.value ?: return null
            if (top.compareAndSet(node, node.next))
                return node.x
        }
    }

    companion object {
        private object DONE
    }
}

private fun randomIndex(max: Int = ELIMINATION_ARRAY_SIZE) = Random.nextInt(max)

private class Node<E>(val x: E, val next: Node<E>?)

private const val ELIMINATION_ARRAY_SIZE = 2 // DO NOT CHANGE IT
private const val ATTEMPTS_NUMBER = 8
