package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemBloknotBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem

class BloknotRVItem(
    data: ItemBloknot,
    recyclerView: RecyclerView,
    selectListener: ((ItemBloknot) -> Unit)? = null,
    tapListener: ((ItemBloknot) -> Unit)? = null,
    longTapListener: ((ItemBloknot) -> Unit)? = null,
    funForTransition: ((FragmentNavigator.Extras) -> Unit)? = null
) : BaseUniRVItem<ItemBloknot>(
    data,
    getUniRVViewHolder(ItemBloknotBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemBloknotBinding) {
            with(vh.binding) {
                fun sverItemOpis(sver: Boolean, anim: Boolean) {
                    sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                    if (textOpis.text != "") {
                        rotateElemItem(ivExpandOpis, sver, anim, 180F)
                        sverWidthElemItem(viewSvertext, !sver, anim, 100.dpToPx)
                    } else {
                        sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                        ivExpandOpis.visibility = View.INVISIBLE
                        ivExpandOpis.layoutParams.width = 1
                    }
                }

                ViewCompat.setTransitionName(tvBloknotName, "blokname${item.id}")
                ViewCompat.setTransitionName(clBloknotItem, "blokcard${item.id}")
                tvBloknotName.text = item.name
                textOpis.text = "${item.opis}"
                sverItemOpis(item.sver, false)
                if (vh.itemView.isSelected) {
//            stateViewModel.selectItemBloknot.value = item
                }
                ivExpandOpis.setOnClickListener {
                    item.sver = item.sver.not()
                    sverItemOpis(item.sver, true)
                    if (vh.itemView.isSelected) {
                        vh.bindItem?.let { rvset.selFunc(it) }
                    }
                }

                if (vh.itemView.isSelected) {
                    selectListener?.invoke(item)
                }
                vh.itemView.setOnClickListener { // } .setOnClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    tapListener?.invoke(item)
                    funForTransition?.invoke(
                        FragmentNavigatorExtras(
                            tvBloknotName to "tv_idea_name_frsp",
                            clBloknotItem to "cl_bloknot_item_frcl"
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