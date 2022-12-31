package com.github.ymsk11.sexp

import org.junit.jupiter.api.Test

class ParserTest {
    private val sut = Parser()
    @Test
    fun parseTest() {
        val inputText = "a"
        assert(sut(inputText) == Atom(inputText))
    }
}
