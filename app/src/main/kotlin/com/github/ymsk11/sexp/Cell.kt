package com.github.ymsk11.sexp

data class Cell(
    val car: Sexp,
    val cdr: Sexp,
) : Sexp {
    override fun toString(): String = "( $car . $cdr )"
}
