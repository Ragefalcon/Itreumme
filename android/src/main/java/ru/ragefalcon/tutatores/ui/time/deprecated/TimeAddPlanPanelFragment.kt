package ru.ragefalcon.tutatores.ui.time.deprecated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class TimeAddPlanPanelFragment : Fragment() {

    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val bgFadeTransition = Fade().addTarget(R.id.back_add_vxod_tpanel)
//        val mainPanTransition = Slide().addTarget(R.id.cl_add_tpanel)
//        val customAnimTransition = TransitionSet().apply {
//            duration = 300L // 1 sec
//            addTransition(bgFadeTransition)
//            addTransition(mainPanTransition)
//        }
//        enterTransition = customAnimTransition
//        exitTransition = customAnimTransition
    }

    private var _binding: FragmentTimeAddPlanPanelBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimeAddPlanPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        butt_cancel_tpanel.setOnClickListener {
//            findNavController().navigateUp()
//        }
////        var fragment = MyTest1Fragment()
////        requireActivity().supportFragmentManager.commit {
////            setReorderingAllowed(false)
////            replace(R.id.fr_vybStat_vxod_testt, fragment)
////            addToBackStack(null)
////        }
////        vybStatPlan.observe(viewLifecycleOwner){
////            butt_add_vxod_record_tpanel.text = vybStat_vxod.selStat.toString()//(requireActivity().supportFragmentManager
////        }
//        vybStatPlan.setTimeSquare()
//        butt_addPlan_record_tpanel.setOnClickListener {
//            viewmodel.addTime.addPlan(
//                vajn = vybStatPlan.selStat.toLong(),
//                name = edit_namePlan_text.text.toString(),
//                gotov = 0.0,
//                data1 = if (cbSrokPlan.isChecked) dateStartPlan.dateLong else 0,
//                data2 = if (cbSrokPlan.isChecked) dateEndPlan.dateLong else 1,
//                opis = edit_opisPlan_text.text.toString(),
//                stat = 0
//            )
//            findNavController().navigateUp()
//        }
        with(binding) {
            var heightCl = 0
            clSrokPlan.doOnPreDraw {
                heightCl = clSrokPlan.height
                collapse(clSrokPlan)
            }
            dateStartPlan.setPattern("от dd MMM yyyy (EEE)")
            dateEndPlan.setPattern("до dd MMM yyyy (EEE)")
            cbSrokPlan.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
//                dateStartPlan.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                expand(clSrokPlan,height = heightCl,duration = 300)
                    expand(clSrokPlan, duration = 300)
                } else {
                    collapse(clSrokPlan, duration = 300)
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            TimeAddPlanPanelFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}