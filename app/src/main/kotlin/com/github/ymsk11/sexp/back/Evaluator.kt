package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    private fun fold(accum: Atom, sexp: Sexp, fn: (Atom, Atom) -> Atom): Atom {
        if (sexp is Atom.Nil) return accum
        if (sexp is Cell) {
            val next = fn(accum, eval(sexp.car) as Atom)
            return fold(next, sexp.cdr, fn)
        }
        throw IllegalArgumentException("foldに失敗しました")
    }

    private fun eval(sexp: Sexp): Sexp {
        if (sexp is Atom) return sexp
        if (sexp is Cell && sexp.car is Atom.Symbol && sexp.cdr is Cell) {
            when (sexp.car.value) {
                "quote" -> return sexp.cdr.car
                "car" -> {
                    val ret = eval(sexp.cdr.car)
                    if (ret is Cell) {
                        return ret.car
                    }
                }
                "cdr" -> {
                    val ret = eval(sexp.cdr.car)

                    if (ret is Cell) {
                        return ret.cdr
                    }
                }
                "cons" -> {
                    val first = eval(sexp.cdr.car)
                    if (sexp.cdr.cdr is Cell) {
                        val second = eval(sexp.cdr.cdr.car)
                        return Cell(first, second)
                    }
                }
                "+" -> {
                    val fn = { a: Atom, b: Atom ->
                        if (a is Atom.IntNumber && b is Atom.IntNumber) {
                            Atom.IntNumber(a.value + b.value)
                        } else {
                            throw IllegalArgumentException("error")
                        }
                    }
                    return fold(Atom.IntNumber(0), sexp.cdr, fn)
                }
            }
        }

        throw IllegalArgumentException("評価できません")
    }
}
