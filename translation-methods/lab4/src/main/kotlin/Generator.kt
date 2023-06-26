import java.nio.file.Path
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.name
import kotlin.io.path.readText

object Generator {
    fun generate(pathFile: String) {
        val filePath = Path.of(pathFile)
        val lexer = MetaGrammarLexer(CharStreams.fromString(filePath.readText()))
        val parser = MetaGrammarParser(CommonTokenStream(lexer))

        val grammar: Grammar = parser.input().grammar

        val fileName = filePath.fileName.name.removeSuffix(".g5")
        val dirPath = Path.of("./src/main/kotlin/generated/$fileName").also {
            it.createDirectories()
        }

        val create = { className: String, text: String ->
            dirPath.resolve(className).also { path ->
                path.bufferedWriter().use { writer ->
                    writer.write(text)
                }
            }
        }

        val packageName = "generated.$fileName"
        create("Tree.kt", generateTree(packageName))
        create("ParseException.kt", generateException(packageName))
        create("Token.kt", generateToken(packageName))
        create("TypeToken.kt", generateTypeToken(packageName, grammar.tokens()))
        create("LexicalAnalyzer.kt", generateLexicalAnalyzer(packageName, grammar.tokens()))
        create("Parser.kt", generateParser(packageName, grammar))
    }

    private fun generateException(packageName: String) =
        """
        package $packageName
        
        data class ParseException(val msg: String) : Exception(msg)
    """.trimIndent()

    private fun generateTypeToken(packageName: String, tokens: List<Terminal>): String {
        val typeTokens = buildString {
            tokens.forEach {
                appendLine("|    ${it.name.uppercase()}(Pattern.compile(${it.regexp})),")
            }
        }

        return """
        |package $packageName
        |
        |import java.util.regex.Pattern
        |
        |enum class TypeToken(val pattern: Pattern) {
        $typeTokens
        |    END(Pattern.compile("\\$"));
        |
        |    companion object {
        |        fun find(s: String): TypeToken? = values().find { it.pattern.matcher(s).matches() }
        |    }
        |}
    """.trimMargin()
    }

    private fun generateToken(packageName: String) =
        """
        package $packageName
        
        data class Token(val typeToken: TypeToken, val text: String) {
            override fun toString(): String = "${"$" + "{" + "typeToken.name" + "}"}(${"$" + "text"})"
        }
    """.trimIndent()

    private fun generateTree(packageName: String) =
        """
        package $packageName
        
        open class Tree(
            private val node: String,
            private val children: MutableList<Tree> = mutableListOf(),
            private var level: Int = 0, // for toString() only
        ) {
            fun add(child: Tree): Tree {
                children.add(child)
                child.updateLevels(level + 1)

                return this
            }

            fun add(child: String) = add(Tree(child))

            private fun updateLevels(startLevel: Int) {
                level = startLevel
                children.forEach {
                    it.updateLevels(startLevel + 1)
                }
            }

            override fun toString(): String {
                return "| ".repeat(level) + node +
                    if (children.isNotEmpty()) children.joinToString(
                        prefix = System.lineSeparator(),
                        separator = System.lineSeparator()
                    ) else ""
            }
        }
    """.trimIndent()

    private fun generateLexicalAnalyzer(packageName: String, tokens: List<Terminal>): String {
        val pattern = tokens.joinToString(separator = "|") {
            it.regexp.drop(1).dropLast(1)
        }

        return """
        package $packageName
        
        import java.util.regex.Pattern
        
        class LexicalAnalyzer(private val input: String) {
            private val tokens: MutableList<String> =
                Pattern.compile("$pattern")
                    .matcher(input).run {
                        val matchedTokens =
                            results().map { it.group() }.toList() ?: throw ParseException("Unexpected error")
                            
                        if (matchedTokens.joinToString("", "", "") != input.replace(" ", "")) {
                            throw ParseException("Invalid input")
                        }
                        
                        matchedTokens.toMutableList()
                    }
            private var token: Token = next()
            
            fun token(): Token = token
            
            fun nextToken() {
                if (token.typeToken != TypeToken.END) {
                    token = next()
                }
            }
            
            private fun next(): Token =
                if (tokens.isEmpty()) {
                    Token(TypeToken.END, "$")
                } else {
                    val t = tokens.removeFirst()
                    val typeToken = TypeToken.find(t) ?: throw ParseException("Unexpected error")
                    
                    Token(typeToken, t)
                }
        }
    """.trimIndent()
    }

