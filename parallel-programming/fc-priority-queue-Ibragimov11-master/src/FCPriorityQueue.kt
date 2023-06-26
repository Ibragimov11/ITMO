import java.util.*
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlin.random.Random

class FCPriorityQueue<E : Comparable<E>> {
    private val q = PriorityQueue<E>()
    private val fcArray = atomicArrayOfNulls<Op<E>?>(size)
    private val locked = atomic(false)

    private sealed interface Op<E>

    class Poll<E> : Op<E>
    class Peek<E> : Op<E>
    class Add<E>(val e: E) : Op<E>
    class Done<E>(val result: E? = null) : Op<E>

    private fun change(j: Int) {
        if (fcArray[j].value != null) {
            when (val v = fcArray[j].value) {
                is Add -> {
                    q.add(v.e)
                    fcArray[j].value = Done()
                }

                is Peek -> {
                    fcArray[j].value = Done(q.peek())
                }

                is Poll -> {
                    fcArray[j].value = Done(q.poll())
                }

                else -> {}
            }
        }
    }

    private fun operation(op: Op<E>): E? {
        var i: Int
        do {
            i = randomIndex()
        } while (!fcArray[i].compareAndSet(null, op))

        while (true) {
            if (locked.compareAndSet(expect = false, update = true)) {
                for (j in 0 until fcArray.size) {
                    change(j)
                }

                locked.value = false
                break
            } else if (fcArray[i].value is Done) {
                break
            }
        }

        val v = fcArray[i].value as Done
        val result = v.result
        fcArray[i].value = null

        return result
    }

    /**
     * Retrieves the element with the highest priority
     * and returns it as the result of this function;
     * returns `null` if the queue is empty.
     */
    fun poll(): E? = operation(Poll())

    /**
     * Returns the element with the highest priority
     * or `null` if the queue is empty.
     */
    fun peek(): E? = operation(Peek())

    /**
     * Adds the specified element to the queue.
     */
    fun add(element: E) {
        operation(Add(element))
    }

    companion object {
        val size = 2 * 2 * Runtime.getRuntime().availableProcessors()

        fun randomIndex() = Random.nextInt(size)
    }
}
