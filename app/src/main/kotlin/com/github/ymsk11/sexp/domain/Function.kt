package com.github.ymsk11.sexp.domain

data class Function(
    val args: Sexp,
    val fn: Sexp,
) : Sexp
