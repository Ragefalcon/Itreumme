package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class


@Parcelize
class ItemTreeSkillsQuest(
    val id: Long,
    val id_area: Long,
    val name: String,
    val id_type_tree: Long,
    val opis: String,
    val icon: Long,
    var countNode: Long,
    var visibleStat: Long,
    var sver: Boolean = true
) : Id_class(id_main = id.toString()), Parcelable
