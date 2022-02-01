import kastree.ast.Node
import kastree.ast.Visitor
import kastree.ast.psi.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.File

internal class ServiceTest {
    @Test
    fun testSolution() {
        val psi = Parser.myParseFile(
            File("src/main/kotlin/Service.kt").readText().replace("\r\n", "\n")
        )
        var notNullAssertionsFound = 0
        Visitor.visit(psi) { v, _ ->
            if (v is Node.Expr.UnaryOp && v.oper.token == Node.Expr.UnaryOp.Token.NULL_DEREF) {
                notNullAssertionsFound += 1
            }
        }
        if (notNullAssertionsFound > 0) {
            fail(
                "Ожидается, что в файле нет использований оператора `!!`, найдено: $notNullAssertionsFound"
            )
        }
    }

    @Test
    fun testCorrectImpl() {
        class MyServiceVisitorStats(
            override val visitorsCounter: VisitorsCounter?
        ) : ServiceVisitorsStats

        class MyMessagingService(
            override val serviceVisitorsStats: ServiceVisitorsStats?,
            override val loggingService: LoggingService?
        ) : MessagingService

        class MyLoggingService : LoggingService {
            val messages: MutableList<String> = mutableListOf()
            override fun log(logMessage: String) {
                messages += logMessage
            }
        }

        fun doTest(visitorsStats: ServiceVisitorsStats?, logger: MyLoggingService?, clients: Int) {
            val ms = MyMessagingService(visitorsStats, logger)
            val expected = mutableListOf<String>()
            for (i in 1..clients) {
                val clientId = "client$i"
                ms.handleRequest(clientId)
                expected += "Request from $clientId"
                if (visitorsStats?.visitorsCounter != null) {
                    expected += "Visitors count: ${visitorsStats.visitorsCounter!!.uniqueVisitorsCount}"
                }
            }
            if (logger != null) {
                assertEquals(expected, logger.messages, "Неверный лог работы сервиса")
            }
            logger?.messages?.clear()
        }

        val visStats1 = MyServiceVisitorStats(VisitorsCounter())
        val visStats2 = MyServiceVisitorStats(null)
        val visStats3 = null
        val logger1 = MyLoggingService()
        val logger2 = null

        for (v in listOf(visStats1, visStats2, visStats3)) {
            for (l in listOf(logger1, logger2)) {
                doTest(v, l, 5)
            }
        }
    }
}