package ru.ragefalcon.tutatores.adapter

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import ru.ragefalcon.tutatores.R

enum class FinanceType(
    val nameRazdel: String,
    @ColorRes
    val colorMain: Int,
    @ColorRes
    val colorText: Int
) {
    RASXOD("Расход",R.color.colorRasxodTheme,R.color.colorRasxodItem),
    DOXOD("Доход",R.color.colorDoxodTheme,R.color.colorDoxodItem),
    SCHET("Счета",R.color.colorSchetTheme,R.color.colorSchetItem);

    companion object {
        fun fromString(nameRazdel: String) : FinanceType {
            return when (nameRazdel){
                "Расход" -> RASXOD
                "Доход" -> DOXOD
                "Счета" -> SCHET
                else -> throw IllegalStateException("неизвестный тип раздела: $nameRazdel")
            }
        }
    }
}

