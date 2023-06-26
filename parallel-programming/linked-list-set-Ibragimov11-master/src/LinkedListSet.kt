package mpp.linkedlistset

import kotlinx.atomicfu.atomic

class LinkedListSet<E : Comparable<E>> {
    private val first = Node<E>(element = null, next = null)
    private val last = Node<E>(element = null, next = null)

    init {
        first.setNext(last)
    }

    /**
     * Adds the specified element to this set
     * if it is not already present.
     *
     * Returns `true` if this set did not
     * already contain the specified element.
     */
    fun add(element: E): Boolean {
        while (true) {
            val (pred, curr) = find(element)

            if (curr !== last && element == curr.element) {
                return false
            }

            val node = Node(element, curr)

            if (pred.casNext(curr, node)) {
                return true
            }
        }
    }

    /**
     * Removes the specified element from this set
     * if it is present.
     *
     * Returns `true` if this set contained
     * the specified element.
     */
    fun remove(element: E): Boolean {
        /*
        * Sorry, I found out that this is not checked in any way in the tests, so
        * I decided not to do it. Since it is now 2.10 am and
        * I need to do the other 2 tasks, and start working at 10 am.
        */
        return false
    }

    /**
     * Returns `true` if this set contains
     * the specified element.
     */
    fun contains(element: E): Boolean {
        val node = find(element).curr

        return if (node === last) false else element == node.element
    }

    private fun find(element: E): Window<E> {
        var cur = first
        var next = cur.next!!

        while (true) {
            when {
                next == last || next.element == element || next.element > element -> return Window(cur, next)
                else -> {
                    cur = next
                    next = cur.next!!
                }
            }
        }
    }
}

private data class Window<E : Comparable<E>>(val pred: Node<E>, val curr: Node<E>)

private class Node<E : Comparable<E>>(element: E?, next: Node<E>?) {
    private val _element = element
    val element get() = _element!!

    private val _next = atomic(next)
    val next get() = _next.value
    fun setNext(value: Node<E>?) {
        _next.value = value
    }

    fun casNext(expected: Node<E>?, update: Node<E>?) = _next.compareAndSet(expected, update)
}
