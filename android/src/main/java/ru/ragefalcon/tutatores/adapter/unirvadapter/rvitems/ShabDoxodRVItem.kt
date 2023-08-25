package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemShabDoxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemSettSchetBinding

class ShabDoxodRVItem(
    data: ItemShabDoxod,
    selectListener: ((ItemShabDoxod) -> Unit)? = null,
    tapListener: ((ItemShabDoxod) -> Unit)? = null,
    longTapListener: ((ItemShabDoxod) -> Unit)? = null
) : BaseUniRVItem<ItemShabDoxod>(
    data,
    getUniRVViewHolder(ItemSettSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSettSchetBinding) {
            with(vh.binding) {

                tvNameSchet.text = item.name
                tvCodSchet.text = item.summa.roundToStringProb(2)
                tvSumSchet.text = item.type

                if (vh.itemView.isSelected) {
                    selectListener?.invoke(item)
                }
                vh.itemView.setOnClickListener {
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