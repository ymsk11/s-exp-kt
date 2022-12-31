package com.github.ymsk11.sexp

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TokenizerTest {
    private val sut = Tokenizer()
    @Test
    fun testCase() {
        val testCases = mapOf(
            "1" to listOf(Token.Symbol("1")),
            "(1)" to listOf(Token.LParen, Token.Symbol("1"), Token.RParen),
            "(1 (2 3))" to listOf(
                Token.LParen,
                Token.Symbol("1"),
                Token.LParen,
                Token.Symbol("2"),
                Token.Symbol("3"),
                Token.RParen,
                Token.RParen
            ),
            "nil" to listOf(Token.Nil),
            "(nil)" to listOf(Token.LParen, Token.Nil, Token.RParen)
        )
        testCases.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
