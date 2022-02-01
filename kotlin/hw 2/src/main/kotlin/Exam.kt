enum class Mark { A, B, Fail }

data class ExaminationSheet(
    val studentName: String,
    val courseName: String,
    /** Количество пересдач. Если экзамен сдан с первой попытки, должно быть нулём. */
    var retries: Int = 0,
    /** Текущая оценка. Значением null обозначена ситуация, когда студен ещё не пытался сдавать экзамен. */
    var mark: Mark? = null
) {
    init {
        if (mark == null) require(retries == 0)
    }
}

fun examineStudent(examinationSheet: ExaminationSheet): Mark {
    return when {
        examinationSheet.retries > 3 -> Mark.B
        examinationSheet.studentName.first() == examinationSheet.courseName.first() -> Mark.A
        examinationSheet.studentName.first().let { it in 'А'..'Я' && (it - 'А') % 2 == 0 } -> Mark.B
        else -> Mark.Fail
    }
}

/*
* Для каждой экзаменационной ведомости принять экзамен и заменить оценку на новую. В случае пересдачи увеличить
* значение количества пересдач.
* TODO В этой функции есть ошибка. Найдите и исправьте её.
*/
fun runExam(examinationSheets: List<ExaminationSheet>) {
    examinationSheets.forEach { sheet ->
        /** Если у студента уже есть отметка, то он пришёл на пересдачу. */
        sheet.mark?.let { sheet.retries++ }

        sheet.mark = examineStudent(sheet)
    }
}

fun main() {
    val sheets = listOf(
        ExaminationSheet("Альберт", "Алгебра", 0, null),
        ExaminationSheet("Георгий", "Биология", 1, Mark.B)
    )
    runExam(sheets)
    println(sheets.joinToString(""))
}