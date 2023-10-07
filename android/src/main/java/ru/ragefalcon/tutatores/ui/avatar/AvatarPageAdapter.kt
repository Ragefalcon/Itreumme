package ru.ragefalcon.tutatores.ui.avatar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ragefalcon.tutatores.ui.avatar.dream.AvatarDreamTabFragment
import ru.ragefalcon.tutatores.ui.avatar.goal.AvatarGoalTabFragment

class AvatarPageAdapter(fm: FragmentManager, ls: Lifecycle) : FragmentStateAdapter(fm,ls) {

    override fun createFragment(position: Int): Fragment {
        return when (AvatarTabType.values()[position]) {
            AvatarTabType.AVATAR -> AvatarAvatarFragment()
            AvatarTabType.HISTORY -> AvatarHistoryFragment()
            AvatarTabType.HOAL -> AvatarGoalTabFragment()
            AvatarTabType.DREAM -> AvatarDreamTabFragment()
        }

    }

    override fun getItemCount(): Int {
        return AvatarTabType.values().size
    }

}