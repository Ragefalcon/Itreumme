package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan

@Parcelize
data class ItemPlan (
    val id: String,
    val name: String,
    val vajn: Long,
    val gotov:Double,
    val data1: Long,
    val data2: Long,
    val opis: String,
    val stat: TypeStatPlan,
    val hour: Double,
    val open_countstap: Long,
    val countstap: Long,
    val direction: Boolean,
    val sort: Long,
    val quest_id: Long = 0,
    val quest_key_id: Long = 0,
    val namequest: String = "",
    val min_aim: Double? = null,
    val max_aim: Double? = null,
    val summa: Double? = null,
    val summaRasxod: Double? = null,
    val schplOpen: Boolean? = null,
    override val sver: Boolean = true
) : SverOpis<ItemPlan>, Id_class(id_main = id.toString()), Parcelable {
    override fun sver(newSver: Boolean): ItemPlan = this.copy( sver = newSver )
}
