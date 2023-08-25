package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemSumOnSchet(
    val id: Long,
    val name: String,
    val summa: Double,
    val summaStr: String,
    val valut: String,
    val procent: Double
) : Parcelable