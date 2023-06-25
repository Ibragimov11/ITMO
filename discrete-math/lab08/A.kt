import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

/**
 *@author Said Ibragimov on 09.04.2022 20:37
 */

fun main() {
    class MyReader(inputStream: InputStream) {
        private val reader = BufferedReader(InputStreamReader(inputStream))
        private var tokenizer: StringTokenizer? = null

        fun next(): String {
            while (true) {
                tokenizer.let {
                    if (it == null || !it.hasMoreTokens()) {
                        tokenizer = StringTokenizer(reader.readLine())
                    } else {
                        return it.nextToken()
                    }
                }
            }
        }
    }

    val reader = MyReader(System.`in`)
    fun nextInt() = reader.next().toInt()
    fun nextLong() = reader.next().toLong()

    val mod = 998244353L

    fun Array<Long>.getOrZero(i: Int): Long {
        if (i < size) {
            return this[i]
        }

        return 0L
    }

    fun deleteLastZeroes(a: Array<Long>): List<Long> {
        if (a.all { it == 0L }) {
            return listOf(0L)
        }

        return a.dropLastWhile { it == 0L }
    }

    fun add(p: Array<Long>, q: Array<Long>) {
        val result = Array(max(p.size, q.size)) { 0L }
        for (i in 0 until max(p.size, q.size)) {
            result[i] = (p.getOrZero(i) + q.getOrZero(i)).mod(mod)
        }

        val ans = deleteLastZeroes(result)
        println(ans.size - 1)
        println(ans.joinToString(separator = " "))
    }

    fun multiply(p: Array<Long>, q: Array<Long>) {
        val result = Array(p.size + q.size - 1) { 0L }
        for (n in result.indices) {
            for (i in 0..n) {
                result[n] = (result[n] + (p.getOrZero(i) * q.getOrZero(n - i)).mod(mod)).mod(mod)
            }
        }

        val ans = deleteLastZeroes(result)
        println(ans.size - 1)
        println(ans.joinToString(separator = " "))
    }

    fun divide(p: Array<Long>, q: Array<Long>) {
        val result = ArrayList<Long>()
        for (n in 0 until 1000) {
            var t = 0L
            for (i in 0 until n) {
                t += (result[i] * q.getOrZero(n - i)).mod(mod)
            }
            result.add((p.getOrZero(n) - t).mod(mod))
        }

        println(result.joinToString(separator = " "))
    }

    val n = nextInt()
    val m = nextInt()
    val p = Array(n + 1) { nextLong() }
    val q = Array(m + 1) { nextLong() }

    add(p, q)
    multiply(p, q)
    divide(p, q)
}
