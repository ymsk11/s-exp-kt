package com.github.ymsk11.sexp.back.operator

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

object Quote : Operator {
    override fun eval(args: Cell): Sexp = args.car
}
