package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemBestDays(
    val id: String,
    val name: String,
    val data: Long,
    val enableIcon: Boolean
) : Id_class(id_main = id), Parcelable