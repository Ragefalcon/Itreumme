package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemTyperasxod(
    val id: String,
    val typer: String,
    val planschet: String,
    val schpl_id: Long,
    val open: Boolean,
) : Id_class(id_main = id), Parcelable