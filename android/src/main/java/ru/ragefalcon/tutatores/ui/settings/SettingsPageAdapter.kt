package ru.ragefalcon.tutatores.ui.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SettingsPageAdapter(fm: FragmentManager, ls: Lifecycle) : FragmentStateAdapter(fm,ls) {

    override fun createFragment(position: Int): Fragment {
        return when (SettingsTabType.values()[position]) {
            SettingsTabType.FINANCE -> SettingsFinFragment()
            SettingsTabType.SINC -> SettingsSincFragment()
//            SettingsTabType.TIME -> SettingsFinFragment()
        }

    }

    override fun getItemCount(): Int {
        return SettingsTabType.values().size
    }

}