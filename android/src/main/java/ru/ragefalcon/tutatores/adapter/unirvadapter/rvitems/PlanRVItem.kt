package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemBloknotBinding
import ru.ragefalcon.tutatores.databinding.ItemPlanBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem
import java.util.*

class PlanRVItem(
    data: ItemPlan,
    recyclerView: RecyclerView,
    funSelItem: ((ItemPlan, Int) -> Unit)? = null,
    listener: ((ItemPlan, View) -> Unit)? = null,
    getProgBar: ((ProgressBar, (Double) -> Unit) -> Unit)? = null
) : BaseUniRVItem<ItemPlan>(
    data,
    getUniRVViewHolder(ItemPlanBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemPlanBinding) with(vh.binding) {
            val time = Date().time

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                if (textOpis.text != "") {
                    rotateElemItem(ivStatDp, sver, anim, 270F)
                    sverWidthElemItem(viewSvertext, !sver, anim, 200.dpToPx)
                } else {
                    sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                }
            }

            val item = item

            if (item.stat != 10L) {
                cardViewItem.setCardBackgroundColor(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorSchetItem0
                    )
                )
            } else {
                cardViewItem.setCardBackgroundColor(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_00
                    )
                )
            }

            ivSelectIndic.requestLayout()
            textName.text = item.name
            textSumhour.text = item.hour.roundToString(1)
//            text_data.text = Date(item.data).format("dd MMM yyyy")
            progbarGotov.progress = (progbarGotov.max * item.gotov / 100).toInt()
            textOpis.text = item.opis
            if (vh.itemView.isSelected) {
                getProgBar?.invoke(progbarGotov) {
                    item.gotov = it
                }
                funSelItem?.invoke(item, progbarGotov.progress)

            }
            when (item.vajn.toInt()) {
                0 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_00
                    ), PorterDuff.Mode.MULTIPLY
                )

                1 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_01
                    ), PorterDuff.Mode.MULTIPLY
                )

                2 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_02
                    ), PorterDuff.Mode.MULTIPLY
                )

                3 -> ivStatDp.setColorFilter(
                    ContextCompat.getColor(
                        vh.itemView.context,
                        R.color.colorStatTimeSquareTint_03
                    ), PorterDuff.Mode.MULTIPLY
                )
//            0 -> iv_stat_dp.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
//            3 -> iv_stat_dp.setColorFilter(
//                MyColorARGB.DOXODDARKGREEN.toIntColor(),
//                android.graphics.PorterDuff.Mode.MULTIPLY
//            )
                /**
                 * https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android/45571812#45571812
                 *
                 * У пользователя @Tad есть свой ответ в правильном направлении, но он работает только с API 21+.
                 * Чтобы установить оттенок на всех версиях Android, используйте ImageViewCompat:
                 * ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(yourTint));
                 * Обратите внимание, что yourTintв этом случае должен быть "цвет int". Если у вас есть ресурс цвета, например R.color.blue, вам нужно сначала загрузить цвет int:
                 * ContextCompat.getColor(context, R.color.blue);
                 * */
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
            ivStatDp.setOnClickListener {
                item.sver = item.sver.not()
                sverItemOpis(item.sver, true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            vh.itemView.setOnClickListener { // } .setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                listener?.invoke(item, vh.itemView)
                Log.d("item", "До ${vh.itemView.javaClass}")
                true
            }
        }
    }
)

