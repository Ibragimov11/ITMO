package generated.calculator

data class Token(val typeToken: TypeToken, val text: String) {
    override fun toString(): String = "${typeToken.name}($text)"
}