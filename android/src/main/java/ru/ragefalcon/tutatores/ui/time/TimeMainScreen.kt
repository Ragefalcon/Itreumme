package ru.ragefalcon.tutatores.ui.time

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionInflater
import com.google.android.material.transition.MaterialElevationScale
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentTimeBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class TimeMainScreen : BaseFragmentVM<FragmentTimeBinding>(FragmentTimeBinding::inflate) {

    private lateinit var timePageAdapter: TimePageAdapter

    private lateinit var myActivity: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
//            exitTransition = Hold()
//            reenterTransition = Hold()
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300 //resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300 //resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
//        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) myActivity = context //as Activity
//        Log.d("statBar", "TimeMS onAttach0 stateViewModel.firstStart: ${stateViewModel.firstStart}")
//        if (stateViewModel.firstStart) {
//            Log.d("statBar", "TimeMS onAttach1 stateViewModel.firstStart: ${stateViewModel.firstStart}")
//            Log.d("statBar", "TimeMS onAttach2 stateViewModel.firstStart: $myActivity")
//            myActivity.setWindowTransparency { statusBarSize, navigationBarSize ->
//                Log.d("statBar", "TimeMS onAttach3 stateViewModel.firstStart: ${stateViewModel.firstStart}")
//                if (stateViewModel.firstStart) {
//                    stateViewModel.statusBarSize = statusBarSize
//                    stateViewModel.navigationBarSize = navigationBarSize
//                    Log.d("statBar", "TimeMS onAttach  statusBarSize: $statusBarSize")
//                    Log.d("statBar", "TimeMS onAttach  stateViewModel.statusBarSize: ${stateViewModel.statusBarSize}")
//                    setMargins(binding.tabTime, 0, stateViewModel.statusBarSize, 0, 0)
//                    setMargins(binding.vpTime, 0, 0, 0, stateViewModel.navigationBarSize)
//                    stateViewModel.firstStart = false
//                }
//            }
//        }
        timePageAdapter = TimePageAdapter(childFragmentManager) // childFragmentManager
    }

    override fun onResume() {
        super.onResume()
        Log.d("statBar", "TimeMS onResume stateViewModel.statusBarSize: ${stateViewModel.statusBarSize}")
        Log.d("statBar", "TimeMS onResume stateViewModel.navigationBarSize: ${stateViewModel.navigationBarSize}")
        setMargins(
            binding.clMainTime,
            0,
            stateViewModel.statusBarSize.value!!,
            0,
            stateViewModel.navigationBarSize.value!!
        )
//        setMargins(binding.tabTime, 0, stateViewModel.statusBarSize.value!!, 0, 0)
//        setMargins(binding.vpTime, 0, 0, 0, stateViewModel.navigationBarSize.value!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyTut", "TimeMainScreen: onViewCreated")
        with(binding) {
            stateViewModel.statusBarSize.observe(viewLifecycleOwner) {
                Log.d("statBar", "TimeMS onViewCreated stateViewModel.statusBarSize: ${stateViewModel.statusBarSize}")
                Log.d(
                    "statBar",
                    "TimeMS onViewCreated stateViewModel.navigationBarSize: ${stateViewModel.navigationBarSize}"
                )
                setMargins(
                    clMainTime,
                    0,
                    stateViewModel.statusBarSize.value!!,
                    0,
                    stateViewModel.navigationBarSize.value!!
                )
            }


            vpTime.adapter = timePageAdapter

            with(viewmodel) {
                timeSpis.spisEffekt.observe(viewLifecycleOwner) {
                    println("Check current date and update spisEffekt count: ${it.count()}")
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
//                    for (item in it) Log.d(
//                        "MyTut",
//                        "effektSpis: ${item.name}|${item.norma}|${item.sumNedel}|${item.sumMonth}|${item.sumYear}"
//                    );
                }
            }
            llEffekt.setOnClickListener {
                showMyFragDial(EffektSpisFragDial())
            }

            with(tabTime) {
                setupWithViewPager(vpTime)
//            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                }
//
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    val position = tab?.position ?: 0
//                    stateViewModel.currentFinType = FinanceType.values()[position]
//                    if ((cl_backgr.background as ColorDrawable).color != colorMain[position]) {
//                        val rect = Rect()
//                        tab?.let {
//                            val tabView = it.view as View
//                            tabView.getGlobalVisibleRect(rect)
//                            animateBackgrReval(position, rect.centerX(), rect.centerY())
//                        }
//                    }
//                }
//
//            })
            }
        }
        Log.d("MyTut", "TimeMainScreen: onViewCreated2")
    }

}