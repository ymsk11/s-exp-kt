package com.github.ymsk11.sexp.front

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TokenizerTest {
    private val sut = Tokenizer()
    @Test
    fun testCase() {
        val testCases = mapOf(
            "1" to listOf(Token.IntNumber(1)),
            "(1)" to listOf(Token.LParen, Token.IntNumber(1), Token.RParen),
            "(1 (2 3))" to listOf(
                Token.LParen,
                Token.IntNumber(1),
                Token.LParen,
                Token.IntNumber(2),
                Token.IntNumber(3),
                Token.RParen,
                Token.RParen
            ),
            "nil" to listOf(Token.Nil),
            "(nil)" to listOf(Token.LParen, Token.Nil, Token.RParen),
            "\"hello world\"" to listOf(Token.Str("\"hello world\"")),
            "(a\"Hello, ()()World\")" to listOf(Token.LParen, Token.Symbol("a"), Token.Str("\"Hello, ()()World\""), Token.RParen),
            "nila" to listOf(Token.Symbol("nila")),
            "anil" to listOf(Token.Symbol("anil")),
            "(nil\"\")" to listOf(Token.LParen, Token.Nil, Token.Str("\"\""), Token.RParen),
            "+3" to listOf(Token.IntNumber(3)),
            "-3" to listOf(Token.IntNumber(-3)),
            "+" to listOf(Token.Symbol("+")),
            "-3.0" to listOf(Token.DoubleNumber(-3.0)),
            "+3.0" to listOf(Token.DoubleNumber(+3.0)),
        )
        testCases.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
