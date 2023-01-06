package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.front.Parser
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EvaluatorTest {
    private val sut = Evaluator()
    private val parser = Parser()

    @Test
    fun evaluateTest() {
        val testCase = mapOf(
            parser("(quote a)") to Atom.Symbol("a"),
            parser("(quote (1 2 3))") to Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Cell(Atom.IntNumber(3), Atom.Nil))),
            parser("(car (quote (1 2)))") to Atom.IntNumber(1),
            parser("(cdr (quote (1 2)))") to Cell(Atom.IntNumber(2), Atom.Nil),
            parser("(cons 1 2)") to Cell(Atom.IntNumber(1), Atom.IntNumber(2)),
            parser("(cons (car (quote (1 2))) (cdr (quote (1 2))))") to Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Atom.Nil))
        )

        testCase.forEach { input, expect ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
