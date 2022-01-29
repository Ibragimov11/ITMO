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

    val deque = ArrayDeque<Int>()

    for (v in 0 until n) {
        deque.addLast(v)
    }

    repeat(n * (n - 1)) {
        if (!graph[deque[0]][deque[1]]) {
            var i = 2

            while (!graph[deque[0]][deque[i]] || !graph[deque[1]][deque[i + 1]]) {
                i++
            }

            reverseDeque(deque, 1, i)
        }

        deque.add(deque.first())
        deque.removeFirst()
    }

    for (v in deque) {
        print("${v + 1} ")
    }
}

fun reverseDeque(deque: ArrayDeque<Int>, v: Int, i: Int) {
    var j = 0

    while (v + j < i - j) {
        val t = deque[v + j]

        deque[v + j] = deque[i - j]
        deque[i - j] = t

        j++
    }
}
