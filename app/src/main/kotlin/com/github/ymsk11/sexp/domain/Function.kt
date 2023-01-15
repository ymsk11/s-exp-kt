package com.github.ymsk11.sexp.domain

data class Function(
    val args: Cell,
    val fn: Cell,
) : Sexp
