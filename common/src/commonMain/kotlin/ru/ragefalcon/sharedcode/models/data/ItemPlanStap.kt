package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap

@Parcelize
data class ItemPlanStap(
    val level: Long,
    val id: String,
    val parent_id: String,
    val name: String,
    val gotov: Double,
    val data1: Long,
    val data2: Long,
    val opis: String,
    val svernut: Boolean,
    val stat: TypeStatPlanStap,
    val hour: Double,
    val podstapcount: Long,
    val marker: Long,
    val sortCTE: String,
    val sort: Long,
    val quest_id: Long = 0,
    val quest_key_id: Long = 0,
    val min_aim: Double? = null,
    val max_aim: Double? = null,
    val summa: Double? = null,
    val summaRasxod: Double? = null,
    val schplOpen: Boolean? = null,
    override val sver: Boolean = true
) : SverOpis<ItemPlanStap>, Id_class(id_main = id.toString()), Parcelable {
    override fun sver(newSver: Boolean): ItemPlanStap = this.copy( sver = newSver )
}
