package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemDialogLine(
    val id: String,
    val type_message: String,
    val subtype_id: Long,
    val key_id: Long,
    val name: String,
    val datetime: Long,
    val minimaze: Long
) : Id_class(id_main = id), Parcelable