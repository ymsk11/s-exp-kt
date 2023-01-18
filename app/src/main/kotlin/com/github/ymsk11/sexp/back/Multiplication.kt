package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

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
