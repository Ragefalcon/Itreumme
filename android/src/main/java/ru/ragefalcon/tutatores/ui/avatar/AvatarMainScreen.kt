package ru.ragefalcon.tutatores.ui.avatar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentMainAvatarBinding
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.ui.avatar.dream.AvatarDreamTabFragment
import ru.ragefalcon.tutatores.ui.avatar.goal.AvatarGoalTabFragment


class AvatarMainScreen : BaseFragmentVM<FragmentMainAvatarBinding>(FragmentMainAvatarBinding::inflate) {

    private lateinit var avatarPageAdapter: AvatarPageAdapter
    private lateinit var myActivity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        avatarPageAdapter = AvatarPageAdapter(requireActivity()) // childFragmentManager
        if (context is Activity) myActivity = context //as Activity
    }

    override fun onResume() {
        super.onResume()
        setMargins(binding.tabLayAvatar, 0, stateViewModel.statusBarSize.value!!, 0, 0)
        setMargins(binding.vpAvatar, 0, 0, 0, stateViewModel.navigationBarSize.value!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            stateViewModel.statusBarSize.observe(viewLifecycleOwner) {
                setMargins(tabLayAvatar, 0, stateViewModel.statusBarSize.value!!, 0, 0)
                setMargins(vpAvatar, 0, 0, 0, stateViewModel.navigationBarSize.value!!)
            }
            vpAvatar.adapter = avatarPageAdapter
            TabLayoutMediator(tabLayAvatar, vpAvatar) { tab, position ->
                tab.text = AvatarTabType.values()[position].nameTab
            }.attach()
        }
    }
}


enum class AvatarTabType(
    val nameTab: String,
    @ColorRes
    val colorMain: Int,
    @ColorRes
    val colorText: Int
) {
    AVATAR("Аватар", R.color.colorSchetTheme, R.color.colorSchetItem),
    HISTORY("История", R.color.colorRasxodTheme, R.color.colorRasxodItem),
    HOAL("Цели", R.color.colorDoxodTheme, R.color.colorDoxodItem),
    DREAM("Мечты", R.color.colorDoxodTheme, R.color.colorDoxodItem);
}

class AvatarPageAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

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