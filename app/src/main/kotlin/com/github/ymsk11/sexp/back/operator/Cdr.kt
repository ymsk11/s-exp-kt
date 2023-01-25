package com.github.ymsk11.sexp.back.operator

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Cdr(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        val ret = parentEval(args.car)
        return (ret as Cell).cdr
    }
}
