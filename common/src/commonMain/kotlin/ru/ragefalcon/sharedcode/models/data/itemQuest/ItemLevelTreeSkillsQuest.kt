package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemLevelTreeSkillsQuest(
    val id: Long,
    val id_tree: Long,
    val name: String,
    val num_level: Long,
    val opis: String,
    val proc_porog: Double,
    var mustCountNode: Long,
    var countNode: Long,
    val visible_stat: Long,
    var sver: Boolean = true
) : Id_class(id_main = id.toString()), Parcelable