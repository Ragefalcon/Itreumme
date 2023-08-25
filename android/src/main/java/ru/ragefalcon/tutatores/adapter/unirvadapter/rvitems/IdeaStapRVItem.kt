package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemIdeaStapBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem

class IdeaStapRVItem(
    data: ItemIdeaStap,
    recyclerView: RecyclerView,
    selectListener: ((ItemIdeaStap) -> Unit)? = null,
    tapListener: ((ItemIdeaStap) -> Unit)? = null,
    longTapListener: ((ItemIdeaStap) -> Unit)? = null
) : BaseUniRVItem<ItemIdeaStap>(
    data,
    getUniRVViewHolder(ItemIdeaStapBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemIdeaStapBinding) {
            with(vh.binding) {

                fun sverItemOpis(sver: Boolean, anim: Boolean) {
                    sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                    if (textOpis.text != "") {
                        rotateElemItem(ivExpandOpis, sver, anim, 180F)
                        sverWidthElemItem(viewSvertext, !sver, anim, 200.dpToPx)
                    } else {
                        sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                        ivExpandOpis.visibility = View.INVISIBLE
                        ivExpandOpis.layoutParams.width = 1
                    }
                }

                ivSelectIndic.requestLayout()
                textName.text = item.name
                textOpis.text = item.opis
                when (item.stat.toInt()) {
                    0 -> ivStatIdeaGrad.setColorFilter(
                        ContextCompat.getColor(
                            vh.itemView.context,
                            R.color.colorStatTint_01
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    1 -> ivStatIdeaGrad.setColorFilter(
                        ContextCompat.getColor(
                            vh.itemView.context,
                            R.color.colorStatTint_02
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    2 -> ivStatIdeaGrad.setColorFilter(
                        ContextCompat.getColor(
                            vh.itemView.context,
                            R.color.colorStatTint_03
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    3 -> ivStatIdeaGrad.setColorFilter(
                        ContextCompat.getColor(
                            vh.itemView.context,
                            R.color.colorStatTint_04
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                    4 -> ivStatIdeaGrad.setColorFilter(
                        ContextCompat.getColor(
                            vh.itemView.context,
                            R.color.colorStatTint_05
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
                sverItemOpis(item.sver, false)
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