package dijkstra

import java.util.*
import java.util.concurrent.Phaser
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.random.Random

private val NODE_DISTANCE_COMPARATOR = Comparator<Node> { o1, o2 -> Integer.compare(o1!!.distance, o2!!.distance) }

fun shortestPathParallel(start: Node) {
    val workers = Runtime.getRuntime().availableProcessors()
    start.distance = 0
    val q = MultiQueue(workers, NODE_DISTANCE_COMPARATOR)
    q.add(start)
    val onFinish = Phaser(workers + 1)
    val active = AtomicInteger(1)
    repeat(workers) {
        thread {
            while (active.get() > 0) {
                val node: Node = q.poll() ?: continue

                for (edge in node.outgoingEdges) {
                    while (true) {
                        val new = node.distance + edge.weight
                        val old = edge.to.distance

                        if (old <= new) break

                        if (edge.to.casDistance(old, new)) {
                            q.add(edge.to)
                            active.incrementAndGet()

                            break
                        }
                    }
                }
                active.decrementAndGet()
            }
            onFinish.arrive()
        }
    }
    onFinish.arriveAndAwaitAdvance()
}

class MultiQueue(t: Int, comparator: Comparator<Node>) {
    private val queueNumber = 4 * t
    private val queues = Array<PriorityQueueWithLock<Node>>(queueNumber) {
        PriorityQueueWithLock(
            PriorityQueue(comparator)
        )
    }

    private fun randomIndex(): Int = Random.nextInt(queueNumber)

    fun add(node: Node) {
        while (true) {
            val q = queues[randomIndex()]
            if (!q.tryLock()) continue
            q.add(node)
            q.unlock()
            return
        }
    }

    fun poll(): Node? {
        while (true) {
            val q1 = queues[randomIndex()]
            val q2 = queues[randomIndex()]
            val t1 = q1.top()
            val t2 = q2.top()

            val q = when {
                t1 == null && t2 == null -> return@poll null
                t1 == null -> q2
                t2 == null || NODE_DISTANCE_COMPARATOR.compare(t1, t2) < 0 -> q1
                else -> q2
            }

            if (!q.tryLock()) continue

            val node = q.poll()
            q.unlock()

            return node
        }
    }

    data class PriorityQueueWithLock<E>(
        val queue: PriorityQueue<E>,
    ) {
        private val lock: ReentrantLock = ReentrantLock(true)

        fun tryLock() = lock.tryLock()
        fun unlock() = lock.unlock()

        fun top(): E? = queue.peek()
        fun add(e: E) = queue.add(e)
        fun poll(): E? = queue.poll()
    }
}
