package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    private fun eval(sexp: Sexp): Sexp {
        if (sexp is Cell && sexp.car is Atom && sexp.car.value == "quote") {
            return sexp.cdr
        }

        throw IllegalArgumentException("評価できません")
    }
}
