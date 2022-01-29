import java.io.PrintWriter
import java.lang.Double.max
import java.lang.Double.min

fun main() {
    fun readLn() = readLine()!! // string line
    fun readInt() = readLn().toInt() // single int
    fun readStrings() = readLn().split(" ") // list of strings
    fun readInts() = readStrings().map { it.toInt() } // list of ints
    fun readLongs() = readStrings().map { it.toLong() } // list of ints

    val out = PrintWriter(System.out)
    val s = readLn()

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

    for (i in 1 until z.size) {
        out.write("${z[i]} ")
    }

    out.close()
}
