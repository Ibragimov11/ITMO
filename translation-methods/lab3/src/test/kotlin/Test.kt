import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 *@author Said Ibragimov on 12.01.2023 15:18
 */

class Test {

    @Test
    fun assignment() = test(
        pythonCode = """
            a = int(input())
            b = int(input())
        """.trimIndent(),
        cCode = """
            int a, b;

            int main() {
                scanf("%d", &a);
                scanf("%d", &b);
                return 0;
            }
        """.trimIndent()
    )

    @Test
    fun expr() = test(
        pythonCode = """
            a = 1 + 2 * 3
        """.trimIndent(),
        cCode = """
            int a;

            int main() {
                a = 1+2*3;
                return 0;
            }
        """.trimIndent()
    )

    @Test
    fun print() = test(
        pythonCode = """
            a = int(input())
            print(a)
            print(10)
            print(3 * 27 - 7 * 23)
        """.trimIndent(),
        cCode = """
            int a;
            
            int main() {
                scanf("%d", &a);
                printf("%d\n", a);
                printf("%d\n", 10);
                printf("%d\n", 3*27-7*23);
                return 0;
            }
        """.trimIndent()
    )

    @Test
    fun `if elif else`() = test(
        pythonCode = """
            if 1 < 2:
                print(-1)
            elif 1 == 2:
                print(0)
            else:
                print(1)
        """.trimIndent(),
        cCode = """
            int main() {
                if (1 < 2) {
                    printf("%d\n", -1);
                } else if (1 == 2) {
                    printf("%d\n", 0);
                } else {
                    printf("%d\n", 1);
                }
                return 0;
            }
        """.trimIndent()
    )

    @Test
    fun `while`() = test(
        pythonCode = """
            while 1 != 2:
                print(12345)
        """.trimIndent(),
        cCode = """
            int main() {
                while (1 != 2) {
                    printf("%d\n", 12345);
                }
                return 0;
            }
        """.trimIndent()
    )

    @Test
    fun general() = test(
        pythonCode = """
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
        """.trimIndent(),
        cCode = """
            int a, b, x, y, z, c;

            int main() {
                scanf("%d", &a);
                b = 2+5-a*8;
                printf("%d\n", a*(3-b)*b);
                if (a+b > 20) {
                    scanf("%d", &x);
                    scanf("%d", &y);
                    printf("%d\n", x*y);
                } else if (a+b == 20) {
                    printf("%d\n", 20);
                } else {
                    scanf("%d", &z);
                    printf("%d\n", z-a+b);
                }
                while (a != 0) {
                    printf("%d\n", a);
                    scanf("%d", &a);
                }
                scanf("%d", &c);
                printf("%d\n", c);
                return 0;
            }
        """.trimIndent()
    )

    private fun test(pythonCode: String, cCode: String) {
        val lexer = GrammarLexer(CharStreams.fromString(pythonCode))
        val tokens = CommonTokenStream(lexer)
        val parser = GrammarParser(tokens)
        val tree: ParseTree = parser.program()
        val visitor = Visitor()
        Assertions.assertEquals(cCode, visitor.visit(tree))
    }
}
