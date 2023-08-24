package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemValut(
    val id: String,
    val name: String,
    val cod: String,
    val kurs: Double,
    val countschet: Long
) : Id_class(id_main = id), Parcelable