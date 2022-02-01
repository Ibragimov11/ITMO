@file:Suppress("EnumEntryName", "NonAsciiCharacters", "FunctionName")

package csc.markobot.dsl

import csc.markobot.api.Schedule
import csc.markobot.api.WeekDay

fun MakroBotScenario.расписание(schedule: Schedule.() -> Unit): MakroBotScenario {
    this.schedule = Schedule().apply(schedule)
    return this
}

fun MakroBotScenario.сброситьРасписание(): MakroBotScenario {
    this.schedule = null
    return this
}

typealias время = Pair<WeekDay, Int>

infix fun WeekDay.в(hour: Int) = время(this, hour)

fun Schedule.повторять(vararg timePointsToAdd: время) {
    timePoints.addAll(timePointsToAdd)
}

infix fun ClosedRange<WeekDay>.в(hour: Int): List<время> {
    return WeekDay.values().filter { it in this }.map { время(it, hour) }           // can't iterate over ClosedRange
}

fun Schedule.повторять(timePointsToAdd: List<время>) = повторять(*timePointsToAdd.toTypedArray())

fun Schedule.кроме(vararg daysOfMonth: Int) {
    exceptDaysOfMonth.addAll(daysOfMonth.toList())
}
