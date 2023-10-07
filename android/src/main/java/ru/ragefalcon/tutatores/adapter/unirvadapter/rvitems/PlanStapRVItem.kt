package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemPlanStapBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class PlanStapRVItem(
    data: ItemPlanStap,
    recyclerView: RecyclerView,
    viewModel: AndroidFinanceViewModel,
    funSelItem: ((ItemPlanStap, Int) -> Unit)? = null,
    listener: ((ItemPlanStap) -> Unit)? = null,
    getProgBar: ((ProgressBar, (Double) -> Unit) -> Unit)? = null
) : BaseUniRVItem<ItemPlanStap>(
    data,
    getUniRVViewHolder(ItemPlanStapBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemPlanStapBinding) with(vh.binding) {

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                if (textOpis.text != "") {
                    if (!ivExpandOpis.isVisible) {
                        ivExpandOpis.visibility = View.VISIBLE
                        ivExpandOpis.layoutParams.width = 30.dpToPx
                    }
                    rotateElemItem(ivExpandOpis, sver, anim, 180F)
                    sverWidthElemItem(viewSvertext, !sver, anim, 200.dpToPx)
                } else {
                    sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                    ivExpandOpis.visibility = View.INVISIBLE
                    ivExpandOpis.layoutParams.width = 1
                }
            }

            (vh.itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx * item.level.toInt()
            vh.itemView.invalidate()
            vh.itemView.requestLayout()

            var col = MyColorARGB("FFF7D9")
            var lvl = item.level
            while (lvl > 0) {
                col.R = (col.R * 0.95).toInt()
                lvl--
            }

            if (vh.itemView.isSelected) {
                getProgBar?.invoke(progbarGotov) {
//                    item.gotov = it
                }
                funSelItem?.invoke(item, progbarGotov.progress)
            }

            if (item.stat != TypeStatPlanStap.COMPLETE) {
                cardViewItem.setCardBackgroundColor(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorSchetItem0
                    )
                )
            } else {
                cardViewItem.setCardBackgroundColor(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_00
                    )
                )
            }

            ivSelectIndic.requestLayout()
            textName.text = item.name
            textName.requestLayout()
            textSumhour.text = item.hour.roundToString(1)
            progbarGotov.progress = (progbarGotov.max * item.gotov / 100).toInt()
            textOpis.text = item.opis
            sverItemOpis(item.sver, false)
            if (item.podstapcount > 0) {
                ivStatDp.alpha = 1F
                ivStatDp.layoutParams.width = 30.dpToPx
                ivStatDp.requestLayout()
                when (item.svernut) {
                    false -> ivStatDp.setImageResource(R.drawable.ic_minus)
                    true -> ivStatDp.setImageResource(R.drawable.ic_add_butt)
                }
            } else {
                ivStatDp.alpha = 0F
                ivStatDp.layoutParams.width = 1.dpToPx
                ivStatDp.requestLayout()
            }
            ivStatDp.setOnClickListener {
                if (item.podstapcount > 0) {
                    viewModel.timeFun.setExpandStapPlan(item.id.toLong(), item.svernut.not())
                }
            }
            ivExpandOpis.setOnClickListener {
                sverItemOpis(item.sver.not(), true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            vh.itemView.setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                listener?.invoke(item)
                true
            }
        }
    }
)
