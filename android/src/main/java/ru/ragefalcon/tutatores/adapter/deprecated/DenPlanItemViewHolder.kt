package ru.ragefalcon.tutatores.adapter.deprecated

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*
/**
class DenPlanItemViewHolder(convertView: View, val stateViewModel: MyStateViewModel) :
    RVMainAdapterViewHolder<ItemDenPlan>(convertView) {

    val time = Date().time

    fun sverItemOpis(sver: Boolean, anim: Boolean) {
        mRecyclerView?.also {
            sverOpis(it,pos,text_opis,sver,anim)
        }
        if (text_opis.text != "") {
            rotateElemItem(iv_stat_dp, sver, anim, 270F)
            sverWidthElemItem(view_svertext, !sver, anim, 200.dpToPx)
        }   else    {
            sverWidthElemItem(view_svertext, true, false, 200.dpToPx)
        }
    }

    override fun adaptF(item: ItemDenPlan) {
        text_name.text = item.name
        text_time1.text = item.time1.subSequence(0,5)
        text_time2.text = item.time2.subSequence(0,5)
        text_sumhour.text = item.sum_hour.roundToString(1)
        progbar_gotov.progress = (progbar_gotov.max * item.gotov / 100).toInt()
        text_opis.text = item.opis
        when (item.vajn.toInt()) {
            0 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_00), android.graphics.PorterDuff.Mode.MULTIPLY)
            1 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_01), android.graphics.PorterDuff.Mode.MULTIPLY)
            2 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_02), android.graphics.PorterDuff.Mode.MULTIPLY)
            3 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_03), android.graphics.PorterDuff.Mode.MULTIPLY)
//            0 -> iv_stat_dp.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
//            1 -> iv_stat_dp.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
////                2 -> ImageViewCompat.setImageTintList(iv_stat_dp, ColorStateList.valueOf(MyColorARGB.DOXODDARKGREEN.toIntColor()))
//            2 -> iv_stat_dp.setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.MULTIPLY)
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
        }
        sverItemOpis(item.sver,false)
        iv_stat_dp.setOnClickListener {
//            svernut = svernut.not()
            item.sver = item.sver.not()
            sverItemOpis(item.sver, true)
            if (itemView.isSelected) {
                selFun(pos, svernut)
            }
        }
//        text_name_oper.setOnClickListener {
////            print("aaa")
//        }
        this.itemView.setOnClickListener { // } .setOnClickListener {
            selFun(pos, svernut)
        }
        this.itemView.setOnLongClickListener {
            selFun(pos, svernut)
            funFromParentRV?.invoke(item)
            true
        }
    }
    override fun onItemSelected() {
//        Log.d("item", "onItemSelected: ${text_name.text}")
    }

    override fun onItemCleared() {
//        Log.d("item", "onItemCleared: ${text_name.text}")
    }

    override fun adaptF(item: ItemDenPlan) {
        TODO("Not yet implemented")
    }
}
 */
