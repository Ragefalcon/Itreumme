package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet


@Parcelize
class ItemSaveSetStyle (
    val id: Long,
    val name: String,
    val type: TypeSaveStyleSet
): Id_class (id_main = id.toString())
{
}