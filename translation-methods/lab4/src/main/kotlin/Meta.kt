class Grammar {
    lateinit var name: String
    private val imports = mutableListOf<String>()
    private val tokens = mutableListOf<Terminal>()
    private val rules = mutableListOf<Rule>()

    fun imports() = imports

    fun tokens() = tokens

    fun rules() = rules

    fun addImport(text: String) {
        imports.add(text)
    }

    fun addToken(name: String, regexp: String) {
        tokens.add(Terminal(name, regexp))
    }

    fun addRule(nonTerminal: RuleDefinition, rule: RuleBody) {
        rules.add(Rule(nonTerminal, rule))
    }
}

data class Terminal(val name: String, val regexp: String)

data class Rule(val definition: RuleDefinition, val body: RuleBody)

data class RuleDefinition(val name: String, val inheritedAttr: String?, val calculatedAttr: String?)

data class RuleBody(val ruleCallings: List<TokenOrRuleCalling>, val code: String?) // code only for rule -> EPS with {code}

data class TokenOrRuleCalling(val name: String, val heritableArgs: String?, val code: String?)
