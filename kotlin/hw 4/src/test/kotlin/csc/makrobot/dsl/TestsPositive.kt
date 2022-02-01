@file:Suppress("LocalVariableName", "NonAsciiCharacters")

package csc.makrobot.dsl

import csc.markobot.api.*
import csc.markobot.dsl.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestsPositive {

    @Test
    fun testNonDSL() {
        val robot = MakroBot("Wall-E",
            Head(Plastic(2), listOf(LampEye(10), LampEye(10), LedEye(3)), Mouth(Speaker(3))),
            Body(Metal(1), listOf("I don't want to survive.", "I want live.")),
            Hands(Plastic(3), LoadClass.Light, LoadClass.Medium),
            Chassis.Caterpillar(10)
        )
        verify(robot)
    }

    @Test
    fun testDSL() {
        val robot = robot("Wall-E") {
            head {
                plastic withThickness 2

                eyes {
                    lamps {
                        count = 2
                        luminance = 10
                    }
                    leds {
                        count = 1
                        luminance = 3
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

            chassis = caterpillar withWidth 10
        }

        verify(robot)
    }

    @Test
    fun testDSLOtherChassis() {
        val robotWithWheels = robot("Wall-E") {
            head {
                plastic withThickness 2

                eyes {
                    lamps {
                        count = 2
                        luminance = 10
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
            }

            hands {
                plastic withThickness 3
                load = light - medium
            }
            chassis = wheels {
                diameter = 4
                count = 2
            }
        }

        Assertions.assertEquals(Chassis.Wheel(2, 4), robotWithWheels.chassis)

        val robotWithLegs = robot("Wall-E") {
            head {
                plastic withThickness 2

                eyes {
                    lamps {
                        count = 2
                        luminance = 10
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
            }

            hands {
                plastic withThickness 3
                load = light - medium
            }
            chassis = legs
        }

        Assertions.assertEquals(Chassis.Legs, robotWithLegs.chassis)
    }

    private fun verify(robot: MakroBot) {
        Assertions.assertEquals("Wall-E", robot.name)
        Assertions.assertEquals(Plastic(2), robot.head.material)
        Assertions.assertArrayEquals(arrayOf(LampEye(10), LampEye(10), LedEye(3)), robot.head.eyes.toTypedArray())
        Assertions.assertEquals(Mouth(Speaker(3)), robot.head.mouth)

        Assertions.assertEquals(Metal(1), robot.body.material)
        Assertions.assertArrayEquals(arrayOf("I don't want to survive.", "I want live."), robot.body.strings.toTypedArray())

        Assertions.assertEquals(Plastic(3), robot.hands.material)
        Assertions.assertEquals(LoadClass.Light, robot.hands.minLoad)
        Assertions.assertEquals(LoadClass.Medium, robot.hands.maxLoad)

        Assertions.assertEquals(Chassis.Caterpillar(10), robot.chassis)
    }
}
