package ru.ragefalcon.tutatores.ui.finance

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ragefalcon.tutatores.adapter.FinanceType
import ru.ragefalcon.tutatores.ui.time.DenPlanFragment
import ru.ragefalcon.tutatores.ui.time.PlanTabFragment
import ru.ragefalcon.tutatores.ui.time.TimeTypePanel
import ru.ragefalcon.tutatores.ui.time.VxodTabFragment

class FinancePageAdapter(var analiz: Boolean, fm: FragmentManager, ls: Lifecycle) : FragmentStateAdapter(fm,ls) {

    override fun getItemCount(): Int {
        return FinanceType.values().size
    }

    fun toggleAnaliz(){
        analiz=analiz.not()
        this.notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return if (analiz){
            FinanceAnalizFragment.newInstance(FinanceType.values()[position].nameRazdel)
        }   else {
            when(FinanceType.values()[position]){
                FinanceType.RASXOD -> RasxodFragment()
                FinanceType.DOXOD -> DoxodFragment()
                FinanceType.SCHET -> SchetFragment()
            }

        }
    }

}