package ru.ragefalcon.sharedcode.models.data


import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan

@Parcelize
class ItemBindForSchplWithName(
    val id: Long,
    val type: TypeBindElementForSchetPlan,
    val element_id: Long,
    val schet_plan_id: Long,
    val name: String,
    val stat: Long
) : Id_class(id_main = id.toString()), Parcelable