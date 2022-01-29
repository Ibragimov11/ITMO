private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

// Правильно, но медленно. Переписал на плюсы
fun main() {
    val n = readInt()
    val graph: Array<Array<Boolean>> = Array(n) { Array(n) { false } }

    readLn()

    for (i in 1 until n) {
        val s = readLn()

        for (j in 0 until i) {
            if (s[j] == '1') {
                graph[i][j] = true
                graph[j][i] = true
            }
        }
    }
}