package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem;

import ru.ragefalcon.sharedcode.models.data.ItemBodyDialog
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemBdButtonBinding

class BDButtonRVItem(
    data: ItemBodyDialog,
    buttListener: ((ItemBodyDialog) -> Unit) = {}
) : BaseUniRVItem<ItemBodyDialog>(
    data,
    getUniRVViewHolder(ItemBdButtonBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemBdButtonBinding) {
            with(vh.binding) {
                buttBd.text = item.text
                buttBd.setOnClickListener {
                    buttListener.invoke(item)
                }
            }
        }
    }
)