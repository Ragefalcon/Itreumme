package ru.ragefalcon.tutatores.ui.avatar.goal;

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentGoalDetailBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.avatar.PrivsGoalDial

class AvatarGoalDetailFragment() : BaseFragmentVM<FragmentGoalDetailBinding>(FragmentGoalDetailBinding::inflate) {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = getMyTransition(end = {
            Log.d("safsaf", "sharedElementReturnTransition idea")
        })
        sharedElementEnterTransition =  getMyTransition(end = {
                binding.clGoalDetailFrcl.invalidate()
                binding.clGoalDetailFrcl.requestLayout()
        })

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        postponeEnterTransition()
        with(binding) {

            buttBack.setOnClickListener {
                findNavController().navigateUp()
            }
            buttPriv.setOnClickListener {
                showMyFragDial(PrivsGoalDial(stateViewModel.selectItemGoal.value?.id?.toLong()))
            }
            hsvStatikDiag.requestDisallowInterceptTouchEvent(true)
            stateViewModel.selectItemGoal.value?.let {
                tvNameGoalFrcl.text = it.name
                tvGoalHourFrcl.text = it.hour.roundToString(1)
                if (it.opis!="") tvOpisGoal.text = it.opis  else collapse(tvOpisGoal,duration = 0)
                with(viewmodel) {
                    avatarFun.selectGoalForDiagram(it.id.toLong())
                    avatarFun.setListenerStatikHourGoal {
//                        if (it.count()>0) {
                            rectDiagStatikGoal.setItemsYears(it)
                            rectDiagStatikGoal.layoutParams = LinearLayout.LayoutParams(
                                rectDiagStatikGoal.getWidthRectDiag(),
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                            hsvStatikDiag.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
//                        }
                    }
                    avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                    avatarFun.setListenerForStatistikHourGoal{ week, month, year, all, count ->
                        tvValueGoalStat4.text = week
                        tvValueGoalStat3.text = month
                        tvValueGoalStat2.text = year
                        tvGoalHourFrcl.text = all
                        tvPrivsGoalCount.text = count
                    }
                }
            }
        }
    }
}