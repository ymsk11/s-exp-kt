package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class If(
    private val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        val condition = parentEval(args.car)
        return if (condition == Atom.Nil) {
            parentEval(((args.cdr as Cell).cdr as Cell).car)
        } else {
            parentEval((args.cdr as Cell).car)
        }
    }
}
