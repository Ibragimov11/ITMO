fun main() {
    fun readLn() = readLine()!! // string line
    fun readInt() = readLn().toInt() // single int
    fun readStrings() = readLn().split(" ") // list of strings
    fun readInts() = readStrings().map { it.toInt() } // list of ints
    fun readLongs() = readStrings().map { it.toLong() } // list of ints

    val M = (1e7 + 137).toLong()
    val X = 317
    val P = readLn()
    val T = readLn()
    val lenP = P.length
    val lenT = T.length

    if (lenT < lenP) {
        println(0)
    } else {
        val powers = Array(lenT + 1) { 1L }
        val p = Array(lenT + 1) { 0L }

        for (i in 1 .. lenT) {
            powers[i] = (powers[i - 1] * X) % M
            p[i] = (((p[i - 1] * X) % M + T[i - 1].toLong()) % M + M) % M
        }

        var hashP = 0L
        for (i in P.indices) {
            hashP = (((hashP * X) % M + P[i].toLong()) % M + M) % M
        }

        fun hash(l: Int, r: Int): Long {
            return ((p[r] - p[l - 1] * powers[r - l + 1]) % M + M) % M
        }

        var cnt = 0
        for (i in 1..lenT - lenP + 1) {
            if (hashP == hash(i, i + lenP - 1)) {
                cnt++
            }
        }

        println(cnt)
        for (i in 1..lenT - lenP + 1) {
            if (hashP == hash(i, i + lenP - 1)) {
                print("$i ")
            }
        }
    }
}