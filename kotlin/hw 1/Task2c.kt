package com.h0tk3y.spbsu.kotlin.course.lesson1

/*
 * Реализуйте функцию fizzbuzz, принимающую целочисленный диапазон и выводящую на каждый элемент диапазона
 * по одной строке в консоль. Для чисел в диапазоне, кратных трём, выводите слово fizz, для кратных пяти –
 * слово buzz, а для кратных 15-и (то есть кратных и трём, и пяти) – слово fizzbuzz.
 * Для остальных чисел выводите их строковое представление.
 *
 * Пример вывода для диапазона 10..15: *//*
  buzz
  11
  fizz
  13
  14
  fizzbuzz
 */
fun fizzbuzz(range: IntRange) {
    for (number in range) {
        println(
            when {
                number % 15 == 0 -> "fizzbuzz"
                number % 5 == 0 -> "buzz"
                number % 3 == 0 -> "fizz"
                else -> number
            }
        )
    }
}