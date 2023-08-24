package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemSettSchetPlan(
    val id: String,
    val name: String,
    val summa: Double,
    val min_aim: Double,
    val max_aim: Double,
    val summaRasxod: Double,
    val open_: Long,
    val countoper: Long
) : Id_class(id_main = id), Parcelable