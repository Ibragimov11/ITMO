import kotlin.math.max

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints
private fun readLongs() = readStrings().map { it.toLong() } // list of ints

const val INF: Long = 1000000000000000000L;

fun main() {
    val n = readInt()
    val graph = ArrayList<EdgeC>()

    for (i in 0 until n) {
        val list = readLongs()
        for (j in 0 until n) {
            if (list[j] != 100000L) {
                graph.add(EdgeC(i, j, list[j]))
            }
        }
    }

    val d = Array(n) { INF }
    val p = Array(n) { -1 }

    var x = 0
    for (i in 0 until n) {
        x = -1
        for (edge in graph)
            if (d[edge.b] > d[edge.a] + edge.cost) {
                d[edge.b] = max(-INF, d[edge.a] + edge.cost)
                p[edge.b] = edge.a
                x = edge.b
            }
    }

    if (x == -1)
        println("NO")
    else {
        var y = x
        for (i in 0 until n) {
            y = p[y]
        }
        val ans = ArrayList<Int>()

        var u = y
        while (true) {
            if (u == y && ans.size > 1) {
                break
            }
            ans.add(u)
            u = p[u]
        }

        for (i in 0 until ans.size) {
            ans[i]++
        }
        println("YES")
        println(ans.size)
        println(ans.reversed().joinToString(separator = " "))
    }
}

data class EdgeC(val a: Int, val b: Int, val cost: Long)
