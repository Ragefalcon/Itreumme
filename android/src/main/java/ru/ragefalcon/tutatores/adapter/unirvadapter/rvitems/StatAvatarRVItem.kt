package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemStat
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemAvatarStatBinding

class StatAvatarRVItem(
    data: ItemStat,
    listener: ((ItemStat) -> Unit)? = null,
    longTapListener: ((ItemStat) -> Unit)? = null
) : BaseUniRVItem<ItemStat>(
    data,
    getUniRVViewHolder(ItemAvatarStatBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemAvatarStatBinding) {
            with(vh.binding) {

                tvNameAvatarStat.text = item.name
                tvValueAvatarStat.text = item.value

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