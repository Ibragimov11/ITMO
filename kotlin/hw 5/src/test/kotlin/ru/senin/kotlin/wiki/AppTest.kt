package ru.senin.kotlin.wiki

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream
import org.junit.jupiter.api.*
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AppTest {
    companion object {
        private const val TEST_DATA_PATH = "src/test/resources/testData"
        private const val TEMPORARY_DIRECTORY = "temp_test_data"
        private const val BZIP2_SUFFIX = ".bz2"
        private const val TIMEOUT = 30L

        @BeforeAll
        @JvmStatic
        fun createArchives() {
            val testRoot = File(TEMPORARY_DIRECTORY)
            if (!testRoot.exists()) {
                testRoot.mkdirs()
            }
            File(TEST_DATA_PATH).listFiles().orEmpty().filter { it.extension == "xml" }.forEach {
                createTemporaryBzip2File(it)
            }
            // create invalid Bzip2 file
            File("invalid".toBzip2Inputs()).writeText("Превед, Медвед!")
        }

        @AfterAll
        @JvmStatic
        fun cleanUp() {
            File(TEMPORARY_DIRECTORY).deleteRecursively()
        }

        private fun String.toBzip2Inputs(): String = this.split(',').joinToString(",") {
            Paths.get(TEMPORARY_DIRECTORY).resolve(it + BZIP2_SUFFIX).toString()
        }

        private fun createTemporaryBzip2File(file: File) {
            val input = file.inputStream()
            input.use {
                val output = BZip2CompressorOutputStream(FileOutputStream(file.name.toBzip2Inputs()))
                output.use {
                    input.copyTo(output)
                }
            }
        }
    }

    @Test
    @Timeout(TIMEOUT)
    fun `good XML`() {
        testInputs("simple.xml", threads = 1)
    }

    @Test
    @Timeout(TIMEOUT)
    fun `not well formed XML`() {
        assertThrows<Throwable> {
            testInputs("not-well-formed.xml", threads = 1)
        }
    }

    @Test
    @Timeout(TIMEOUT)
    fun `missed tags in XML`() {
        testInputs("missed-tags.xml", threads = 1)
    }

    @Test
    @Timeout(TIMEOUT)
    fun `XML without pages`() {
        testInputs("no-pages.xml", threads = 1)
    }

    @Test
    @Timeout(TIMEOUT)
    fun `wrong nesting of tags in XML`() {
        testInputs("wrong-nesting.xml", threads = 1)
    }

    @Test
    @Timeout(TIMEOUT)
    fun `incorrect bzip2`() {
        assertThrows<Throwable> {
            testInputs("invalid", threads = 1)
        }
    }

    @Test
    @Timeout(TIMEOUT)
    fun `incorrect input`() {
        assertThrows<Throwable> {
            testInputs("nonexistent", threads = 1)
        }
    }

    @Test
    @Timeout(TIMEOUT)
    fun `multiple inputs`() {
        testInputs("simple.xml,second.xml", threads = 1)
    }

    @Test
    @Timeout(TIMEOUT)
    fun `big XML single thread`() {
        testInputs("big.xml", threads = 1)
    }

    @Test
    @Timeout(TIMEOUT)
    fun `big XML multiple threads`() {
        testInputs("big.xml", threads = 4)
    }

    private fun testInputs(xmlInputs: String, threads: Int) {
        val outputPrefix = xmlInputs.replace(",", "__")
        val outputFileName = "$outputPrefix.actual.txt"

        val args = arrayOf(
            "--threads", threads.toString(),
            "--inputs", xmlInputs.toBzip2Inputs(),
            "--output", outputFileName.relativeToTemporaryDir()
        )
        main(args)
        val expectedFileName = "$outputPrefix.expected.txt"
        assertFilesHaveSameContent(expectedFileName, outputFileName)
    }

    private fun assertFilesHaveSameContent(expectedFileName: String, actualFileName: String, message: String? = null) {
        val actual = Paths.get(TEMPORARY_DIRECTORY).resolve(actualFileName).toFile().readText()
        val expected = Paths.get(TEST_DATA_PATH).resolve(expectedFileName).toFile().readText()
        assertEquals(expected, actual, message)
    }

    private fun String.relativeToTemporaryDir(): String = Paths.get(TEMPORARY_DIRECTORY).resolve(this).toString()
}
