package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemSchet(
    val id: String,
    val name: String,
    val val_id: Long,
    val open_: Boolean,
    val cod: String
) : Id_class(id_main = id), Parcelable