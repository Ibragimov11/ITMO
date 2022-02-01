package org.csc.kotlin2021.mastermind.generation

import org.csc.kotlin2021.mastermind.generateSecret
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class SmartGeneratorTest {
    @Test
    fun `testing the generation of a secret that allows the repetition of letters`() {
        for (alphabetSize in 1..10) {
            for (wordLength in 1..10) {
                val secret = generateSecret(diffLetters = false, alphabetSize = alphabetSize, wordLength = wordLength)
                val alphabet = List(alphabetSize) { i -> 'A' + i }

                Assertions.assertTrue(
                    secret.length == wordLength,
                    "The length of the secret did not match the expected one"
                )
                Assertions.assertTrue(
                    secret.all { it in alphabet },
                    "The secret contains invalid letters"
                )
            }
        }
    }
}
