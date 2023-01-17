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

fun List<Token>.splitMultipleParen(): List<List<Token>> {
    val parenCorresponding = this.checkParenCorresponding()

    var startIndex = 0
    var endIndex = -1
    val ret = mutableListOf<List<Token>>()
    while (endIndex != this.lastIndex) {
        startIndex = endIndex + 1
        endIndex = parenCorresponding[startIndex]!!
        ret.add(this.slice(startIndex..endIndex))
    }

    return ret
}
