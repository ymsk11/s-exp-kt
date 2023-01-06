package com.github.ymsk11.sexp.domain

import org.junit.jupiter.api.Test

class CellTest {
    @Test
    fun cellToString() {
        val cell = Cell(Atom.Symbol("1"), Cell(Atom.Symbol("2"), Atom.Symbol("3")))
        assert(cell.toString() == "( 1 . ( 2 . 3 ) )")
    }
}
