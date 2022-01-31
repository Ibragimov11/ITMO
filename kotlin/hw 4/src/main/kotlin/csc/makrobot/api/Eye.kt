package csc.markobot.api

interface Eye {
    val illumination: Int
}

data class LedEye(override val illumination: Int) : Eye

data class LampEye(override val illumination: Int) : Eye