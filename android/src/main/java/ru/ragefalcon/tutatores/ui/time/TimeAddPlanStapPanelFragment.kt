package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.commonfragments.MyFragDial
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddPlanStapPanelBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class TimeAddPlanStapPanelFragment(
    item: ItemPlanStap? = null,
    parPlan: ItemPlan? = null,
    parPlanStap: ItemPlanStap? = null,
    callbackKey: String? = null
) :
    FragAddChangeDialHelper<ItemPlanStap,FragmentTimeAddPlanStapPanelBinding>(FragmentTimeAddPlanStapPanelBinding::inflate,item) {

    var callback_Key: String? by instanceState(callbackKey)

    private var parentPlan: ItemPlan? by instanceState<ItemPlan?>(parPlan) { cache, value ->
        if (cache != value) {
            parentPlanStap = null
        }
        binding.nameParentPlan.text = value?.name
    }
    var parentPlanStap: ItemPlanStap? by instanceState(parPlanStap) { cache, value ->
        if (value != null) {
            binding.nameParentPlanStap.text = value.name
            binding.buttUnselStap.isVisible = true
        } else {
            binding.nameParentPlanStap.text = "Сделать подъэтапом"
            binding.buttUnselStap.isVisible = false
        }
    }

    override fun addNote() {
        with(binding) {
            viewmodel.addTime.addStapPlan(
                parent_id = parentPlanStap?.run { id.toLong() } ?: -1,
                name = editNamePlanStapText.text.toString(),
                gotov = 0.0,
                data1 = if (cbSrokPlanStap.isChecked) dateStartPlanStap.dateLong else 0,
                data2 = if (cbSrokPlanStap.isChecked) dateEndPlanStap.dateLong else 1,
                opis = editOpisPlanStapText.text.toString(),
                stat = 0,
                svernut = "false",
                idplan = parentPlan?.id?.toLong() ?: -1
            )
//            callback?.invoke(null)
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addTime.updPlanStap(
                    id = it.id.toLong(),
                    parent_id = parentPlanStap?.run { id.toLong() } ?: -1,
                    name = editNamePlanStapText.text.toString(),
                    data1 = if (cbSrokPlanStap.isChecked) dateStartPlanStap.dateLong else 0,
                    data2 = if (cbSrokPlanStap.isChecked) dateEndPlanStap.dateLong else 1,
                    opis = editOpisPlanStapText.text.toString(),
                    idplan = parentPlan?.id?.toLong() ?: -1
                )
            }
            Log.d("MyTut", "FragList: PlanStapChange callbackKey = $callback_Key");

            /**
             * https://habr.com/ru/post/515080/
             * */
            callback_Key?.let {
                getSFM().setFragmentResult(it, bundleOf("bundleKey" to item))
                Log.d("MyTut", "FragList: PlanStapChange send rezult");
            }
//            callback?.invoke(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d("MyTut", "FragList: TimeAddPlanStapPanelFrag parentManager = $parentFragmentManager");
//        Log.d("MyTut", "FragList: TimeAddPlanStapPanelFrag getCFM = ${getCFM()}");
//        Log.d("MyTut", "FragList: TimeAddPlanStapPanelFrag getSFM = ${getSFM()}");
        with(binding) {

            item?.let {
//            vybStatPlan.selectStat(it.vajn.toInt())
                editNamePlanStapText.setText(it.name)
                editOpisPlanStapText.setText(it.opis)
                if (it.data1 != 0L) {
                    cbSrokPlanStap.isChecked = true
                    dateStartPlanStap.setDate(it.data1)
                    dateEndPlanStap.setDate(it.data2)
                }
            } ?: {}
//        if (parPlan == null) {
//            stateViewModel.stateSelPrivPlan = MyStateViewModel.privPlanState.addplanstap
//        }
            nameParentPlan.text = parentPlan?.name
            parentPlanStap = parentPlanStap

            collapse(clSrokPlanStap)
            dateStartPlanStap.setPattern("от dd MMM yyyy (EEE)")
            dateEndPlanStap.setPattern("до dd MMM yyyy (EEE)")
            cbSrokPlanStap.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    expand(clSrokPlanStap, duration = 300)
                } else {
                    collapse(clSrokPlanStap, duration = 300)
                }
            }
            val callbackSelPrivPlan = "callbackSelPrivPlanForStapPlan"
            SelectedPlanPanelFragment.setRezListener(this@TimeAddPlanStapPanelFragment, callbackSelPrivPlan) {
                parentPlan = it
            }
            clSelectedParentPlan.setOnClickListener {
                showMyFragDial(
                    SelectedPlanPanelFragment(parentPlan, callbackSelPrivPlan),
                    bound = MyFragDial.BoundSlide.top
                )
//            val directions = TimeAddPlanStapPanelFragmentDirections.actionAddplanstappanelToSelectPlan()
//            findNavController().navigate(
//                directions, FragmentNavigatorExtras(
//                    clSelectedParentPlan to "clSelectParentPlanTpanel"
//                )
//            )

            }
            val callbackSelPrivStapPlan = "callbackSelPrivStapPlanForDenPlan"
            SelectedPlanStapPanelFragment.setRezListener(
                this@TimeAddPlanStapPanelFragment,
                callbackSelPrivStapPlan
            ) { itplst ->
                parentPlanStap = itplst
            }
            clSelectedParentPlanStap.setOnClickListener {
                showMyFragDial(
                    SelectedPlanStapPanelFragment(
                        parentPlan!!,
                        parentPlanStap,
                        arrayListOf( item?.id?.toLong() ?: -1),
                        callbackSelPrivStapPlan
                    ), bound = MyFragDial.BoundSlide.top
                )
//            val directions = TimeAddPlanStapPanelFragmentDirections.actionAddplanstappanelToSelectPlanStap()
//            findNavController().navigate(
//                directions, FragmentNavigatorExtras(
//                    clSelectedParentPlanStap to "clSelectParentPlanStapTpanel"
//                )
//            )

            }

//        stateViewModel.selPrivItemPlan_forPlanStap.observe(viewLifecycleOwner) {
//            it?.let {
//                nameParentPlan.text = it.name
//                viewmodel.setPlanForSpisStapPlanForSelect(it.id.toLong())
//            }
//        }
//        stateViewModel.selPrivItemPlanStap_forPlanStap.observe(viewLifecycleOwner) {
//            if (parPlanStap != null) {
//                nameParentPlanStap.text = parPlanStap?.name
//                buttUnselStap.isVisible = true
//            } else {
//                nameParentPlanStap.text = "Сделать подъэтапом"
//                buttUnselStap.isVisible = false
//            }
//        }
//        parPlanStap = parPlanStap
            buttUnselStap.setOnClickListener {
//            stateViewModel.selPrivItemPlanStap_forPlanStap.value = null
                parentPlanStap = null
            }

        }
    }
}