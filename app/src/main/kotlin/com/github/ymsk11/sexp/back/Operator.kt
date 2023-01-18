package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

sealed interface Operator {
    fun eval(args: Sexp): Sexp
}

class Operators(
    private val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
    private val parentEval: (Sexp) -> Sexp,
) {
    private val registered = mapOf(
        "+" to Addition(parentEval),
        "*" to Multiplication(parentEval),
        "-" to Subtraction(parentEval)
    )
    fun find(key: String): Operator? = registered[key]
}

class Addition(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        if (args is Cell) {
            return args.fold(Atom.IntNumber(0)) { acc, sexp ->
                Atom.IntNumber(acc.value + (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数がおかしい")
    }
}

class Multiplication(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        if (args is Cell) {
            return args.fold(Atom.IntNumber(1)) { acc, sexp ->
                Atom.IntNumber(acc.value * (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数がおかしい")
    }
}

class Subtraction(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        if (args is Cell && args.car is Atom.IntNumber && args.cdr is Cell) {
            return args.cdr.fold(args.car) { acc, sexp ->
                Atom.IntNumber(acc.value - (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数がおかしい")
    }
}
