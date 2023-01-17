package com.github.ymsk11.sexp.front

fun List<Token>.checkParenCorresponding(): Map<Int, Int> {
    val list = mutableListOf<Int>()
    val ret = mutableMapOf<Int, Int>()
    this.forEachIndexed { index, token ->
        when (token) {
            Token.LParen -> list.add(index)
            Token.RParen -> {
                ret[list.last()] = index
                list.removeAt(list.lastIndex)
            }
            else -> {}
        }
    }
    return ret
}
