import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * An element is transferred from sender to receiver only when [send] and [receive]
 * invocations meet in time (rendezvous), so [send] suspends until another coroutine
 * invokes [receive] and [receive] suspends until another coroutine invokes [send].
 */
class SynchronousQueue<E> {
    private val dummy = Node<E>(null, false)
    private val head: AtomicRef<Node<E>> = atomic(dummy)
    private val tail: AtomicRef<Node<E>> = atomic(dummy)

    /**
     * Sends the specified [element] to this channel, suspending if there is no waiting
     * [receive] invocation on this channel.
     */
    suspend fun send(element: E) {
        while (true) {
            val h = head.value
            val t = tail.value
            val n = Node(e = element, isSender = true)

            if (t == h || t.isSender) {
                if (saw(t, n)) {
                    return
                }
            } else {
                val hn = h.next.value ?: continue
                if (head.compareAndSet(h, hn)) {
                    hn.e = element
                    hn.cont!!.resume(true)
                    return
                }
            }
        }
    }

    /**
     * Retrieves and removes an element from this channel if there is a waiting [send] invocation on it,
     * suspends the caller if this channel is empty.
     */
    suspend fun receive(): E {
        while (true) {
            val h = head.value
            val t = tail.value

            if (h == t || !t.isSender) {
                val n = Node<E>(e = null, isSender = false)
                if (saw(t, n)) {
                    return n.e!!
                }
            } else {
                val hn = h.next.value ?: continue
                if (head.compareAndSet(h, hn)) {
                    hn.cont!!.resume(true)
                    return hn.e!!
                }
            }
        }
    }

    private suspend fun saw(t: Node<E>, n: Node<E>): Boolean {
        return suspendCoroutine { c ->
            n.cont = c
            if (t.next.compareAndSet(null, n)) {
                tail.compareAndSet(t, n)
            } else {
                tail.compareAndSet(t, t.next.value!!)
                c.resume(false)
            }
        }
    }
}

private class Node<E>(var e: E?, val isSender: Boolean) {
    val next = atomic<Node<E>?>(null)
    var cont: Continuation<Boolean>? = null
}
