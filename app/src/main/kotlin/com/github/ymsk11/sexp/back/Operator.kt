package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

interface Operator {
    val symbol: String
    fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp
}

class Addition() : Operator {
    override val symbol: String = "+"

    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp {
        if (args is Cell) {
            return args.fold(Atom.IntNumber(0)) { acc, sexp ->
                Atom.IntNumber(acc.value + (eval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数が整数ではありません")
    }
}

class Multiplication() : Operator {
    override val symbol: String = "*"

    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp {
        if (args is Cell) {
            return args.fold(Atom.IntNumber(1)) { acc, sexp ->
                Atom.IntNumber(acc.value * (eval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数が整数ではありません")
    }
}

class Subtraction() : Operator {
    override val symbol: String = "-"

    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp {
        if (args is Cell && args.car is Atom.IntNumber && args.cdr is Cell) {
            return args.cdr.fold(args.car) { acc, sexp ->
                Atom.IntNumber(acc.value - (eval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数が整数ではありません")
    }
}
