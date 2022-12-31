package com.github.ymsk11.sexp

class Parser(
    private val tokenizer: Tokenizer = Tokenizer()
) {
    operator fun invoke(text: String): Sexp {
        val tokens = tokenizer(text)
        return invoke(tokens)
    }

    operator fun invoke(tokens: List<Token>): Sexp {
        if (tokens.isEmpty()) return Nil
        if (tokens.size == 1) {
            return when (val token = tokens.first()) {
                Token.Nil -> Nil
                is Token.Symbol -> Atom(token.value)
                else -> Nil
            }
        }

        val token = tokens.filterNot { it == Token.RParen || it == Token.LParen }
        val car = token.first()
        val cdr = token.subList(1, token.size)

        if (cdr.isNotEmpty()) {
            return Cell(invoke(listOf(car)), Cell(invoke(cdr), Nil))
        } else {
            return Cell(invoke(listOf(car)), Nil)
        }
    }
}
