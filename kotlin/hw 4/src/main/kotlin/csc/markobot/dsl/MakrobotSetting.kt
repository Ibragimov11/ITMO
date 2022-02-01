package csc.markobot.dsl

import csc.markobot.api.*

@MakroBotDsl
class MakroBotSetting(private val name: String) {
    private var head: Head = Head(Plastic(2), listOf(LampEye(10), LampEye(10), LedEye(3)), Mouth(Speaker(3)))
    private var body: Body = Body(Metal(1), listOf("I don't want to survive.", "I want live."))
    private var hands: Hands = Hands(Plastic(3), LoadClass.Light, LoadClass.Medium)
    internal var chassis: Chassis = Chassis.Caterpillar(10)

    fun head(settings: HeadSetting.() -> Unit) {
        this.head = HeadSetting().apply(settings).toHead()
    }

    fun body(settings: BodySetting.() -> Unit) {
        this.body = BodySetting().apply(settings).toBody()
    }

    fun hands(settings: HandsSetting.() -> Unit) {
        this.hands = HandsSetting().apply(settings).toHands()
    }

    infix fun caterpillar.withWidth(width: Int): Chassis.Caterpillar {
        return Chassis.Caterpillar(width)
    }

    fun wheels(settings: WheelsSetting.() -> Unit): Chassis {
        return WheelsSetting().apply(settings).toWheels()
    }

    interface MaterialInterface {
        companion object {
            private val plasticInstance = Plastic(2)
            private val metalInstance = Metal(2)
        }

        val plastic
            get() = plasticInstance
        val metal
            get() = metalInstance
    }

    open class MaterialSetting : MaterialInterface {
        var material: Material = Plastic(2)
    }

    @MakroBotDsl
    class HeadSetting : MaterialSetting() {
        private var eyes = listOf(LampEye(10), LampEye(10), LedEye(3))
        private var mouth = Mouth(Speaker(3))

        infix fun Material.withThickness(thickness: Int) {
            if (this !is Plastic && this !is Metal) {
                throw Exception("Incorrect or non-existent material")
            }

            if (this == plastic) {
                this@HeadSetting.material = Plastic(thickness)
            } else {
                this@HeadSetting.material = Metal(thickness)
            }
        }

        fun eyes(settings: EyesSetting.() -> Unit) {
            this.eyes = EyesSetting().apply(settings).toEyes()
        }

        @MakroBotDsl
        class EyesSetting {
            private val lightSources = arrayListOf<Eye>()

            private enum class LightSource {
                Lamp, Led
            }

            fun lamps(settings: EyeSetting.() -> Unit) {
                addEyes(settings, LightSource.Lamp)
            }

            fun leds(settings: EyeSetting.() -> Unit) {
                addEyes(settings, LightSource.Led)
            }

            private fun addEyes(settings: EyeSetting.() -> Unit, lightSource: LightSource) {
                val eyeSetting = EyeSetting().apply(settings)

                repeat(eyeSetting.count) {
                    this.lightSources.add(
                        when (lightSource) {
                            LightSource.Lamp -> LampEye(eyeSetting.luminance)
                            else -> LedEye(eyeSetting.luminance)
                        }
                    )
                }
            }

            @MakroBotDsl
            data class EyeSetting(var count: Int = 2, var luminance: Int = 3)

            fun toEyes(): List<Eye> {
                return lightSources
            }
        }

        fun mouth(settings: MouthSetting.() -> Unit) {
            this.mouth = MouthSetting().apply(settings).toMouth()
        }

        @MakroBotDsl
        class MouthSetting {
            private var speaker = Speaker(3)

            fun speaker(settings: SpeakerSetting.() -> Unit) {
                this.speaker = SpeakerSetting().apply(settings).toSpeaker()
            }

            @MakroBotDsl
            class SpeakerSetting {
                var power = 3

                fun toSpeaker(): Speaker {
                    return Speaker(power)
                }
            }

            fun toMouth(): Mouth {
                return Mouth(speaker)
            }
        }

        fun toHead(): Head {
            return Head(material, eyes, mouth)
        }
    }

    @MakroBotDsl
    class BodySetting : MaterialSetting() {
        private var text = ArrayList<String>()

        infix fun Material.withThickness(thickness: Int) {
            if (this !is Plastic && this !is Metal) {
                throw Exception("Incorrect or non-existent material")
            }

            if (this == plastic) {
                this@BodySetting.material = Plastic(thickness)
            } else {
                this@BodySetting.material = Metal(thickness)
            }
        }

        class WriteBlock {
            val strings = arrayListOf<String>()

            operator fun String.unaryPlus() {
                strings.add(this)
            }
        }

        infix fun inscription(text: WriteBlock.() -> Unit) {
            this.text = WriteBlock().apply(text).strings
        }

        fun toBody(): Body {
            return Body(material, text)
        }
    }

    @MakroBotDsl
    class HandsSetting : MaterialSetting() {
        val veryLight = LoadClass.VeryLight
        val light = LoadClass.Light
        val medium = LoadClass.Medium
        val heavy = LoadClass.Heavy
        val veryHeavy = LoadClass.VeryHeavy
        val enormous = LoadClass.Enormous

        internal var load: Pair<LoadClass, LoadClass> = Pair(light, medium)

        operator fun LoadClass.minus(other: LoadClass): Pair<LoadClass, LoadClass> {
            return Pair(this, other)
        }

        infix fun Material.withThickness(thickness: Int) {
            if (this !is Plastic && this !is Metal) {
                throw Exception("Incorrect or non-existent material")
            }

            if (this == plastic) {
                this@HandsSetting.material = Plastic(thickness)
            } else {
                this@HandsSetting.material = Metal(thickness)
            }
        }

        fun toHands(): Hands {
            return Hands(material, load.first, load.second)
        }
    }

    @MakroBotDsl
    class WheelsSetting {
        var diameter = 4
        var count = 2

        fun toWheels(): Chassis {
            return Chassis.Wheel(count, diameter)
        }
    }

    fun toMakroBot(): MakroBot {
        return MakroBot(name, head, body, hands, chassis)
    }
}

fun robot(name: String, settings: MakroBotSetting.() -> Unit): MakroBot {
    return configRobot(name, settings).toMakroBot()
}

fun configRobot(name: String, settings: MakroBotSetting.() -> Unit): MakroBotSetting {
    return MakroBotSetting(name).apply(settings)
}

typealias legs = Chassis.Legs

object caterpillar
