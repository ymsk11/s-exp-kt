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
        if (tokens.size == 1) return parse(tokens.first())
        if (tokens.size == 2 && tokens.first() == Token.LParen && tokens.last() == Token.RParen) return Nil
        if (tokens.size == 2) throw IllegalArgumentException("括弧で囲われていない")

        val parenCorresponding = checkParenCorresponding(tokens)
        if (parenCorresponding[0] != tokens.lastIndex) {
            throw IllegalArgumentException("括弧で囲われていない")
        }

        val (car, cdr) = if (parenCorresponding[1] != null) {
            // Car部分がCellの場合
            Pair(
                tokens.subList(1, parenCorresponding[1]!! + 1),
                tokens.subList(parenCorresponding[1]!! + 1, tokens.lastIndex)
            )
        } else {
            // Car部分がAtomの場合
            Pair(
                listOf(tokens[1]),
                tokens.subList(2, tokens.lastIndex)
            )
        }

        return Cell(parse(car), parse(listOf(Token.LParen) + cdr + listOf(Token.RParen)))
    }

    fun checkParenCorresponding(tokens: List<Token>): Map<Int, Int> {
        val list = mutableListOf<Int>()
        val ret = mutableMapOf<Int, Int>()
        tokens.forEachIndexed { index, token ->
            when (token) {
                Token.LParen -> list.add(index)
                Token.RParen -> {
                    ret[list.last()] = index
                    list.removeAt(list.lastIndex)
                }
                else -> {}
            }
        }
        return ret
    }
}
