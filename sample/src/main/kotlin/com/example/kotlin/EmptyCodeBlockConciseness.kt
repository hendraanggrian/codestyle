package com.example.kotlin

class EmptyCodeBlockConciseness {
    fun foo() {}

    fun bar() {
        if (true) {
        } else if (false) {
        } else {
        }
    }

    class Baz(val baz: (Int) -> Unit = {})
}
