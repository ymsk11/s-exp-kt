package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.back.operator.Addition
import com.github.ymsk11.sexp.back.operator.Car
import com.github.ymsk11.sexp.back.operator.Cdr
import com.github.ymsk11.sexp.back.operator.Cond
import com.github.ymsk11.sexp.back.operator.Cons
import com.github.ymsk11.sexp.back.operator.Define
import com.github.ymsk11.sexp.back.operator.Equal
import com.github.ymsk11.sexp.back.operator.If
import com.github.ymsk11.sexp.back.operator.IsAtom
import com.github.ymsk11.sexp.back.operator.Lambda
import com.github.ymsk11.sexp.back.operator.Mod
import com.github.ymsk11.sexp.back.operator.Multiplication
import com.github.ymsk11.sexp.back.operator.Operator
import com.github.ymsk11.sexp.back.operator.Progn
import com.github.ymsk11.sexp.back.operator.Quote
import com.github.ymsk11.sexp.back.operator.Subtraction
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
        "equal" to Equal(parentEval),
        "cons" to Cons(parentEval),
        "quote" to Quote,
        "if" to If(parentEval),
        "cond" to Cond(parentEval),
        "lambda" to Lambda,
        "define" to Define(parentEval, setEnvironment),
        "progn" to Progn(parentEval),
        "+" to Addition(parentEval),
        "*" to Multiplication(parentEval),
        "-" to Subtraction(parentEval),
        "mod" to Mod(parentEval)
    )
    fun find(key: String): Operator? = registered[key]
}
