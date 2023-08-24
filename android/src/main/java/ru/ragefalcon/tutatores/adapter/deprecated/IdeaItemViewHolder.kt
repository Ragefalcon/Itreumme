package ru.ragefalcon.tutatores.adapter.deprecated

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.ItemIdeaBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.rotateElemItem
import ru.ragefalcon.tutatores.extensions.sverOpis
import ru.ragefalcon.tutatores.extensions.sverWidthElemItem
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*

class IdeaItemViewHolder(
    ideaBinding: ItemIdeaBinding,
    val stateViewModel: MyStateViewModel,
    val viewModel: AndroidFinanceViewModel
) :
    RVMainAdapterViewHolder<ItemIdea, ItemIdeaBinding>(ideaBinding) {

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


    override fun adaptF(item: ItemIdea) {
        with(binding) {

            ViewCompat.setTransitionName(clIdeaItemRv, "ideacard${item.id}")
            ViewCompat.setTransitionName(tvNameIdea, "ideaname${item.id}")

            (itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 16.dpToPx * item.level.toInt()
            var col = MyColorARGB("FFF7D9")
            var lvl = item.level
            while (lvl > 0) {
                col.R = (col.R * 0.95).toInt()
                lvl--
            }

            ivSelectIndic.requestLayout()
            tvNameIdea.text = item.name
//            textData.text = Date(item.data).format("dd MMM yyyy")
            textOpis.text = item.opis
            sverItemOpis(item.sver, false)
//            textOpis.text = time.toString() // item.opis
//            if (itemView.isSelected){
//                onItemSelected()
//            }   else    {
//                onItemCleared()
//            }
            if (item.podstapcount > 0) {
                ivStatDp.alpha = 1F
                ivStatDp.layoutParams.width = 30.dpToPx
                ivStatDp.requestLayout()
                when (item.sverChild) {
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
                }
            } else {
                ivStatDp.alpha = 0F
                ivStatDp.layoutParams.width = 1.dpToPx
                ivStatDp.requestLayout()
            }
            ivStatDp.setOnClickListener {
                if (item.podstapcount > 0) {
                    item.sverChild = item.sverChild.not()
                }
            }
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
            stateViewModel.selectItemIdea.value = item
//            val trtv = itemView.findViewById<TextView>(R.id.tv_bloknotName)
            funForTransition?.invoke(
                FragmentNavigatorExtras(
                    binding.tvNameIdea to "tvIdeaName_frideastap",
                    binding.clIdeaItemRv to "clIdeaItem_frideastap"
                ),
                binding.clIdeaItemRv
            )
        }
        this.itemView.setOnLongClickListener {
            selFun(pos, svernut)
            Log.d("item", "До ${itemView.javaClass}")
            true
        }
    }

    /** Нигде не используется, нужно только для вызовов самостоятельно, возможно совсем лишнее */
    override fun onItemSelected() {
//        Log.d("item", "onItemSelected: ${tvNameIdea.text}")
    }

    /** Нигде не используется, нужно только для вызовов самостоятельно, возможно совсем лишнее */
    override fun onItemCleared() {
//        Log.d("item", "onItemCleared: ${tvNameIdea.text}")
    }
}