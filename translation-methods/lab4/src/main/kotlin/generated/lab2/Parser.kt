package generated.lab2

import kotlin.properties.Delegates
import generated.lab2.TypeToken.*

        

class Parser private constructor(private val lex: LexicalAnalyzer) {

    private fun input() : Input {
        val res = Input("Input")

        when(lex.token().typeToken) {
            LAMBDA -> {
                ensure(LAMBDA)
                val LAMBDA0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val v1 = v()
                res.add(v1)
                ensure(COLON)
                val COLON2 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val le3 = le()
                res.add(le3)
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun v() : V {
        val res = V("V")

        when(lex.token().typeToken) {
            VAR -> {
                val vars0 = vars()
                res.add(vars0)
            }
            COLON -> {
                res.add("eps")
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun vars() : Vars {
        val res = Vars("Vars")

        when(lex.token().typeToken) {
            VAR -> {
                ensure(VAR)
                val VAR0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val varsS1 = varsS()
                res.add(varsS1)
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun varsS() : VarsS {
        val res = VarsS("VarsS")

        when(lex.token().typeToken) {
            COMMA -> {
                ensure(COMMA)
                val COMMA0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                ensure(VAR)
                val VAR1 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val varsS2 = varsS()
                res.add(varsS2)
            }
            COLON -> {
                res.add("eps")
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun le() : Le {
        val res = Le("Le")

        when(lex.token().typeToken) {
            OPEN, VAR, NUM -> {
                val lt0 = lt()
                res.add(lt0)
                val leS1 = leS()
                res.add(leS1)
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun leS() : LeS {
        val res = LeS("LeS")

        when(lex.token().typeToken) {
            OR -> {
                ensure(OR)
                val OR0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val lt1 = lt()
                res.add(lt1)
                val leS2 = leS()
                res.add(leS2)
            }
            END, CLOSE -> {
                res.add("eps")
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun lt() : Lt {
        val res = Lt("Lt")

        when(lex.token().typeToken) {
            OPEN, VAR, NUM -> {
                val e0 = e()
                res.add(e0)
                val ltS1 = ltS()
                res.add(ltS1)
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun ltS() : LtS {
        val res = LtS("LtS")

        when(lex.token().typeToken) {
            AND -> {
                ensure(AND)
                val AND0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val e1 = e()
                res.add(e1)
                val ltS2 = ltS()
                res.add(ltS2)
            }
            OR, END, CLOSE -> {
                res.add("eps")
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun e() : E {
        val res = E("E")

        when(lex.token().typeToken) {
            OPEN, VAR, NUM -> {
                val t0 = t()
                res.add(t0)
                val eS1 = eS()
                res.add(eS1)
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun eS() : ES {
        val res = ES("ES")

        when(lex.token().typeToken) {
            ADD -> {
                ensure(ADD)
                val ADD0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val t1 = t()
                res.add(t1)
                val eS2 = eS()
                res.add(eS2)
            }
            AND, OR, END, CLOSE -> {
                res.add("eps")
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun t() : T {
        val res = T("T")

        when(lex.token().typeToken) {
            OPEN, VAR, NUM -> {
                val f0 = f()
                res.add(f0)
                val tS1 = tS()
                res.add(tS1)
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun tS() : TS {
        val res = TS("TS")

        when(lex.token().typeToken) {
            MUL -> {
                ensure(MUL)
                val MUL0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val f1 = f()
                res.add(f1)
                val tS2 = tS()
                res.add(tS2)
            }
            ADD, AND, OR, END, CLOSE -> {
                res.add("eps")
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun f() : F {
        val res = F("F")

        when(lex.token().typeToken) {
            OPEN -> {
                ensure(OPEN)
                val OPEN0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val le1 = le()
                res.add(le1)
                ensure(CLOSE)
                val CLOSE2 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
            }
            VAR -> {
                ensure(VAR)
                val VAR0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
            }
            NUM -> {
                ensure(NUM)
                val NUM0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
            }
            else -> unexpectedToken()
        }

        return res
    }


    private fun ensure(typeToken: TypeToken) {
       if (lex.token().typeToken != typeToken)
           throw ParseException(
               "Expected $typeToken, but found ${lex.token().typeToken}"
           )
    }

    private fun unexpectedToken(): Tree {
        throw ParseException("Unexpected token ${lex.token()}")
    }

    companion object {
        fun parse(s: String) : Input {
            return Parser(LexicalAnalyzer(s)).input()
        }

        class Input(node: String) : Tree(node) {
        }
        class V(node: String) : Tree(node) {
        }
        class Vars(node: String) : Tree(node) {
        }
        class VarsS(node: String) : Tree(node) {
        }
        class Le(node: String) : Tree(node) {
        }
        class LeS(node: String) : Tree(node) {
        }
        class Lt(node: String) : Tree(node) {
        }
        class LtS(node: String) : Tree(node) {
        }
        class E(node: String) : Tree(node) {
        }
        class ES(node: String) : Tree(node) {
        }
        class T(node: String) : Tree(node) {
        }
        class TS(node: String) : Tree(node) {
        }
        class F(node: String) : Tree(node) {
        }

    }
}