import java.util.*

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

enum class ResultH {
    WIN, LOSS, DRAW, UNKNOWN
}

fun main() {
    val nms = readInts()
    val n = nms[0]
    val m = nms[1]
    val s = nms[2] - 1
    val graph1 = ArrayList<ArrayList<Int>>().apply {
        repeat(n) {
            add(ArrayList())
        }
    }
    val graph2 = ArrayList<ArrayList<Int>>().apply {
        repeat(n) {
            add(ArrayList())
        }
    }
    val out = Array(n) { 0 }
    val d = Array(n) { ResultH.UNKNOWN }

    repeat(m) {
        val uv = readInts()
        val u = uv[0] - 1
        val v = uv[1] - 1

        out[u]++
        graph1[u].add(v)
        graph2[v].add(u)
    }

    val q = ArrayDeque<Int>().apply {
        for (v in 0 until n) {
            if (out[v] == 0) {
                add(v)
                d[v] = ResultH.LOSS
            }
        }
    }

    while (q.isNotEmpty()) {
        val v = q.first
        q.removeFirst()
        if (d[v] == ResultH.LOSS) {
            for (u in graph2[v]) {
                if (d[u] == ResultH.UNKNOWN) {
                    d[u] = ResultH.WIN
                    q.add(u)
                }
            }
        } else {
            for (u in graph2[v]) {
                if (d[u] == ResultH.UNKNOWN) {
                    out[u]--
                    if (out[u] == 0) {
                        d[u] = ResultH.LOSS
                        q.add(u)
                    }
                }
            }
        }
    }

    for (v in 0 until n) {
        if (d[v] == ResultH.UNKNOWN) {
            d[v] = ResultH.DRAW
        }
    }

    println(
        "${
            if (d[s] == ResultH.WIN) {
                "First"
            } else {
                "Second"
            }
        } player wins"
    )
}
