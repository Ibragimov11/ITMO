package generated.calculator

import kotlin.properties.Delegates
import generated.calculator.TypeToken.*

import custom.MyInt


class Parser private constructor(private val lex: LexicalAnalyzer) {

    private fun expr() : Expr {
        val res = Expr("Expr")

        when(lex.token().typeToken) {
            NUM, OPEN, MINUS -> {
                val term0 = term()
                res.add(term0)
                val exprS1 = exprS(term0.value)
                res.add(exprS1)
                res.value = exprS1.value
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun exprS(acc: MyInt) : ExprS {
        val res = ExprS("ExprS")

        when(lex.token().typeToken) {
            PLUS -> {
                ensure(PLUS)
                val PLUS0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val term1 = term()
                res.add(term1)
                res.value = acc + term1.value
                val exprS2 = exprS(res.value)
                res.add(exprS2)
                res.value = exprS2.value
            }
            MINUS -> {
                ensure(MINUS)
                val MINUS0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val term1 = term()
                res.add(term1)
                res.value = acc - term1.value
                val exprS2 = exprS(res.value)
                res.add(exprS2)
                res.value = exprS2.value
            }
            END, CLOSE -> {
                res.add("eps")
                res.value = acc
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun term() : Term {
        val res = Term("Term")

        when(lex.token().typeToken) {
            NUM, OPEN, MINUS -> {
                val factor0 = factor()
                res.add(factor0)
                val termS1 = termS(factor0.value)
                res.add(termS1)
                res.value = termS1.value
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun termS(acc: MyInt) : TermS {
        val res = TermS("TermS")

        when(lex.token().typeToken) {
            MUL -> {
                ensure(MUL)
                val MUL0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val factor1 = factor()
                res.add(factor1)
                res.value = acc * factor1.value
                val termS2 = termS(res.value)
                res.add(termS2)
                res.value = termS2.value
            }
            DIV -> {
                ensure(DIV)
                val DIV0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val factor1 = factor()
                res.add(factor1)
                res.value = acc / factor1.value
                val termS2 = termS(res.value)
                res.add(termS2)
                res.value = termS2.value
            }
            PLUS, MINUS, END, CLOSE -> {
                res.add("eps")
                res.value = acc
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun factor() : Factor {
        val res = Factor("Factor")

        when(lex.token().typeToken) {
            NUM -> {
                ensure(NUM)
                val NUM0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                res.value = MyInt(NUM0.toInt())
            }
            OPEN -> {
                ensure(OPEN)
                val OPEN0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val expr1 = expr()
                res.add(expr1)
                ensure(CLOSE)
                val CLOSE2 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                res.value = expr1.value
            }
            MINUS -> {
                ensure(MINUS)
                val MINUS0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val unary1 = unary()
                res.add(unary1)
                res.value = unary1.value
            }
            else -> unexpectedToken()
        }

        return res
    }
    private fun unary() : Unary {
        val res = Unary("Unary")

        when(lex.token().typeToken) {
            NUM -> {
                ensure(NUM)
                val NUM0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                res.value = MyInt(("-" + NUM0).toInt())
            }
            OPEN -> {
                ensure(OPEN)
                val OPEN0 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                val expr1 = expr()
                res.add(expr1)
                ensure(CLOSE)
                val CLOSE2 = lex.token().text
                res.add(lex.token().text)
                lex.nextToken()
                res.value = -expr1.value
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
        fun parse(s: String) : Expr {
            return Parser(LexicalAnalyzer(s)).expr()
        }

        class Expr(node: String) : Tree(node) {
            var value by Delegates.notNull<MyInt>()
        }
        class ExprS(node: String) : Tree(node) {
            var value by Delegates.notNull<MyInt>()
        }
        class Term(node: String) : Tree(node) {
            var value by Delegates.notNull<MyInt>()
        }
        class TermS(node: String) : Tree(node) {
            var value by Delegates.notNull<MyInt>()
        }
        class Factor(node: String) : Tree(node) {
            var value by Delegates.notNull<MyInt>()
        }
        class Unary(node: String) : Tree(node) {
            var value by Delegates.notNull<MyInt>()
        }

    }
}