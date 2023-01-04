package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    private fun eval(sexp: Sexp): Sexp {
        throw IllegalArgumentException("評価できません")
    }
}
