package com.github.ymsk11.sexp.front

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp
import kotlin.IllegalArgumentException

class Parser(
    private val tokenizer: Tokenizer = Tokenizer()
) {
    operator fun invoke(text: String): Sexp {
        val tokens = tokenizer(text)
        return parse(tokens)
    }
    operator fun invoke(tokens: List<Token>): Sexp {
        return parse(tokens)
    }

    private fun parse(token: Token): Sexp = when (token) {
        Token.Nil -> Atom.Nil
        Token.T -> Atom.T
        is Token.Symbol -> Atom.Symbol(token.value)
        is Token.Str -> Atom.Str(token.value)
        is Token.IntNumber -> Atom.IntNumber(token.value)
        is Token.DoubleNumber -> Atom.DoubleNumber(token.value)
        else -> {
            throw IllegalArgumentException("Tokenを変換できませんでした")
        }
    }
    private fun parse(tokens: List<Token>): Sexp {
        if (tokens.isEmpty()) return Atom.Nil
        if (tokens.size == 1) return parse(tokens.first())
        if (tokens.size == 2 && tokens.first() == Token.LParen && tokens.last() == Token.RParen) return Atom.Nil
        if (tokens.size == 2) throw IllegalArgumentException("括弧で囲われていない")

        val parenCorresponding = tokens.checkParenCorresponding()
        if (parenCorresponding[0] != tokens.lastIndex) {
            throw IllegalArgumentException("括弧で囲われていない")
        }

        val car = if (parenCorresponding[1] != null) {
            // car部分がCell
            tokens.subList(1, parenCorresponding[1]!! + 1)
        } else {
            // car部分がAtom
            listOf(tokens[1])
        }

        // tokenのcar部分の次のtokenがDotかどうかを調べる
        val hasDot: Boolean = tokens[1 + car.size] == Token.Dot
        // cdr部分のIndexはDot分ずらす
        val cdrStartIndex = 1 + car.size + if (hasDot) 1 else 0
        val cdrTokens = tokens.subList(cdrStartIndex, tokens.lastIndex)
        val cdr = if (hasDot) {
            // Dotが含まれる場合は、そのままをcdrTokensとする: ( a . b c ) -> cdr: b c となりエラー
            cdrTokens
        } else {
            // Dotが含まれない場合は、括弧のTokenをたす: (a b c) -> cdr: (b c)
            listOf(Token.LParen) + cdrTokens + Token.RParen
        }
        return Cell(parse(car), parse(cdr))
    }
}
