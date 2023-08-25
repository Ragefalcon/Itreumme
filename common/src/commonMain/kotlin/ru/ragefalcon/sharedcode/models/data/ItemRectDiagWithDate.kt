package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemRectDiagWithDate(
    val year: String,
    val month: String,
    val date: Long,
    val summa: Double,
    val sumyear: Double,
    val procent: Double
) : Parcelable
