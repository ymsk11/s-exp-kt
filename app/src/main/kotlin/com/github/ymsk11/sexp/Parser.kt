package com.github.ymsk11.sexp

import kotlin.IllegalArgumentException

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
        else -> throw IllegalArgumentException()
    }
    private fun parse(tokens: List<Token>): Sexp {
        if (tokens.isEmpty()) return Nil
        if (tokens.size == 2 && tokens.first() == Token.LParen && tokens.last() == Token.RParen) return Nil
        if (tokens.size == 1) return parse(tokens.first())

        var nestCount = 0
        var canRemoveParen = true
        tokens.forEachIndexed { index, token ->
            when (token) {
                Token.LParen -> nestCount++
                Token.RParen -> {
                    nestCount--
                    if (nestCount == 0 && index != tokens.lastIndex) {
                        canRemoveParen = false
                    }
                }
                else -> {}
            }
        }
        val tokens = if (canRemoveParen) {
            tokens.removeParen()
        } else {
            tokens
        }

        val (car, cdr) = tokens.split()

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
        var flag = true
        this.forEachIndexed { index, token ->
            when (token) {
                Token.LParen -> nestCount++
                Token.RParen -> {
                    nestCount--
                    if (nestCount == 0 && flag) {
                        carLastIndex = index
                        flag = false
                    }
                }
                else -> {}
            }
        }
        Pair(
            this.take(carLastIndex + 1),
            listOf(Token.LParen) + this.drop(carLastIndex + 1) + listOf(Token.RParen)
        )
    } else {
        Pair(
            listOf(this.first()),
            this.drop(1)
        )
    }
}
