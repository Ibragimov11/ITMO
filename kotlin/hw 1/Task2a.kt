package com.h0tk3y.spbsu.kotlin.course.lesson1

/*
 * Реализуйте функцию factorial, вычисляющую значение факториала. Значение факториала нуля считайте едииницей.
 * Факториалом отрицательных чисел считайте -1. Целочисленное переполнение игнорируйте.
 */
tailrec fun factorial(n: Int, result: Int = 1): Int = when {
    n < 0 -> -1
    n == 0 -> result
    else -> factorial(n - 1, result * n)
}