package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import ru.ragefalcon.sharedcode.models.data.ItemRasxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemRasxodBinding
import ru.ragefalcon.tutatores.extensions.format
import java.util.*

class RasxodRVItem(
    data: ItemRasxod,
    selectListener: ((ItemRasxod) -> Unit)? = null,
    tapListener: ((ItemRasxod) -> Unit)? = null,
    longTapListener: ((ItemRasxod) -> Unit)? = null
) : BaseUniRVItem<ItemRasxod>(
    data,
    getUniRVViewHolder(ItemRasxodBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemRasxodBinding) {
            with(vh.binding) {

                textName.text = item.name
                textType.text = item.type
                textSchet.text = item.schet
                textSumma.text = item.summa.toString()
                textData.text = Date(item.data).format("dd MMM yyyy")

                if (vh.itemView.isSelected) {
                    selectListener?.invoke(item)
                }
                vh.itemView.setOnClickListener { // } .setOnClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    tapListener?.invoke(item)
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

