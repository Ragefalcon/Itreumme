package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
class ItemIdea(
    val level: Long,
    val id: String,
    val name: String,
    val opis: String,
    val data: Long,
    val stat: Long,
    val parent_id: Long,
    val bloknot: Long,
    val podstapcount: Long,
    var sverChild: Boolean = false,
    var sver: Boolean = true

): Id_class (id_main = id), Parcelable
{
}