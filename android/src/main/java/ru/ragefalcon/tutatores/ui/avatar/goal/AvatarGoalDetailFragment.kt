package ru.ragefalcon.tutatores.ui.avatar.goal;

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        })
        sharedElementEnterTransition =  getMyTransition(end = {
                binding.clGoalDetailFrcl.invalidate()
                binding.clGoalDetailFrcl.requestLayout()
        })

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    avatarSpis.diagramStatikHourGoal.observe(viewLifecycleOwner){
//                    avatarFun.setListenerStatikHourGoal {
                            rectDiagStatikGoal.setItemsYears(it)
                            rectDiagStatikGoal.layoutParams = LinearLayout.LayoutParams(
                                rectDiagStatikGoal.getWidthRectDiag(),
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                            hsvStatikDiag.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
                    }
                    avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                    avatarSpis.goalStat.observe(viewLifecycleOwner){ goalStat ->
                        Log.d("MyTag", "avatarSpis.goalStat.observe(viewLifecycleOwner)")
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            Log.d("MyTag", "avatarSpis Scope(viewLifecycleOwner)")
                            tvValueGoalStat4.text = goalStat.week
                            tvValueGoalStat3.text = goalStat.month
                            tvValueGoalStat2.text = goalStat.year
                            tvGoalHourFrcl.text = goalStat.all
                            tvPrivsGoalCount.text = goalStat.count
                        }
                    }
                }
            }
        }
    }
}