import java.io.*
import java.util.*
import kotlin.collections.HashSet

class MyReaderD(inputStream: InputStream) {
    private val reader = BufferedReader(InputStreamReader(inputStream))
    private var tokenizer: StringTokenizer? = null

    fun next(): String {
        while (true) {
            tokenizer.let {
                if (it == null || !it.hasMoreTokens()) {
                    tokenizer = StringTokenizer(reader.readLine())
                } else {
                    return it.nextToken()
                }
            }
        }
    }
}

fun main() {
    val reader = MyReaderD(FileInputStream("check.in"))
    val out = PrintWriter(FileOutputStream("check.out"))
    fun next() = reader.next()
    fun nextInt() = next().toInt()

    var axiom1 = false
    var axiom2 = true
    var axiom3 = true

    nextInt()
    val m = nextInt()

    val hashSets = HashSet<HashSet<Int>>().apply {
        repeat(m) {
            val size = nextInt()
            if (size == 0) {
                axiom1 = true
            }
            val set = HashSet<Int>().apply {
                repeat(size) {
                    add(nextInt())
                }
            }
            add(set)
        }
    }

    loop@ for (set in hashSets) {
        for (e in set) {
            if (!hashSets.contains(set.minus(e))) {
                axiom2 = false

                break@loop
            }
        }
    }

    if (!axiom1 || !axiom2) {
        out.println("NO")
    } else {
        out@ for (set1 in hashSets) {
            loop@ for (set2 in hashSets) {
                if (set1.size <= set2.size) {
                    continue
                }
                /*set1.size > set2.size*/
                for (e in set1.minus(set2)) {
                    if (hashSets.contains(set2.plus(e))) {
                        continue@loop
                    }
                }

                axiom3 = false
                break@out
            }
        }

        if (axiom3) {
            out.println("YES")
        } else {
            out.println("NO")
        }
    }

    out.close()
}
