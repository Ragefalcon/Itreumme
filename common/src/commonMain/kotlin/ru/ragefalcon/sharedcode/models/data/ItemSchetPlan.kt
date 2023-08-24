package ru.ragefalcon.sharedcode.models.data


import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemSchetPlan(
    val id: String,
    val name: String,
    val min_aim: Double,
    val max_aim: Double,
    val open_: Long
) : Id_class(id_main = id), Parcelable