package ru.ragefalcon.tutatores.ui.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentSettingsBinding
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.extensions.setWindowTransparency
import ru.ragefalcon.tutatores.ui.finance.FinancePageAdapter
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class SettingsMainScreen : BaseFragmentVM<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private lateinit var settingsPageAdapter: SettingsPageAdapter

//    private lateinit var myActivity: Activity

    override fun onDetach() {
        super.onDetach()
//        settingsPageAdapter =
//        childFragmentManager.fragments.forEach { it.onDetach() }
        Log.d("MyTag", "!!!!!________________________--------------SettingsMainScreen onDetach")
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyTag", "!!!!!________________________--------------SettingsMainScreen onAttach")
    // childFragmentManager
//        if (context is Activity) myActivity = context //as Activity
    }

    override fun onResume() {
        super.onResume()
        setMargins(binding.tabLaySett, 0, stateViewModel.statusBarSize.value!!, 0, 0)
        setMargins(binding.vpSettings, 0, 0, 0, stateViewModel.navigationBarSize.value!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsPageAdapter = SettingsPageAdapter( childFragmentManager,viewLifecycleOwner.lifecycle)
//        viewLifecycleOwner.lifecycleScope.launch {
//            var count = 0
//            while (true){
//                Log.d("MyTag", "SettingsMainScreen $this :: $count")
//                delay(1000)
//                count++
//            }
//        }
        with(binding) {
            stateViewModel.statusBarSize.observe(viewLifecycleOwner) {
                setMargins(tabLaySett, 0, stateViewModel.statusBarSize.value!!, 0, 0)
                setMargins(vpSettings, 0, 0, 0, stateViewModel.navigationBarSize.value!!)
            }
            vpSettings.adapter = settingsPageAdapter
            TabLayoutMediator(tabLaySett, vpSettings) { tab, position ->
                tab.text = SettingsTabType.values()[position].nameTab
            }.attach()
        }
    }

}