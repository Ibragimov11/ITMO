import com.amazonaws.auth.*
import com.amazonaws.regions.*
import com.amazonaws.services.s3.*
import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
import org.jetbrains.kotlinx.lincheck.verifier.*
import org.junit.*
import java.io.*
import kotlin.coroutines.*

class SynchronousQueueTest {
    private val q = SynchronousQueue<Int>()

    @Operation(cancellableOnSuspension = false)
    suspend fun send(element: Int) {
        q.send(element)
    }

    @Operation(cancellableOnSuspension = false)
    suspend fun receive(): Int = q.receive()

    @Test
    fun modelCheckingTest() = try {
        ModelCheckingOptions()
            .iterations(100)
            .invocationsPerIteration(10_000)
            .actorsBefore(0)
            .threads(3)
            .actorsPerThread(3)
            .actorsAfter(0)
            .checkObstructionFreedom()
            .sequentialSpecification(SynchronousQueueSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("model-checking")
        throw t
    }

    @Test
    fun stressTest() = try {
        StressOptions()
            .iterations(100)
            .invocationsPerIteration(50_000)
            .actorsBefore(0)
            .threads(3)
            .actorsPerThread(3)
            .actorsAfter(0)
            .sequentialSpecification(SynchronousQueueSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("stress")
        throw t
    }
}

class SynchronousQueueSequential : VerifierState() {
    private val senders = ArrayList<Pair<Continuation<Unit>, Int>>() // pair = continuation + element
    private val receivers = ArrayList<Continuation<Int>>()

    suspend fun send(element: Int) {
        if (receivers.isNotEmpty()) {
            val r = receivers.removeAt(0)
            r.resume(element)
        } else {
            suspendCoroutine<Unit> { cont ->
                senders.add(cont to element)
            }
        }
    }

    suspend fun receive(): Int {
        if (senders.isNotEmpty()) {
            val (s, elem) = senders.removeAt(0)
            s.resume(Unit)
            return elem
        } else {
            return suspendCoroutine { cont ->
                receivers.add(cont)
            }
        }
    }

    override fun extractState() = Unit
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
private val TASK_NAME = "SynchronousQueue"
private val S3_BUCKET_NAME = "mpp2022incorrectimplementations"
