package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.content.res.ColorStateList
import android.graphics.Color
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemSettTypedoxod
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemSettSchetBinding

class SettTypedoxodRVItem(
    data: ItemSettTypedoxod,
    selectListener: ((ItemSettTypedoxod) -> Unit)? = null,
    tapListener: ((ItemSettTypedoxod) -> Unit)? = null,
    longTapListener: ((ItemSettTypedoxod) -> Unit)? = null
) : BaseUniRVItem<ItemSettTypedoxod>(
    data,
    getUniRVViewHolder(ItemSettSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSettSchetBinding) {
            with(vh.binding) {

                tvNameSchet.text = item.typed
                tvSumSchet.text = ""
                tvCodSchet.text = item.countoper.toString()//""

                if(!item.open)
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