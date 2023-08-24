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
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.toIntColor
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*
enum class LocateSpisPlan{PlanTab, SelectParentPlan}
/**
class PlanItemViewHolder(convertView: View, val stateViewModel: MyStateViewModel, val locateSpis: LocateSpisPlan = LocateSpisPlan.PlanTab) :
    RVMainAdapterViewHolder<ItemPlan>(convertView) {
    val time = Date().time
//    val customAnimTransition = TransitionSet().apply {
//        val imageFadeTransition = ChangeBounds().addTarget(R.id.view_svertext)
//        //        val textSlideTransition = Slide().addTarget(R.id.tv_test_plan)
//        val textSlideTransition = ChangeBounds().addTarget(R.id.text_opis)
//        val rvBoundsTransition = ChangeBounds().addTarget(R.id.card_view_item)
//        duration = 500L // 1 sec
//        addTransition(rvBoundsTransition)
//        addTransition(imageFadeTransition)
//        addTransition(textSlideTransition)
//    }
//    val logoScene = Scene(card_view_item).apply {
//        setEnterAction {
//            if (text_opis.text == "") {
//                view_svertext.visibility = android.view.View.INVISIBLE
//                view_svertext.layoutParams.height = 0
//                view_svertext.requestLayout()
//            } else {
//                view_svertext.visibility = android.view.View.VISIBLE
//                view_svertext.layoutParams.height = 2.dpToPx
//                view_svertext.requestLayout()
//            }
//            text_opis.visibility = android.view.View.INVISIBLE
//            text_opis.layoutParams.height = 0
//            text_opis.requestLayout()
//        }
//
//    }
//    val logoScene2 = Scene(card_view_item).apply {
//        setEnterAction {
//            view_svertext.visibility = android.view.View.INVISIBLE
//            view_svertext.layoutParams.height = 0
//            view_svertext.requestLayout()
//            text_opis.layoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
//            text_opis.visibility = android.view.View.VISIBLE
//            text_opis.requestLayout()
//        }
//    }
//
//    fun sverOpis(sver: Boolean) {
//        if (text_opis.text == "" || sver) {
////            if (text_opis.visibility != View.INVISIBLE) {
//                TransitionManager.go(logoScene,customAnimTransition)
////            }
////            if (text_opis.text == "") {
////                view_svertext.visibility = View.INVISIBLE
////                view_svertext.layoutParams.height = 0
////                view_svertext.requestLayout()
////            } else {
////                view_svertext.visibility = View.VISIBLE
////                view_svertext.layoutParams.height = 2.dpToPx
////                view_svertext.requestLayout()
////            }
////            text_opis.visibility = View.INVISIBLE
////            text_opis.layoutParams.height = 0
////            text_opis.requestLayout()
//        } else {
////            if (text_opis.visibility != View.VISIBLE) {
//                TransitionManager.go(logoScene2,customAnimTransition)
////            }
////            view_svertext.visibility = View.INVISIBLE
////            view_svertext.layoutParams.height = 0
////            view_svertext.requestLayout()
////            text_opis.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
////            text_opis.visibility = View.VISIBLE
////            text_opis.requestLayout()
//        }
//    }

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

    override fun adaptF(item: ItemPlan) {
        iv_select_indic.requestLayout()
        text_name.text = item.name
        text_sumhour.text = item.hour.roundToString(1)
//            text_data.text = Date(item.data).format("dd MMM yyyy")
        progbar_gotov.progress = (progbar_gotov.max * item.gotov / 100).toInt()
        text_opis.text = item.opis
        if (itemView.isSelected){
            when(locateSpis){
                LocateSpisPlan.PlanTab -> stateViewModel.selectItemPlan.value = item
                LocateSpisPlan.SelectParentPlan -> {}
            }
            Log.d("MyTut", "$funGetProgBar: $progbar_gotov");
            funGetProgBar?.invoke(progbar_gotov)
            stateViewModel.gotovSelPlan.value = progbar_gotov.progress

        }
//            text_opis.text = time.toString() // item.opis
//            if (itemView.isSelected){
//                onItemSelected()
//            }   else    {
//                onItemCleared()
//            }
        when (item.vajn.toInt()) {
            0 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_00), android.graphics.PorterDuff.Mode.MULTIPLY)
            1 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_01), android.graphics.PorterDuff.Mode.MULTIPLY)
            2 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_02), android.graphics.PorterDuff.Mode.MULTIPLY)
            3 -> iv_stat_dp.setColorFilter(ContextCompat.getColor(containerView!!.context, R.color.colorStatTimeSquareTint_03), android.graphics.PorterDuff.Mode.MULTIPLY)
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
        }
        sverItemOpis(item.sver,false)
        iv_stat_dp.setOnClickListener {
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

    override fun adaptF(item: ItemPlan) {
        TODO("Not yet implemented")
    }
}
 */
