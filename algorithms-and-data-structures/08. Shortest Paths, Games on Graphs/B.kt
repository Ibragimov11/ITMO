import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class MyReaderB(inputStream: InputStream) {
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
    val reader = MyReaderB(System.`in`)
    fun next() = reader.next()
    fun nextInt() = next().toInt()

    val n = nextInt()
    val m = nextInt()
    val d = Array(n) { Int.MAX_VALUE }
    d[0] = 0

    val graph = Array(n) { ArrayList<Pair<Int, Int>>() }

    repeat(m) {
        val a = nextInt() - 1
        val b = nextInt() - 1
        val w = nextInt()

        graph[a].add(Pair(b, w))
        graph[b].add(Pair(a, w))

    }

    val set = TreeSet(compareBy<Int> { d[it] }.thenBy { it })
    set.add(0)

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

    print(d.joinToString(separator = " "))
}
