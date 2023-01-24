package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Function
import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    // TODO: 現状、defineはグローバルに定義されるので、スコープをどうするか検討
    private val environment = mutableMapOf<Atom.Symbol, Sexp>()

    private val operators = Operators(
        setEnvironment = { symbol, sexp -> environment[symbol] = sexp },
        parentEval = { eval(it) }
    )

    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    operator fun invoke(xs: List<Sexp>): List<Sexp> {
        return xs.map { eval(it) }
    }

    private fun eval(sexp: Sexp): Sexp {
        if (sexp is Atom.Symbol) return environment[sexp]!!
        if (sexp is Atom) return sexp
        if (sexp is Cell && sexp.car is Atom.Symbol && sexp.cdr is Cell) {
            operators.find(sexp.car.value)?.eval(sexp.cdr)?.let {
                return it
            }
            when (sexp.car.value) {
                "cond" -> {
                    sexp.cdr.forEach {
                        if (it is Cell) {
                            if (eval(it.car) != Atom.Nil) {
                                return eval((it.cdr as Cell).car)
                            }
                        } else {
                            throw IllegalArgumentException("cond statement error")
                        }
                    }
                }
                "lambda" -> {
                    return Function(
                        args = sexp.cdr.car as Cell,
                        fn = (sexp.cdr.cdr as Cell).car as Cell,
                    )
                }
                else -> {
                    val f = environment[sexp.car]
                    if (f is Function) {
                        return evalFunction(f, sexp.cdr)
                    }
                }
            }
        }

        if (sexp is Cell && sexp.car is Cell && sexp.cdr is Cell) {
            val car = eval(sexp.car)
            return evalFunction(car as Function, sexp.cdr)
        }

        throw IllegalArgumentException("評価できません $sexp")
    }

    private fun evalFunction(f: Function, args: Cell): Sexp {
        var fn = f.fn
        val args = args.toList()
        f.args.forEachIndexed { index, sexp ->
            fn = fn.replace(sexp as Atom.Symbol, eval(args.get(index)))
        }
        return eval(fn)
    }
}
