package com.h0tk3y.spbsu.kotlin.course.lesson1

fun greet(name: String) {
    /*
     * Напишите тело функции, выводящее приветствие в виде:
     * "Hello, NAME!" без кавычек, подставляя вместо "NAME"
     * переданную строку.
     */
    println("Hello, $name!")
}

fun main(args: Array<String>) {
    /*
     * Вызовите функцию greet для каждого
     * аргумента командной строки или же, если не
     * передано ни одного аргумента, вызовите greet один
     * раз, чтобы вывести "Hello, world!"
     */
    if (args.isEmpty()) {
        greet("world")
    } else {
        for (name in args) {
            greet(name)
        }
    }
}