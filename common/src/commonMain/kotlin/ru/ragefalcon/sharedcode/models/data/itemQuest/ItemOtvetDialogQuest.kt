package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class

@Parcelize
class ItemOtvetDialogQuest(
    val id: String,
    val quest_id: Long,
    val key_id: Long,
    val dialog_id: Long,
    val text: String,
    val order_number: Long,
) : Id_class(id_main = id) {
}