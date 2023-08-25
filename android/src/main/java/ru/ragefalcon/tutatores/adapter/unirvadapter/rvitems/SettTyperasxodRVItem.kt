package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import ru.ragefalcon.sharedcode.models.data.ItemSettTyperasxod
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemSettSchetBinding

class SettTyperasxodRVItem(
    data: ItemSettTyperasxod,
    selectListener: ((ItemSettTyperasxod) -> Unit)? = null,
    tapListener: ((ItemSettTyperasxod) -> Unit)? = null,
    longTapListener: ((ItemSettTyperasxod) -> Unit)? = null
) : BaseUniRVItem<ItemSettTyperasxod>(
    data,
    getUniRVViewHolder(ItemSettSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSettSchetBinding) {
            with(vh.binding) {

                tvNameSchet.text = item.typer
                tvSumSchet.text = item.countoper.toString()
                tvCodSchet.text = item.planschet

                if (!item.open)
                    clSettItem.setBackgroundResource(R.drawable.ripple_bg_sett_item_close)
                else
                    clSettItem.setBackgroundResource(R.drawable.ripple_bg_idea_item)

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