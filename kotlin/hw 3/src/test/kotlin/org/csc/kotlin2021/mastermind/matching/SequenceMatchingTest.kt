package org.csc.kotlin2021.mastermind.matching

import org.csc.kotlin2021.mastermind.Answer
import org.csc.kotlin2021.mastermind.checkGuess
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class SequenceMatchingTest {

    companion object {
        @JvmStatic
        fun sequences() = listOf(
            Arguments.of("A", "A", 1, 0),
            Arguments.of("A", "B", 0, 0),
            Arguments.of("XYZ", "XYZ", 3, 0),
            Arguments.of("ACEB", "BCDF", 1, 1),
            Arguments.of("AAAB", "AABA", 2, 2),
            Arguments.of("ABCD", "EFGH", 0, 0),
            Arguments.of("AAAB", "BBBA", 0, 2),
            Arguments.of("ITMO", "MIPT", 0, 3),
            Arguments.of("ABCDEF", "BCDEFA", 0, 6),
            Arguments.of("ABACABA", "AAAABBC", 3, 4)
        )
    }

    @ParameterizedTest
    @MethodSource("sequences")
    fun testSequenceMatching(initial: String, actual: String, expectedFullMatch: Int, expectedPartMatch: Int) {
        val result: Answer = checkGuess(initial, actual)
        val actualFullMatch = result.positions
        val actualPartMatch = result.letters

        Assertions.assertEquals(
            expectedFullMatch, actualFullMatch, "Full matches don't equal! " +
                    "Actual full match count = $actualFullMatch, expected full match count = $expectedFullMatch"
        )
        Assertions.assertEquals(
            expectedPartMatch, actualPartMatch, "Part matches don't equal! " +
                    "Part full part count = $actualPartMatch, expected part match count = $expectedPartMatch"
        )
    }
}
