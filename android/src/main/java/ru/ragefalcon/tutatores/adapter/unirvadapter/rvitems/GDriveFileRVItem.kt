package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import ru.ragefalcon.sharedcode.myGoogleLib.ItemGDriveFile
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemSchetBinding
import java.util.*

class GDriveFileRVItem(
    data: ItemGDriveFile,
    selectListener: ((ItemGDriveFile) -> Unit)? = null,
    tapListener: ((ItemGDriveFile) -> Unit)? = null,
    longTapListener: ((ItemGDriveFile) -> Unit)? = null
) : BaseUniRVItem<ItemGDriveFile>(
    data,
    getUniRVViewHolder(ItemSchetBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemSchetBinding) {
            with(vh.binding) {

                textName.text = item.name
                textType.text = item.mimeType
                textSchet.text = item.id
                textSumma.text = ""
                textData.text = item.kind

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
