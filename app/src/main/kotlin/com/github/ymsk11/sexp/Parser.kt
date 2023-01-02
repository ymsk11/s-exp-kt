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
        if (tokens.size == 2) throw IllegalArgumentException("括弧の対応がおかしい")

        val parenCorresponding = checkParenCorresponding(tokens)
        if (parenCorresponding[0] != tokens.lastIndex) {
            throw IllegalArgumentException("括弧で囲われていない")
        }

        val (car, cdr) = if (parenCorresponding.containsKey(1)) {
            val carRParenIndex = parenCorresponding[1]!!
            val car = tokens.subList(1, carRParenIndex + 1)
            val cdr: List<Token> = try {
                listOf(Token.LParen) + tokens.subList(carRParenIndex + 1, tokens.lastIndex) + listOf(Token.RParen)
            } catch (e: IndexOutOfBoundsException) {
                emptyList()
            }
            Pair(
                car,
                cdr,
            )
        } else {
            val cdr: List<Token> = try {
                listOf(Token.LParen) + tokens.subList(2, tokens.lastIndex) + listOf(Token.RParen)
            } catch (e: IndexOutOfBoundsException) {
                emptyList()
            }
            Pair(
                listOf(tokens[1]),
                cdr
            )
        }

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
