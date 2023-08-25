package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.extensions.timeFromHHmmss

@Parcelize
data class ItemDenPlan(
    val id: String,
    val name: String,
    val time1: String,
    val time2: String,
    val sum_hour: Double,
    val vajn: Long,
    val gotov: Double,
    val data: Long,
    val opis: String,
    val privplan: Long,
    val stap_prpl: Long,
    val nameprpl: String,
    val namestap: String,
    override val sver: Boolean = true
) : SverOpis<ItemDenPlan>, Id_class(id_main = id.toString()), Parcelable {
    override fun sver(newSver: Boolean): ItemDenPlan = this.copy(sver = newSver)
    fun setGotov(
        value: Double,
        time1tut: String = time1,
        time2tut: String = time2,
        finishFun: (Double, Double, Double) -> Unit
    ) {
        val time1 = timeFromHHmmss(time1tut)
        var time2 = timeFromHHmmss(time2tut)
        if (time2 < time1) time2 += 24 * 60 * 60 * 1000
        val hourMls =
            (time2 - time1) * value / 100.0
        val sum_hour1 = hourMls.toDouble() / 1000F / 60F / 60F
        val exp: Double = when (vajn) {
            0L -> sum_hour1 * 1.25
            1L -> sum_hour1 //* 1.0
            2L -> sum_hour1 * 0.5
            3L -> sum_hour1 * 0.25
            else -> 0.0
        }
        finishFun(sum_hour1, value, exp)
    }

    fun getExp(value: Double, time1tut: String = time1, time2tut: String = time2): Double {
        val time1 = timeFromHHmmss(time1tut)
        var time2 = timeFromHHmmss(time2tut)
        if (time2 < time1) time2 += 24 * 60 * 60 * 1000
        val hourMls =
            (time2 - time1) * value / 100.0
        val sum_hour1 = hourMls.toDouble() / 1000F / 60F / 60F
        val exp: Double = when (vajn) {
            0L -> sum_hour1 * 1.25
            1L -> sum_hour1 //* 1.0
            2L -> sum_hour1 * 0.5
            3L -> sum_hour1 * 0.25
            else -> 0.0
        }

        return exp
    }
}
