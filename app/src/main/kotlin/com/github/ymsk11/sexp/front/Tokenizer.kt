package com.github.ymsk11.sexp.front

class Tokenizer {

    operator fun invoke(text: String): List<Token> {
        val list = mutableListOf<Token>()
        var index = 0
        while (index <= text.lastIndex) {
            val target = text.substring(index)
            when {
                target[0].isWhitespace() -> {
                    index++
                }
                target[0] == '(' -> {
                    list += Token.LParen
                    index++
                }
                target[0] == ')' -> {
                    list += Token.RParen
                    index++
                }
                target[0] == '.' -> {
                    list += Token.Dot
                    index++
                }
                else -> {
                    val last = target.indexOfFirst {
                        it.isWhitespace() ||
                            it == '(' ||
                            it == ')' ||
                            it == '.'
                    }
                    val value = if (last < 0) {
                        target.substring(0)
                    } else {
                        target.substring(0, last)
                    }
                    list += Token.from(value)
                    index += value.length
                }
            }
        }
        return list
    }
}
