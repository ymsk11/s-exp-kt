package com.github.ymsk11.sexp

@JvmInline
value class Atom(val value: String): Sexp {
    override fun toString(): String = value
}