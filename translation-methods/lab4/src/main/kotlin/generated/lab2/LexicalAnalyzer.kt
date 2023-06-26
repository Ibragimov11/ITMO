package generated.lab2

import java.util.regex.Pattern

class LexicalAnalyzer(private val input: String) {
    private val tokens: MutableList<String> =
        Pattern.compile("lambda|,|\\||&|\\*|\\+|\\(|\\)|:|(0|[1-9][0-9]*)|[a-z][A-Z]*")
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