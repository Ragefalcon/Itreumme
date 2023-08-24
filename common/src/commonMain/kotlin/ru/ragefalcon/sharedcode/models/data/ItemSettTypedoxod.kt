package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemSettTypedoxod(
    val id: String,
    val typed: String,
    val open: Boolean,
    val countoper: Long
) : Id_class(id_main = id), Parcelable