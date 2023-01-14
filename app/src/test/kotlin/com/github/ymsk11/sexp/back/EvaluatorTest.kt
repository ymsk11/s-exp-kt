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
            parser("(cons (car (quote (1 2))) (cdr (quote (1 2))))") to Cell(Atom.IntNumber(1), Cell(Atom.IntNumber(2), Atom.Nil)),
            parser("(+ 1 2)") to Atom.IntNumber(3),
            parser("(+ (+ 1 2) (+ 3 4))") to Atom.IntNumber(10),
            parser("(+ 1 2 3 4 5 6 7 8 9 10)") to Atom.IntNumber(55),
            parser("(+ 1 (+ 2 3 4 5) (+ 6 7 (+ 8 9 10)))") to Atom.IntNumber(55),
            parser("(- 100 (+ (- 10 9) (+ 2 3 4 5) (+ 6 7 (+ 8 9 10))))") to Atom.IntNumber(45),
            parser("(atom 1)") to Atom.T,
            parser("(atom (quote (1 2)))") to Atom.Nil,
            parser("(atom (+ 1 2 3))") to Atom.T,
            parser("(equal (+ 1 2) 3)") to Atom.T,
            parser("(equal 1 3)") to Atom.Nil,
            parser("(equal () nil)") to Atom.T,
            parser("(equal (quote (1 2 3)) (quote (1 2 3)))") to Atom.T,
            parser("(equal (quote (1 2 3)) (quote (1 2 (3))))") to Atom.Nil,
            parser("(if t 1 2)") to Atom.IntNumber(1),
            parser("(if nil 1 2)") to Atom.IntNumber(2),
            parser("(if \"text\" (+ 1 2) (hoge))") to Atom.IntNumber(3),
            parser("(if nil (hoge) (+ 3 4))") to Atom.IntNumber(7),
            parser("(cond (nil 1) (t 2))") to Atom.IntNumber(2),
            parser("(cond (t 1) (nil hoge))") to Atom.IntNumber(1),
            parser("(cond (nil hoge) ((equal 1 2) fuga) ((equal 1 1) (+ 3 2)))") to Atom.IntNumber(5)
        )

        testCase.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
