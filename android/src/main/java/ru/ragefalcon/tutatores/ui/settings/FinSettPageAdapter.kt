package ru.ragefalcon.tutatores.ui.settings

import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ragefalcon.tutatores.R

class FinSettPageAdapter(fm: FragmentManager, ls: Lifecycle) : FragmentStateAdapter(fm,ls) {

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

}