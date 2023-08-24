package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemBestDays
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemBestdaysBinding
import ru.ragefalcon.tutatores.extensions.format
import java.util.*

class BestDaysRVItem(
    data: ItemBestDays,
    listener: ((ItemBestDays) -> Unit)? = null,
    longTapListener: ((ItemBestDays) -> Unit)? = null
) : BaseUniRVItem<ItemBestDays>(
    data,
    getUniRVViewHolder(ItemBestdaysBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemBestdaysBinding) {
            with(vh.binding) {

                tvNameBestdays.text = item.name
                tvDateBestdays.text = Date(item.data).format("dd MMM yyyy ")

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