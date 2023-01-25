package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Subtraction(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        if (args.car is Atom.IntNumber && args.cdr is Cell) {
            return args.cdr.fold(args.car) { acc, sexp ->
                Atom.IntNumber(acc.value - (parentEval(sexp) as Atom.IntNumber).value)
            }
        }
        throw IllegalArgumentException("引数がおかしい")
    }
}
