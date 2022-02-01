import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.system.exitProcess

private lateinit var edges: HashMap<Int, MutableList<Int>>
private lateinit var visited: Array<Int>
private val order: MutableList<Int> = ArrayList()
// top sort
fun main() {
    val scanner = Scanner(System.`in`)
    val n = scanner.nextInt()
    val m = scanner.nextInt()

    visited = Array(n) { 0 }
    edges = HashMap()

    for (i in 0 until n) {
        edges[i] = ArrayList()
    }

    for (i in 0 until m) {
        val first = scanner.nextInt() - 1
        val second = scanner.nextInt() - 1

        edges[first]!!.add(second)
    }

    for (x in edges.keys) {
        dfs(x)
    }

    println(order.map { it + 1 }.reversed().joinToString(prefix = "", postfix = "", separator = " "))
}

private fun dfs(v: Int) {
    if (visited[v] == 2) {
        return
    } else if (visited[v] == 1) {
        print(-1)
        exitProcess(0)
    }

    visited[v] = 1

    for (u in edges[v]!!) {
        dfs(u)
    }

    visited[v] = 2
    order.add(v)
}
