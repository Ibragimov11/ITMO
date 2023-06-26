enum class TYPE(private val spec: String) {
    INT("%d");

    fun specifier() = spec
}

enum class CKEYWORDS(private val s: String) {
    SCANF("scanf"),
    PRINTF("printf"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    L_BRACKET("("),
    R_BRACKET(")"),
    L_CURLY_BRACE("{"),
    R_CURLY_BRACE("}"),
    ASSIGN("="),
    SEMICOLON(";"),
    TAB("    ");

    override fun toString() = s
}