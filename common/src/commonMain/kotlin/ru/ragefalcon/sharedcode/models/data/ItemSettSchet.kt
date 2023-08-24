package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemSettSchet(
    val id: String,
    val val_id: Long,
    val name: String,
    val summa: Double,
    val cod: String,
    val open_: Boolean,
    val countoper: Long
) : Id_class(id_main = id), Parcelable