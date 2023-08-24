package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemLoadQuest(
    val id: String,
    val name: String,
    val dateopen: Long,
    val complete: Long,
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable