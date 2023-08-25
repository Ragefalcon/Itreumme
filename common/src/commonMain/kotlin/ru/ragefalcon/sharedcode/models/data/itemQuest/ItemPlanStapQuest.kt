package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class

@Parcelize
class ItemPlanStapQuest(
    val level: Long,
    val id: String,
    val parent_id: String,
    val name: String,
    var hour: Double,
    val srok: Long,
    val opis: String,
    val svernut: Boolean,
    val statsrok: Long,
    val podstapcount: Long,
    val commstat: Long,
    val idplan: Long,
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable {
}
