import java.util.*

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

enum class ResultE {
    FIRST, SECOND, DRAW, UNKNOWN
}

fun main() {
    while (true) {
        val line = readLine() ?: break
        val nm = line.split(" ").map { it.toInt() }
        val n = nm[0]
        val m = nm[1]
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
        val d = Array(n) { ResultE.UNKNOWN }

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
                    d[v] = ResultE.SECOND
                }
            }
        }

        while (q.isNotEmpty()) {
            val v = q.first
            q.removeFirst()
            if (d[v] == ResultE.SECOND) {
                for (u in graph2[v]) {
                    if (d[u] == ResultE.UNKNOWN) {
                        d[u] = ResultE.FIRST
                        q.add(u)
                    }
                }
            } else {
                for (u in graph2[v]) {
                    if (d[u] == ResultE.UNKNOWN) {
                        out[u]--
                        if (out[u] == 0) {
                            d[u] = ResultE.SECOND
                            q.add(u)
                        }
                    }
                }
            }
        }

        for (v in 0 until n) {
            if (d[v] == ResultE.UNKNOWN) {
                d[v] = ResultE.DRAW
            }
        }
        
        println(d.joinToString(separator = "\n"))
        println()
    }
}
