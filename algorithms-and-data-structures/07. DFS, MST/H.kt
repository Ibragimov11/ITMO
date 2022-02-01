import java.util.*
import kotlin.properties.Delegates

private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints


var n by Delegates.notNull<Int>()
lateinit var used: Array<Int>
lateinit var graph1: Array<Array<Int>>
lateinit var graph2: Array<Array<Int>>
var comp = 0

fun main() {
    n = readInt()

    graph1 = Array(n) { Array(n) { 0 } }
    graph2 = Array(n) { Array(n) { 0 } }
    used = Array(n) { 0 }

    for (i in 0 until n) {
        val weights = readInts()
        for (j in 0 until n) {
            val weight = weights[j]

            graph1[i][j] = weight
            graph2[j][i] = weight
        }
    }

    var left = -1
    var right = 1_000_000_000

    while (right - left > 1) {
        val middle = (right + left) / 2

        if (checkMiddle(middle)) {
            right = middle
        } else {
            left = middle
        }
    }

    println(right)
}

fun checkMiddle(middle: Int): Boolean {
    comp = 0
    used.fill(0)

    for (i in 0 until n) {
        if (used[i] == 0) {
            comp++
            dfs(i, middle, graph1)
        }
    }

    val save: Int = comp
    comp = 0
    used.fill(0)

    for (i in 0 until n) {
        if (used[i] == 0) {
            comp++
            dfs(i, middle, graph2)
        }
    }

    return comp == 1 && save == 1
}

fun dfs(v: Int, middle: Int, graph: Array<Array<Int>>) {
    used[v] = comp

    for (i in 0 until n) {
        if (used[i] == 0 && graph[v][i] <= middle) {
            dfs(i, middle, graph)
        }
    }
}
