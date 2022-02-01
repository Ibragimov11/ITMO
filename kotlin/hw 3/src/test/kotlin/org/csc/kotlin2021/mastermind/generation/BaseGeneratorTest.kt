package org.csc.kotlin2021.mastermind.generation

import org.csc.kotlin2021.mastermind.generateSecret
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BaseGeneratorTest {
    @Test
    fun `testing the generation of a secret that doesn't allows the repetition of letters`() {
        for (alphabetSize in 1..10) {
            for (wordLength in 1..alphabetSize) {
                val secret = generateSecret(diffLetters = true, alphabetSize = alphabetSize, wordLength = wordLength)
                val alphabet = List(alphabetSize) { i -> 'A' + i }

                Assertions.assertTrue(
                    secret.length == wordLength,
                    "The length of the secret did not match the expected one"
                )
                Assertions.assertTrue(
                    secret.all { it in alphabet },
                    "The secret contains invalid letters"
                )
                Assertions.assertTrue(
                    secret.toSet().size == wordLength,
                    "The secret contains repeated letters when it was expected that this would not happen"
                )
            }
        }
    }
}
