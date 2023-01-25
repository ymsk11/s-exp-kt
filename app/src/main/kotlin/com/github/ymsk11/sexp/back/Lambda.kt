package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Function
import com.github.ymsk11.sexp.domain.Sexp

object Lambda : Operator {
    override fun eval(args: Cell): Sexp {
        return Function(
            args = args.car as Cell,
            fn = (args.cdr as Cell).car as Cell
        )
    }
}
