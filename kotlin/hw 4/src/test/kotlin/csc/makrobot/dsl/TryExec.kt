@file:Suppress("LocalVariableName", "NonAsciiCharacters")

package csc.makrobot.dsl

import csc.markobot.api.*
import csc.markobot.api.WeekDay.*
import csc.markobot.dsl.*

fun main() {

    val волли = MakroBot("Wall-E",
        Head(Plastic(2), listOf(LampEye(10), LampEye(10)), Mouth(Speaker(3))),
        Body(Metal(1), listOf("I don't want to survive.", "I want live.")),
        Hands(Plastic(3), LoadClass.Light, LoadClass.Medium),
        Chassis.Caterpillar(10)
    )

   val воллиЧерезDSL = robot("Wall-E") {
        head {
            plastic withThickness 2

            eyes {
                leds {
                    count = 2
                    luminance = 10
                }
                lamps {
                    count = 1
                    luminance = 40
                }
            }

            mouth {
                speaker {
                    power = 3
                }
            }
        }

        body {
            metal withThickness 1

            inscription {
                +"I don't want to survive."
                +"I want live."
            }
        }

        hands {
            plastic withThickness 3
            load = light - medium
        }

//        chassis = legs
        chassis = caterpillar withWidth 4
        /*chassis = wheels {
            diameter = 4
            count = 2
        }*/
    }

    сценарий {
        волли {                             // invoke operator overload
            speed = 2                       // initialization DSL
            power = 3
        }

        волли вперед 3                      // infix functions
        волли воспроизвести {
            +"Во поле береза стояла"
            +"Во поле кудрявая стояла"
        }
        волли.развернуться()
        волли назад 3

        расписание {                        // context-based high level function with context-lambda

            // волли вперед 3                // control methods availability with @DslMarker

            повторять(пн в 10, вт в 12)     // typealias, infix functions, vararg
            кроме(13)
            повторять(ср..пт в 11)
        }

    }.запуститьСейчас()
        .сброситьРасписание()               // calls chaining
        .расписание {
            повторять(пт в 23)
        }

    val (name, speed) = волли               // destructuring declarations
}
