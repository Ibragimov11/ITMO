private fun readLn() = readLine()!! // string line
private fun readInt() = readLn().toInt() // single int
private fun readStrings() = readLn().split(" ") // list of strings
private fun readInts() = readStrings().map { it.toInt() } // list of ints

fun main() {
    val n = readInt()
    val lamps = Array(n) { i -> i + 1 }

    val myComparator = Comparator { a: Int, b: Int ->
        println("1 $a $b")

        if (readLn() == "YES") -1 else 1
    }

    var correct = true

    for (i in 1 until n) {
        if (lamps[i] < lamps[i - 1]) {
            correct = false
            break
        }
    }

    if (correct) {
        println("0 ${lamps.sortedWith(comparator = myComparator).joinToString(separator = " ")}")
    } else {
        val zeroes = Array(n + 1) { 0 }
        println(zeroes.joinToString(separator = " "))
    }
}
