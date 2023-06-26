package mpp.linkedlistset

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.junit.*
import java.io.File

class LinkedListSetTest {
    private val s = LinkedListSet<Int>()

    @Operation
    fun add(element: Int): Boolean = s.add(element)

    @Operation
    fun remove(element: Int): Boolean = s.remove(element)

    @Operation
    fun contains(element: Int): Boolean = s.contains(element)

    @Test
    fun modelCheckingTest() = try {
        ModelCheckingOptions()
            .iterations(100)
            .invocationsPerIteration(10_000)
            .threads(3)
            .actorsPerThread(3)
            .checkObstructionFreedom()
            .sequentialSpecification(IntSetSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("model-checking")
        throw t
    }

    @Test
    fun stressTest() = try {
        StressOptions()
            .iterations(100)
            .invocationsPerIteration(10_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(IntSetSequential::class.java)
            .check(this::class.java)
    } catch (t: Throwable) {
        uploadWrongSolutionToS3("stress")
        throw t
    }
}

class IntSetSequential : VerifierState() {
    private val s = HashSet<Int>()

    fun add(element: Int): Boolean = s.add(element)

    fun remove(element: Int): Boolean = s.remove(element)

    fun contains(element: Int): Boolean = s.contains(element)

    override fun extractState() = s
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
private val TASK_NAME = "LinkedListSet"
private val S3_BUCKET_NAME = "mpp2022incorrectimplementations"