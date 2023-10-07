package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class DreamStat(
    val id: String,
    var week: String = "",
    var month: String = "",
    var year: String = "",
    var all: String = "",
    var count: String = ""
) : Id_class(id_main = id), Parcelable