package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemMainParam(
    val id: String,
    val name: String,
    val intparam: Long,
    val stringparam: String
) : Id_class(id_main = id), Parcelable