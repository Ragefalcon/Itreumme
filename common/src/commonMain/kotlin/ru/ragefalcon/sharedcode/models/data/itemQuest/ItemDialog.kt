package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class


@Parcelize
class ItemDialog(
    val id: String,
    val name: String,
    val maintext: String,
    val govorun_name: String,
    val govorun_id: Long,
    var sver: Boolean = true

    ) : Id_class(id_main = id) {
}

@Parcelize
class ItemDialogQuest(
    val id: String,
    val quest_id: Long,
    val key_id: Long,
    val name: String,
    val maintext: String,
    val govorun_name: String,
    val govorun_id: Long,
) : Id_class(id_main = id) {
}

@Parcelize
class ItemDialogQuestEvent(
    val id: String,
    val quest_id: Long,
    val key_id: Long,
    val name: String,
    val maintext: String,
    val govorun_name: String,
    val govorun_id: Long,
    val quest_name: String,
    val image_govorun: String,
) : Id_class(id_main = id) {
}