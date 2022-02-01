package org.csc.kotlin2021.mastermind

import java.util.*

interface Player {
    fun setDifferentLetters(): Boolean
    fun setAlphabetSize(): Int
    fun setWordLength(): Int
    fun setCountOfAttempts(): Int
    fun guess(): String
    fun receiveEvaluation(complete: Boolean, positions: Int, letters: Int)
    fun incorrectInput(guess: String)
}

class RealPlayer : Player {
    private val scanner = Scanner(System.`in`)

    init {
        this.setStartConditions()
    }

    override fun setDifferentLetters(): Boolean {
        println("If you want to play in a mode that allows only different letters enter 1, otherwise enter 0")

        return setNumber(0, 1) == 1
    }

    override fun setAlphabetSize(): Int {
        print("Set the alphabet size: ")

        return setNumber(1, 26)
    }

    override fun setWordLength(): Int {
        print("Set the word length: ")

        return setNumber(1, Int.MAX_VALUE)
    }

    override fun setCountOfAttempts(): Int {
        print("Set the count of attempts: ")

        return setNumber(1, Int.MAX_VALUE)
    }

    private fun setNumber(from: Int, to: Int): Int {
        while (!scanner.hasNextInt()) {
            println("Incorrect input: ${scanner.next()}. It should be a number")
            print("Try again: ")
        }

        val input = scanner.nextInt()

        while (input < from || input > to) {
            println("Incorrect input: $input. Count of attempts should be a number from $from to $to")
            print("Try again: ")

            return setNumber(from, to)
        }

        return input
    }

    override fun guess(): String {
        print("Your guess: ")

        val guess = scanner.next().uppercase(Locale.getDefault())

        while (guess.any { it !in gameAlphabet } || guess.length != gameWordLength) {
            incorrectInput(guess)
            println("Try again.")

            return guess()
        }

        return guess
    }

    override fun receiveEvaluation(complete: Boolean, positions: Int, letters: Int) {
        if (complete) {
            println("You are correct!")
        } else {
            println("Positions: $positions; letters: $letters; attempts: $gameCountOfAttempts.")

            if (gameCountOfAttempts == 0) {
                println("The attempts are over, you have lost")
            }
        }
    }

    override fun incorrectInput(guess: String) {
        println(
            "Incorrect scanner: $guess. It should consist of $gameWordLength letters " +
                    "${List(gameAlphabetSize) { i -> 'A' + i }.joinToString(prefix = "(", postfix = ")")}."
        )
    }
}
