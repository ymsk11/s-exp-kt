package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

object Quote : Operator {
    override fun eval(args: Sexp): Sexp = (args as Cell).car
}
