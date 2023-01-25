package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Cond(
    private val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        args.forEach {
            if (it is Cell) {
                if (parentEval(it.car) != Atom.Nil) {
                    return parentEval((it.cdr as Cell).car)
                }
            }
        }
        throw IllegalArgumentException("cond statement error")
    }
}
