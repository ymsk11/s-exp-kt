package com.github.ymsk11.sexp

class Parser(
    private val tokenizer: Tokenizer = Tokenizer()
) {
    operator fun invoke(text: String): Sexp {
        val tokens = tokenizer(text)
        return invoke(tokens)
    }

    operator fun invoke(tokens: List<Token>): Sexp {
        if (tokens.size > 1) {
            val token = tokens.filterNot { it == Token.RParen || it == Token.LParen }
            return Cell(invoke(token), Nil)
        } else {
            val token = tokens[0]
            return when (token) {
                Token.Nil -> Nil
                is Token.Symbol -> Atom(token.value)
                else -> Nil
            }
        }
    }
}
