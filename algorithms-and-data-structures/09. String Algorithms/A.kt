fun main() {
    fun readLn() = readLine()!! // string line
    fun readInt() = readLn().toInt() // single int
    fun readStrings() = readLn().split(" ") // list of strings
    fun readInts() = readStrings().map { it.toInt() } // list of ints
    fun readLongs() = readStrings().map { it.toLong() } // list of ints

    val M = (1e9 + 123).toLong()
    val X = 37
    val s = readLn()
    val m = readInt()
    val length = s.length

    val powers = Array(length + 1) { 1L }
    val p = Array(length + 1) { 0L }

    for (i in 1 .. length) {
        powers[i] = (powers[i - 1] * X) % M
        p[i] = (((p[i - 1] * X) % M + s[i - 1].code.toLong()) % M + M) % M
    }

    fun hash(l: Int, r: Int): Long {
        return ((p[r] - p[l - 1] * powers[r - l + 1]) % M + M) % M
    }

    repeat(m) {
        val a = readInt()
        val b = readInt()
        val c = readInt()
        val d = readInt()

        if (hash(a, b) == hash(c, d)) {
            println("Yes")
        } else {
            println("No")
        }
    }
}
