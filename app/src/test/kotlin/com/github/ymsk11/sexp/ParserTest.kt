package com.github.ymsk11.sexp

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ParserTest {
    private val sut = Parser()
    @Test
    fun parseTest() {
        val testCases = mapOf(
            "a" to Atom("a")
        )
        testCases.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
