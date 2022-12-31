package com.github.ymsk11.sexp

import org.junit.jupiter.api.Test

class CellTest {
    @Test
    fun cellToString() {
        val cell = Cell(Atom("1"), Cell(Atom("2"), Atom("3")))
        assert(cell.toString() == "( 1 . ( 2 . 3 ) )")
    }
}
