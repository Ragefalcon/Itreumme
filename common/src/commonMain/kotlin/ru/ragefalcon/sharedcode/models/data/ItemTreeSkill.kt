package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills

@Parcelize
class ItemTreeSkill(
    val id: String,
    val id_area: Long,
    val name: String,
    val id_type_tree: Long,
    val opis: String,
    val icon: Long,
    var stat: TypeStatTreeSkills,
    var open_edit: Boolean = stat == TypeStatTreeSkills.OPEN_EDIT,
    var completeCountNode: Long,
    var countNode: Long,
    val quest_id: Long,
    var namequest: String = "",
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable