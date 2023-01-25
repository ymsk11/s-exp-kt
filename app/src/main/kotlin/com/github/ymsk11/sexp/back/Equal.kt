package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Equal(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        val first = parentEval(args.car)
        val second = parentEval((args.cdr as Cell).car)
        return if (first == second) Atom.T else Atom.Nil
    }
}
