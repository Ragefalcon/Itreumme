package ru.ragefalcon.tutatores.ui.finance

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import ru.ragefalcon.tutatores.adapter.FinanceType

class FinancePageAdapter(var analiz: Boolean, fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    fun toggleAnaliz(){
        analiz=analiz.not()
    }

    override fun getItem(position: Int): Fragment {
        if (analiz){
            return FinanceAnalizFragment.newInstance(FinanceType.values()[position].nameRazdel)
        }   else {
            when(FinanceType.values()[position]){
                FinanceType.RASXOD -> return RasxodFragment()
                FinanceType.DOXOD -> return DoxodFragment() //FinanceFragment.newInstance(FinanceType.values()[position].nameRazdel)
                FinanceType.SCHET -> return SchetFragment()
            }

        }
    }

    override fun getItemPosition(`object`: Any): Int {
//        return super.getItemPosition(`object`)
        return PagerAdapter.POSITION_NONE
    }
    override fun getPageTitle(position: Int): CharSequence? {
        //return super.getPageTitle(position)
//                    return "asfsadf"
        return FinanceType.values()[position].nameRazdel
    }

    override fun getCount(): Int {
        return FinanceType.values().size
    }

}