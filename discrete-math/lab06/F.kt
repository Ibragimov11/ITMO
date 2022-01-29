import java.util.*

private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }

fun main() {
    val n = readInt()
    val graph = ArrayList<Pair<Int, Int>>()
    val bigV = TreeSet<Int>()
    val bigP = readInts().map { it - 1 }.toMutableList()
    val countInP = Array(n) { 0 }

    for (v in 0 until n) {
        bigV.add(v)
    }

    for (v in bigP) {
        countInP[v]++
        bigV.remove(v)
    }

    for (i in 0 until bigP.size) {
        val u = bigP[i]
        val v = bigV.first()

        bigV.remove(v)
        countInP[u]--

        if (countInP[u] == 0) {
            bigV.add(u)
        }

        graph.add(Pair(u, v))
    }

    graph.add(Pair(bigV.first(), bigV.last()))

    for (pair in graph) {
        println("${pair.first + 1} ${pair.second + 1}")
    }
}
