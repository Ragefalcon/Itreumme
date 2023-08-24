package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemHourVajn (
    val vajn: Long,
    val sumNedel: Double,
    val sumMonth: Double,
    val sumYear: Double

):  Id_class (id_main = vajn.toString()), Parcelable


