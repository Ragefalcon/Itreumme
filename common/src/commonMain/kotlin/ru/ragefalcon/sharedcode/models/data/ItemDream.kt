package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemDream(
    val id: String,
    val lvl:   Long,
    val name:  String,
    val data1: Long,
    val opis:  String,
    val stat: Long,
    val hour: Double,
//    val privscount: Long,
    val foto:  Long,
    var sver: Boolean = true
) : Id_class(id_main = id), Parcelable