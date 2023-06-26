import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.loop

class AtomicArrayNoAba<E>(size: Int, initialValue: E) {
    private val a = Array(size) { Ref(initialValue) }

    fun get(index: Int) =
        a[index].value!!

    fun cas(index: Int, expected: E, update: E) =
        a[index].compareAndSet(expected, update)

    @Suppress("UNCHECKED_CAST")
    fun cas2(
        index1: Int, expected1: E, update1: E,
        index2: Int, expected2: E, update2: E,
    ): Boolean = when {
        index1 > index2 -> {
            cas2(index2, expected2, update2, index1, expected1, update1)
        }

        index1 == index2 -> {
            if (expected1 == expected2) {
                cas(index2, expected2, (update2 as Int + 1) as E)
            } else false
        }

        else -> {
            val descriptor = CASNDescriptor(
                a[index1], expected1, update1,
                a[index2], expected2, update2,
            )
            if (a[index1].compareAndSet(expected1, descriptor)) {
                descriptor.complete()
            } else false
        }
    }
}

private enum class Outcome {
    UNDECIDED, SUCCESS, FAIL
}

private abstract class Descriptor {
    abstract fun complete(): Boolean
}

private class CASNDescriptor<A, B>(
    private val a: Ref<A>, private val expectA: A, private val updateA: A,
    private val b: Ref<B>, private val expectB: B, private val updateB: B,
    private val outcome: Ref<Outcome> = Ref(Outcome.UNDECIDED),
) : Descriptor() {
    override fun complete(): Boolean {
        casOutcome(if (b.compareAndSet(expectB, this)) Outcome.SUCCESS else Outcome.FAIL)

        return if (outcome.v.value == Outcome.SUCCESS) {
            a.v.compareAndSet(this, updateA)
            b.v.compareAndSet(this, updateB)
            true
        } else {
            a.v.compareAndSet(this, expectA)
            b.v.compareAndSet(this, expectB)
            false
        }
    }

    private fun casOutcome(newOutcome: Outcome) = outcome.v.compareAndSet(Outcome.UNDECIDED, newOutcome)
}

class Ref<T>(initialValue: T) {
    val v = atomic<Any?>(initialValue)

    var value: T
        get() {
            v.loop { cur ->
                @Suppress("UNCHECKED_CAST")
                when (cur) {
                    is Descriptor -> cur.complete()
                    else -> return cur as T
                }
            }
        }
        set(upd) {
            v.loop { cur ->
                when (cur) {
                    is Descriptor -> cur.complete()
                    else -> if (v.compareAndSet(cur, upd))
                        return
                }
            }
        }

    fun compareAndSet(expect: Any?, update: Any?): Boolean {
        while (true) {
            if (v.compareAndSet(expect, update)) return true

            val vValue = v.value
            when {
                vValue != expect -> return false
                vValue is Descriptor ->
                    if (vValue == update) return true
                    else vValue.complete()
            }
        }
    }
}
