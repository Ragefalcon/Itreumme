package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize


@Parcelize
data class ItemNapom(
    val id: String,
    val idplan: String,
    val idstap: String,
    val name: String,
    val opis: String,
    val data: Long,
    val time: String,
    var gotov: Boolean,
    override val sver: Boolean = true
) : SverOpis<ItemNapom>, Id_class(id_main = id.toString()), Parcelable {
    override fun sver(newSver: Boolean): ItemNapom = this.copy(sver = newSver)
}




