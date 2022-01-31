package csc.markobot.api

class Hands(val material: Material, val minLoad: LoadClass, val maxLoad: LoadClass)

enum class LoadClass {
    VeryLight, Light, Medium, Heavy, VeryHeavy, Enormous
}