package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class IsAtom(
    val parentEval: (Sexp) -> Sexp
) : Operator {
    override fun eval(args: Sexp): Sexp {
        return if (parentEval((args as Cell).car) is Atom) Atom.T else Atom.Nil
    }
}
