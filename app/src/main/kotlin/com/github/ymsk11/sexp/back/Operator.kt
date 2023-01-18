package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

sealed interface Operator {
    val setEnvironment: (Atom.Symbol, Sexp) -> Unit
    val parentEval: (Sexp) -> Sexp
    fun eval(args: Sexp): Sexp
}

class Operators(
    private val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
    private val parentEval: (Sexp) -> Sexp,
) {
    private val registered = mapOf(
        "+" to Addition(setEnvironment, parentEval),
        "*" to Multiplication(setEnvironment, parentEval),
        "-" to Subtraction(setEnvironment, parentEval)
    )
    fun find(key: String): Operator? = registered[key]
}

class Addition(
    override val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
    override val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        if (args is Cell) {
            return args.fold(Atom.IntNumber(0)) { acc, sexp ->
                Atom.IntNumber(acc.value + (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数が整数ではありません")
    }
}

class Multiplication(
    override val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
    override val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        if (args is Cell) {
            return args.fold(Atom.IntNumber(1)) { acc, sexp ->
                Atom.IntNumber(acc.value * (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数が整数ではありません")
    }
}

class Subtraction(
    override val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
    override val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        if (args is Cell && args.car is Atom.IntNumber && args.cdr is Cell) {
            return args.cdr.fold(args.car) { acc, sexp ->
                Atom.IntNumber(acc.value - (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数が整数ではありません")
    }
}
