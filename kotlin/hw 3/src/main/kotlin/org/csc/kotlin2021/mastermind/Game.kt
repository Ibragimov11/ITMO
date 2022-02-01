package org.csc.kotlin2021.mastermind

import java.lang.Integer.min

var gameDiffLetters = true
var gameAlphabetSize = 8
var gameWordLength = 4
var gameCountOfAttempts = Int.MAX_VALUE
var gameAlphabet = List(gameAlphabetSize) { i -> 'A' + i }

fun playMastermind(player: Player = RealPlayer(), secret: String = generateSecret()) {
    var complete = false

    do {
        gameCountOfAttempts--

        val guess = player.guess()
        val answer = checkGuess(secret, guess)

        if (answer.positions == gameWordLength) {
            complete = true
        }

        player.receiveEvaluation(complete, answer.positions, answer.letters)
    } while (!complete && gameCountOfAttempts > 0)
}

fun Player.setStartConditions() {
    gameDiffLetters = setDifferentLetters()
    gameAlphabetSize = setAlphabetSize()
    gameAlphabet = List(gameAlphabetSize) { i -> 'A' + i }
    gameWordLength = setWordLength()
    gameCountOfAttempts = setCountOfAttempts()

    while (gameDiffLetters && gameWordLength > gameAlphabetSize) {
        println(
            "Incorrect start conditions: if you want to play in a mode that doesn't allow " +
                    "repetitions in secret, then the word length should not exceed the size of the alphabet"
        )
        println("Try to re-enter the length of the word.")

        gameWordLength = setWordLength()
    }
}

fun generateSecret(
    diffLetters: Boolean = gameDiffLetters,
    alphabetSize: Int = gameAlphabetSize,
    wordLength: Int = gameWordLength
): String {
    val alphabet = if (alphabetSize != gameAlphabetSize) List(alphabetSize) { i -> 'A' + i } else gameAlphabet

    return if (diffLetters) {
        alphabet.shuffled().take(wordLength)
    } else {
        MutableList(wordLength) { alphabet.random() }
    }.joinToString(prefix = "", postfix = "", separator = "")
}

data class Answer(val positions: Int, val letters: Int)

fun checkGuess(secret: String, guess: String): Answer {
    val positions = guess.filterIndexed { i, letter -> secret[i] == letter }.length
    val letters = guess.toSet()
        .fold(0) { sum, x -> sum + min(secret.count { c -> c == x }, guess.count { c -> c == x }) } - positions

    return Answer(positions, letters)
}
