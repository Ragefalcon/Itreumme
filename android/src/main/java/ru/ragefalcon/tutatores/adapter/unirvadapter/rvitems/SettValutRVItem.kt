package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemValut
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemSettSchetBinding

class SettValutRVItem(
    data: ItemValut,
    selectListener: ((ItemValut) -> Unit)? = null,
    tapListener: ((ItemValut) -> Unit)? = null,
    longTapListener: ((ItemValut) -> Unit)? = null
) : BaseUniRVItem<ItemValut>(
    data,
    getUniRVViewHolder(ItemSettSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSettSchetBinding) {
            with(vh.binding) {

                tvNameSchet.text = item.name
                tvSumSchet.text = item.kurs.roundToString(2)
                tvCodSchet.text = item.countschet.toString()//""

//                if(!item.open)
//                    clSettItem.setBackgroundResource(R.drawable.ripple_bg_sett_item_close)
//                else
//                    clSettItem.setBackgroundResource(R.drawable.ripple_bg_idea_item)

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