package com.github.ymsk11.sexp.back.operator

import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

sealed interface Operator {
    fun eval(args: Cell): Sexp
}
