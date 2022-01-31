package ru.senin.kotlin.wiki

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.xml.parsers.SAXParserFactory

fun solve(parameters: Parameters) {
    val inputFiles = parameters.inputs
    val outputFile = parameters.output
    val threads = parameters.threads
    val factory = SAXParserFactory.newInstance()
    val parser = factory.newSAXParser()
    val statistics = Statistics()
    val service = Executors.newFixedThreadPool(threads)

    for (bz2File in inputFiles) {
        service.submit {
            val file = BZip2CompressorInputStream(FileInputStream(bz2File))
            val handler = MyHandler(statistics)

            parser.parse(file, handler)
        }
    }

    service.shutdown()

    if (!service.awaitTermination(20, TimeUnit.MINUTES)) {
        service.shutdownNow()
    }

    writeStatistics(outputFile, statistics)
}

class Statistics {
    val wordsInTitles = ConcurrentHashMap<String, Int>()
    val wordsInRevisions = ConcurrentHashMap<String, Int>()
    val sizes = ConcurrentHashMap<Int, Int>()
    val years = ConcurrentHashMap<Int, Int>()

    fun correctPageStatistics(): Boolean {
        return wordsInTitles.isNotEmpty() && wordsInRevisions.isNotEmpty() && years.size == 1 && sizes.size == 1
    }

    fun addAll(pageStatistics: Statistics) {
        add(pageStatistics.wordsInTitles, wordsInTitles)
        add(pageStatistics.wordsInRevisions, wordsInRevisions)
        add(pageStatistics.sizes, sizes)
        add(pageStatistics.years, years)
    }

    private fun <K> add(map1: ConcurrentHashMap<K, Int>, map2: ConcurrentHashMap<K, Int>) {
        map1.forEach { (key, value) ->
            map2[key] = map2[key]?.plus(value) ?: value
        }
    }
}

class MyHandler(private val allStatistics: Statistics) : DefaultHandler() {
    private val tagMediawiki = "mediawiki"
    private val tagPage = "page"
    private val tagTitle = "title"
    private val tagRevision = "revision"
    private val tagText = "text"
    private val tagTimestamp = "timestamp"
    private var pageStatistics = Statistics()
    private val stackTags = Stack<String>()
    private var buffer = StringBuffer()

    override fun startElement(uri: String?, localName: String?, tagName: String?, attributes: Attributes?) {
        stackTags.add(tagName)

        when (tagName) {
            tagPage -> {
                pageStatistics = Statistics()
            }
            tagTitle, tagTimestamp, tagText -> {
                if (correctPath()) {
                    buffer = StringBuffer()

                    if (tagName == tagText) {
                        val bytes = attributes?.getValue("bytes")

                        bytes?.let {
                            var size = it.toInt()
                            var key = 0

                            while (size >= 10) {
                                key++
                                size /= 10
                            }

                            pageStatistics.sizes[key] = pageStatistics.sizes[key]?.plus(1) ?: 1
                        }
                    }
                }
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, tagName: String?) {
        if (tagName == stackTags.peek()) {
            stackTags.pop()

            when (tagName) {
                tagPage -> {
                    if (pageStatistics.correctPageStatistics()) {
                        allStatistics.addAll(pageStatistics)
                    }
                }
                tagTitle, tagText -> {
                    val text = buffer.toString()
                    val regex = Regex("""[а-яА-Я]{3,}""")
                    val matches = regex.findAll(text).map { it.groupValues[0] }.map { it.lowercase() }
                    val map = if (tagName == tagTitle) {
                        pageStatistics.wordsInTitles
                    } else {
                        pageStatistics.wordsInRevisions
                    }

                    matches.forEach {
                        map[it] = map[it]?.plus(1) ?: 1
                    }
                }
                tagTimestamp -> {
                    val text = buffer.toString()
                    val regex = Regex("""^[0-9]{4}""")
                    val year = regex.find(text)

                    if (year != null) {
                        pageStatistics.years[year.value.toInt()] =
                            pageStatistics.years[year.value.toInt()]?.plus(1) ?: 1
                    }
                }
            }
        } else {
            throw Exception("Incorrect XML")
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        if (correctPath()) {
            ch?.let {
                buffer.append(String(it, start, length))
            }
        }
    }

    private fun correctPath(): Boolean {
        val size = stackTags.size

        fun isCorrectTitlePath() = size > 2
                && stackTags[size - 3] == tagMediawiki
                && stackTags[size - 2] == tagPage
                && stackTags[size - 1] == tagTitle

        fun isCorrectTimestampPath() = size > 3
                && stackTags[size - 4] == tagMediawiki
                && stackTags[size - 3] == tagPage
                && stackTags[size - 2] == tagRevision
                && stackTags[size - 1] == tagTimestamp

        fun isCorrectTextPath() = size > 3
                && stackTags[size - 4] == tagMediawiki
                && stackTags[size - 3] == tagPage
                && stackTags[size - 2] == tagRevision
                && stackTags[size - 1] == tagText

        return isCorrectTitlePath() || isCorrectTimestampPath() || isCorrectTextPath()
    }
}

private fun writeStatistics(outputFile: String, statistics: Statistics) {
    fun PrintWriter.writeWords(map: ConcurrentHashMap<String, Int>) {
        val sortedMap = map.toList().groupBy({ it.second }, { it.first }).toSortedMap(compareBy { -it })
        var counter = 300

        loop@ for ((key, list) in sortedMap) {
            for (value in list.sorted()) {
                if (counter <= 0) {
                    break@loop
                }

                this.write("$key $value")
                this.println()
                counter--
            }
        }
    }

    fun PrintWriter.writeNumbers(map: ConcurrentHashMap<Int, Int>) {
        if (map.isNotEmpty()) {
            val min = map.minOf { it.key }
            val max = map.maxOf { it.key }

            for (key in min..max) {
                this.write("$key ${map[key] ?: 0}")
                this.println()
            }
        }
    }

    PrintWriter(FileOutputStream(outputFile), true, Charsets.UTF_8).use {
        it.write("Топ-300 слов в заголовках статей:")
        it.println()
        it.writeWords(statistics.wordsInTitles)
        it.println()

        it.write("Топ-300 слов в статьях:")
        it.println()
        it.writeWords(statistics.wordsInRevisions)
        it.println()

        it.write("Распределение статей по размеру:")
        it.println()
        it.writeNumbers(statistics.sizes)
        it.println()

        it.write("Распределение статей по времени:")
        it.println()
        it.writeNumbers(statistics.years)
    }
}

