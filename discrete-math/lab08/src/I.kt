import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 *@author Said Ibragimov on 19.04.2022 18:22
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

//    val mod = 104857601L

    fun Array<Long>.getOrZero(i: Int): Long {
        if (i < size) {
            return this[i]
        }

        return 0L
    }

    val k = nextInt()
    var n = nextLong() - 1
    val a = Array( 2 * k) { 0L }
    for (i in 0 until k) {
        a[i] = nextLong()
    }
    val c = Array(k + 1) { 1L }
    for (i in 1 until k + 1) {
        c[i] = 104857601L - nextLong()
    }

    fun mul(a: Array<Long>) {
        val result = Array(k + 1) { 0L }
        for (i in 0..2 * k step 2) {
            for (j in 0..i) {
                result[i / 2] = (result[i / 2] + (c.getOrZero(j) * a.getOrZero(i - j)).mod(104857601L)).mod(104857601L)
            }
        }

        result.copyInto(c)
    }

    while (n >= k) {
        a.fill(0L, k, 2 * k)
        for (i in k until 2 * k) {
            for (j in 1..k) {
                a[i] = (a[i] - (c[j] * a[i - j]).mod(104857601L)).mod(104857601L)
                if (a[i] < 0) {
                    a[i] += 104857601L
                }
            }
        }

        mul(Array(k + 1) { i -> if (i % 2 == 0) c[i] else 104857601L - c[i]})

        for (i in (n % 2).toInt() until 2 * k step 2) {
            a[i / 2] = a[i]
        }

        n /= 2
    }

    println(a[n.toInt()])
}