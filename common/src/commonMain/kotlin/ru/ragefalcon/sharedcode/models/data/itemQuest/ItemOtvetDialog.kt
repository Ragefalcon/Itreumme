package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class


@Parcelize
class ItemOtvetDialog(
    val id: String,
    val dialog_id: Long,
    val text: String,
    val order_number: Long,
) : Id_class(id_main = id) {
}