import kotlin.properties.Delegates

/**
 *@author Said Ibragimov on 07.12.2022 02:09
 */

class LexicalAnalyzer(private val s: String) {
    private var pos by Delegates.notNull<Int>()
    private var curChar by Delegates.notNull<Char>()
    private lateinit var token: Token

    init {
        pos = 0
        nextChar()
        nextToken()
    }

    fun curPos() = pos

    fun curToken() = token

    fun nextToken() {
        skipWS()

        token = when {
            curChar == ':' -> {
                nextChar()
                Token.COLON
            }
            curChar == ',' -> {
                nextChar()
                Token.COMMA
            }
            curChar == '|' -> {
                nextChar()
                Token.OR
            }
            curChar == '&' -> {
                nextChar()
                Token.AND
            }
            curChar == '+' -> {
                nextChar()
                Token.ADD
            }
            curChar == '*' -> {
                nextChar()
                Token.MUL
            }
            curChar == '(' -> {
                nextChar()
                Token.LPAREN
            }
            curChar == ')' -> {
                nextChar()
                Token.RPAREN
            }

            curChar.isLetter() -> parseLambdaOrVar()
            curChar.isDigit() -> parseNumber()

            curChar == '$' -> {
                Token.END
            }

            else -> throw ParseException("Unexpected symbol $curChar at pos $pos")
        }
    }

    private fun parseLambdaOrVar(): Token {
        val v = buildString {
            while (curChar.isLetterOrDigit()) {
                append(curChar)
                nextChar()
            }
        }

        return if (v == "lambda")
            Token.LAMBDA
        else
            Token.VAR.apply {
                value = v
            }
    }

    private fun parseNumber(): Token {
        val v = buildString {
            while (curChar.isDigit()) {
                append(curChar)
                nextChar()
            }
        }

        return Token.NUM.apply {
            value = v
        }
    }

    private fun skipWS() {
        while (Character.isWhitespace(curChar)) {
            nextChar()
        }
    }

    private fun nextChar() {
        curChar = when {
            pos < s.length -> s[pos]
            pos == s.length -> '$'
            else -> throw ParseException("nextChar() after end of input (pos = $pos, length = ${s.length}")
        }

        pos++
    }
}
