package ru.ragefalcon.tutatores.adapter.deprecated

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*

/**
class VxodItemViewHolder(convertView: View, val stateViewModel: MyStateViewModel, val viewModel: AndroidFinanceViewModel) ://, val funscroll:(Int,Int)->Unit = { aa,bb ->}
    RVMainAdapterViewHolder<ItemVxod>(convertView) {
    val time = Date().time
    val customAnimTransition = TransitionSet().apply {
        val imageFadeTransition = ChangeBounds().addTarget(R.id.view_svertext)
        //        val textSlideTransition = Slide().addTarget(R.id.tv_test_plan)
//            val textSlideTransition = ChangeBounds().addTarget(R.id.text_opis)
        val rvBoundsTransition = ChangeBounds().addTarget(R.id.card_view_item)
        val ivBoundsTransition = ChangeBounds().addTarget(itemView)
        duration = 500L // 1 sec
        addTransition(ivBoundsTransition)
        addTransition(rvBoundsTransition)
        addTransition(imageFadeTransition)
//            addTransition(textSlideTransition)
    }
    val logoScene = Scene(card_view_item).apply {
        setEnterAction {
            if (text_opis.text == "") {
                view_svertext.visibility = android.view.View.INVISIBLE
                view_svertext.layoutParams.height = 0
                view_svertext.requestLayout()
                iv_expand_opis.alpha = 0F
            } else {
                view_svertext.visibility = android.view.View.VISIBLE
                view_svertext.layoutParams.height = 2.dpToPx
                view_svertext.requestLayout()
                iv_expand_opis.alpha = 1F
                iv_expand_opis.setImageResource(R.drawable.ic_baseline_expand_more_24)
            }
            text_opis.visibility = android.view.View.INVISIBLE
            text_opis.layoutParams.height = 1
            text_opis.requestLayout()
        }

    }
    val logoScene2 = Scene(card_view_item).apply {
        setEnterAction {
            view_svertext.visibility = android.view.View.INVISIBLE
            view_svertext.layoutParams.height = 0
            view_svertext.requestLayout()
            iv_expand_opis.alpha = 1F
            iv_expand_opis.setImageResource(R.drawable.ic_baseline_expand_less_24)
            text_opis.layoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            text_opis.visibility = android.view.View.VISIBLE
            text_opis.requestLayout()
        }
    }

    fun sverItemOpis(sver: Boolean, anim: Boolean) {
        sverOpis(mRecyclerView!!,pos,text_opis,sver,anim)
        if (text_opis.text != "\n") {
            rotateElemItem(iv_expand_opis, sver, anim, 180F)
            sverWidthElemItem(view_svertext, !sver, anim, 100.dpToPx)
        }   else    {
            sverWidthElemItem(view_svertext, true, false, 200.dpToPx)
            iv_expand_opis.visibility = View.INVISIBLE
            iv_expand_opis.layoutParams.width = 1
        }
    }

    override fun adaptF(item: ItemVxod) {

        iv_select_indic.requestLayout()
        text_name.text = item.name
        text_opis.text = "${item.opis}\n"
        text_data.text = Date(item.data).format("dd MMM yyyy")
        when (item.stat.toInt()) {
//            0 -> iv_stat_dp.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
//            1 -> iv_stat_dp.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
////                2 -> ImageViewCompat.setImageTintList(iv_stat_dp, ColorStateList.valueOf(MyColorARGB.DOXODDARKGREEN.toIntColor()))
//            2 -> iv_stat_dp.setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.MULTIPLY)
//            3 -> iv_stat_dp.setColorFilter(
//                MyColorARGB.DOXODDARKGREEN.toIntColor(),
//                android.graphics.PorterDuff.Mode.MULTIPLY
//            )
//            4 -> iv_stat_dp.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
            0 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTint_01), android.graphics.PorterDuff.Mode.MULTIPLY)
            1 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTint_02), android.graphics.PorterDuff.Mode.MULTIPLY)
            2 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTint_03), android.graphics.PorterDuff.Mode.MULTIPLY)
            3 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTint_04), android.graphics.PorterDuff.Mode.MULTIPLY)
            4 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTint_05), android.graphics.PorterDuff.Mode.MULTIPLY)
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
        iv_expand_opis.setOnClickListener {
//        iv_stat_dp.setOnClickListener {
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

    override fun adaptF(item: ItemVxod) {
        TODO("Not yet implemented")
    }
}
 */
