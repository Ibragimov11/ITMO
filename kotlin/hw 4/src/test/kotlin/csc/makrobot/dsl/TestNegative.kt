package csc.makrobot.dsl

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestNegative {

    @Test
    fun testContext1() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                robot("Wall-e") {
                    head {
                        plastic withThickness 2
                        
                        head {
                            plastic withThickness 2
                        }
                    }
                }
            }
        """)
    }

    @Test
    fun testContext2() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                robot("Wall-e") {
                    plastic withThickness 2
                }
            }
        """)
    }

    @Test
    fun testContext3() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                robot("Wall-e") {
                    eyes {
                        lamps {
                            count = 2
                            luminance = 10
                        }
                    }
                }
            }
        """)
    }

    @Test
    fun testContext4() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                robot("Wall-e") {
                    chassis = wheels {
                        plastic withThickness 2
                        diameter = 4
                        count = 2
                    }
                }
            }
        """)
    }

    @Test
    fun testContext5() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                robot("Wall-e") {
                    chassis = legs {
                        diameter = 4
                        count = 2
                    }
                }
            }
        """)
    }

    @Test
    fun testContext6() {
        assertCompilationFails("""
            import csc.markobot.api.*
            import csc.markobot.dsl.*
            
            fun badContext() {
                robot("Wall-e") {
                    body {
                        metal withThickness 1
                        caterpillar withWidth 10
                    }
                }
            }
        """)
    }

    private fun assertCompilationFails(source: String) {
        val result = KotlinCompilation().apply {
            sources = listOf(SourceFile.kotlin("BadCode.kt", source))
            inheritClassPath = true
        }.compile()

        Assertions.assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
    }
}