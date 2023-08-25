package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemGoal(
    val id: String,
    val lvl: Long,
    val name: String,
    val data1: Long,
    val data2: Long,
    val opis: String,
    val gotov: Double,
    val hour: Double,
    val foto: Long,
    val min_aim: Double? = null,
    val max_aim: Double? = null,
    val summa: Double? = null,
    val summaRasxod: Double? = null,
    val schplOpen: Boolean? = null,
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable