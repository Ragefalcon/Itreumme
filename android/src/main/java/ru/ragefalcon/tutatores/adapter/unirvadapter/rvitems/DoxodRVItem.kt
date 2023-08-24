package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import ru.ragefalcon.sharedcode.models.data.ItemDoxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemDoxodBinding
import ru.ragefalcon.tutatores.extensions.format
import java.util.*

class DoxodRVItem(
    data: ItemDoxod,
    selectListener: ((ItemDoxod) -> Unit)? = null,
    tapListener: ((ItemDoxod) -> Unit)? = null,
    longTapListener: ((ItemDoxod) -> Unit)? = null
) : BaseUniRVItem<ItemDoxod>(
    data,
    getUniRVViewHolder(ItemDoxodBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemDoxodBinding) {
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