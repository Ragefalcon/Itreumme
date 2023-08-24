package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemBodyDialog(
    val id: String,
    val text: String,
    val type: String = "text"
) : Id_class(id_main = id), Parcelable