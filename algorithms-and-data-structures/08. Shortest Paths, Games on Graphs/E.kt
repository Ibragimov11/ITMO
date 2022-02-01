import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

class MyReaderE(inputStream: InputStream) {
    private val reader = BufferedReader(InputStreamReader(inputStream))
    private var tokenizer: StringTokenizer? = null

    fun next(): String {
        while (true) {
            tokenizer.let {
                if (it == null || !it.hasMoreTokens()) {
                    tokenizer = StringTokenizer(reader.readLine())
                } else {
                    return it.nextToken()
                }
            }
        }
    }
}

const val inf = 1_000_000_000_000_000_001

fun main() {
    val reader = MyReaderE(System.`in`)
    fun next() = reader.next()
    fun nextInt() = next().toInt()
    fun nextLong() = next().toLong()

    val n = nextInt()
    val m = nextInt()
    val s = nextInt() - 1
    val g = Array(n) { ArrayList<Int>() }
    val graph = ArrayList<EdgeE>().apply {
        repeat(m) {
            val u = nextInt() - 1
            val v = nextInt() - 1
            val w = nextLong()

            add(EdgeE(u, v, w))
            g[u].add(v)
        }
    }
    val q = ArrayDeque<Int>()

    val d = Array(n) { inf }
    d[s] = 0

    for (i in 0 until n) {
        for (edge in graph) {
            if (d[edge.u] != inf) {
                if (d[edge.u] + edge.cost < d[edge.v]) {
                    d[edge.v] = d[edge.u] + edge.cost

                    if (i == n - 1) {
                        q.addLast(edge.v)
                    }
                }
            }
        }
    }

    while (q.isNotEmpty()) {
        val u = q.first()
        q.removeFirst()

        for (v in g[u]) {
            if (d[v] != -inf) {
                d[v] = -inf
                q.addLast(v)
            }
        }
    }

    println(d.joinToString(separator = "\n") {
        when (it) {
            inf -> {
                "*"
            }
            -inf -> {
                "-"
            }
            else -> {
                it.toString()
            }
        }
    })
}

data class EdgeE(val u: Int, val v: Int, val cost: Long)
