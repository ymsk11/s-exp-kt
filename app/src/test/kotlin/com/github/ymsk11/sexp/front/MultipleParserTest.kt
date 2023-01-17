package com.github.ymsk11.sexp.front

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MultipleParserTest {
    val sut = MultipleParser()

    @Test
    fun multipleParseTest() {
        val testCases = mapOf(
            "()()" to listOf(Atom.Nil, Atom.Nil),
            "((1)) (2)" to listOf(
                Cell(Cell(Atom.IntNumber(1), Atom.Nil), Atom.Nil),
                Cell(Atom.IntNumber(2), Atom.Nil)
            ),
            """
                (+ 1 2)
                (- 1 2)
            """.trimIndent() to listOf(
                Cell(Atom.Symbol("+"), Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Atom.Nil))),
                Cell(Atom.Symbol("-"), Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Atom.Nil)))
            ),
        )
        testCases.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
