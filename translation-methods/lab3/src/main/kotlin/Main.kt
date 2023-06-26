import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

fun main() {
    val pythonCode = """
        a = int(input())
        b = 2 + 5 - a * 8
        print(a * (3 - b) * b)

        if a + b > 20:
            x = int(input())
            y = int(input())
            print(x * y)
        elif a + b == 20:
            print(20)
        else:
            z = int(input())
            print(z - a + b)

        while a != 0:
            print(a)
            a = int(input())

        c = int(input())
        print(c)
    """.trimIndent()

    val lexer = GrammarLexer(CharStreams.fromString(pythonCode))
    val tokens = CommonTokenStream(lexer)
    val parser = GrammarParser(tokens)
    val tree: ParseTree = parser.program()
    val visitor = Visitor()
    println(visitor.visit(tree))
}
