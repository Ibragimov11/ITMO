package csc.markobot.api

sealed class Chassis {
    data class Wheel(val number: Int, val diameter: Int) : Chassis()
    object Legs : Chassis()
    data class Caterpillar(val width: Int) : Chassis()
}