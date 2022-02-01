private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }

private var k = -1
private lateinit var colors: Array<Int>
private lateinit var edges: Array<ArrayList<Int>>

fun main() {
    val nm = readInts()
    val n = nm[0]
    val m = nm[1]
    edges = Array(n) { ArrayList() }
    val d = Array(n) { 0 }
    colors = Array(n) { -1 }

    repeat(m) {
        val vu = readInts()
        val v = vu[0] - 1
        val u = vu[1] - 1

        edges[v].add(u)
        edges[u].add(v)

        d[v]++
        d[u]++
    }

    var s = -1

    for (i in 0 until n) {
        if (d[i] > k) {
            s = i
            k = d[i]
        }
    }

    if (k % 2 == 0) {
        k++
    }

    if (k == 1) {
        k = 3
    }

    bfs(s)

    println(k)
    for (color in colors) {
        println(color + 1)
    }
}

private fun bfs(v: Int) {
    if (colors[v] != -1) {
        return
    }

    val usedColors = Array(k) { false }

    for (u in edges[v]) {
        if (colors[u] != -1) {
            usedColors[colors[u]] = true
        }
    }

    colors[v] = usedColors.indexOfFirst { !it }

    for (u in edges[v]) {
        bfs(u)
    }
}
