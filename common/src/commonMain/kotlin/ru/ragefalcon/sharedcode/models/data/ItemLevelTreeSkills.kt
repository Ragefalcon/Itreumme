package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemLevelTreeSkills(
    val id: Long,
    val id_tree: Long,
    val name: String,
    val num_level: Long,
    val opis: String,
    val proc_porog: Double,
    val open: Long,
    var mustCompleteCountNode: Long,
    var mustCountNode: Long,
    var completeCountNode: Long,
    var countNode: Long,
    val quest_id: Long,
    val quest_key_id: Long,
    var sver: Boolean = true
) : Id_class(id_main = id.toString()), Parcelable