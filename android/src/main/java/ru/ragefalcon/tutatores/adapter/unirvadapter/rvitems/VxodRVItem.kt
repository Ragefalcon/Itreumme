package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemVxodBinding
import ru.ragefalcon.tutatores.extensions.*
import java.util.*

class VxodRVItem(
    data: ItemVxod,
    recyclerView: RecyclerView,
    listener: ((ItemVxod) -> Unit)? = null,
    longTapListener: ((ItemVxod) -> Unit)? = null
) : BaseUniRVItem<ItemVxod>(
    data,
    getUniRVViewHolder(ItemVxodBinding::inflate) { vh, dataIn, rvset ->
        if (vh.binding is ItemVxodBinding) {
            val binding = vh.binding

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, binding.textOpis, sver, anim)
                if (binding.textOpis.text != "\n") {
                    rotateElemItem(binding.ivExpandOpis, sver, anim, 180F)
                    sverWidthElemItem(binding.viewSvertext, !sver, anim, 100.dpToPx)
                } else {
                    sverWidthElemItem(binding.viewSvertext, true, false, 200.dpToPx)
                    binding.ivExpandOpis.visibility = View.INVISIBLE
                    binding.ivExpandOpis.layoutParams.width = 1
                }
            }

            binding.ivSelectIndic.requestLayout()
            binding.textName.text = dataIn.name
            binding.textOpis.text = "${dataIn.opis}\n"
            binding.textData.text = Date(dataIn.data).format("dd MMM yyyy")
            when (dataIn.stat.toInt()) {
                0 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorStatTint_01
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                1 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorStatTint_02
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                2 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorStatTint_03
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                3 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorStatTint_04
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                )

                4 -> binding.ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context, //vh.itemView
                        R.color.colorStatTint_05
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
            sverItemOpis(dataIn.sver, false)
            (binding as ItemVxodBinding).ivExpandOpis.setOnClickListener {
//        iv_stat_dp.setOnClickListener {
//                svernut = svernut.not()
                dataIn.sver = dataIn.sver.not()
                sverItemOpis(dataIn.sver, true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            vh.itemView.setOnClickListener { // } .setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                listener?.invoke(dataIn)
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                longTapListener?.invoke(dataIn)
                true
            }
        }
    }

)
