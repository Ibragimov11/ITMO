import java.io.*
import java.util.*

class MyReaderA(inputStream: InputStream) {
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
    val reader = MyReaderA(FileInputStream("schedule.in"))
    val out = PrintWriter(FileOutputStream("schedule.out"))
    //    val reader = MyReaderA(System.`in`)
    //    val out = PrintWriter(System.out)
    fun next() = reader.next()
    fun nextInt() = next().toInt()

    val n = nextInt()
    val tasks = Array(n) { Task(nextInt(), nextInt()) }
    var ans = tasks.fold(0L) { sum, task -> sum + task.w }
    tasks.sort()

    val successTasks = TreeMap<Int/*weight*/, Int/*count of this weight*/>()
    var count = 0

    for (task in tasks) {
        if (count < task.d) {
            successTasks[task.w] = successTasks[task.w]?.plus(1) ?: 1
            count++
        } else if (successTasks.isNotEmpty() && successTasks.firstKey() < task.w) {
            val k = successTasks.firstKey()

            if (successTasks[k] == 1) {
                successTasks.remove(k)
            } else {
                successTasks[k] = successTasks[k]!!.minus(1)
            }

            successTasks[task.w] = successTasks[task.w]?.plus(1) ?: 1
        }
    }

    for ((w, cnt) in successTasks) {
        repeat(cnt) {
            ans -= w
        }
    }

    out.println(ans)
    out.close()
}

data class Task(val d: Int, val w: Int) : Comparable<Task> {
    override fun compareTo(other: Task): Int {
        return if (d != other.d) {
            compareValues(d, other.d)
        } else {
            -compareValues(w, other.w)
        }
    }
}