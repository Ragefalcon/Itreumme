package ru.ragefalcon.tutatores.ui.settings

import androidx.annotation.ColorRes
import ru.ragefalcon.tutatores.R

enum class SettingsTabType(
        val nameTab: String,
        @ColorRes
        val colorMain: Int,
        @ColorRes
        val colorText: Int
    ) {
    SINC("Синхронизация", R.color.colorSchetTheme, R.color.colorSchetItem),
    FINANCE("Финансы", R.color.colorRasxodTheme, R.color.colorRasxodItem),
    TIME("Время", R.color.colorDoxodTheme, R.color.colorDoxodItem);

//        companion object {
//            fun fromString(nameRazdel: String) : FinanceType {
//                return when (nameRazdel){
//                    "Расход" -> RASXOD
//                    "Доход" -> DOXOD
//                    "Счета" -> SCHET
//                    else -> throw IllegalStateException("неизвестный тип раздела: $nameRazdel")
//                }
//            }
//        }
//    }


}