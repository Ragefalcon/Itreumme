package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class

@Parcelize
class ItemGovorun(
    val id: String,
    val name: String,
    val opis: String,
    val image_file: String,
    var sver: Boolean = true
) : Id_class(id_main = id) {
}
