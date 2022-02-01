private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

var n = 0

// Правильно, но медленно. Переписал на плюсы
fun main() {
    val nm = readInts()
    n = nm.first()
    val m = nm.last()
    val graph = Array(n) { HashSet<Int>() }

    repeat(m) {
        val vu = readInts()
        val v = vu.first() - 1
        val u = vu.last() - 1

        graph[v].add(u)
        graph[u].add(v)
    }

    val result: Array<Int> = chromaticPolynomial(graph, n)

    println(result.indexOfLast { it > 0 })
    println(result.reversed().joinToString(separator = " "))
}

fun chromaticPolynomial(graph2: Array<HashSet<Int>>, countV: Int): Array<Int> {
    val returnArray = Array(n + 1) { 0 }
    var zero = true

    for (i in 0 until n) {
        if (graph2[i].isNotEmpty()) {
            zero = false
            break
        }
    }

    if (zero) {
        returnArray[countV] = 1
        return returnArray
    }

    val graph1 = Array(n) { HashSet<Int>() }

    for (i in 0 until n) {
        graph1[i].addAll(graph2[i])
    }

    val v = graph1.indexOfFirst { it.size > 0 }
    val u = graph1[v].first()
    graph1[v].remove(u)
    graph1[u].remove(v)

    graph2[v].remove(u)
    graph2[u].remove(v)

    for (w in graph2[u]) {
        graph2[v].add(w)
        graph2[w].add(v)
        graph2[w].remove(u)
    }
    graph2[u].clear()

    val result1 = chromaticPolynomial(graph1, countV)
    val result2 = chromaticPolynomial(graph2, countV - 1)

    for (i in 0..n) {
        returnArray[i] = result1[i] - result2[i]
    }

    return returnArray
}