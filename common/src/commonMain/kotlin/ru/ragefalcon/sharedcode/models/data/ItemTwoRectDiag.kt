package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemTwoRectDiag(
    val year: String,
    val month: String,
    val monthyear: String,
    val summadox: Double,
    val summarasx: Double,
    val sumyeardox: Double,
    val sumyearrasx: Double,
    val procentdox: Double,
    val procentrasx: Double
) : Parcelable
