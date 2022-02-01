import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

private lateinit var edgesOut: MutableList<MutableList<Int>>
private lateinit var edgesIn: MutableList<MutableList<Int>>
private lateinit var visited1: Array<Boolean>
private val order: MutableList<Int> = ArrayList()
private lateinit var components: Array<Int>
private var numberOfComponent = 0

fun main() {
    val scanner = Scanner(System.`in`)
    val n = scanner.nextInt()
    val m = scanner.nextInt()
    val countAfter = HashSet<Pair<Int, Int>>()

    edgesOut = ArrayList()
    edgesIn = ArrayList()
    visited1 = Array(n) { false }
    components = Array(n) { -1 }

    for (i in 0 until n) {
        edgesOut.add(ArrayList())
        edgesIn.add(ArrayList())
    }

    for (i in 0 until m) {
        val first = scanner.nextInt() - 1
        val second = scanner.nextInt() - 1

        edgesOut[first].add(second)
        edgesIn[second].add(first)
    }

    for (v in 0 until n) {
        if (!visited1[v]) {
            dfs1(v)
        }
    }

    for (v in order.reversed()) {
        if (components[v] == -1) {
            numberOfComponent++
            dfs2(v)
        }
    }

    for (i in 0 until edgesOut.size) {
        for (u in edgesOut[i]) {
            if (components[i] != components[u]) {
                countAfter.add(Pair(components[i], components[u]))
            }
        }
    }

    println(countAfter.size)
}

private fun dfs1(v: Int) {
    visited1[v] = true

    for (u in edgesOut[v]) {
        if (!visited1[u]) {
            dfs1(u)
        }
    }

    order.add(v)
}

private fun dfs2(v: Int) {
    components[v] = numberOfComponent

    for (u in edgesIn[v]) {
        if (components[u] == -1) {
            dfs2(u)
        }
    }
}
