package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemIdeaStapBinding
import ru.ragefalcon.tutatores.extensions.*

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
//            textData.text = Date(item.data).format("dd MMM yyyy")
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
//                    0 -> ivStatIdeaGrad.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
//                    1 -> ivStatIdeaGrad.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
////                2 -> ImageViewCompat.setImageTintList(ivStatDp, ColorStateList.valueOf(MyColorARGB.DOXODDARKGREEN.toIntColor()))
//                    2 -> ivStatIdeaGrad.setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.MULTIPLY)
//                    3 -> ivStatIdeaGrad.setColorFilter(
//                        MyColorARGB.DOXODDARKGREEN.toIntColor(),
//                        android.graphics.PorterDuff.Mode.MULTIPLY
//                    )
//                    4 -> ivStatIdeaGrad.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
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