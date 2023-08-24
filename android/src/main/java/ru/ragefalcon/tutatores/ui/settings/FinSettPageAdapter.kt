package ru.ragefalcon.tutatores.ui.settings

import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ragefalcon.tutatores.R

class FinSettPageAdapter(fm: FragmentActivity): FragmentStateAdapter(fm) {

    override fun createFragment(position: Int): Fragment {
        return when(FinSettTabType.values()[position]){
            FinSettTabType.SCHETA -> FinSettSchetPage()
            FinSettTabType.TYPERASXOD -> FinSettTyperasxodPage()
            FinSettTabType.TYPEDOXOD -> FinSettTypedoxodPage()
            FinSettTabType.VALUTA -> FinSettValutPage()
        }

    }

    override fun getItemCount(): Int {
        return FinSettTabType.values().size
    }

}

enum class FinSettTabType(
    val nameTab: String,
    @ColorRes
    val colorMain: Int,
    @ColorRes
    val colorText: Int
) {
    SCHETA("Синхро234низация", R.color.colorSchetTheme, R.color.colorSchetItem),
    TYPERASXOD("Фин234ансы", R.color.colorRasxodTheme, R.color.colorRasxodItem),
    TYPEDOXOD("Вре234мя", R.color.colorDoxodTheme, R.color.colorDoxodItem),
    VALUTA("Вр234емя", R.color.colorDoxodTheme, R.color.colorDoxodItem);

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