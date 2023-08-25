package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize


@Parcelize
data class ItemShablonDenPlan(
    val id: String,
    val sort: Long,
    val name: String,
    val namepl: String,
    val nameprpl: String,
    val namestap: String,
    val opis: String,
    var vajn: Long,
    val time1: String,
    var time2: String,
    val privplan: Long,
    val stap_prpl: Long,
    val povtor: String,
    override val sver: Boolean = true
) : SverOpis<ItemShablonDenPlan>, Id_class(id_main = id), Parcelable {
    override fun sver(newSver: Boolean): ItemShablonDenPlan = this.copy(sver = newSver)
}
