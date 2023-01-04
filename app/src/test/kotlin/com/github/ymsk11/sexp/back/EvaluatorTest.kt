package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Nil
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EvaluatorTest {
    private val sut = Evaluator()

    @Test
    fun evaluateTest() {
        val testCase = mapOf(
            Cell(Atom("quote"), Cell(Atom("1"), Nil)) to Cell(Atom("1"), Nil)
        )

        testCase.forEach { input, expect ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
