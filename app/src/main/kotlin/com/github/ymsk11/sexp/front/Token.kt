package com.github.ymsk11.sexp.front

sealed interface Token {
    object LParen : Token {
        override fun toString(): String = "Token.LParen"
    }
    object RParen : Token {
        override fun toString(): String = "Token.RParen"
    }

    object Dot : Token {
        override fun toString(): String = "Token.Dot"
    }

    object Nil : Token {
        override fun toString(): String = "Token.Nil"
    }

    @JvmInline
    value class Symbol(val value: String) : Token

    companion object {
        fun from(text: String) = when (text) {
            "(" -> LParen
            ")" -> RParen
            "." -> Dot
            "nil" -> Nil
            else -> Symbol(text)
        }
    }
}