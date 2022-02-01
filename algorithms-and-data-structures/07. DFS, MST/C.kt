import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

private lateinit var edges: MutableList<MutableList<Int>>
private lateinit var visited: Array<Boolean>
private lateinit var timeIn: Array<Int>
private lateinit var up: Array<Int>
private var T = 0
private lateinit var edgesToNumbers: HashMap<Pair<Int, Int>, ArrayList<Int>>
private lateinit var isCutPoint: Array<Boolean>

fun main() {
    val scanner = Scanner(System.`in`)
    val n = scanner.nextInt()
    val m = scanner.nextInt()

    edges = ArrayList()
    visited = Array(n) { false }
    timeIn = Array(n) { 0 }
    up = Array(n) { 0 }
    edgesToNumbers = HashMap()
    isCutPoint = Array(n) { false }

    for (i in 0 until n) {
        edges.add(ArrayList())
    }

    for (i in 0 until m) {
        val first = scanner.nextInt() - 1
        val second = scanner.nextInt() - 1

        edges[first].add(second)
        edges[second].add(first)

        if (Pair(min(first, second), max(first, second)) !in edgesToNumbers) {
            edgesToNumbers[Pair(min(first, second), max(first, second))] = ArrayList()
        }

        edgesToNumbers[Pair(min(first, second), max(first, second))]!!.add(i)
    }

    for (v in 0 until n) {
        if (!visited[v]) {
            dfs(v, -1)
        }
    }

    println(isCutPoint.filter { it }.count())
    isCutPoint.forEachIndexed { index, it ->
        if (it) {
            print("${index + 1} ")
        }
    }
}

private fun dfs(v: Int, p: Int) {
    timeIn[v] = T++
    up[v] = timeIn[v]
    visited[v] = true
    var count = 0

    for (u in edges[v]) {
        if (u == p) {
            continue
        }

        if (!visited[u]) {
            dfs(u, v)
            count++
            up[v] = min(up[v], up[u])
            if (p != -1 && up[u] >= timeIn[v]) {
                isCutPoint[v] = true
            }
        } else {
            up[v] = min(up[v], timeIn[u])
        }
    }

    if (p == -1 && count >= 2) {
        isCutPoint[v] = true
    }
}
