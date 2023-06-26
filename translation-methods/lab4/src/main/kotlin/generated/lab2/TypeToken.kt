package generated.lab2

import java.util.regex.Pattern

enum class TypeToken(val pattern: Pattern) {
    LAMBDA(Pattern.compile("lambda")),
    COMMA(Pattern.compile(",")),
    OR(Pattern.compile("\\|")),
    AND(Pattern.compile("&")),
    MUL(Pattern.compile("\\*")),
    ADD(Pattern.compile("\\+")),
    OPEN(Pattern.compile("\\(")),
    CLOSE(Pattern.compile("\\)")),
    COLON(Pattern.compile(":")),
    NUM(Pattern.compile("(0|[1-9][0-9]*)")),
    VAR(Pattern.compile("[a-z][A-Z]*")),

    END(Pattern.compile("\\$"));

    companion object {
        fun find(s: String): TypeToken? = values().find { it.pattern.matcher(s).matches() }
    }
}