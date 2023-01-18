package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Sexp

sealed interface Operator {
    fun eval(args: Sexp): Sexp
}
