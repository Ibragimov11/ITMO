package generated.calculator

import java.util.regex.Pattern

enum class TypeToken(val pattern: Pattern) {
    PLUS(Pattern.compile("\\+")),
    MINUS(Pattern.compile("-")),
    MUL(Pattern.compile("\\*")),
    DIV(Pattern.compile("/")),
    NUM(Pattern.compile("(0|[1-9][0-9]*)")),
    OPEN(Pattern.compile("\\(")),
    CLOSE(Pattern.compile("\\)")),

    END(Pattern.compile("\\$"));

    companion object {
        fun find(s: String): TypeToken? = values().find { it.pattern.matcher(s).matches() }
    }
}