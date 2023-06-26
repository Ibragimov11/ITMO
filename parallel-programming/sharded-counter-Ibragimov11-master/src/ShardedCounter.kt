package mpp.counter

import kotlinx.atomicfu.AtomicIntArray
import kotlin.random.Random

class ShardedCounter {
    private val counters = AtomicIntArray(ARRAY_SIZE)

    /**
     * Atomically increments by one the current value of the counter.
     */
    fun inc() {
        val i = randomIndex()
        counters[i].incrementAndGet()
    }

    /**
     * Returns the current counter value.
     */
    fun get(): Int {
        var result = 0
        for (i in 0 until counters.size) {
            result += counters[i].value
        }

        return result
    }
}

private const val ARRAY_SIZE = 2 // DO NOT CHANGE ME

private fun randomIndex() = Random.nextInt(ARRAY_SIZE)
