package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize


@Parcelize
class ItemColorLibrary (
    val id: Long,
    val color: MyColorARGB
): Parcelable, Id_class (id_main = id.toString())
{
}