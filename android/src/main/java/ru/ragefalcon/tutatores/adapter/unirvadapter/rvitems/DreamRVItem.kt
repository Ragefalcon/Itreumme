package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemDreamBinding

class DreamRVItem(
    data: ItemDream,
    selectListener: ((ItemDream) -> Unit)? = null,
    listener: ((ItemDream) -> Unit)? = null,
    longTapListener: ((ItemDream) -> Unit)? = null,
    funForTransition: ((FragmentNavigator.Extras)-> Unit)? = null
) : BaseUniRVItem<ItemDream>(
    data,
    getUniRVViewHolder(ItemDreamBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemDreamBinding) {
            with(vh.binding) {

                ViewCompat.setTransitionName(tvNameDream, "tvNameDream${item.id}")
                ViewCompat.setTransitionName(tvDreamHour, "tvDreamHour${item.id}")
                ViewCompat.setTransitionName(clDreamItem, "clDreamItem${item.id}")
                tvNameDream.text = item.name
                tvDreamHour.text = item.hour.roundToString(1)
                if (vh.itemView.isSelected) {
                    selectListener?.invoke(item)
                }

                vh.itemView.setOnClickListener { // } .setOnClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    listener?.invoke(item)
                    funForTransition?.invoke(
                        FragmentNavigatorExtras(
                            tvNameDream to "tv_name_dream_frcl",
                            tvDreamHour to "tv_dream_hour_frcl",
                            clDreamItem to "cl_dream_detail_frcl"
                        )
                    )
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