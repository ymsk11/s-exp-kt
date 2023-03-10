package com.github.ymsk11.sexp.front

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ParserTest {
    private val sut = Parser()
    @Test
    fun parseTest() {
        val testCases = mapOf(
            "a" to Atom.Symbol("a"),
            "nil" to Atom.Nil,
            "(1)" to Cell(Atom.IntNumber(1), Atom.Nil),
            "(1 2)" to Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Atom.Nil)),
            "(1 2 3)" to Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Cell(Atom.IntNumber(3), Atom.Nil))),
            "((1))" to Cell(Cell(Atom.IntNumber(1), Atom.Nil), Atom.Nil),
            "(1 (2))" to Cell(Atom.IntNumber(1), Cell(Cell(Atom.IntNumber(2), Atom.Nil), Atom.Nil)),
            "(1 (2) (3))" to Cell(Atom.IntNumber(1), Cell(Cell(Atom.IntNumber(2), Atom.Nil), Cell(Cell(Atom.IntNumber(3), Atom.Nil), Atom.Nil))),
            "(((1)) (2))" to Cell(Cell(Cell(Atom.IntNumber(1), Atom.Nil), Atom.Nil), Cell(Cell(Atom.IntNumber(2), Atom.Nil), Atom.Nil)),
            "(((1 (2))))" to Cell(
                Cell(
                    Cell(
                        Atom.IntNumber(1),
                        Cell(
                            Cell(Atom.IntNumber(2), Atom.Nil),
                            Atom.Nil
                        )
                    ),
                    Atom.Nil
                ),
                Atom.Nil
            ),
            "(1 . 2)" to Cell(Atom.IntNumber(1), Atom.IntNumber(2)),
            "((1 . 2) . (3 4))" to Cell(Cell(Atom.IntNumber(1), Atom.IntNumber(2)), Cell(Atom.IntNumber(3), Cell(Atom.IntNumber(4), Atom.Nil))),
            "(2.0 . -3.0)" to Cell(Atom.DoubleNumber(2.0), Atom.DoubleNumber(-3.0)),
            "t" to Atom.T,
        )
        testCases.forEach { (input, expect) ->
            println("TESTCASE".repeat(10))
            println("input $input")
            println("expect $expect")
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
