import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max

class MyReaderF(inputStream: InputStream) {
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

fun main() {
    val reader = MyReaderF(System.`in`)
    fun next() = reader.next()
    fun nextInt() = next().toInt()

    val n = nextInt()
    val m = nextInt()
    val graph = Array(n) { ArrayList<Pair<Int, Int>>() }

    repeat(m) {
        val a = nextInt() - 1
        val b = nextInt() - 1
        val w = nextInt()

        graph[a].add(Pair(b, w))
        graph[b].add(Pair(a, w))
    }

    val d = Array(n) { 1_000_000_000_000_000 }
    val a = nextInt() - 1
    val b = nextInt() - 1
    val c = nextInt() - 1

    d[a] = 0
    val set = TreeSet(compareBy<Int> { d[it] }.thenBy { it }).apply {
        add(a)
    }

    while (set.isNotEmpty()) {
        val v = set.first()
        set.remove(v)

        for (pair in graph[v]) {
            val u = pair.first
            val w = pair.second
            if (d[v] + w < d[u]) {
                if (d[u] < Long.MAX_VALUE) {
                    set.remove(u)
                }
                d[u] = d[v] + w
                set.add(u)
            }
        }
    }
    
    val ab = d[b]
    val ac = d[c]
    
    d.fill(1_000_000_000_000_000)
    d[b] = 0
    set.clear()
    set.apply {
        add(b)
    }

    while (set.isNotEmpty()) {
        val v = set.first()
        set.remove(v)

        for (pair in graph[v]) {
            val u = pair.first
            val w = pair.second
            if (d[v] + w < d[u]) {
                if (d[u] < Long.MAX_VALUE) {
                    set.remove(u)
                }
                d[u] = d[v] + w
                set.add(u)
            }
        }
    }

    val bc = d[c]

    if (ab != 1_000_000_000_000_000 && ac != 1_000_000_000_000_000 && bc != 1_000_000_000_000_000) {
        println(ab + ac + bc - max(ab, max(ac, bc)))
    } else {
        println(-1)
    }
}
