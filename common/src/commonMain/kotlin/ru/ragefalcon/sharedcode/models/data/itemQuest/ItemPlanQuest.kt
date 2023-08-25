package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class

@Parcelize
class ItemPlanQuest(
    val id: String,
    val vajn: Long,
    val name: String,
    var opis: String,
    val hour: Double,
    val srok: Long,
    val statsrok: Long,
    val commstat: Long,
    val countstap: Long,
    var sver: Boolean = true
) : Id_class(id_main = id) {
}