import java.util.*
import kotlin.math.max
import kotlin.math.min


private lateinit var head: Array<Int>
private lateinit var dsu: Array<MutableList<Int>>
class J {
    fun main() {
        val scanner = Scanner(System.`in`)
        val n = scanner.nextInt()
        val m = scanner.nextInt()
        val Q = TreeSet(
            compareBy<Pair<Int, Pair<Int, Int>>> { it.first }.thenBy { it.second.first }.thenBy { it.second.second })

        head = Array(n) { i -> i }
        dsu = Array(n) { i -> listOf(i).toMutableList() }

        for (i in 0 until m) {
            val first = scanner.nextInt() - 1
            val second = scanner.nextInt() - 1
            val weight = scanner.nextInt()

            Q.add(Pair(weight, Pair(min(first, second), max(first, second))))
        }

        var summaryWeight = 0L

        for (x in Q) {
            val uv = x.second

            if (head[uv.first] != head[uv.second]) {
                union(uv.first, uv.second)
                summaryWeight += x.first
            }
        }

        println(summaryWeight)
    }

    fun union(first: Int, second: Int) {
        var x = head[first]
        var y = head[second]

        if (dsu[x].size > dsu[y].size) {
            val t = x
            x = y
            y = t
        }

        for (element in dsu[x]) {
            head[element] = y
            dsu[y].add(element)
        }

        dsu[x].clear()
    }
}