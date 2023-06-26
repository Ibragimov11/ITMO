const val EPS = "ε"

data class GeneratorException(val msg: String) : Exception(msg)

class FirstFollow(val grammar: Grammar) {
    val first = mutableMapOf<String, MutableSet<String>>()
    val follow = mutableMapOf<String, MutableSet<String>>()

    fun calculate() {
        calculateFirst()
        calculateFollow()
    }

    private fun calculateFirst() {
        grammar.rules()
            .map { it.definition.name }
            .distinct()
            .forEach {
                first[it] = mutableSetOf()
            }

        var changed = true
        while (changed) {
            changed = false

            for (rule in grammar.rules()) {
                val name = rule.definition.name
                val body = rule.body

                val current = first[name]!!
                val set = getSetFirst(body)

                if (!current.containsAll(set)) {
                    changed = true
                    current.addAll(set)
                }
            }
        }
    }

    private fun calculateFollow() {
        grammar.rules()
            .map { it.definition.name }
            .distinct()
            .forEach {
                follow[it] = mutableSetOf()
            }

        val firstRuleName = grammar.rules().first().definition.name
        follow[firstRuleName]!!.add("END")

        var changed = true
        while (changed) {
            changed = false

            for (rule in grammar.rules()) {
                val name = rule.definition.name
                val body = rule.body

                body.ruleCallings.forEachIndexed { i, ruleCalling ->
                    if (!ruleCalling.isToken()) {
                        val current = follow[ruleCalling.name]!!
                        val set = getSetFollow(body.ruleCallings, i + 1, name)

                        if (!current.containsAll(set)) {
                            changed = true
                            current.addAll(set)
                        }
                    }
                }
            }
        }
    }

    private fun getSetFirst(body: RuleBody): Set<String> =
        if (body.isEpsRule()) {
            setOf(EPS)
        } else {
            val firstCalling = body.ruleCallings.first()
            if (firstCalling.isToken()) {
                setOf(firstCalling.name)
            } else {
                first[firstCalling.name] ?: throw GeneratorException("Something went wrong")
            }
        }

    private fun getSetFollow(ruleCallings: List<TokenOrRuleCalling>, i: Int, name: String): Set<String> =
        if (i == ruleCallings.size) {
            follow[name]!!
        } else {
            val next = ruleCallings[i]

            if (next.isToken()) {
                setOf(next.name)
            } else {
                val res = mutableSetOf<String>().apply {
                    addAll(first[next.name]!!)
                }

                if (res.contains(EPS)) {
                    res.remove(EPS)
                    res.addAll(getSetFollow(ruleCallings, i + 1, name))
                }

                res
            }
        }
}

fun RuleBody.isEpsRule() = ruleCallings.isEmpty()
fun TokenOrRuleCalling.isToken() = name.first().isUpperCase()
