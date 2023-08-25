package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemBloknot(
    val id: String,
    val name: String,
    val opis: String,
    val countidea: Long,
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable {
}