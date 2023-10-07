package ru.ragefalcon.tutatores.ui.time

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TimePageAdapter(fm: FragmentManager, ls: Lifecycle) : FragmentStateAdapter(fm,ls) {


    override fun createFragment(position: Int): Fragment {
        return when(TimeTypePanel.values()[position]){
            TimeTypePanel.DENPLAN -> DenPlanFragment.newInstance()
            TimeTypePanel.GLOBALPLAN -> PlanTabFragment.newInstance()
            TimeTypePanel.VXOD -> VxodTabFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        return TimeTypePanel.values().size
    }
}