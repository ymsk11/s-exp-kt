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
        else -> Nil // TODO: throw error
    }
    private fun parse(tokens: List<Token>): Sexp {
        println("TTT $tokens")
        if (tokens.isEmpty()) return Nil
        if (tokens.size == 1) {
            return parse(tokens.first())
        }

        val tokens = tokens.removeParen()

        val (car, cdr) = tokens.split()

        println("CAR $car")
        println("CDR $cdr")

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

    private fun List<Token>.removeParen() = this.filterIndexed { index, token ->
        ((index == 0 && token == Token.LParen) || (index == this.lastIndex && token == Token.RParen)).not()
    }

    private fun List<Token>.split() = if (this.first() == Token.LParen) {
        var nestCount = 0
        var carLastIndex = 0
        this.forEachIndexed { index, token ->
            when (token) {
                Token.LParen -> nestCount++
                Token.RParen -> {
                    nestCount--
                    if (nestCount == 0) {
                        carLastIndex = index
                        return@forEachIndexed
                    }
                }
                else -> {}
            }
        }
        Pair(
            this.subList(0, carLastIndex),
            this.subList(carLastIndex, this.lastIndex)
        )
    } else {
        Pair(
            listOf(this.first()),
            this.subList(1, this.size)
        )
    }
}
