import java.lang.Double.max
import java.lang.Double.min
import kotlin.Array
import kotlin.Int
import kotlin.String

fun main() {
    fun readLn() = readLine()!! // string line
    fun readInt() = readLn().toInt() // single int
    fun readStrings() = readLn().split(" ") // list of strings
    fun readInts() = readStrings().map { it.toInt() } // list of ints
    fun readLongs() = readStrings().map { it.toLong() } // list of ints

    val s = readLn()
    val len = s.length

    fun zFunction(s: String): Array<Int> {
        val n = s.length
        val z = Array(s.length) { 0 }
        var l = 0
        var r = 0

        for (i in 1 until n) {
            z[i] = max(0.0, min((r - i).toDouble(), z[i - l].toDouble())).toInt()

            while (i + z[i] < n && s[z[i]] == s[i + z[i]]) {
                z[i]++
            }

            if (i + z[i] > r) {
                l = i
                r = i + z[i]
            }
        }

        return z
    }

    val z = zFunction(s)
    var period = len

    for (i in 1 until len) {
        if (len % i == 0 && z[i] == len - i) {
            period = i
            break
        }
    }

    println(period)
}
