package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemShabDoxod(
    val id: String,
    val sort: Long,
    val name: String,
    val nameoper: String,
    val summa: Double,
    val type: String,
    val nameSchet: String,
    val schet_id: Long,
) : Id_class(id_main = id), Parcelable