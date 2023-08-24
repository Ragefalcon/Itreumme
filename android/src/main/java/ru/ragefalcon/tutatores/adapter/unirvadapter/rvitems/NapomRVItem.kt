package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.databinding.ItemDenplanBinding
import ru.ragefalcon.tutatores.databinding.ItemNapomBinding
import ru.ragefalcon.tutatores.extensions.*
import java.util.*

class NapomRVItem(
    data: ItemNapom,
    recyclerView: RecyclerView,
    listener: ((ItemNapom) -> Unit)? = null,
    longTapListener: ((ItemNapom) -> Unit)? = null,
    setVypNap: (Boolean,String)->Unit
) : BaseUniRVItem<ItemNapom>(
    data,
    getUniRVViewHolder(ItemDenplanBinding::inflate) { vh, dataIn, rvset ->
        if (vh.binding is ItemNapomBinding) with(vh.binding) {

            fun sverItemOpis(sver: Boolean, anim: Boolean) {
                    sverOpis(recyclerView, rvset.position, textOpis, sver, anim)
                if (textOpis.text != "\n") {
                    rotateElemItem(ivExpandOpis, sver, anim, 180F)
                    sverWidthElemItem(viewSvertext, !sver, anim, 100.dpToPx)
                } else {
                    sverWidthElemItem(viewSvertext, true, false, 200.dpToPx)
                    ivExpandOpis.visibility = View.INVISIBLE
                    ivExpandOpis.layoutParams.width = 1
                }
            }

            when (dataIn.gotov) {
                false -> cbNapom.setImageResource(R.drawable.napom_et) //setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                true -> cbNapom.setImageResource(R.drawable.napom_g) //setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
            }

            cbNapom.setOnClickListener {
                dataIn.gotov = dataIn.gotov.not()
                setVypNap(dataIn.gotov,dataIn.id)
                when (dataIn.gotov) {
                    false -> cbNapom.setImageResource(R.drawable.napom_et) //setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.MULTIPLY)
                    true -> cbNapom.setImageResource(R.drawable.napom_g) //setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
                }
            }
            ivSelectIndic.requestLayout()
            textName.text = dataIn.name
            textOpis.text = "${dataIn.opis}\n"
            textData.text = Date(dataIn.data).format("dd MMM yyyy")
            sverItemOpis(dataIn.sver, false)
            ivExpandOpis.setOnClickListener {
                dataIn.sver = dataIn.sver.not()
                sverItemOpis(dataIn.sver, true)
                if (vh.itemView.isSelected) {
                    vh.bindItem?.let { rvset.selFunc(it) }
                }
            }
            if (vh.itemView.isSelected) listener?.invoke(dataIn)
            vh.itemView.setOnClickListener { // } .setOnClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
            }
            vh.itemView.setOnLongClickListener {
                vh.bindItem?.let { rvset.selFunc(it) }
                longTapListener?.invoke(dataIn)
                true
            }
        }
    }
)