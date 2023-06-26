package mpp.dynamicarray

import kotlinx.atomicfu.*

interface DynamicArray<E> {
    /**
     * Returns the element located in the cell [index],
     * or throws [IllegalArgumentException] if [index]
     * exceeds the [size] of this array.
     */
    fun get(index: Int): E

    /**
     * Puts the specified [element] into the cell [index],
     * or throws [IllegalArgumentException] if [index]
     * exceeds the [size] of this array.
     */
    fun put(index: Int, element: E)

    /**
     * Adds the specified [element] to this array
     * increasing its [size].
     */
    fun pushBack(element: E)

    /**
     * Returns the current size of this array,
     * it increases with [pushBack] invocations.
     */
    val size: Int
}

class DynamicArrayImpl<E> : DynamicArray<E> {
    private val core = atomic(Core<E>(INITIAL_CAPACITY))

    override val size: Int
        get() = core.value.size

    override fun get(index: Int): E = core.value.get(index)

    override fun put(index: Int, element: E) = core.value.put(index, element)

    override fun pushBack(element: E) {
        while (true) {
            val core1 = core.value
            var s = core1.size
            val capacity = core1.capacity

            while (s < capacity) {
                val successFlag = core1.compareAndSet(s, null, Value(element))
                core1.casSize(s, s + 1)
                if (successFlag) return
                s++
            }

            val core2 = Core<E>(capacity * 2, s + 1).apply {
                compareAndSet(s, null, Value(element))
            }

            if (core1.next.compareAndSet(null, core2)) {
                move(core1, core2, s)
                return
            } else {
                move(core1, core1.next.value!!, s)
            }
        }
    }

    private fun move(core1: Core<E>, core2: Core<E>, s: Int) {
        repeat(s) { i ->
            while (true) {
                when (val e = core1.array[i].value) {
                    is Moved -> {
                        core2.compareAndSet(i, null, Value(e.e))
                        break
                    }
                    is Value -> {
                        if (core1.compareAndSet(i, e, Moved(e.e))) {
                            core2.compareAndSet(i, null, e)
                            break
                        }
                    }

                    else -> {}
                }
            }
        }

        core.compareAndSet(core1, core2)
    }
}

private sealed interface Cell<E>
private data class Moved<E>(val e: E) : Cell<E>
private data class Value<E>(val e: E) : Cell<E>

private class Core<E>(val capacity: Int, size: Int = 0) {
    val array = atomicArrayOfNulls<Cell<E>?>(capacity)
    private val _size = atomic(size)
    val next = atomic<Core<E>?>(null)

    val size: Int
        get() = _size.value

    @Suppress("UNCHECKED_CAST")
    fun get(index: Int): E {
        require(index < size)
        var cur = this
        var prev: E? = null

        while (true) {
            when (val elem = cur.array[index].value) {
                is Moved -> {
                    prev = elem.e
                    cur = cur.next.value!!
                }

                is Value -> return elem.e

                null -> {
                    require(prev != null)
                    return prev
                }
            }
        }
    }

    fun put(i: Int, element: E) {
        require(i < size)
        var core = this

        while (true) {
            val curElem = core.array[i].value
            when {
                curElem is Moved -> core = core.next.value!!
                core.compareAndSet(i, curElem, Value(element)) -> return
            }
        }
    }

    fun compareAndSet(index: Int, expect: Cell<E>?, update: Cell<E>?): Boolean =
        array[index].compareAndSet(expect, update)

    fun casSize(expect: Int, update: Int) = _size.compareAndSet(expect, update)
}

private const val INITIAL_CAPACITY = 1 // DO NOT CHANGE ME
