package ru.ragefalcon.tutatores.adapter.deprecated

import android.graphics.Color
import android.util.Log
import android.view.View
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.tutatores.databinding.ItemIdeaStapBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*

@Deprecated("Использовать RVItem")
class IdeaStapItemViewHolder  (bindingView: ItemIdeaStapBinding, val stateViewModel: MyStateViewModel, val viewModel: AndroidFinanceViewModel) :
    RVMainAdapterViewHolder<ItemIdeaStap, ItemIdeaStapBinding>(bindingView) {

    val time = Date().time

    fun sverItemOpis(sver: Boolean, anim: Boolean) {
        with(binding) {
            sverOpis(mRecyclerView!!, pos, textOpis, sver, anim)
            if (textOpis.text != "") {
                rotateElemItem(ivExpandOpis, sver, anim, 180F)
                sverWidthElemItem(viewSvertext, !sver, anim, 200.dpToPx)
            } else {
                sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                ivExpandOpis.visibility = View.INVISIBLE
                ivExpandOpis.layoutParams.width = 1
            }
        }
    }


    override fun adaptF(item: ItemIdeaStap) {
        with(binding) {

            ivSelectIndic.requestLayout()
            textName.text = item.name
//            textData.text = Date(item.data).format("dd MMM yyyy")
            textOpis.text = item.opis
            when (item.stat.toInt()) {
                0 -> ivStatDp.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                1 -> ivStatDp.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
//                2 -> ImageViewCompat.setImageTintList(ivStatDp, ColorStateList.valueOf(MyColorARGB.DOXODDARKGREEN.toIntColor()))
                2 -> ivStatDp.setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.MULTIPLY)
                3 -> ivStatDp.setColorFilter(
                    MyColorARGB.DOXODDARKGREEN.toIntColor(),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                4 -> ivStatDp.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
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
//                svernut = svernut.not()
                item.sver = item.sver.not()
                sverItemOpis(item.sver, true)
                if (itemView.isSelected) {
                    selFun(pos, svernut)
                }
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
//        Log.d("item", "onItemSelected: ${textName.text}")
    }

    /** Нигде не используется, нужно только для вызовов самостоятельно, возможно совсем лишнее */
    override fun onItemCleared() {
//        Log.d("item", "onItemCleared: ${textName.text}")
    }
}