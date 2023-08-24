package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemSettSchet
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemSettSchetBinding

class SettSchetRVItem(
    data: ItemSettSchet,
    selectListener: ((ItemSettSchet) -> Unit)? = null,
    tapListener: ((ItemSettSchet) -> Unit)? = null,
    longTapListener: ((ItemSettSchet) -> Unit)? = null
) : BaseUniRVItem<ItemSettSchet>(
    data,
    getUniRVViewHolder(ItemSettSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSettSchetBinding) {
            with(vh.binding) {

                tvNameSchet.text = item.name
                tvCodSchet.text = item.cod
                tvSumSchet.text = item.summa.roundToStringProb(2)

                if(!item.open_)
                    clSettItem.setBackgroundResource(R.drawable.ripple_bg_sett_item_close)
                else
                    clSettItem.setBackgroundResource(R.drawable.ripple_bg_idea_item)

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