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

    object T : Token {
        override fun toString(): String = "Token.T"
    }

    data class Str(val value: String) : Token

    data class IntNumber(val value: Int) : Token

    data class DoubleNumber(val value: Double) : Token

    data class Symbol(val value: String) : Token
}
