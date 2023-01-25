package com.github.ymsk11.sexp.back.operator

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Addition(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        return args.fold(Atom.IntNumber(0)) { acc, sexp ->
            Atom.IntNumber(acc.value + (parentEval(sexp) as Atom.IntNumber).value)
        }
    }
}
