package com.github.ymsk11.sexp

sealed interface Token {
    object LParen : Token {
        override fun toString(): String = "Token.("
    }
    object RParen : Token {
        override fun toString(): String = "Token.)"
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
            "nil" -> Nil
            else -> Symbol(text)
        }
    }
}
