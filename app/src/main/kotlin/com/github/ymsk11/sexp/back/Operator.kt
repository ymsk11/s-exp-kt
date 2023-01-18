package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

interface Operator {
    val symbol: String
    fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp
}

class Fold(
    private val accum: Atom,
    private val fn: (Atom, Atom) -> Atom
) : Operator {
    override val symbol: String = "fold"
    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Atom {
        println("$accum $args")
        if (args is Atom.Nil) return accum
        if (args is Cell) {
            val next = fn(accum, eval(args.car) as Atom)
            return Fold(next, fn).eval(args.cdr, setEnvionment, eval)
        }
        throw IllegalArgumentException("foldに失敗しました")
    }
}

class Addition() : Operator {
    override val symbol: String = "+"

    private val fn = { a: Atom, b: Atom ->
        if (a is Atom.IntNumber && b is Atom.IntNumber) {
            Atom.IntNumber(a.value + b.value)
        } else {
            throw IllegalArgumentException("引数が整数ではありません")
        }
    }

    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp {
        return Fold(Atom.IntNumber(0), fn).eval(args, setEnvionment, eval)
    }
}

class Multiplication() : Operator {
    override val symbol: String = "*"

    private val fn = { a: Atom, b: Atom ->
        if (a is Atom.IntNumber && b is Atom.IntNumber) {
            Atom.IntNumber(a.value * b.value)
        } else {
            throw IllegalArgumentException("error")
        }
    }

    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp {
        return Fold(Atom.IntNumber(1), fn).eval(args, setEnvionment, eval)
    }
}

class Subtraction() : Operator {
    override val symbol: String = "-"

    private val fn = { a: Atom, b: Atom ->
        if (a is Atom.IntNumber && b is Atom.IntNumber) {
            Atom.IntNumber(a.value - b.value)
        } else {
            throw IllegalArgumentException("error")
        }
    }
    override fun eval(args: Sexp, setEnvionment: (Atom.Symbol, Sexp) -> Unit, eval: (Sexp) -> Sexp): Sexp {
        val accum = (args as Cell).car as Atom.IntNumber
        return Fold(accum, fn).eval(args.cdr, setEnvionment, eval)
    }
}
