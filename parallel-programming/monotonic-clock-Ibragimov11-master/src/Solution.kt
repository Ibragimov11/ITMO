/**
 * В теле класса решения разрешено использовать только переменные делегированные в класс RegularInt.
 * Нельзя volatile, нельзя другие типы, нельзя блокировки, нельзя лазить в глобальные переменные.
 *
 * @author : Ibragimov Said
 */
class Solution : MonotonicClock {
    private val h = RegularInt(0)
    private val m = RegularInt(0)
    private val s = RegularInt(0)

    private val h1 = RegularInt(0)
    private val m1 = RegularInt(0)
    private val s1 = RegularInt(0)

    override fun write(time: Time) {
        h1.value = time.h
        m1.value = time.m
        s1.value = time.s

        s.value = time.s
        m.value = time.m
        h.value = time.h
    }

    override fun read(): Time {
        val localH = h.value
        val localM = m.value
        val localS = s.value

        val localS1 = s1.value
        val localM1 = m1.value
        val localH1 = h1.value

        return when {
            localH != localH1 -> Time(maxOf(localH, localH1), 0, 0)
            localM != localM1 -> Time(localH, maxOf(localM, localM1), 0)
            localS != localS1 -> Time(localH, localM, maxOf(localS, localS1))
            else -> Time(localH, localM, localS)
        }
    }
}
