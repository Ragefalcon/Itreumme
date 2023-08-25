package ru.ragefalcon.sharedcode.models.data


import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemSchetPlanWithSum(
    val id: Long,
    val name: String,
    val summa: Double,
    val min_aim: Double,
    val max_aim: Double,
    val summaStr: String,
    val summaRasxod: Double,
    val procent: Double
) : Parcelable