import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 *@author Said Ibragimov on 10.04.2022 21:33
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

    val k = nextInt()
    val a = Array(k) { nextLong() }
    val c = Array(k) { nextLong() }

    val q = mutableListOf(1L).apply {
        addAll(c.map { -it })
    }

    val p = Array(k) { 0L }
    for (n in p.indices) {
        for (i in 0..n) {
            p[n] += + (a[i] * q[n - i])
        }
    }

    val f = p.dropLastWhile { it == 0L }
    println(f.size - 1)
    println(f.joinToString(separator = " "))

    println(q.size - 1)
    println(q.joinToString(separator = " "))
}