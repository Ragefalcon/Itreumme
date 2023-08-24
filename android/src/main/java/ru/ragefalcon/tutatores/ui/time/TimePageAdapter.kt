package ru.ragefalcon.tutatores.ui.time

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class TimePageAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        when(TimeTypePanel.values()[position]){
            TimeTypePanel.DENPLAN -> return DenPlanFragment.newInstance()
//            TimeTypePanel.DENPLAN -> return PlanTabFragment.newInstance()
            TimeTypePanel.GLOBALPLAN -> return PlanTabFragment.newInstance()
//            TimeTypePanel.VXOD -> return PlanTabFragment.newInstance()
            TimeTypePanel.VXOD -> return VxodTabFragment.newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
//        return super.getItemPosition(`object`)
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return super.getPageTitle(position)
//                    return "asfsadf"
        return TimeTypePanel.values()[position].nameRazdel
    }

    override fun getCount(): Int {
        return TimeTypePanel.values().size
    }

}