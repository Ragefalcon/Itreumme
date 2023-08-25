package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemIconNodeTree(
    val id: Long,
    val extension: String,
    val type_ramk: Long
) : Id_class(id_main = id.toString()), Parcelable {
    fun name(): String = "icon_$id.$extension"
}

