package ru.ragefalcon.tutatores.ui.time

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.transition.TransitionInflater
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialElevationScale
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentTimeBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.avatar.AvatarTabType

class TimeMainScreen : BaseFragmentVM<FragmentTimeBinding>(FragmentTimeBinding::inflate) {

    private lateinit var timePageAdapter: TimePageAdapter
    private lateinit var myActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) myActivity = context
    }

    override fun onResume() {
        super.onResume()
        setMargins(
            binding.clMainTime,
            0,
            stateViewModel.statusBarSize.value!!,
            0,
            stateViewModel.navigationBarSize.value!!
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timePageAdapter = TimePageAdapter(childFragmentManager,viewLifecycleOwner.lifecycle)
        with(binding) {
            stateViewModel.statusBarSize.observe(viewLifecycleOwner) {
                setMargins(
                    clMainTime,
                    0,
                    stateViewModel.statusBarSize.value!!,
                    0,
                    stateViewModel.navigationBarSize.value!!
                )
            }

            vpTime.adapter = timePageAdapter
            TabLayoutMediator(tabTime, vpTime) { tab, position ->
                tab.text = TimeTypePanel.values()[position].nameRazdel
            }.attach()

            with(viewmodel) {
                timeSpis.spisEffekt.observe(viewLifecycleOwner) {
                    if (it.isNotEmpty()) {
                        it.firstOrNull()?.let {    item ->
                            nameEffektTms.text = item.name
                            val nr = item.norma
                            tvHoursInfoEffektTms.text = "Н: ${item.sumNedel.roundToString(1)}/${nr.roundToString(1)} " +
                                    "  М: ${item.sumMonth.roundToString(1)}/${(nr * 4.286).roundToString(1)} " +
                                    "  Г: ${item.sumYear.roundToString(1)}/${(nr * 52).roundToString(1)}"
                            effektShkalTms.setItemEffekt(item)
                        }

                    } else {
                        collapse(llEffekt, duration = 0)
                    }
                }
            }
            llEffekt.setOnClickListener {
                showMyFragDial(EffektSpisFragDial())
            }

//            with(tabTime) {
//                setupWithViewPager(vpTime)
//            }
        }
    }

}