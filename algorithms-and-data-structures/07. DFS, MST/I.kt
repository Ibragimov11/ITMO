import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

private lateinit var V: Array<Pair<Int, Int>>

// TL на 20м тесте, переписал на C++
fun main() {
    val scanner = Scanner(System.`in`)
    val n = scanner.nextInt()

    V = Array(n) { Pair(0, 0) }
    val d = Array(n) { 0.0 }
    val inA = Array(n) { false }

    for (i in 0 until n) {
        val x = scanner.nextInt()
        val y = scanner.nextInt()

        V[i] = Pair(x, y)
    }

    inA[0] = true
    d[0] = -1.0

    for (i in 1 until n) {
        d[i] = distance(Pair(0, i))
    }

    var summaryWeight = 0.0

    repeat(n - 1) {
        var v = d.indexOfFirst { it > 0 }

        for (i in (v + 1) until n) {
            if (!inA[i] && d[i] < d[v]) {
                v = i
            }
        }

        summaryWeight += d[v]

        inA[v] = true
        d[v] = -1.0

        for (u in 0 until n) {
            if (!inA[u] && distance(Pair(min(u, v), max(u, v))) < d[u]) {
                d[u] = distance(Pair(min(u, v), max(u, v)))
            }
        }

    }

    println(summaryWeight)
}

fun distance(pair: Pair<Int, Int>): Double {
    return distance(V[pair.first], V[pair.second])
}

fun distance(x: Pair<Int, Int>, y: Pair<Int, Int>): Double {
    val x1 = x.first
    val y1 = x.second
    val x2 = y.first
    val y2 = y.second

    return sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble())
}