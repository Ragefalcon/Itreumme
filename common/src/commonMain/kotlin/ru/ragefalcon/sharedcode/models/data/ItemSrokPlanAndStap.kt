package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap

@Parcelize
data class ItemSrokPlanAndStap(
    val id: Long,
    val plan_id: Long,
    val stap_id: Long,
    val name: String,
    val namePlan: String,
    val gotov: Double,
    val data1: Long,
    val data2: Long,
    val stat: Long,
    val marker: Long,
    val quest: Boolean,
    val listDate: List<Long>
):Id_class (id_main = id.toString()),  Parcelable
{
}
