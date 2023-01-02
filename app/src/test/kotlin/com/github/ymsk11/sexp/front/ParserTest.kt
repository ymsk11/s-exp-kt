package com.github.ymsk11.sexp.front

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Nil
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
            "((1))" to Cell(Cell(Atom("1"), Nil), Nil),
            "(1 (2))" to Cell(Atom("1"), Cell(Cell(Atom("2"), Nil), Nil)),
            "(1 (2) (3))" to Cell(Atom("1"), Cell(Cell(Atom("2"), Nil), Cell(Cell(Atom("3"), Nil), Nil))),
            "(((1)) (2))" to Cell(Cell(Cell(Atom("1"), Nil), Nil), Cell(Cell(Atom("2"), Nil), Nil)),
            "(((1 (2))))" to Cell(
                Cell(
                    Cell(
                        Atom("1"),
                        Cell(
                            Cell(Atom("2"), Nil),
                            Nil
                        )
                    ),
                    Nil
                ),
                Nil
            ),
            "(1 . 2)" to Cell(Atom("1"), Atom("2")),
            "((1 . 2) . (3 4))" to Cell(Cell(Atom("1"), Atom("2")), Cell(Atom("3"), Cell(Atom("4"), Nil)))
        )
        testCases.forEach { (input, expect) ->
            println("TESTCASE".repeat(10))
            println("input $input")
            println("expect $expect")
            assertThat(sut(input)).isEqualTo(expect)
        }
    }

    @Test
    fun checkParenTest() {
        val testCases = mapOf(
            listOf(Token.LParen, Token.RParen) to mapOf(0 to 1),
            listOf(Token.LParen, Token.RParen, Token.LParen, Token.RParen) to mapOf(0 to 1, 2 to 3),
            listOf(Token.LParen, Token.LParen, Token.RParen, Token.LParen, Token.RParen, Token.RParen) to mapOf(0 to 5, 1 to 2, 3 to 4),
        )
        testCases.forEach { (input, expect) ->
            assertThat(sut.checkParenCorresponding(input)).isEqualTo(expect)
        }
    }
}
