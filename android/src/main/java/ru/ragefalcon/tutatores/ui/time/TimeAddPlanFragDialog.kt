package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand

class TimeAddPlanFragDialog(item: ItemPlan? = null) : FragAddChangeDialHelper<ItemPlan,FragmentTimeAddPlanPanelBinding>(FragmentTimeAddPlanPanelBinding::inflate,item) {

    override fun addNote() {
        with(binding) {
            viewmodel.addTime.addPlan(
                vajn = vybStatPlan.selStat.toLong(),
                name = editNamePlanText.text.toString(),
                gotov = 0.0,
                data1 = if (cbSrokPlan.isChecked) dateStartPlan.dateLong else 0,
                data2 = if (cbSrokPlan.isChecked) dateEndPlan.dateLong else 1,
                opis = editOpisPlanText.text.toString(),
                stat = 0
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addTime.updPlan(
                    id = it.id.toLong(),
                    vajn = vybStatPlan.selStat.toLong(),
                    name = editNamePlanText.text.toString(),
                    data1 = if (cbSrokPlan.isChecked) dateStartPlan.dateLong else 0,
                    data2 = if (cbSrokPlan.isChecked) dateEndPlan.dateLong else 1,
                    opis = editOpisPlanText.text.toString()
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
                vybStatPlan.selectStat(it.vajn.toInt())
                editNamePlanText.setText(it.name)
                editOpisPlanText.setText(it.opis)
                if (it.data1 != 0L) {
                    cbSrokPlan.isChecked = true
                    dateStartPlan.setDate(it.data1)
                    dateEndPlan.setDate(it.data2)
                }
            }


            vybStatPlan.setTimeSquare()
            var heightCl = 0
            clSrokPlan.doOnPreDraw {
                heightCl = clSrokPlan.height
                if (!cbSrokPlan.isChecked) collapse(clSrokPlan)
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

}