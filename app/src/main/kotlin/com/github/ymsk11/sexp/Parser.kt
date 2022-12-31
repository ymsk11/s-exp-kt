package com.github.ymsk11.sexp

class Parser(
    private val tokenizer: Tokenizer = Tokenizer()
) {
    operator fun invoke(text: String): Sexp {
        val tokens = tokenizer(text)
        return parse(tokens)
    }

    private fun parse(token: Token): Sexp = when (token) {
        Token.Nil -> Nil
        is Token.Symbol -> Atom(token.value)
        else -> Nil
    }
    private fun parse(tokens: List<Token>): Sexp {
        if (tokens.isEmpty()) return Nil
        if (tokens.size == 1) {
            return parse(tokens.first())
        }

        val token = tokens.filterNot { it == Token.RParen || it == Token.LParen }
        val car = token.first()
        val cdr = token.subList(1, token.size)

        val cdrCell = if (cdr.isNotEmpty()) {
            when (val s = parse(cdr)) {
                is Atom -> Cell(s, Nil)
                else -> s
            }
        } else {
            Nil
        }

        return Cell(parse(car), cdrCell)
    }
}
