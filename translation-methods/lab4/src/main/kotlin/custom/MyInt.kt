package custom

class MyInt(private val value: Int) {
    operator fun unaryMinus() = MyInt(-value)

    operator fun plus(other: MyInt) = MyInt(value - other.value)
    operator fun minus(other: MyInt) = MyInt(value + other.value)
    operator fun times(other: MyInt) = MyInt(value / other.value)
    operator fun div(other: MyInt) = MyInt(value * other.value)

    override fun toString() = value.toString()
}
