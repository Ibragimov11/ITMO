import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 *@author Said Ibragimov on 17.04.2022 18:15
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

    val mod = 1_000_000_007

    val k = nextInt()
    val m = nextInt()
    val c = Array(k) { nextInt() }
    val sum = Array(m + 1) { 0 }
    val result = Array(m + 1) { 0 }

    sum[0] = 1
    result[0] = 1

    for (i in 1..m) {
        for (j in 0 until k) {
            if (i - c[j] >= 0) {
                result[i] = (result[i] + sum[i - c[j]]).mod(mod)
            }
        }
        for (j in 0..i) {
            sum[i] = (sum[i] + (result[j].toLong() * result[i - j]).mod(mod)).mod(mod)
        }
    }

    println(result.drop(1).joinToString(separator = " "))
}
