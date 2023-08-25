package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem;

import ru.ragefalcon.sharedcode.models.data.ItemBodyDialog
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemBdTextBinding

class BDTextRVItem(
    data: ItemBodyDialog,
    selectListener: ((ItemBodyDialog) -> Unit)? = null,
    tapListener: ((ItemBodyDialog) -> Unit)? = null,
    longTapListener: ((ItemBodyDialog) -> Unit)? = null
) : BaseUniRVItem<ItemBodyDialog>(
    data,
    getUniRVViewHolder(ItemBdTextBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemBdTextBinding) {
            with(vh.binding) {

                textBd.text = item.text
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