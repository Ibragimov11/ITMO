fun main() {
    fun readLn() = readLine()!! // string line
    fun readInt() = readLn().toInt() // single int
    fun readStrings() = readLn().split(" ") // list of strings
    fun readInts() = readStrings().map { it.toInt() } // list of ints
    fun readLongs() = readStrings().map { it.toLong() } // list of ints

    fun prefixFunction(s: String): Array<Int> {
        val p = Array(s.length) { 0 }

        for (i in 1 until s.length) {
            var k = p[i - 1]

            while (k > 0 && s[i] != s[k]) {
                k = p[k - 1]
            }

            if (s[i] == s[k]) {
                k++
            }

            p[i] = k
        }

        return p
    }

    println(prefixFunction(readLn()).joinToString(separator = " "))
}