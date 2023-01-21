package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Cons(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        val first = parentEval(args.car)
        val second = parentEval((args.cdr as Cell).car)
        return Cell(first, second)
    }
}
