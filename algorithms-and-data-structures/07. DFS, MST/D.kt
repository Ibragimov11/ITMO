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
private var Number = 0
private lateinit var edgesToCount: HashMap<Pair<Int, Int>, Int>
private var buffer = Stack<Int>()
private lateinit var numberOfComp: Array<Int>

fun main() {
    val scanner = Scanner(System.`in`)
    val n = scanner.nextInt()
    val m = scanner.nextInt()

    edges = ArrayList()
    visited = Array(n) { false }
    timeIn = Array(n) { 0 }
    up = Array(n) { 0 }
    edgesToCount = HashMap()
    numberOfComp = Array(n) { -1 }

    for (i in 0 until n) {
        edges.add(ArrayList())
    }

    for (i in 0 until m) {
        val first = scanner.nextInt() - 1
        val second = scanner.nextInt() - 1

        edges[first].add(second)
        edges[second].add(first)

        if (Pair(min(first, second), max(first, second)) !in edgesToCount) {
            edgesToCount[Pair(min(first, second), max(first, second))] = 0
        }

        edgesToCount[Pair(min(first, second), max(first, second))] = edgesToCount[Pair(min(first, second), max(first, second))]!! + 1
    }

    for (v in 0 until n) {
        if (!visited[v]) {
            dfs(v, -1)
        }
    }

    println(Number)
    numberOfComp.map { print("$it ") }
}

private fun dfs(v: Int, p: Int) {
    timeIn[v] = T++
    up[v] = timeIn[v]
    visited[v] = true
    buffer.add(v)

    for (u in edges[v]) {
        if (u == p && edgesToCount[Pair(min(v, p), max(v, p))] == 1) {
            continue
        }

        if (!visited[u]) {
            dfs(u, v)
            up[v] = min(up[v], up[u])
        } else {
            up[v] = min(up[v], timeIn[u])
        }
    }

    if (up[v] == timeIn[v]) {
        Number++

        while (true) {
            val x = buffer.pop()
            numberOfComp[x] = Number

            if (x == v) {
                break
            }
        }
    }
}
