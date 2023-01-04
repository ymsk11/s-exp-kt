package com.github.ymsk11.sexp.front

class Tokenizer {
    operator fun invoke(text: String): List<Token> = text
        .replace("(", " ( ")
        .replace(")", " ) ")
        .replace(".", " . ")
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { Token.from(it) }
}