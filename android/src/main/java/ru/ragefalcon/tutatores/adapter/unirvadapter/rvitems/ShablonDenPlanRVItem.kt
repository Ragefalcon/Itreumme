package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemShablonDenPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemDenShablonBinding
import ru.ragefalcon.tutatores.databinding.ItemPlanBinding
import ru.ragefalcon.tutatores.extensions.*
import java.util.*

class ShablonDenPlanRVItem(
    data: ItemShablonDenPlan,
    recyclerView: RecyclerView,
    listener: ((ItemShablonDenPlan) -> Unit)? = null,
    longTapListener: ((ItemShablonDenPlan) -> Unit)? = null
) : BaseUniRVItem<ItemShablonDenPlan>(
    data,
    getUniRVViewHolder(ItemDenShablonBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemDenShablonBinding) {
            val binding = vh.binding //as ItemShablonDenPlanBinding
//            vh.run {

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, binding.textOpis, sver, anim)
                if (binding.textOpis.text != "\n") {
                    rotateElemItem(binding.ivExpandOpis, sver, anim, 180F)
                    sverWidthElemItem(binding.viewSvertext, !sver, anim, 100.dpToPx)
                } else {
                    sverWidthElemItem(binding.viewSvertext, true, false, 200.dpToPx)
//                    binding.ivExpandOpis.visibility = View.GONE
                    binding.ivExpandOpis.visibility = View.INVISIBLE
//                    binding.ivExpandOpis.layoutParams.width = 1
                }
            }

            binding.ivSelectIndic.requestLayout()
            binding.textName.text = item.name
            binding.textOpis.text = "${item.opis}\n"
            binding.textData.text = ""
//            Date(data.data).format("dd MMM yyyy")

            when (item.vajn.toInt()) {
                0 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_00
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                1 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_01
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                2 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_02
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                3 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_03
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )
                /**
                 * https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android/45571812#45571812
                 *
                 * У пользователя @Tad есть свой ответ в правильном направлении, но он работает только с API 21+.
                 * Чтобы установить оттенок на всех версиях Android, используйте ImageViewCompat:
                 * ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(yourTint));
                 * Обратите внимание, что yourTintв этом случае должен быть "цвет int". Если у вас есть ресурс цвета, например R.color.blue, вам нужно сначала загрузить цвет int:
                 * ContextCompat.getColor(context, R.color.blue);
                 * */
            }
            sverItemOpis(item.sver, false)
            (binding as ItemDenShablonBinding).ivExpandOpis.setOnClickListener {
//        iv_stat_dp.setOnClickListener {
//                svernut = svernut.not()
                item.sver = item.sver.not()
                sverItemOpis(item.sver, true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            vh.itemView.setOnClickListener { // } .setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                listener?.invoke(item)
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                longTapListener?.invoke(item)
                true
            }
//            }
        }
    }
)

