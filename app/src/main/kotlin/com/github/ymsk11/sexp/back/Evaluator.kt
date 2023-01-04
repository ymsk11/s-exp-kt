package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    private fun eval(sexp: Sexp): Sexp {
        println("SEXP: $sexp")
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
            }
        }

        throw IllegalArgumentException("評価できません")
    }
}
