package com.github.ymsk11.sexp

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ParserTest {
    private val sut = Parser()
    @Test
    fun parseTest() {
        val testCases = mapOf(
            "a" to Atom("a"),
            "nil" to Nil,
            "(1)" to Cell(Atom("1"), Nil),
            "(1 2)" to Cell(Atom("1"), Cell(Atom("2"), Nil)),
            "(1 2 3)" to Cell(Atom("1"), Cell(Atom("2"), Cell(Atom("3"), Nil))),
        )
        testCases.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
