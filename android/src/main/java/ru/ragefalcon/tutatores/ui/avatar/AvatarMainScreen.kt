package ru.ragefalcon.tutatores.ui.avatar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import com.google.android.material.tabs.TabLayoutMediator
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentMainAvatarBinding
import ru.ragefalcon.tutatores.extensions.setMargins


class AvatarMainScreen : BaseFragmentVM<FragmentMainAvatarBinding>(FragmentMainAvatarBinding::inflate) {

    private lateinit var avatarPageAdapter: AvatarPageAdapter
    private lateinit var myActivity: Activity

    override fun onDetach() {
        super.onDetach()
        Log.d("MyTag", "!!!!!________________________--------------AvatarMainScreen onDetach")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyTag", "!!!!!________________________--------------AvatarMainScreen onAttach")
        if (context is Activity) myActivity = context //as Activity
    }

    override fun onResume() {
        super.onResume()
        setMargins(binding.tabLayAvatar, 0, stateViewModel.statusBarSize.value!!, 0, 0)
        setMargins(binding.vpAvatar, 0, 0, 0, stateViewModel.navigationBarSize.value!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatarPageAdapter = AvatarPageAdapter(childFragmentManager,viewLifecycleOwner.lifecycle)
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

