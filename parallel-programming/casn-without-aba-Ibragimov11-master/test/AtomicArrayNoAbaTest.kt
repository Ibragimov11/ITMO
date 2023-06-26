import com.amazonaws.auth.*
import com.amazonaws.regions.*
import com.amazonaws.services.s3.*
import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.paramgen.*
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
import org.jetbrains.kotlinx.lincheck.verifier.*
import org.junit.*
import java.io.*

@Param.Params(
    Param(name = "index", gen = IntGen::class, conf = "0:4"),
)
class AtomicArrayNoAbaTest {
    private val a = AtomicArrayNoAba(5, 0)

    @Operation(params = ["index"])
    fun get(index: Int) =
        a.get(index)

    @Operation(params = ["index"])
    fun inc(index: Int) {
        while (true) {
            val value = a.get(index)
            if (a.cas(index, value, value + 1)) break
        }
    }

    @Operation(params = ["index", "index"])
    fun inc(index1: Int, index2: Int) {
        while (true) {
            val value1 = a.get(index1)
            val value2 = a.get(index2)
            if (a.cas2(index1, value1, value1 + 1, index2, value2, value2 + 1)) return
        }
    }

    @Test
    fun stressTest() = try {
        StressOptions()
            .iterations(100)
            .invocationsPerIteration(10_000)
            .actorsBefore(0)
            .actorsAfter(0)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(AtomicArrayIntSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("stress")
        throw t
    }

    @Test
    fun modelCheckingTest() = try {
        ModelCheckingOptions()
            .iterations(100)
            .invocationsPerIteration(10_000)
            .actorsBefore(0)
            .actorsAfter(0)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(AtomicArrayIntSequential::class.java)
            .checkObstructionFreedom(true)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("model-checking")
        throw t
    }
}

class AtomicArrayIntSequential : VerifierState() {
    private val a = IntArray(5)

    fun get(index: Int): Int? = a[index]

    fun inc(index: Int) {
        a[index]++
    }

    fun inc(index1: Int, index2: Int) {
        a[index1]++
        a[index2]++
    }
    override fun extractState() = a.toList()
}

private fun uploadWrongSolutionToS3(strategy: String) = runCatching {
    val solutionFile = File("src/$TASK_NAME.kt")
    val date = java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm").format(java.util.Date())
    val destinationFileLocation = "$YEAR/$TASK_NAME/$TASK_NAME-$date-$strategy-${kotlin.random.Random.nextInt(1000)}.kt"
    val credentials = BasicAWSCredentials("AKIA27OSP7CB7EEHHOX7", "iyFzeiqHS0amZQj79Jh1DNMy+s96f+fcJvy+BHQu")
    val s3client = AmazonS3ClientBuilder.standard()
        .withCredentials(AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.US_EAST_2)
        .build()
    s3client.putObject(S3_BUCKET_NAME, destinationFileLocation, solutionFile)
}.let {
    if (it.isFailure) {
        System.err.println("INCORRECT IMPLEMENTATION UPLOADING HAS FAILED, PLEASE CONTACT NIKITA KOVAL TO FIX THE ISSUE")
        it.exceptionOrNull()!!.printStackTrace()
    }
}

private val YEAR = "2022"
private val TASK_NAME = "AtomicArrayNoAba"
private val S3_BUCKET_NAME = "mpp2022incorrectimplementations"
