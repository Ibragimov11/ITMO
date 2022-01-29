import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

fun main() {
    val nm = readInts()
    val n = nm[0]
    val m = nm[1]
    val isExist = HashSet<Pair<Int, Int>>()
    val graphOut = Array(n) { ArrayList<Int>() }
    val graphIn = Array(n) { ArrayList<Int>() }
    val digits = Array(n) { HashSet<Int>() }
    val d = Array(n) { 0 }
    val g = Array(n) { -1 }

    repeat(m) {
        val edge = readInts()
        val x = edge[0] - 1
        val y = edge[1] - 1

        if (Pair(x, y) !in isExist) {
            isExist.add(Pair(x, y))
            d[x]++
            graphOut[x].add(y)
            graphIn[y].add(x)
        }
    }

    val q = ArrayDeque<Int>().apply {
        for (v in 0 until n) {
            if (d[v] == 0) {
                add(v)
            }
        }
    }

    while (q.isNotEmpty()) {
        val u = q.first
        q.removeFirst()

        g[u] = IntRange(0, n - 1).indexOfFirst { it !in digits[u] }

        for (w in graphIn[u]) {
            d[w]--
            digits[w].add(g[u])

            if (d[w] == 0) {
                q.addLast(w)
            }
        }
    }

    println(g.joinToString(separator = "\n"))
}