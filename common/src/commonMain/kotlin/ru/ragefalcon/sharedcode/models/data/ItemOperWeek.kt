package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemOperWeek (
    val data: Long,
    val summa: Double,
    val sumCap: Double
    ): Parcelable
