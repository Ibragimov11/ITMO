@file:Suppress("FunctionName", "NonAsciiCharacters")

package csc.markobot.dsl

import csc.markobot.api.MakroBot
import csc.markobot.api.Schedule

@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class MakroBotDsl

@MakroBotDsl
class MakroBotScenario {
    private val actions = arrayListOf<() -> Unit>()

    internal var schedule: Schedule? = null

    infix fun MakroBot.вперед(steps: Int) {
        this@MakroBotScenario.actions.add { stepForward(steps) }
    }

    infix fun MakroBot.назад(steps: Int) {
        this@MakroBotScenario.actions.add { stepBack(steps) }
    }

    fun MakroBot.развернуться() {
        this@MakroBotScenario.actions.add { turnAround() }
    }

    class PronounceBlock {
        private val strings = arrayListOf<String>()

        operator fun String.unaryPlus() {                               // operator overload
            strings.add(this)
        }

        val text: String get() = strings.joinToString(separator = "\n")
    }

    infix fun MakroBot.воспроизвести(text: PronounceBlock.() -> Unit) {

        val pronounceBlock = PronounceBlock().apply(text)

        this@MakroBotScenario.actions.add { pronounce(pronounceBlock.text) }
    }

    operator fun MakroBot.invoke(settings: MakroBot.() -> Unit) = this.settings()

    // no way to restrict its call inside scenario :(
    fun запуститьСейчас(): MakroBotScenario {
        actions.forEach { it() }

        schedule?.let {
            println(it)
        }
        return this
    }
}

fun сценарий(operations: MakroBotScenario.() -> Unit): MakroBotScenario {
    return MakroBotScenario().apply(operations)
}

operator fun MakroBot.component1(): String = name
operator fun MakroBot.component2(): Int = speed
operator fun MakroBot.component3(): Int = power
