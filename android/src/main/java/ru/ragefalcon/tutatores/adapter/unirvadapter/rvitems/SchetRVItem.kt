package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemSchetBinding
import ru.ragefalcon.tutatores.extensions.format
import java.util.*

class SchetRVItem(
    data: ItemCommonFinOper,
    selectListener: ((ItemCommonFinOper) -> Unit)? = null,
    tapListener: ((ItemCommonFinOper) -> Unit)? = null,
    longTapListener: ((ItemCommonFinOper) -> Unit)? = null
) : BaseUniRVItem<ItemCommonFinOper>(
    data,
    getUniRVViewHolder(ItemSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSchetBinding) {
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