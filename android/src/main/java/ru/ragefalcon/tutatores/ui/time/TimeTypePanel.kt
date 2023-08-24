package ru.ragefalcon.tutatores.ui.time

import androidx.annotation.ColorRes
import ru.ragefalcon.tutatores.R
import androidx.annotation.ColorInt

enum class TimeTypePanel(
    val nameRazdel: String,
    @ColorRes
    val colorMain: Int,
    @ColorRes
    val colorText: Int
) {
    GLOBALPLAN("Проекты", R.color.colorDoxodTheme, R.color.colorDoxodItem),
    DENPLAN("Ежедневник", R.color.colorRasxodTheme, R.color.colorRasxodItem),
    VXOD("Входящие", R.color.colorSchetTheme, R.color.colorSchetItem);

    companion object {
        fun fromString(nameRazdel: String): TimeTypePanel {
            return when (nameRazdel) {
                "Ежедневник" -> DENPLAN
                "Проекты" -> GLOBALPLAN
                "Входящие" -> VXOD
                else -> throw IllegalStateException("неизвестный тип раздела: $nameRazdel")
            }
        }
    }
}

