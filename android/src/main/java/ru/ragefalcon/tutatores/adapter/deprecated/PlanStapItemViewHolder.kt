package ru.ragefalcon.tutatores.adapter.deprecated

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*

/**
class PlanStapItemViewHolder(convertView: View, val stateViewModel: MyStateViewModel, val viewModel: AndroidFinanceViewModel) :
    RVMainAdapterViewHolder<ItemPlanStap>(convertView) {
    val time = Date().time

    fun sverItemOpis(sver: Boolean, anim: Boolean) {
        sverOpis(mRecyclerView!!,pos,text_opis,sver,anim)
        if (text_opis.text != "") {
            if (!iv_expand_opis.isVisible) {
                iv_expand_opis.visibility = View.VISIBLE
                iv_expand_opis.layoutParams.width = 30.dpToPx
            }
            rotateElemItem(iv_expand_opis, sver, anim, 180F)
            sverWidthElemItem(view_svertext, !sver, anim, 200.dpToPx)
        }   else    {
            sverWidthElemItem(view_svertext, true, false, 200.dpToPx)
            iv_expand_opis.visibility = View.INVISIBLE
            iv_expand_opis.layoutParams.width = 1
        }
    }


    override fun adaptF(item: ItemPlanStap) {
//        if ((itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart != 16.dpToPx*item.level.toInt()) {
            (itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx*item.level.toInt()
//        }
        (itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx*item.level.toInt()
        itemView.invalidate()
        itemView.requestLayout()
//        itemView.elevation = item.level.toFloat()*100
        var col = MyColorARGB("FFF7D9")
        var lvl = item.level
        while (lvl>0){
            col.R = (col.R*0.95).toInt()
            lvl--
        }
//        itemView.background.setColorFilter(col.toIntColor(), PorterDuff.Mode.MULTIPLY)
//        iv_select_indic.layoutParams.width = 4.dpToPx + 8.dpToPx * item.level.toInt()

        if (itemView.isSelected){
            funGetProgBar?.invoke(progbar_gotov)
            stateViewModel.gotovSelPlanStap.value = progbar_gotov.progress
        }

        iv_select_indic.requestLayout()
        Log.d("itemPS", "name: ${item.name}, level: ${item.level}, svernut: ${item.svernut}")
        text_name.text = item.name
        text_name.requestLayout()
        text_sumhour.text = item.hour.roundToString(1)
//            text_data.text = Date(item.data).format("dd MMM yyyy")
        progbar_gotov.progress = (progbar_gotov.max * item.gotov / 100).toInt()
        text_opis.text = item.opis
        sverItemOpis(item.sver,false)
//            text_opis.text = time.toString() // item.opis
//            if (itemView.isSelected){
//                onItemSelected()
//            }   else    {
//                onItemCleared()
//            }
        if (item.podstapcount > 0) {
            iv_stat_dp.alpha = 1F
            iv_stat_dp.layoutParams.width = 30.dpToPx
            iv_stat_dp.requestLayout()
            when (item.svernut) {
                false -> iv_stat_dp.setImageResource(R.drawable.ic_minus) //setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                true -> iv_stat_dp.setImageResource(R.drawable.ic_add_butt) //setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
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
            iv_stat_dp.alpha = 0F
            iv_stat_dp.layoutParams.width = 1.dpToPx
            iv_stat_dp.requestLayout()
        }
        iv_stat_dp.setOnClickListener {
            if (item.podstapcount > 0) {
                viewModel.setExpandStapPlan(item.id.toLong(), item.svernut.not())
            }
        }
        iv_expand_opis.setOnClickListener {
//                svernut = svernut.not()
            item.sver = item.sver.not()
            sverItemOpis(item.sver,true)
            if (itemView.isSelected) {
                selFun(pos, svernut)
            }
        }
        this.itemView.setOnClickListener { // } .setOnClickListener {
            selFun(pos, svernut)
        }
        this.itemView.setOnLongClickListener {
            selFun(pos, svernut)
            funFromParentRV?.invoke(item)
            Log.d("item", "До ${itemView.javaClass}")
            true
        }
    }

    /** Нигде не используется, нужно только для вызовов самостоятельно, возможно совсем лишнее */
    override fun onItemSelected() {
//        Log.d("item", "onItemSelected: ${text_name.text}")
    }

    /** Нигде не используется, нужно только для вызовов самостоятельно, возможно совсем лишнее */
    override fun onItemCleared() {
//        Log.d("item", "onItemCleared: ${text_name.text}")
    }

    override fun adaptF(item: ItemPlanStap) {
        TODO("Not yet implemented")
    }
}
 */
