package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemTypeTrigger(
    val id: String,
    val cod: String,
    val name: String
) : Id_class(id_main = id), Parcelable