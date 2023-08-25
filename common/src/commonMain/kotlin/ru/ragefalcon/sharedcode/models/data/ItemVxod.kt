package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemVxod(
    val id: String,
    val name: String,
    val opis: String,
    val data: Long,
    val stat: Long,
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable {
}