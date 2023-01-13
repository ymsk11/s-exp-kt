package com.github.ymsk11.sexp.domain

data class Cell(
    val car: Sexp,
    val cdr: Sexp,
) : Sexp, Iterable<Sexp> {
    override fun iterator(): Iterator<Sexp> = CellIterator(this)

    override fun toString(): String = "( $car . $cdr )"
}

private class CellIterator(
    private var cell: Sexp
) : Iterator<Sexp> {
    override fun hasNext(): Boolean {
        if (cell is Atom) return false // cdr 部分が Atom.Nil以外でもhasNextはfalseとする
        return true
    }

    override fun next(): Sexp {
        return when (val target = cell) {
            is Atom -> target
            is Cell -> {
                cell = target.cdr
                target.car
            }
        }
    }
}
