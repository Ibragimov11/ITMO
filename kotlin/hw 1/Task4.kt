const val string1 = "не был так близок к провалу"
const val string2 = "ещё"

fun foo(
        head: String = "никогда",
        phrase: String = " не говори",
        tail: String = " никогда"
    ) = if (phrase.isNotEmpty()) {
        "$head$phrase$tail"
    }
    else {
        ""
    }

fun main() {
    println(
            foo(
                    head = foo("", phrase = string2),
                    tail = string1,
                    phrase = " Штирлиц "
            )
    )
}