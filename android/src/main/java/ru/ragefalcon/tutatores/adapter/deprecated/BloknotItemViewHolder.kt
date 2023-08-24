package ru.ragefalcon.tutatores.adapter.deprecated

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.ItemBloknotBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class BloknotItemViewHolder(
    vh: ItemBloknotBinding,
    val stateViewModel: MyStateViewModel,
    val viewModel: AndroidFinanceViewModel
) ://, val funscroll:(Int,Int)->Unit = { aa,bb ->}
    RVMainAdapterViewHolder<ItemBloknot, ItemBloknotBinding>(vh) {


    fun sverItemOpis(sver: Boolean, anim: Boolean) {
        with(binding) {
            sverOpis(mRecyclerView!!, pos, textOpis, sver, anim)
            if (textOpis.text != "") {
                rotateElemItem(ivExpandOpis, sver, anim, 180F)
                sverWidthElemItem(viewSvertext, !sver, anim, 100.dpToPx)
            } else {
                sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                ivExpandOpis.visibility = View.INVISIBLE
                ivExpandOpis.layoutParams.width = 1
            }
        }
    }

    override fun adaptF(item: ItemBloknot) {

//        ivSelectIndic.requestLayout()
        with(binding) {


            ViewCompat.setTransitionName(tvBloknotName, "blokname${item.id}")
            ViewCompat.setTransitionName(clBloknotItem, "blokcard${item.id}")
            tvBloknotName.text = item.name
            textOpis.text = "${item.opis}"
            sverItemOpis(item.sver, false)
            if (itemView.isSelected) {
//            stateViewModel.selectItemBloknot.value = item
            }
            ivExpandOpis.setOnClickListener {
//        ivStat_dp.setOnClickListener {
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
                stateViewModel.selectItemBloknot.value = item
                val trtv = itemView.findViewById<TextView>(R.id.tv_bloknot_name)
                funForTransition?.invoke(
                    FragmentNavigatorExtras(
                        binding.tvBloknotName to "tvIdeaName_frsp",
                        binding.clBloknotItem to "clBloknotItem_frcl"
                    ),
                    binding.clBloknotItem
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
        Log.d("item", "onItemSelected: ${binding.tvBloknotName.text}")
    }

    /** Нигде не используется, нужно только для вызовов самостоятельно, возможно совсем лишнее */
    override fun onItemCleared() {
        Log.d("item", "onItemCleared: ${binding.tvBloknotName.text}")
    }
}