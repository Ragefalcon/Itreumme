package ru.ragefalcon.sharedcode.extensions

import com.soywiz.klock.*

enum class TimeUnits(val milliseconds: Long) {
    SECOND(1000),
    MINUTE(60*SECOND.milliseconds),
    HOUR(60*MINUTE.milliseconds),
    DAY(24*HOUR.milliseconds),
    MONTH(30*DAY.milliseconds),
    YEAR(365*DAY.milliseconds);

    fun plural(value: Int): String {
        var st1: String = "день"
        var st2: String = "дня"
        var st3: String = "дней"
        when (this) {
            TimeUnits.SECOND -> {
                st1 = "секунду"; st2 = "секунды"; st3 = "секунд"
            }
            TimeUnits.MINUTE -> {
                st1 = "минуту"; st2 = "минуты"; st3 = "минут"
            }
            TimeUnits.HOUR -> {
                st1 = "час"; st2 = "часа"; st3 = "часов"
            }
            TimeUnits.DAY -> {
                st1 = "день"; st2 = "дня"; st3 = "дней"
            }
            TimeUnits.MONTH -> {
                st1 = "месяц"; st2 = "месяца"; st3 = "месяцев"
            }
            TimeUnits.YEAR -> {
                st1 = "год"; st2 = "года"; st3 = "лет"
            }
        }



        if ((value % 100 > 4) && (value % 100 < 21)) {
            return value.toString() + " " + st3
        } else {
            if (value % 10 == 1) {
                return value.toString() + " " + st1
            } else if ((value % 10 > 1) && (value % 10 < 5)) {
                return value.toString() + " " + st2
            } else {
                return value.toString() + " " + st3
            }
        }

    }
}

fun DateTimeTz.localUnix(): Long {
    return this.local.unixMillisLong
}

fun Long.unOffset(): Long {
    val offsetTime: Long = DateTimeTz.nowLocal().offset.time.millisecondsLong
    return (this-offsetTime)
}

fun Long.withOffset(): Long {
    val offsetTime: Long = DateTimeTz.nowLocal().offset.time.millisecondsLong
    return (this+offsetTime)
}

fun Long.longMinusTimeLocal(): Long {
    return DateTimeTz.fromUnixLocal(this).minusTime().localUnix()
}
fun Long.longMinusTimeUTC(): Long {
    return DateTimeTz.fromUnix(this).minusTime().utc.unixMillisLong
}
fun DateTimeTz.minusTime(): DateTimeTz {
    return this -this.hours.hours-this.minutes.minutes-this.seconds.seconds-this.milliseconds.milliseconds// - this.offset.time
}

fun DateTimeTz.unOffset(): DateTimeTz {
    val offsetTime: Long = DateTimeTz.nowLocal().offset.time.millisecondsLong
    return this - offsetTime.milliseconds
}
fun DateTimeTz.withOffset(): DateTimeTz {
    val offsetTime: Long = DateTimeTz.nowLocal().offset.time.millisecondsLong
    return this + offsetTime.milliseconds
}

fun DateTimeTz.firstDayOfMonth(): DateTimeTz {
    return this.minusTime() -this.dayOfMonth.days + 1.days
}
fun DateTimeTz.firstDayOfNextMonth(): DateTimeTz {
    return this.firstDayOfMonth()  + 1.months
}

fun DateTimeTz.firstDayOfWeek(): DateTimeTz {
    return this.minusTime() - this.dayOfWeekInt.days + 1.days
}

fun timeFromHHmmss(str: String):Long {
//    var aa = Calendar.getInstance()
//    aa.set(Calendar.HOUR_OF_DAY, time.subSequence(0, 1).toString().toInt())
//    aa.set(Calendar.MINUTE, time.subSequence(3, 4).toString().toInt())
    val hours = str.subSequence(0, 2).toString().toLong()
    val minutes = if(str.length>=5)str.subSequence(3, 5).toString().toLong() else 0
    val seconds = if(str.length==8)str.subSequence(6, 8).toString().toLong() else 0
    return ((hours*60L+minutes)*60L+seconds)*1000L
}


fun DateUnixToDouble(dateUnix: Int): Double {
    var rez = dateUnix.toDouble()
    return (rez / 3600000.0 / 24.0 + 25569.0)
}

fun DoubleToDateUnix(date: Double): Int {
    var rez = ((date - 25569.0) * 3600000.0 * 24.0).toInt()
    return rez
}
