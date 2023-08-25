package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelize


@Parcelize
class ItemSetColorLibrary(
    val id: Long,
    val name: String
) : Id_class(id_main = id.toString()) {
}