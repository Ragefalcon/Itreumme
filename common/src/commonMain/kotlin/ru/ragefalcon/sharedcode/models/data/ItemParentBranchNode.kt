package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemParentBranchNode(
    val id_node: Long,
    val direct: Array<Long>,
    val indirect: Array<Long>
) : Id_class(id_main = id_node.toString()), Parcelable {
    fun getAllParent(): Array<Long> = arrayListOf<Long>().apply { addAll(direct); addAll(indirect) }.toTypedArray()
    fun getNodeAndAllParent(): Array<Long> =
        arrayListOf<Long>().apply { add(id_node); addAll(direct); addAll(indirect) }.toTypedArray()
}