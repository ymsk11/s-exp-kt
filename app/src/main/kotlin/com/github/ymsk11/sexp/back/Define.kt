package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Sexp

class Define(
    val parentEval: (Sexp) -> Sexp,
    val setEnvironment: (Atom.Symbol, Sexp) -> Unit,
) : Operator {
    override fun eval(args: Cell): Sexp {
        val symbol = args.car as Atom.Symbol
        val value = parentEval((args.cdr as Cell).car)
        setEnvironment(symbol, value)
        // FIXME: valueを返した方が良い気がするが、テストでここをちゃんと検証するのも面倒なため。。
        return Atom.T
    }
}
