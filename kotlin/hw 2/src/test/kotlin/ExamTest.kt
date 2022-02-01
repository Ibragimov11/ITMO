import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Test {
    @Test
    fun testPositive() {
        val sheets = listOf(
            ExaminationSheet("Альберт", "Алгебра", 0, null),
            ExaminationSheet("Георгий", "Биология", 1, Mark.B)
        )
        runExam(sheets)
        assertEquals(Mark.A, sheets[0].mark, "Неверная оценка")
        assertEquals(
            0,
            sheets[0].retries,
            "Ожидается, что студент, 'способный' сдать экзамен с первой попытки, будет иметь ноль пересдач"
        )
    }

    @Test
    fun testNonZeroForAll() {
        val sheets = listOf(
            ExaminationSheet("Альберт", "Алгебра", 0, Mark.B),
            ExaminationSheet("Георгий", "Геология", 1, Mark.B)
        )
        runExam(sheets)
        assertEquals(listOf(Mark.A, Mark.A), sheets.map { it.mark })
        assertEquals(1, sheets[0].retries, "Неверное количество пересдач")
        assertEquals(2, sheets[1].retries, "Неверное количество пересдач")
    }

    @Test
    fun testZeroForAll() {
        val sheets = listOf(
            ExaminationSheet("Альберт", "Алгебра", 0, Mark.B),
            ExaminationSheet("Георгий", "Геология", 1, Mark.B)
        )
        runExam(sheets)
        assertEquals(listOf(Mark.A, Mark.A), sheets.map { it.mark })
        assertEquals(1, sheets[0].retries, "Неверное количество пересдач")
        assertEquals(2, sheets[1].retries, "Неверное количество пересдач")
    }
}