package com.github.ymsk11.sexp

sealed interface Token {
    object LParen : Token
    object RParen : Token
    @JvmInline
    value class Symbol(val value: String) : Token

    companion object {
        fun from(text: String) = when (text) {
            "(" -> LParen
            ")" -> RParen
            else -> Symbol(text)
        }
    }
}
