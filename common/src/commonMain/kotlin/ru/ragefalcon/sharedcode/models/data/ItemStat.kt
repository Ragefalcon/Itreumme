package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemStat(
    val id: String,
    val name: String,
    val value: String
) : Id_class(id_main = id), Parcelable