package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemDenplanBinding
import ru.ragefalcon.tutatores.databinding.ItemVxodBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*

class DenPlanRVItem(
    data: ItemDenPlan,
    recyclerView: RecyclerView,
    listener: ((ItemDenPlan) -> Unit)? = null,
    getProgBar: ((ProgressBar, ItemDenPlan, TextView, (Double, Double) -> Unit) -> Unit)? = null
) : BaseUniRVItem<ItemDenPlan>(
    data,
    getUniRVViewHolder(ItemDenplanBinding::inflate) { vh, dataIn, rvset ->
        if (vh.binding is ItemDenplanBinding) with(vh.binding) {

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                if (textOpis.text != "") {
                    rotateElemItem(ivStatDp, sver, anim, 270F)
                    sverWidthElemItem(viewSvertext, !sver, anim, 200.dpToPx)
                } else {
                    sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                }
            }

            val item = dataIn
            textName.text = item.name
            textTime1.text = item.time1.subSequence(0, 5)
            textTime2.text = item.time2.subSequence(0, 5)
            textSumhour.text = Date().fromHourFloat(item.sum_hour.toFloat())
                .humanizeTime()
            if (item.nameprpl != "") {
                val str = "${item.nameprpl}${if (item.namestap != "") " -> [${item.namestap}]" else ""}"
                textPrivplan.text = str
                textPrivplan.visibility = ViewGroup.VISIBLE
            } else {
                textPrivplan.visibility = ViewGroup.GONE
            }
            progbarGotov.progress = (progbarGotov.max * item.gotov / 100).toInt()
            textOpis.text = item.opis
            if (vh.itemView.isSelected) {
                getProgBar?.invoke(progbarGotov, dataIn, textSumhour, { got, sum ->

                })
            }
            when (item.vajn.toInt()) {
                0 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_00
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                1 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_01
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                2 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_02
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                3 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_03
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
            sverItemOpis(item.sver, false)
            ivStatDp.setOnClickListener {
                item.sver = item.sver.not()
                sverItemOpis(item.sver, true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            vh.itemView.setOnClickListener { // } .setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                listener?.invoke(dataIn)
                true
            }
        }
    }
)

