package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemCharacteristic (
    val id: Long,
    val sort:   Long,
    val name:  String,
    val opis:  String,
    val stat: Long,
    val startStat: Long,
    val progress: Double,
    val hour: Double,
    var sver: Boolean = true,
) : Id_class(id_main = id.toString()), Parcelable