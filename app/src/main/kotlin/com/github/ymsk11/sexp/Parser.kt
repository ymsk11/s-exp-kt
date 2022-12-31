package com.github.ymsk11.sexp

class Parser(
    private val tokenizer: Tokenizer = Tokenizer()
) {
    operator fun invoke(text: String): Sexp {
        val token = tokenizer(text)[0]
        return when (token) {
            Token.Nil -> Nil
            is Token.Symbol -> Atom(token.value)
            else -> Nil
        }
    }
}
