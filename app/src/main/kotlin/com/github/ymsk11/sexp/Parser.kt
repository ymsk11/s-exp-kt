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
        if (tokens.isEmpty()) return Nil
        if (tokens.size == 1) {
            return parse(tokens.first())
        }
        println("HOGEHOGE $tokens")

        // 左右のLParen, RParenを取り除く
        val tokens = tokens.filterIndexed { index, token ->
            ((index == 0 && token == Token.LParen) || (index == tokens.lastIndex && token == Token.RParen)).not()
        }
        println("REMOVE LRParen $tokens")

        var car: List<Token> = emptyList()
        var cdr: List<Token> = emptyList()
        if (tokens.first() == Token.LParen) {
            // 一番最初がLParenだったら、Car部分までを取り出す
            var nestCount = 0
            var carLastIndex = 0
            println("Car is list of tokens")
            tokens.forEachIndexed { index, token ->
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
            car = tokens.subList(0, carLastIndex)
            cdr = tokens.subList(carLastIndex, tokens.lastIndex)
        } else {
            println("Car is First of Tokens")
            car = listOf(tokens.first())
            cdr = tokens.subList(1, tokens.size)
        }
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
}
