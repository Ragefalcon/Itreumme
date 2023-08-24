package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem;

import ru.ragefalcon.sharedcode.models.data.ItemBodyDialog
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemBdButtonBinding

class BDButtonRVItem(
    data: ItemBodyDialog,
//    selectListener: ((ItemBodyDialog) -> Unit)? = null,
    buttListener: ((ItemBodyDialog) -> Unit) = {}
//    longTapListener: ((ItemBodyDialog) -> Unit)? = null
) : BaseUniRVItem<ItemBodyDialog>(
    data,
    getUniRVViewHolder(ItemBdButtonBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemBdButtonBinding) {
            with(vh.binding) {

                buttBd.text = item.text
                buttBd.setOnClickListener {
                    buttListener.invoke(item)
                }
//                if (vh.itemView.isSelected) {
//                    selectListener?.invoke(item)
//                }
//                vh.itemView.setOnClickListener { // } .setOnClickListener {
//                    vh.bindItem?.let { rvset.selFunc(it) }
//                    tapListener?.invoke(item)
//                }
//                vh.itemView.setOnLongClickListener {
//                    vh.bindItem?.let { rvset.selFunc(it) }
//                    longTapListener?.invoke(item)
//                    true
//                }
            }
        }
    }
)