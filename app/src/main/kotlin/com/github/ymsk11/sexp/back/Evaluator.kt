package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Nil
import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    private fun eval(sexp: Sexp): Sexp {
        if (sexp is Atom) return sexp
        if (sexp is Nil) return sexp
        if (sexp is Cell && sexp.car is Atom && sexp.cdr is Cell) {
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
            }
        }

        throw IllegalArgumentException("評価できません")
    }
}
