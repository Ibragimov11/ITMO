/**
 * Время из трех целых чисел.
 */
data class Time(val h: Int, val m: Int, val s: Int) : Comparable<Time> {
    override fun compareTo(other: Time): Int =
        compareValuesBy(this, other, Time::h, Time::m, Time::s)
}

/**
 * Монотонные часы.
 */
interface MonotonicClock {
    fun write(time: Time)
    fun read(): Time
}
