import Token.*

class Parser private constructor(private val lex: LexicalAnalyzer) {

    private fun input(): Tree =
        when (lex.curToken()) {
            LAMBDA -> {
                val res = Tree("INPUT")
                    .add(l())
                    .add(inputPrime())

                ensure(END)
                res
            }
            else -> unexpectedToken()
        }

    private fun inputPrime(): Tree =
        when(lex.curToken()) {
            LAMBDA -> {
                Tree("INPUT'")
                    .add(l())
                    .add(inputPrime())
            }
            LPAREN, NUM, VAR -> {
                Tree("INPUT'")
                    .add(le())
            }
            else -> unexpectedToken()
        }

    private fun l(): Tree =
        when(lex.curToken()) {
            LAMBDA -> {
                ensure(LAMBDA)
                val vars0 = vars0()
                ensure(COLON)

                Tree("L")
                    .add(LAMBDA.value)
                    .add(vars0)
                    .add(COLON.value)
            }
            else -> unexpectedToken()
        }

    private fun vars0(): Tree =
        when (lex.curToken()) {
            VAR -> Tree("VARS0")
                .add(vars())
            COLON -> Tree("VARS0")
            else -> unexpectedToken()
        }

    private fun vars(): Tree =
        when (lex.curToken()) {
            VAR -> {
                val value = VAR.value
                ensure(VAR)

                Tree("VARS")
                    .add(value)
                    .add(varsPrime())
            }
            else -> unexpectedToken()
        }

    private fun varsPrime(): Tree =
        when (lex.curToken()) {
            COMMA -> {
                ensure(COMMA)
                val value = VAR.value
                ensure(VAR)

                Tree("VARS'")
                    .add(COMMA.value)
                    .add(value)
                    .add(varsPrime())
            }
            COLON -> Tree("VARS'")
            else -> unexpectedToken()
        }

    private fun le(): Tree =
        when (lex.curToken()) {
            LPAREN, NUM, VAR -> {
                Tree("LE")
                    .add(lt())
                    .add(lePrime())
            }
            else -> unexpectedToken()
        }

    private fun lePrime(): Tree =
        when (lex.curToken()) {
            OR -> {
                ensure(OR)

                Tree("LE'")
                    .add(OR.value)
                    .add(lt())
                    .add(lePrime())
            }
            END, RPAREN -> Tree("LE'")
            else -> unexpectedToken()
        }

    private fun lt(): Tree =
        when (lex.curToken()) {
            LPAREN, NUM, VAR -> {
                Tree("LT")
                    .add(e())
                    .add(ltPrime())
            }
            else -> unexpectedToken()
        }

    private fun ltPrime(): Tree =
        when (lex.curToken()) {
            AND -> {
                ensure(AND)

                Tree("LT'")
                    .add(AND.value)
                    .add(e())
                    .add(ltPrime())
            }
            OR, END, RPAREN -> Tree("LT'")
            else -> unexpectedToken()
        }

    private fun e(): Tree =
        when (lex.curToken()) {
            LPAREN, NUM, VAR -> {
                Tree("E")
                    .add(t())
                    .add(ePrime())
            }
            else -> unexpectedToken()
        }

    private fun ePrime(): Tree =
        when (lex.curToken()) {
            ADD -> {
                ensure(ADD)

                Tree("E'")
                    .add(ADD.value)
                    .add(t())
                    .add(ePrime())
            }
            AND, OR, END, RPAREN -> Tree("E'")
            else -> unexpectedToken()
        }

    private fun t(): Tree =
        when (lex.curToken()) {
            LPAREN, NUM, VAR -> {
                Tree("T")
                    .add(f())
                    .add(tPrime())
            }
            else -> unexpectedToken()
        }

    private fun tPrime(): Tree =
        when (lex.curToken()) {
            MUL -> {
                ensure(MUL)

                Tree("T'")
                    .add(MUL.value)
                    .add(f())
                    .add(tPrime())
            }
            ADD, AND, OR, END, RPAREN -> Tree("T'")
            else -> unexpectedToken()
        }

    private fun f(): Tree =
        when (lex.curToken()) {
            LPAREN -> {
                ensure(LPAREN)
                val le = le()
                ensure(RPAREN)

                Tree("F")
                    .add(LPAREN.value)
                    .add(le)
                    .add(RPAREN.value)
            }
            VAR -> {
                val value = VAR.value
                ensure(VAR)

                Tree("F")
                    .add(value)
            }
            NUM -> {
                val num = NUM.value
                ensure(NUM)

                Tree("F")
                    .add(num)
            }
            else -> unexpectedToken()
        }

    private fun ensure(token: Token) =
        if (lex.curToken() != token)
            throw ParseException(
                "pos ${lex.curPos()}: Expected ${token.getName()}, but found ${lex.curToken().getName()}"
            )
        else
            lex.nextToken()

    private fun unexpectedToken(): Tree {
        throw ParseException("Unexpected token ${lex.curToken()} at pos ${lex.curPos()}")
    }

    companion object {
        fun parse(s: String): Tree {
            return Parser(LexicalAnalyzer(s)).input()
        }
    }
}
