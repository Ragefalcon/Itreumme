package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem;

import androidx.lifecycle.LifecycleOwner
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemBodyDialog
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemBdDateBinding

class BDDateRVItem(
    data: ItemBodyDialog,
    owner: LifecycleOwner,
    dateListener: ((Long) -> Unit) = { }
) : BaseUniRVItem<ItemBodyDialog>(
    data,
    getUniRVViewHolder(ItemBdDateBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemBdDateBinding) {
            with(vh.binding) {
                myDateBd.setDate(item.text.toLong())
                myDateBd.observe(owner, dateListener )
            }
        }
    }
)