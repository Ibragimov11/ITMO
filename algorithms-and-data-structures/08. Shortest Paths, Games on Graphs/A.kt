import kotlin.math.min

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

fun main() {
    val n = readInt()
    val d = Array(n) { Array(n) { 0 } }

    for (i in 0 until n) {
        val line = readInts()
        for (j in 0 until n) {
            d[i][j] = line[j]
        }
    }

    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                d[i][j] = min(d[i][j], d[i][k] + d[k][j])
            }
        }
    }

    println(d.joinToString(separator = "\n") { it.joinToString(separator = " ") })
}
