package mpp.counter

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.jetbrains.kotlinx.lincheck.annotations.Operation;
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.junit.Test
import java.io.File

class ShardedCounterTest {
    private val c = ShardedCounter()

    @Operation
    fun inc() = c.inc()

    @Operation
    fun get() = c.get()

    @Test
    fun modelCheckingTest() = try {
        ModelCheckingOptions()
            .iterations(30)
            .invocationsPerIteration(10_000)
            .threads(3)
            .actorsPerThread(3)
            .checkObstructionFreedom()
            .sequentialSpecification(CounterSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("model-checking")
        throw t
    }

    @Test
    fun stressTest() = try {
        StressOptions()
            .iterations(30)
            .invocationsPerIteration(50_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(CounterSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("stress")
        throw t
    }
}

class CounterSequential : VerifierState() {
    private var c = 0

    fun inc() { c++ }
    fun get() = c

    override fun extractState() = c
}

private fun uploadWrongSolutionToS3(strategy: String) = runCatching {
    val solutionFile = File("src/$TASK_NAME.kt")
    val date = java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm").format(java.util.Date())
    val destinationFileLocation =
        "$YEAR/$TASK_NAME/$TASK_NAME-$date-$strategy-${kotlin.random.Random.nextInt(1000)}.kt"
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
private val TASK_NAME = "ShardedCounter"
private val S3_BUCKET_NAME = "mpp2022incorrectimplementations"