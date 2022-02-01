import java.io.*
import java.util.*

class MyReaderC(inputStream: InputStream) {
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
    val reader = MyReaderC(FileInputStream("matching.in"))
    val out = PrintWriter(FileOutputStream("matching.out"))

    fun next() = reader.next()
    fun nextInt() = next().toInt()

    val n = nextInt()
    val weights = Array(n) { v -> Pair(v, nextInt()) }
    Arrays.sort(weights, compareBy<Pair<Int, Int>> { -it.second }.thenBy { it.first })

    val graph = ArrayList<List<Int>>().apply {
        repeat(n) {
            val count = nextInt()
            val list = ArrayList<Int>()
            repeat(count) {
                list.add(nextInt() - 1)
            }
            add(list)
        }
    }

    val used = Array(n) { false }
    val matches = Array(n) { -1 }

    fun kuhn(v: Int): Boolean {
        if (used[v]) {
            return false
        }

        used[v] = true

        for (i in 0 until graph[v].size) {
            val u = graph[v][i]

            if (matches[u] == -1 || kuhn(matches[u])) {
                matches[u] = v

                return true
            }
        }

        return false
    }

    for (i in 0 until n) {
        used.fill(false)
        kuhn(weights[i].first)
    }

    out.println(Array(n) { 0 }.apply {
        for (i in 0 until n) {
            if (matches[i] != -1) {
                this[matches[i]] = i + 1
            }
        }
    }.joinToString(separator = " "))

    out.close()
}
