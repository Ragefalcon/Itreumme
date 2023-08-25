package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemBindingNode(
    val id_node: Long,
    val kin: Array<Long>
) : Id_class(id_main = id_node.toString()), Parcelable

