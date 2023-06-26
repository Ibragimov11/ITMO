import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 *@author Said Ibragimov on 10.12.2022 04:34
 */

class Test {
    
    @ParameterizedTest(name = "{1}")
    @MethodSource("validPythonLambdas")
    fun success(lambda: String, desc: String) {
        assertDoesNotThrow {
            Parser.parse(lambda)
        }
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("validM")
    fun successM(lambda: String, desc: String) {
        assertDoesNotThrow {
            Parser.parse(lambda)
        }
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidPythonLambdas")
    fun fail(lambda: String, desc: String) {
        assertThrows(ParseException::class.java) {
            Parser.parse(lambda)
        }
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidM")
    fun failM(lambda: String, desc: String) {
        assertThrows(ParseException::class.java) {
            Parser.parse(lambda)
        }
    }

    companion object {
        @JvmStatic
        fun validPythonLambdas() = listOf(
            "lambda a : a" to "simple",
            "lambda : 22" to "without parameters",
            "lambda x, y, z : x + 1 + y + 2 + z" to "with multiple parameters",
            "lambda Z1ADc2f67sMb : 125125125" to "var naming",
            "lambda a, b : a | b" to "| operation",
            "lambda a, b : a & b" to "& operation",
            "lambda a, b : a * b" to "* operation",
            "lambda a, b : a + b" to "+ operation",
            "lambda a, b : (a + b) * 3" to "brackets",
            "lambda a, b : ((a + b) | 3) * 2" to "inner brackets",
            "lambda a, b : ((a + b)) * 3" to "repeated brackets",
            "lambda a1, B2, c1C2c : a1 + (B2 | 345) & c1C2c * 1100" to "final boss",
        ).map {
            Arguments.of(it.first, it.second)
        }

        @JvmStatic
        fun invalidPythonLambdas() = listOf(
            "lambda" to "\"lambda\" word",
            "lambda1 a : a" to "invalid lambda token",
            "lambda lambda : 1" to "var name cannot be \"lambda\"",
            "lambda a, b : a - b" to "invalid operator",
            "lambda a, b : a / b" to "invalid operator",
            "lambda a; b : a + b" to "invalid separator",
            "lambda a, b : # @ ! % № ^" to "# @ ! % № ^",
            "lambda a, b : \"hello\"" to "invalid return value",
            "lambda a :" to "without return",
            "lambda 1 : 1" to "number in parameters",
            "lambda 1a : 1" to "invalid var naming",
            "lambda a b : a" to "vars without comma",
            "lambda a, b ; a" to "without colon",
            "lambda a, b : a b" to "invalid body",
            "lambda a, b : + a b" to "invalid operators using (1)",
            "lambda a, b : a b |" to "invalid operators using (2)",
            "lambda a, b : a * & b" to "invalid operators using (3)",
            "lambda a, b : ()a + b" to "invalid brackets",
            "lambda a, b : )a + b(" to "invalid brackets",
            "lambda a, b : (a +) b" to "invalid brackets",
            "lambda a, b : a (+) b" to "invalid brackets",
            "lambda a, b : (a + b))" to "invalid brackets",
        ).map {
            Arguments.of(it.first, it.second)
        }

        @JvmStatic
        fun validM() = listOf(
            "lambda a : lambda b : a + b" to "simple inner lambda",
            "lambda a, b : lambda c, d : (a + b) * (c | d) & 1" to "complex inner lambda",
            "lambda a : lambda b : lambda c : a + b + c" to "sum3",
            "lambda : lambda : 2" to "inner lambda without params",
        ).map {
            Arguments.of(it.first, it.second)
        }

        @JvmStatic
        fun invalidM() = listOf(
            "lambda a : lambda b : " to "invalid inner lambda 1",
            "lambda a, lambda b : a + b" to "invalid inner lambda 2",
        ).map {
            Arguments.of(it.first, it.second)
        }
    }
}
