import CKEYWORDS.*

class Visitor : GrammarBaseVisitor<String>() {
    private val vars = mutableMapOf<TYPE, MutableSet<String>>()

    override fun visitProgram(ctx: GrammarParser.ProgramContext?): String {
        val body = ctx?.statement()?.joinToString(
            separator = System.lineSeparator(),
        ) { st ->
            visitStatement(st)
        }?.lines()?.joinToString(
            separator = System.lineSeparator(),
            postfix = System.lineSeparator()
        ) {
            TAB.toString() + it
        } ?: throw Exception("ProgramContext is null")

        val varsDeclaration = vars.takeIf { it.isNotEmpty() }?.toList()?.joinToString(
            separator = SEMICOLON.toString() + System.lineSeparator(),
            postfix = SEMICOLON.toString() + System.lineSeparator()
        ) { (t, v) ->
            "${t.toString().lowercase()} ${v.joinToString(separator = ", ")}"
        }

        return buildString {
            varsDeclaration?.let { appendLine(it) }
            appendLine("int main() $L_CURLY_BRACE")
            append(body)
            appendLine("${TAB}return 0;")
            append(R_CURLY_BRACE.toString())
        }
    }

    override fun visitStatement(ctx: GrammarParser.StatementContext?): String =
        ctx?.assignment()?.let {
            visitAssignment(it)
        } ?: ctx?.ifelse()?.let {
            visitIfelse(it)
        } ?: ctx?.while_()?.let {
            visitWhile(it)
        } ?: ctx?.print()?.let {
            visitPrint(it)
        } ?: throw Exception("StatementContext is null")

    override fun visitAssignment(ctx: GrammarParser.AssignmentContext?): String {
        val varName = ctx?.VARIABLE()?.text ?: throw Exception("AssignmentContext is null")

        return ctx.input()?.let { inputCtx ->
            val varType = TYPE.valueOf(inputCtx.TYPE().text.uppercase())
            addVar(varType, varName)

            buildString {
                append(SCANF)
                append(L_BRACKET)
                append("\"")
                append(varType.specifier())
                append("\", ")
                append("&$varName")
                append(R_BRACKET)
                append(SEMICOLON)
            }
        } ?: ctx.expr()?.let { exprCtx ->
            addVar(TYPE.INT, varName)

            buildString {
                append(varName)
                append(" $ASSIGN ")
                append(visitExpr(exprCtx))
                append(SEMICOLON)
            }
        } ?: throw Exception("Something went wrong")
    }

    override fun visitInput(ctx: GrammarParser.InputContext?): String {
        return "unused method" // на самом деле накосячил в грамматике и пришлось костыльнуть
    }

    override fun visitIfelse(ctx: GrammarParser.IfelseContext?): String =
        ctx?.let {
            buildString {
                append(visitIf(ctx.if_()))
                append(" ")
                ctx.elif()?.joinToString(
                    separator = System.lineSeparator(),
                    postfix = System.lineSeparator()
                ) { st ->
                    append(visitElif(st))
                } ?: throw Exception("InnerContext is null")
                ctx.else_()?.let { append(visitElse(it)) }
            }
        } ?: throw Exception("IfelseContext")

    override fun visitIf(ctx: GrammarParser.IfContext?): String =
        ctx?.let {
            buildString {
                append(IF)
                append(" $L_BRACKET")
                append(visitCondition(ctx.condition()))
                appendLine("$R_BRACKET $L_CURLY_BRACE")
                append(visitInner(ctx.inner()))
                append(R_CURLY_BRACE)
            }
        } ?: throw Exception("IfContext is null")

    override fun visitElif(ctx: GrammarParser.ElifContext?): String =
        ctx?.let {
            buildString {
                append("$ELSE $IF")
                append(" $L_BRACKET")
                append(visitCondition(ctx.condition()))
                appendLine("$R_BRACKET $L_CURLY_BRACE")
                append(visitInner(ctx.inner()))
                append("$R_CURLY_BRACE ")
            }
        } ?: throw Exception("IfContext is null")

    override fun visitElse(ctx: GrammarParser.ElseContext?): String =
        ctx?.let {
            buildString {
                append(ELSE)
                appendLine(" $L_CURLY_BRACE")
                append(visitInner(ctx.inner()))
                append(R_CURLY_BRACE)
            }
        } ?: throw Exception("ElseContext is null")

    override fun visitWhile(ctx: GrammarParser.WhileContext?): String =
        ctx?.let {
            buildString {
                append(WHILE)
                append(" $L_BRACKET")
                append(visitCondition(ctx.condition()))
                appendLine("$R_BRACKET $L_CURLY_BRACE")
                append(visitInner(ctx.inner()))
                append(R_CURLY_BRACE)
            }
        } ?: throw Exception("WhileContext is null")

    override fun visitCondition(ctx: GrammarParser.ConditionContext?): String =
        ctx?.let {
            val l = ctx.expr()[0]
            val r = ctx.expr()[1]

            "${visitExpr(l)} ${ctx.COMPARISON().text} ${visitExpr(r)}"
        } ?: throw Exception("ConditionContext is null")

    override fun visitInner(ctx: GrammarParser.InnerContext?): String =
        ctx?.inner_statement()?.joinToString(
            separator = System.lineSeparator(),
            postfix = System.lineSeparator()
        ) { st ->
            TAB.toString() + visitInner_statement(st)
        } ?: throw Exception("InnerContext is null")

    override fun visitInner_statement(ctx: GrammarParser.Inner_statementContext?): String =
        ctx?.assignment()?.let {
            visitAssignment(it)
        } ?: ctx?.print()?.let {
            visitPrint(it)
        } ?: throw Exception("Inner_statementContext is null")

    override fun visitExpr(ctx: GrammarParser.ExprContext?): String =
        ctx?.text ?: throw Exception("ExprContext is null")

    override fun visitPrint(ctx: GrammarParser.PrintContext?): String =
        ctx?.let {
            buildString {
                append(PRINTF)
                append(L_BRACKET)
                append("\"")
                append(TYPE.INT.specifier())
                append("\\n\", ")
                append(visitExpr(ctx.expr()))
                append(R_BRACKET)
                append(SEMICOLON)
            }
        } ?: throw Exception("PrintContext is null")

    private fun addVar(type: TYPE, name: String) {
        // можно переделать в 1 строку
        if (vars.containsKey(type)) {
            vars[type]!!.add(name)
        } else {
            vars[type] = mutableSetOf(name)
        }
    }
}
