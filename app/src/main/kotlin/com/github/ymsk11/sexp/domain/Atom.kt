package com.github.ymsk11.sexp.domain

sealed interface Atom : Sexp {
    data class Symbol(val value: String) : Atom {
        override fun toString(): String = value
    }

    data class Str(val value: String) : Atom {
        override fun toString(): String = value
    }

    data class IntNumber(val value: Int) : Atom {
        override fun toString(): String = "$value"
    }

    data class DoubleNumber(val value: Double) : Atom {
        override fun toString(): String = "$value"
    }

    object Nil : Atom
}
