import kastree.ast.Node
import kastree.ast.Visitor
import kastree.ast.psi.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

internal class ElvisTest {
    @Test
    fun testIfsLimitation() {
        val psi = Parser.myParseFile(
            File("src/main/kotlin/Elvis.kt").readText().replace("\r\n", "\n")
        )
        val func = psi.decls.single {
                it is Node.Decl.Func && it.name == "sendMessageToClient"
            } as Node.Decl.Func
        var ifs = 0
        Visitor.visit(func) { v, _ ->
            if (v is Node.Expr.If) ifs++
        }
        assertTrue(ifs <= 1, "Ожидается, что в теле функции sendMessageToClient не более одного `if`")
    }

    @Test
    fun testCorrectImpl() {
        class MyMailer : Mailer {
            var lastMessage: String? = null
            override fun sendMessage(email: String, message: String) {
                lastMessage = "$email -> $message"
            }
        }

        fun test(message: String?, client: Client) {
            val mailer = MyMailer()
            sendMessageToClient(client, message, mailer)
            if (message == null) {
                assertEquals(
                    null, mailer.lastMessage,
                    "Ожидается, что mailer.sendMessage не вызывается, когда message равно null",
                )
            }
            if (client.personalInfo == null) {
                assertEquals(
                    null, mailer.lastMessage,
                    "Ожидается, что mailer.sendMessage не вызывается, когда client равен null",
                )
            } else {
                if (client.personalInfo!!.email == null) {
                    assertEquals(
                        null, mailer.lastMessage,
                        "Ожидается, что mailer.sendMessage не вызывается, когда personalInfo равно null",
                    )
                } else if (message != null) {
                    assertEquals(
                        "${client.personalInfo!!.email} -> $message", mailer.lastMessage,
                        "Ожидается, что mailer.sendMessage вызывается, когда передан полный набор данных",
                    )
                }
            }
        }

        val clients = listOf(
            Client(null), Client(PersonalInfo(null)),
            Client(
                PersonalInfo("abc@def.com")
            )
        )
        for (m in listOf(null, "hello")) {
            for (c in clients) {
                test(m, c)
            }
        }
    }
}