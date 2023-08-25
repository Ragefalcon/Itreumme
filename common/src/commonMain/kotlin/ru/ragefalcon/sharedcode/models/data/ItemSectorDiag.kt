package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.MyColorFloatARGB
import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemSectorDiag(
    val id: String,
    val name: String,
    val summa: Double,
    val procent: Double,
    val angle: Float,
    val delta: Float,
    val color: MyColorFloatARGB
) : Id_class(id_main = id), Parcelable
