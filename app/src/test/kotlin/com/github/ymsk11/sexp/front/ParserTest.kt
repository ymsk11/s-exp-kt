package com.github.ymsk11.sexp.front

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ParserTest {
    private val sut = Parser()
    @Test
    fun parseTest() {
        val testCases = mapOf(
            "a" to Atom.Symbol("a"),
            "nil" to Atom.Nil,
            "(1)" to Cell(Atom.Symbol("1"), Atom.Nil),
            "(1 2)" to Cell(Atom.Symbol("1"), Cell(Atom.Symbol("2"), Atom.Nil)),
            "(1 2 3)" to Cell(Atom.Symbol("1"), Cell(Atom.Symbol("2"), Cell(Atom.Symbol("3"), Atom.Nil))),
            "((1))" to Cell(Cell(Atom.Symbol("1"), Atom.Nil), Atom.Nil),
            "(1 (2))" to Cell(Atom.Symbol("1"), Cell(Cell(Atom.Symbol("2"), Atom.Nil), Atom.Nil)),
            "(1 (2) (3))" to Cell(Atom.Symbol("1"), Cell(Cell(Atom.Symbol("2"), Atom.Nil), Cell(Cell(Atom.Symbol("3"), Atom.Nil), Atom.Nil))),
            "(((1)) (2))" to Cell(Cell(Cell(Atom.Symbol("1"), Atom.Nil), Atom.Nil), Cell(Cell(Atom.Symbol("2"), Atom.Nil), Atom.Nil)),
            "(((1 (2))))" to Cell(
                Cell(
                    Cell(
                        Atom.Symbol("1"),
                        Cell(
                            Cell(Atom.Symbol("2"), Atom.Nil),
                            Atom.Nil
                        )
                    ),
                    Atom.Nil
                ),
                Atom.Nil
            ),
            "(1 . 2)" to Cell(Atom.Symbol("1"), Atom.Symbol("2")),
            "((1 . 2) . (3 4))" to Cell(Cell(Atom.Symbol("1"), Atom.Symbol("2")), Cell(Atom.Symbol("3"), Cell(Atom.Symbol("4"), Atom.Nil)))
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
