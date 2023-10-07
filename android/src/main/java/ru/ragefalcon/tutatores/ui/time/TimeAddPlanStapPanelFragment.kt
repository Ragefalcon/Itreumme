package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
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
                opis = listOf(
                    ItemComplexOpisText(
                        -1L,
                        TableNameForComplexOpis.spisPlanStap.nameTable,
                        -1L,
                        TypeOpisBlock.simpleText,
                        1L,
                        text = editOpisPlanStapText.text.toString(),
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4
                    )
                ),
                stat = TypeStatPlanStap.VISIB,
                svernut = "false",
                idplan = parentPlan?.id?.toLong() ?: -1L,
                0L
            )

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
                    opis = listOf(
                        ItemComplexOpisText(
                            -1L,
                            TableNameForComplexOpis.spisPlanStap.nameTable,
                            it.id.toLong(),
                            TypeOpisBlock.simpleText,
                            1L,
                            text = editOpisPlanStapText.text.toString(),
                            color = 1,
                            fontSize = 3,
                            cursiv = false,
                            bold = 4
                        )
                    ),
                    idplan = parentPlan?.id?.toLong() ?: -1L,
                    marker = 0L
                )
            }

            callback_Key?.let {
                getSFM().setFragmentResult(it, bundleOf("bundleKey" to item))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            item?.let {

                editNamePlanStapText.setText(it.name)
                editOpisPlanStapText.setText(it.opis)
                if (it.data1 != 0L) {
                    cbSrokPlanStap.isChecked = true
                    dateStartPlanStap.setDate(it.data1)
                    dateEndPlanStap.setDate(it.data2)
                }
            } ?: {}

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
            }

            buttUnselStap.setOnClickListener {
                parentPlanStap = null
            }
        }
    }
}