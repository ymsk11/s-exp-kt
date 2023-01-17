package com.github.ymsk11.sexp.front

import com.github.ymsk11.sexp.domain.Sexp

class MultipleParser(
    private val tokenizer: Tokenizer = Tokenizer(),
    private val parser: Parser = Parser(),
) {
    operator fun invoke(text: String): List<Sexp> {
        val tokens = tokenizer(text)
        return tokens.splitMultipleParen().map { parser(it) }
    }
}
