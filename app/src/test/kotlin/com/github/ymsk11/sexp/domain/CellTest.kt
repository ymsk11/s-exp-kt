package com.github.ymsk11.sexp.domain

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CellTest {
    @Test
    fun cellToString() {
        val testCases = mapOf(
            Cell(Atom.Symbol("1"), Cell(Atom.Symbol("2"), Atom.Symbol("3"))) to "( 1 . ( 2 . 3 ) )"
        )
        testCases.forEach { (input, expect) ->
            assertThat(input.toString()).isEqualTo(expect)
        }
    }

    @Test
    fun cellIterator() {
        val testCases = mapOf(
            Cell(Atom.Symbol("1"), Cell(Atom.Symbol("2"), Cell(Atom.Symbol("3"), Atom.Nil)))
                to listOf(Atom.Symbol("1"), Atom.Symbol("2"), Atom.Symbol("3")),
            Cell(Atom.Symbol("1"), Atom.Symbol("2"))
                to listOf(Atom.Symbol("1")),
            Cell(Cell(Atom.Symbol("1"), Atom.Symbol("2")), Cell(Atom.Symbol("3"), Atom.Nil))
                to listOf(Cell(Atom.Symbol("1"), Atom.Symbol("2")), Atom.Symbol("3"))
        )
        testCases.forEach { (input, expect) ->
            assertThat(input.toList()).isEqualTo(expect)
        }
    }

    @Test
    fun replaceCell() {
        val testCases = mapOf(
            Cell(Atom.Symbol("x"), Atom.Symbol("y")).replace(Atom.Symbol("x"), Atom.Symbol("y"))
                to Cell(Atom.Symbol("y"), Atom.Symbol("y")),
            Cell(Cell(Atom.Symbol("x"), Atom.Nil), Cell(Atom.Symbol("y"), Atom.Nil)).replace(Atom.Symbol("x"), Atom.Symbol("y"))
                to Cell(Cell(Atom.Symbol("y"), Atom.Nil), Cell(Atom.Symbol("y"), Atom.Nil)),
            Cell(Atom.Symbol("x"), Atom.Nil).replace(Atom.Symbol("x"), Cell(Atom.Symbol("x"), Atom.Nil))
                to Cell(Cell(Atom.Symbol("x"), Atom.Nil), Atom.Nil)
        )
        testCases.forEach { (input, expect) ->
            assertThat(input).isEqualTo(expect)
        }
    }
}
