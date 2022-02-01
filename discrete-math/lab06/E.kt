import java.util.*

private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }

fun main() {
    val n = readInt()
    val m = n - 1
    val edges: Array<HashSet<Int>> = Array(n) { HashSet() }
    val d = Array(n) { 0 }
    val set = TreeSet<Int>()

    repeat(m) {
        val vu = readInts()
        val v = vu[0] - 1
        val u = vu[1] - 1

        edges[v].add(u)
        edges[u].add(v)

        d[v]++
        d[u]++
    }

    for (v in 0 until n) {
        if (d[v] == 1) {
            set.add(v)
        }
    }

    val res = Array(n - 2) { 0 }

    for (i in 0 until n - 2) {
        val v = set.first()
        set.remove(v)
        val u = edges[v].first()
        res[i] = u + 1
        d[v] = 0
        d[u]--
        edges[v].remove(u)
        edges[u].remove(v)
        if (d[u] == 1) {
            set.add(u)
        }
    }

    println(res.joinToString(prefix = "", separator = " ", postfix = ""))
}
