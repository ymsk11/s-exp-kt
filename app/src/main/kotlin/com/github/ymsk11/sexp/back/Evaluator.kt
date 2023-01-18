package com.github.ymsk11.sexp.back

import com.github.ymsk11.sexp.domain.Atom
import com.github.ymsk11.sexp.domain.Cell
import com.github.ymsk11.sexp.domain.Function
import com.github.ymsk11.sexp.domain.Sexp

class Evaluator {
    // TODO: 現状、defineはグローバルに定義されるので、スコープをどうするか検討
    private val environment = mutableMapOf<Atom.Symbol, Sexp>()

    operator fun invoke(sexp: Sexp): Sexp {
        return eval(sexp)
    }

    operator fun invoke(xs: List<Sexp>): List<Sexp> {
        return xs.map { eval(it) }
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
        if (sexp is Atom.Symbol) return environment[sexp]!!
        if (sexp is Atom) return sexp
        if (sexp is Cell && sexp.car is Atom.Symbol && sexp.cdr is Cell) {
            when (sexp.car.value) {
                "quote" -> return sexp.cdr.car
                "atom" -> return if (eval(sexp.cdr.car) is Atom) Atom.T else Atom.Nil
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
                "equal" -> {
                    val first = eval(sexp.cdr.car)
                    if (sexp.cdr.cdr is Cell) {
                        val second = eval(sexp.cdr.cdr.car)
                        return if (first == second) Atom.T else Atom.Nil
                    }
                    return Atom.Nil
                }
                "if" -> {
                    val condition = eval(sexp.cdr.car)
                    return if (condition == Atom.Nil) {
                        eval(((sexp.cdr.cdr as Cell).cdr as Cell).car)
                    } else {
                        eval((sexp.cdr.cdr as Cell).car)
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
                "*" -> {
                    val fn = { a: Atom, b: Atom ->
                        if (a is Atom.IntNumber && b is Atom.IntNumber) {
                            Atom.IntNumber(a.value * b.value)
                        } else {
                            throw IllegalArgumentException("error")
                        }
                    }
                    return fold(Atom.IntNumber(1), sexp.cdr, fn)
                }
                "-" -> {
                    val fn = { a: Atom, b: Atom ->
                        if (a is Atom.IntNumber && b is Atom.IntNumber) {
                            Atom.IntNumber(a.value - b.value)
                        } else {
                            throw IllegalArgumentException("error")
                        }
                    }
                    return fold(sexp.cdr.car as Atom.IntNumber, sexp.cdr.cdr, fn)
                }
                "mod" -> {
                    val dividend = eval(sexp.cdr.car) as Atom.IntNumber
                    val divisor = eval((sexp.cdr.cdr as Cell).car) as Atom.IntNumber
                    return Atom.IntNumber(dividend.value.mod(divisor.value))
                }
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
                "define" -> {
                    val symbol = sexp.cdr.car as Atom.Symbol
                    val value = (sexp.cdr.cdr as Cell).car
                    environment[symbol] = eval(value)
                    // FIXME: valueを返した方が良い気がするが、テストでここをちゃんと検証するのも面倒なため。。
                    return Atom.T
                }
            }
        }

        if (sexp is Cell && sexp.car is Cell && sexp.cdr is Cell) {
            val car = eval(sexp.car)
            if (car is Function) {
                var fn = car.fn
                val args = sexp.cdr.toList()
                car.args.forEachIndexed { index, sexp ->
                    fn = fn.replace(sexp as Atom.Symbol, eval(args.get(index)))
                }
                return eval(fn)
            }
        }

        throw IllegalArgumentException("評価できません $sexp")
    }
}
