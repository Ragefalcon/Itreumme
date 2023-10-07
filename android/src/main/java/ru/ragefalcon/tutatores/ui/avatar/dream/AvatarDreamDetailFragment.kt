package ru.ragefalcon.tutatores.ui.avatar.dream

import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentDreamDetailBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.avatar.PrivsGoalDial

class AvatarDreamDetailFragment() : BaseFragmentVM<FragmentDreamDetailBinding>(FragmentDreamDetailBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = getMyTransition(end = {})
        sharedElementEnterTransition =  getMyTransition(end = {
            binding.clDreamDetailFrcl.invalidate()
            binding.clDreamDetailFrcl.requestLayout()
        })

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttBack.setOnClickListener {
                findNavController().navigateUp()
            }
            buttPriv.setOnClickListener {
                showMyFragDial(PrivsGoalDial(stateViewModel.selectItemDream.value?.id?.toLong()))
            }
            hsvStatikDiag.requestDisallowInterceptTouchEvent(true)
            stateViewModel.selectItemDream.value?.let {
                tvNameDreamFrcl.text = it.name
                tvDreamHourFrcl.text = it.hour.roundToString(1)
                if (it.opis!="") tvOpisDream.text = it.opis  else collapse(tvOpisDream,duration = 0)
                with(viewmodel) {
                    avatarFun.selectDreamForDiagram(it.id.toLong())
                    avatarSpis.diagramStatikHourDream.observe(viewLifecycleOwner){
//                    avatarFun.setListenerStatikHourDream {
                        rectDiagStatikDream.setItemsYears(it)
                        rectDiagStatikDream.layoutParams = LinearLayout.LayoutParams(
                            rectDiagStatikDream.getWidthRectDiag(),
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        hsvStatikDiag.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
                    }
                    avatarFun.setSelectedDreamListenerForStatistik(it.id.toLong())
                    avatarSpis.dreamStat.observe(viewLifecycleOwner){ dreamStat ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            tvValueDreamStat4.text = dreamStat.week
                            tvValueDreamStat3.text = dreamStat.month
                            tvValueDreamStat2.text = dreamStat.year
                            tvDreamHourFrcl.text = dreamStat.all
                            tvPrivsDreamCount.text = dreamStat.count
                        }
                    }
                }
            }
        }
    }
}