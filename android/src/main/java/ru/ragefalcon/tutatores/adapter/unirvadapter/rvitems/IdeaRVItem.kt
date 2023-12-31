package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemIdeaBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem

class IdeaRVItem(
    data: ItemIdea,
    recyclerView: RecyclerView,
    selectListener: ((ItemIdea) -> Unit)? = null,
    tapListener: ((ItemIdea) -> Unit)? = null,
    longTapListener: ((ItemIdea) -> Unit)? = null,
    funForTransition: ((FragmentNavigator.Extras) -> Unit)? = null
) : BaseUniRVItem<ItemIdea>(
    data,
    getUniRVViewHolder(ItemIdeaBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemIdeaBinding) {
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

                ViewCompat.setTransitionName(clIdeaItemRv, "ideacard${item.id}")
                ViewCompat.setTransitionName(tvNameIdea, "ideaname${item.id}")

                (vh.itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx * item.level.toInt()
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

                ivSelectIndic.requestLayout()
                tvNameIdea.text = item.name
                textOpis.text = item.opis
                sverItemOpis(item.sver, false)

                if (item.podstapcount > 0) {
                    ivStatDp.alpha = 1F
                    ivStatDp.layoutParams.width = 30.dpToPx
                    ivStatDp.requestLayout()
                    when (item.sverChild) {
                        false -> ivStatDp.setImageResource(ru.ragefalcon.tutatores.R.drawable.ic_minus)
                        true -> ivStatDp.setImageResource(ru.ragefalcon.tutatores.R.drawable.ic_add_butt)
                    }
                } else {
                    ivStatDp.alpha = 0F
                    ivStatDp.layoutParams.width = 1.dpToPx
                    ivStatDp.requestLayout()
                }
                ivStatDp.setOnClickListener {
                    if (item.podstapcount > 0) {
                        item.sverChild = item.sverChild.not()
                    }
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
                vh.itemView.setOnClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    tapListener?.invoke(item)
                    funForTransition?.invoke(
                        FragmentNavigatorExtras(
                            tvNameIdea to "tv_idea_name_frideastap",
                            clIdeaItemRv to "cl_idea_item_frideastap"
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