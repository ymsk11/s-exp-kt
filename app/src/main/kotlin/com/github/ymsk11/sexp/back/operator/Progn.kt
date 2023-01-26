package com.github.ymsk11.sexp.back.operator

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Progn(
    private val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        return args.map(parentEval).last()
    }
}
