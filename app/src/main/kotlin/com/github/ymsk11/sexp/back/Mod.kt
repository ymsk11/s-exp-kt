package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Mod(
    private val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Cell): Sexp {
        val dividend = parentEval(args.car) as Atom.IntNumber
        val divisor = parentEval((args.cdr as Cell).car) as Atom.IntNumber
        return Atom.IntNumber(dividend.value.mod(divisor.value))
    }
}
