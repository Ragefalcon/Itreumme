package ru.ragefalcon.sharedcode.models.data;

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class AvatarStat(
    val id: String,
    var capital: String = "",
    var dreamInReal: String = "",
    var goalInReal: String = "",
    var goalInWork: String = "",
    var hourForDream: String = "",
    var hourForGoal: String = ""
) : Id_class(id_main = id), Parcelable
