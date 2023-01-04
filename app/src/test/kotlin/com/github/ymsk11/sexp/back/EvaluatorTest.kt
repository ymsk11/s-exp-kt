package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Nil
import com.github.ymsk11.sexp.front.Parser
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EvaluatorTest {
    private val sut = Evaluator()
    private val parser = Parser()

    @Test
    fun evaluateTest() {
        val testCase = mapOf(
            parser("(quote 1)") to Atom("1"),
            parser("(quote (1 2 3))") to Cell(Atom("1"), Cell(Atom("2"), Cell(Atom("3"), Nil))),
            parser("(car (quote (1 2)))") to Atom("1"),
            parser("(cdr (quote (1 2)))") to Cell(Atom("2"), Nil),
        )

        testCase.forEach { input, expect ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
