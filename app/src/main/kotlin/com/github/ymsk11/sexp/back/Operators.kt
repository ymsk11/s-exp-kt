package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Sexp

class Operators(
    private val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
    private val parentEval: (Sexp) -> Sexp,
) {
    private val registered = mapOf(
        "car" to Car(parentEval),
        "cdr" to Cdr(parentEval),
        "atom" to IsAtom(parentEval),
        "cons" to Cons(parentEval),
        "quote" to Quote,
        "define" to Define(parentEval, setEnvironment),
        "+" to Addition(parentEval),
        "*" to Multiplication(parentEval),
        "-" to Subtraction(parentEval)
    )
    fun find(key: String): Operator? = registered[key]
}
