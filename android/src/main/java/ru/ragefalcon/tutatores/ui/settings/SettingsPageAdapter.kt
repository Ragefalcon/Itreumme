package ru.ragefalcon.tutatores.ui.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SettingsPageAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    override fun createFragment(position: Int): Fragment {
        return when (SettingsTabType.values()[position]) {
            SettingsTabType.FINANCE -> SettingsFinFragment()
            SettingsTabType.SINC -> SettingsSincFragment()
            SettingsTabType.TIME -> SettingsFinFragment()
        }

    }

    override fun getItemCount(): Int {
        return SettingsTabType.values().size
    }

}