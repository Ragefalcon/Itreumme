package ru.ragefalcon.sharedcode.models.commonfun

import com.soywiz.klock.*
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.minusTime
import ru.ragefalcon.sharedcode.extensions.unOffset

fun vozrast(birthday: Long): String {
    val today = DateTime.now()
    val bd = DateTime.fromUnix(birthday)

    val rangeOpen = bd until today
    val rangeOpen2 = (bd + rangeOpen.span.years.years) until today

    return "${cifrRuString(rangeOpen.span.years, TimeRuUnitPad.YEAR)} " +
            "${cifrRuString(rangeOpen2.duration.days.toInt(), TimeRuUnitPad.DAYS)} " +
            "${drawZero(rangeOpen.span.hours)}:${drawZero(rangeOpen.span.minutes)}:${drawZero(rangeOpen.span.seconds)}"
}

fun srokDoPosle(birthday: Long): String {
    val today1 = DateTime(DateTimeTz.nowLocal().minusTime().localUnix())
    val bd1 = DateTime.fromUnix(birthday)
    val today = if (bd1 > today1) bd1 + 1.days else today1
    val bd = minOf(bd1, today1)

    val rangeOpen = bd until today
    val rangeOpen2 = (bd + rangeOpen.span.years.years) until today
    val rezYear = if (rangeOpen.span.years != 0) "${cifrRuString(rangeOpen.span.years, TimeRuUnitPad.YEAR)} " else ""
    return rezYear + cifrRuString(rangeOpen2.duration.days.toInt(), TimeRuUnitPad.DAYS)
}

fun drawZero(value: Int): String {
    return if (value < 10) "0$value" else value.toString()
}

enum class TimeRuUnitPad(
    val one: String,
    val two: String,
    val ten: String
) {
    YEAR("год", "года", "лет"),
    DAYS("день", "дня", "дней"),
    HOURS("час", "часа", "часов"),
    MINUTES("минута", "минуты", "минут"),
    SECONDS("секунда", "секунды", "секунд")
}

fun cifrRuString(value: Int, unitPad: TimeRuUnitPad): String {
    var strEnd = ""
    if (value % 100 > 10 && value % 100 < 21) strEnd = unitPad.ten
    else when (value % 10) {
        1 -> strEnd = unitPad.one
        2 -> strEnd = unitPad.two
        3 -> strEnd = unitPad.two
        4 -> strEnd = unitPad.two
        5 -> strEnd = unitPad.ten
        6 -> strEnd = unitPad.ten
        7 -> strEnd = unitPad.ten
        8 -> strEnd = unitPad.ten
        9 -> strEnd = unitPad.ten
        0 -> strEnd = unitPad.ten
    }
    return "$value $strEnd"
}

fun unOffsetCorrFromBase(date: Long): Long {
    return date.unOffset()
}