package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Function
import com.github.ymsk11.sexp.front.MultipleParser
import com.github.ymsk11.sexp.front.Parser
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EvaluatorTest {
    private val sut = Evaluator()
    private val parser = Parser()
    private val multipleParser = MultipleParser()

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
            parser("(* 11 (+ 10 1))") to Atom.IntNumber(121),
            parser("(mod 10 3)") to Atom.IntNumber(1),
            parser("(mod (+ 5 5) (+ 1 2))") to Atom.IntNumber(1),
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
            parser("(cond (nil hoge) ((equal 1 2) fuga) ((equal 1 1) (+ 3 2)))") to Atom.IntNumber(5),
            parser("(lambda (x) (+ x x))") to Function(
                Cell(Atom.Symbol("x"), Atom.Nil), Cell(Atom.Symbol("+"), Cell(Atom.Symbol("x"), Cell(Atom.Symbol("x"), Atom.Nil)))
            ),
            parser("((lambda (x) (+ x x)) 2)") to Atom.IntNumber(4),
            parser("((lambda (a b) (+ (+ a b) a) ) 1 2)") to Atom.IntNumber(4),
            parser("((lambda (a b) (+ a b)) (+ 1 2) (+ 3 4))") to Atom.IntNumber(10),
            parser("((lambda (a) (cond ((equal (mod a 15) 0) \"fizzbuzz\") ((equal (mod a 3) 0) \"fizz\") ((equal (mod a 5) 0) \"buzz\") (t a))) 3 )") to Atom.Str("\"fizz\""),
            parser("((lambda (a) (cond ((equal (mod a 15) 0) \"fizzbuzz\") ((equal (mod a 3) 0) \"fizz\") ((equal (mod a 5) 0) \"buzz\") (t a))) 5 )") to Atom.Str("\"buzz\""),
            parser("((lambda (a) (cond ((equal (mod a 15) 0) \"fizzbuzz\") ((equal (mod a 3) 0) \"fizz\") ((equal (mod a 5) 0) \"buzz\") (t a))) 15 )") to Atom.Str("\"fizzbuzz\""),
            parser("((lambda (a) (cond ((equal (mod a 15) 0) \"fizzbuzz\") ((equal (mod a 3) 0) \"fizz\") ((equal (mod a 5) 0) \"buzz\") (t a))) 30 )") to Atom.Str("\"fizzbuzz\""),
            parser("((lambda (a) (cond ((equal (mod a 15) 0) \"fizzbuzz\") ((equal (mod a 3) 0) \"fizz\") ((equal (mod a 5) 0) \"buzz\") (t a))) 1 )") to Atom.IntNumber(1),
        )

        testCase.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }

    @Test
    fun multipleEvaluateTest() {
        val testCases = mapOf(
            multipleParser("(+ 1 2) (+ 3 4)") to listOf(Atom.IntNumber(3), Atom.IntNumber(7)),
            // FIXME: defineの成功時の戻り値は検証したくないため、仕様としてAtom.Tを返すようにしてしまっている
            multipleParser("(define x (* 11 11)) x") to listOf(Atom.T, Atom.IntNumber(121)),
            multipleParser(
                """
                (define fizzbuzz (lambda (a)
                    (cond
                         ((equal (mod a 15) 0) "fizzbuzz")
                         ((equal (mod a 3 ) 0) "fizz")
                         ((equal (mod a 5 ) 0) "buzz")
                         (t a))))
                (fizzbuzz 10)
                (fizzbuzz 6)
                (fizzbuzz 30)
                (fizzbuzz 31)
                """.trimIndent()
            ) to listOf(
                Atom.T,
                Atom.Str("\"buzz\""),
                Atom.Str("\"fizz\""),
                Atom.Str("\"fizzbuzz\""),
                Atom.IntNumber(31)
            )
        )

        testCases.forEach { (input, expect) ->
            assertThat(sut(input)).isEqualTo(expect)
        }
    }
}
