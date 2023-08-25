package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemShablonDenPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemDenShablonBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem

class ShablonDenPlanRVItem(
    data: ItemShablonDenPlan,
    recyclerView: RecyclerView,
    listener: ((ItemShablonDenPlan) -> Unit)? = null,
    longTapListener: ((ItemShablonDenPlan) -> Unit)? = null
) : BaseUniRVItem<ItemShablonDenPlan>(
    data,
    getUniRVViewHolder(ItemDenShablonBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemDenShablonBinding) {
            val binding = vh.binding

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, binding.textOpis, sver, anim)
                if (binding.textOpis.text != "\n") {
                    rotateElemItem(binding.ivExpandOpis, sver, anim, 180F)
                    sverWidthElemItem(binding.viewSvertext, !sver, anim, 100.dpToPx)
                } else {
                    sverWidthElemItem(binding.viewSvertext, true, false, 200.dpToPx)
                    binding.ivExpandOpis.visibility = View.INVISIBLE
                }
            }

            binding.ivSelectIndic.requestLayout()
            binding.textName.text = item.name
            binding.textOpis.text = "${item.opis}\n"
            binding.textData.text = ""

            when (item.vajn.toInt()) {
                0 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_00
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                1 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_01
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                2 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_02
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                3 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_03
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
            sverItemOpis(item.sver, false)
            (binding as ItemDenShablonBinding).ivExpandOpis.setOnClickListener {
                item.sver = item.sver.not()
                sverItemOpis(item.sver, true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            vh.itemView.setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                listener?.invoke(item)
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                longTapListener?.invoke(item)
                true
            }
        }
    }
)

