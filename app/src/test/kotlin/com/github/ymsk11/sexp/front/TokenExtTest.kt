package com.github.ymsk11.sexp.front

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TokenExtTest {
    @Test
    fun checkParenTest() {
        val testCases = mapOf(
            listOf(Token.LParen, Token.RParen) to mapOf(0 to 1),
            listOf(Token.LParen, Token.RParen, Token.LParen, Token.RParen) to mapOf(0 to 1, 2 to 3),
            listOf(Token.LParen, Token.LParen, Token.RParen, Token.LParen, Token.RParen, Token.RParen) to mapOf(0 to 5, 1 to 2, 3 to 4),
        )
        testCases.forEach { (input, expect) ->
            assertThat(input.checkParenCorresponding()).isEqualTo(expect)
        }
    }
}