    private fun generateParser(packageName: String, grammar: Grammar): String {
        val firstFollow = FirstFollow(grammar).also { it.calculate() }

        val treeExtenders = buildString {
            grammar.rules().distinctBy { it.definition.name }.forEach { rule ->
                val className = rule.definition.name.first().uppercase() + rule.definition.name.drop(1)

                appendLine("|        class $className(node: String) : Tree(node) {")
                if (rule.definition.calculatedAttr != null) {
                    val attr = rule.definition.calculatedAttr.drop(1).dropLast(1)
                    val varName = attr.substringBefore(":")
                    val varType = attr.substringAfter(": ")

                    appendLine("|            var $varName by Delegates.notNull<$varType>()")
                }
                appendLine("        }")
            }
        }

        val functions = buildString {
            grammar.rules().groupBy { it.definition }.forEach { (ruleDefinition, rules) ->
                val className = ruleDefinition.name.first().uppercase() + ruleDefinition.name.drop(1)
                val attr = ruleDefinition.inheritedAttr
                val params = attr?.drop(1)?.dropLast(1) ?: ""
                appendLine("|    private fun ${ruleDefinition.name}($params) : $className {")
                appendLine("|        val res = $className(\"$className\")")
                appendLine()
                appendLine("|        when(lex.token().typeToken) {")

                rules.map { it.body }.forEach { body: RuleBody ->
                    if (body.ruleCallings.isEmpty()) { // epsilon
                        val first1 = firstFollow.follow[ruleDefinition.name]!!
                        appendLine("|            ${first1.joinToString(separator = ", ")} -> {")
                        appendLine("|                res.add(\"eps\")")
                        if (body.code != null) {
                            val code = body.code.drop(1).dropLast(1).replace("$", "res.")
                            appendLine("|                $code")
                        }
                        appendLine("|            }")
                    } else {
                        val f = body.ruleCallings.first()
                        val first1: Set<String> =
                            if (f.isToken()) setOf(f.name) else firstFollow.first[f.name]!!
                        appendLine("|            ${first1.joinToString(separator = ", ")} -> {")
                        body.ruleCallings.forEachIndexed { i, rc ->
                            if (rc.isToken()) {
                                appendLine("|                ensure(${rc.name})")
                                appendLine("|                val ${rc.name}$i = lex.token().text")
                                appendLine("|                res.add(lex.token().text)")
                                appendLine("|                lex.nextToken()")
                            } else {
                                val varName = "${rc.name}$i"
                                appendLine(
                                    "|                val $varName = ${rc.name}(${
                                        if (rc.heritableArgs != null) rc.heritableArgs.drop(
                                            1
                                        ).dropLast(1).replace("$", "res.") else ""
                                    })"
                                )
                                appendLine("|                res.add($varName)")
                            }
                            if (rc.code != null) {
                                val code = rc.code.drop(1).dropLast(1).replace("$", "res.")
                                appendLine("|                $code")
                            }
                        }
                        appendLine("|            }")
                    }
                }
                appendLine("|            else -> unexpectedToken()")

                appendLine("|        }")
                appendLine()
                appendLine("|        return res")
                appendLine("|    }")
            }
        }

        val imports = buildString {
            grammar.imports().forEach {
                appendLine("|import ${it.substringAfter("/").dropWhile { c -> c == ' ' }.substringBefore("/").dropLastWhile { c -> c == ' ' }}")
            }
        }

        return """
        |package $packageName
        |
        |import kotlin.properties.Delegates
        |import $packageName.TypeToken.*
        |
        $imports
        |
        |class Parser private constructor(private val lex: LexicalAnalyzer) {
        |
        $functions
        |
        |    private fun ensure(typeToken: TypeToken) {
        |       if (lex.token().typeToken != typeToken)
        |           throw ParseException(
        |               "Expected ${"$" + "typeToken"}, but found ${"$" + "{" + "lex.token().typeToken" + "}"}"
        |           )
        |    }
        |
        |    private fun unexpectedToken(): Tree {
        |        throw ParseException("Unexpected token ${"$" + "{" + "lex.token()" + "}"}")
        |    }
        |
        |    companion object {
        |        fun parse(s: String) : ${
            grammar.rules().first().definition.name.first().uppercase() + grammar.rules()
                .first().definition.name.drop(1)
        } {
        |            return Parser(LexicalAnalyzer(s)).${grammar.rules().first().definition.name}()
        |        }
        |
        $treeExtenders
        |    }
        |}
    """.trimMargin()
    }
}
