package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemEffekt (
    val id: String,
    val name: String,
    val idplan: Long,
    val norma: Double,
    val sumNedel: Double,
    val sumMonth: Double,
    val sumYear: Double

):  Id_class (id_main = id), Parcelable


