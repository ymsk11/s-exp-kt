package com.github.ymsk11.sexp

class Parser {
    operator fun invoke(text: String): Sexp {
        return if (text == "nil") {
            Nil
        } else {
            Atom(text)
        }
    }
}
