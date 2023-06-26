/**
 *@author Said Ibragimov on 06.12.2022 02:56
 */

enum class Token(var value: String = "") {
    LAMBDA("lambda"),
    COLON(":"),
    COMMA(","),
    OR("|"),
    AND("&"),
    ADD("+"),
    MUL("*"),
    LPAREN("("),
    RPAREN(")"),
    VAR,
    NUM,

    END("$");

    fun getName() = when(this) {
        VAR, NUM -> name.lowercase()
        else -> value
    }

    override fun toString(): String = value
}
