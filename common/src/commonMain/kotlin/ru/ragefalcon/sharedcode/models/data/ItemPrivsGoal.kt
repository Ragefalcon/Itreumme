package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemPrivsGoal(
    val id: String,
    val name: String,
    val namePlan: String,
    val vajn: Long,
    val stap: String,
    val id_plan: String,
    var gotov: Double,
    var hour: Double,
    val opis: String = "temp",
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable