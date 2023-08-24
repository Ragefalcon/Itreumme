package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemPlanBinding
import ru.ragefalcon.tutatores.databinding.ItemPlanStapBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class PlanStapRVItem(
    data: ItemPlanStap,
    recyclerView: RecyclerView,
    viewModel: AndroidFinanceViewModel,
    funSelItem: ((ItemPlanStap, Int) -> Unit)? = null,
    listener: ((ItemPlanStap) -> Unit)? = null,
    getProgBar: ((ProgressBar, (Double) -> Unit) -> Unit)? = null
) : BaseUniRVItem<ItemPlanStap>(
    data,
    getUniRVViewHolder(ItemPlanStapBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemPlanStapBinding) with(vh.binding) {

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                if (textOpis.text != "") {
                    if (!ivExpandOpis.isVisible) {
                        ivExpandOpis.visibility = View.VISIBLE
                        ivExpandOpis.layoutParams.width = 30.dpToPx
                    }
                    rotateElemItem(ivExpandOpis, sver, anim, 180F)
                    sverWidthElemItem(viewSvertext, !sver, anim, 200.dpToPx)
                } else {
                    sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                    ivExpandOpis.visibility = View.INVISIBLE
                    ivExpandOpis.layoutParams.width = 1
                }
            }


//                (itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx*data.level.toInt()

            (vh.itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx * item.level.toInt()
            vh.itemView.invalidate()
            vh.itemView.requestLayout()

            var col = MyColorARGB("FFF7D9")
            var lvl = item.level
            while (lvl > 0) {
                col.R = (col.R * 0.95).toInt()
                lvl--
            }

            if (vh.itemView.isSelected) {
                getProgBar?.invoke(progbarGotov) {
                    item.gotov = it
                }
                funSelItem?.invoke(item, progbarGotov.progress)
            }

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
            Log.d("itemPS", "name: ${item.name}, level: ${item.level}, svernut: ${item.svernut}")
            textName.text = item.name
            textName.requestLayout()
            textSumhour.text = item.hour.roundToString(1)
//            text_data.text = Date(item.data).format("dd MMM yyyy")
            progbarGotov.progress = (progbarGotov.max * item.gotov / 100).toInt()
            textOpis.text = item.opis
            sverItemOpis(item.sver, false)
//            text_opis.text = time.toString() // item.opis
//            if (itemView.isSelected){
//                onItemSelected()
//            }   else    {
//                onItemCleared()
//            }
            if (item.podstapcount > 0) {
                ivStatDp.alpha = 1F
                ivStatDp.layoutParams.width = 30.dpToPx
                ivStatDp.requestLayout()
                when (item.svernut) {
                    false -> ivStatDp.setImageResource(R.drawable.ic_minus) //setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                    true -> ivStatDp.setImageResource(R.drawable.ic_add_butt) //setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
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
            } else {
                ivStatDp.alpha = 0F
                ivStatDp.layoutParams.width = 1.dpToPx
                ivStatDp.requestLayout()
            }
            ivStatDp.setOnClickListener {
                if (item.podstapcount > 0) {
                    viewModel.timeFun.setExpandStapPlan(item.id.toLong(), item.svernut.not())
                }
            }
            ivExpandOpis.setOnClickListener {
//                svernut = svernut.not()
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
                listener?.invoke(item)
                Log.d("item", "До ${vh.itemView.javaClass}")
                true
            }
        }
    }
)
