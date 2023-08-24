package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemEffektBinding

class EffektRVItem(
    data: ItemEffekt,
    listener: ((ItemEffekt) -> Unit)? = null,
    longTapListener: ((ItemEffekt) -> Unit)? = null
) : BaseUniRVItem<ItemEffekt>(
    data,
    getUniRVViewHolder(ItemEffektBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemEffektBinding) {
            with(vh.binding) {
                nameEffekt.text = item.name
                val nr = item.norma
                tvHoursInfo.text = "Н: ${item.sumNedel.roundToString(1)}/${nr.roundToString(1)} " +
                        "  М: ${item.sumMonth.roundToString(1)}/${(nr * 4.286).roundToString(1)} " +
                        "  Г: ${item.sumYear.roundToString(1)}/${(nr * 52).roundToString(1)}"
                effektShkalItem.setItemEffekt(item)

                vh.itemView.setOnClickListener { // } .setOnClickListener {
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
    }
)