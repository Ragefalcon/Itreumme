package ru.ragefalcon.sharedcode.extensions

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(round: Int): Double {
    val por = 10.0.pow(round)

    return (this * por).roundToInt() / por
}

fun Double.roundToString(round: Int): String {
    val por = 10.0.pow(round)
    val afterPoint = (this % 1 * por).roundToInt()
    val str =
        "${por.roundToInt()}${if (afterPoint.toDouble() == por && afterPoint.toDouble() != this % 1 * por) afterPoint - 1 else afterPoint}"
    return "${(this - this % 1).roundToInt()}.${str.subSequence(str.length - round, str.length)}"
}

fun Double.roundToStringProb(round: Int): String {
    val number = if (this >= 0) this else -this
    var rez = ""
    val por = 10.0.pow(round)
    val afterPoint = (number % 1 * por).roundToInt()
    val str =
        "${por.roundToInt()}${if (afterPoint.toDouble() == por && afterPoint.toDouble() != number % 1 * por) afterPoint - 1 else afterPoint}"
    val str2 = "${(number - number % 1).roundToInt()}.${str.subSequence(str.length - round, str.length)}"
    val beforeL = str2.substringBefore(".").length
    if (beforeL > 3) {
        for (i in 1..(beforeL - beforeL % 3) / 3) {
            rez = "${str2.subSequence(beforeL - 3 * i, beforeL - 3 * (i - 1))} $rez"
        }
        if (beforeL % 3 != 0) rez = "${str2.subSequence(0, beforeL - 3 * ((beforeL - beforeL % 3) / 3))} $rez"
        rez = "${rez.subSequence(0, rez.length - 1)}"
    } else {
        rez = str2.substringBefore(".")
    }
    rez = "${if (this >= 0) "" else "- "}$rez.${str2.substringAfter(".")}"

    return rez
}