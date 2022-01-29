import kotlin.math.min

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

fun main() {
    val nmks = readInts()
    val n = nmks[0]
    val m = nmks[1]
    val k = nmks[2]
    val s = nmks[3] - 1
    val graph = ArrayList<EdgeD>().apply {
        repeat(m) {
            val uvw = readInts()
            val u = uvw[0] - 1
            val v = uvw[1] - 1
            val w = uvw[2]

            add(EdgeD(u, v, w))
        }
    }

    var d = Array(n) { Int.MAX_VALUE }
    d[s] = 0

    var d1: Array<Int>
    repeat(k) {
        d1 = Array(n) { Int.MAX_VALUE }
        for (edge in graph) {
            if (d[edge.u] != Int.MAX_VALUE) {
                d1[edge.v] = min(d1[edge.v], d[edge.u] + edge.cost)
            }
        }
        d = d1
    }

    println(d.joinToString(separator = "\n") {
        if (it == Int.MAX_VALUE) {
            "-1"
        } else {
            it.toString()
        }
    })
}

data class EdgeD(val u: Int, val v: Int, val cost: Int)
