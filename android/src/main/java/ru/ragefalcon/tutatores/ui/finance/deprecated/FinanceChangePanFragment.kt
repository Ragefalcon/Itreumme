package ru.ragefalcon.tutatores.ui.finance.deprecated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.tutatores.adapter.FinanceType
import ru.ragefalcon.tutatores.databinding.FragmentChangeFinanceBinding
import ru.ragefalcon.tutatores.ui.finance.FinanceMainScreen
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

/*
class FinanceChangePanFragment : Fragment() {
    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels()

    private var _binding: FragmentChangeFinanceBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            stateViewModel.visChangeFinPanel.observe(viewLifecycleOwner) {
                if (it) {
                    if (stateViewModel.currentFinType == FinanceType.SCHET) {
                        buttChangeFin.visibility = View.INVISIBLE
                        tvChangeFin.visibility = View.INVISIBLE
                        if (stateViewModel.changeItemCommonFinOper?.schet != "Перевод") {
                            buttDeleteFin.visibility = View.INVISIBLE
//                        tvDeleteFin.visibility = View.INVISIBLE
                            tvDeleteFin.text =
                                "Работайте с транзакцией в разделе ${stateViewModel.changeItemCommonFinOper?.schet}"
                        } else {
                            buttDeleteFin.visibility = View.VISIBLE
//                    tvDeleteFin.visibility = View.VISIBLE
                            tvDeleteFin.text = "Удалить"
                        }

                    } else {
                        buttChangeFin.visibility = View.VISIBLE
                        tvChangeFin.visibility = View.VISIBLE
                        buttDeleteFin.visibility = View.VISIBLE
//                    tvDeleteFin.visibility = View.VISIBLE
                        tvDeleteFin.text = "Удалить"
                    }
                    changeLayout.alpha = 0f
                    changeLayout.visibility = android.view.View.VISIBLE

                    ivBackchangelay.clearColorFilter()
                    changeLayout.let {
                        ViewCompat.animate(changeLayout)
                            .alpha(1f)
                            .setDuration(300)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .setStartDelay(50)
                    }
                }

            }
            buttDeleteFin.setOnClickListener {
                when (stateViewModel.currentFinType) {
                    FinanceType.RASXOD -> viewmodel.addFin.delRasxod(stateViewModel.changeItemCommonFinOper!!.id.toLong())
                    FinanceType.DOXOD -> viewmodel.addFin.delDoxod(stateViewModel.changeItemCommonFinOper!!.id.toLong())
                    FinanceType.SCHET -> viewmodel.addFin.delPerevod(stateViewModel.changeItemCommonFinOper!!.id.toLong())
                }
                hideChangeLayout(50)
            }
            buttBackChangeFin.setOnClickListener {
                hideChangeLayout(50)
            }
            ivBackchangelay.setOnClickListener {
                hideChangeLayout(50)
            }
            buttChangeFin.setOnClickListener {
                stateViewModel.visAddFinPanel.value = true
                hideChangeLayout(50)
            }
        }
    }

    private fun hideChangeLayout(delay: Long) {
        ViewCompat.animate(binding.changeLayout)
            .alpha(0f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setStartDelay(delay)
            .setListener(FinanceMainScreen.MyVPAL())
        stateViewModel.visChangeFinPanel.value = false
    }


}*/
