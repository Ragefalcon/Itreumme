package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize


//@Parcelize
sealed class ItemNextAction(id_main: String) : SverOpis<ItemNextAction>, Id_class(id_main), Parcelable {
    abstract val id: Long
    abstract val sort: Long
    abstract val common_id: Long
    abstract val name: String
    abstract val namePlan: String
    abstract val nameStap: String
    abstract val vajn: Long
    abstract val privplan: Long
    abstract val stap_prpl: Long

    abstract fun myCopy(
        id: Long = this.id,
        sort: Long = this.sort,
        common: Long = this.common_id,
        name: String = this.name,
        namePlan: String = this.namePlan,
        nameStap: String = this.nameStap,
        vajn: Long = this.vajn,
        privplan: Long = this.privplan,
        stap_prpl: Long = this.stap_prpl,
        sver: Boolean = this.sver
    ): ItemNextAction

    override val sver: Boolean = true
    override fun sver(newSver: Boolean): ItemNextAction = this.myCopy(sver = newSver)
}
@Parcelize
data class ItemNextActionStap(
    override val id: Long,
    override val sort: Long,
    override val common_id: Long,
    override val name: String,
    override val namePlan: String,
    override val nameStap: String,
    override val vajn: Long,
    override val privplan: Long,
    override val stap_prpl: Long,
    override val sver: Boolean = true
) : ItemNextAction(id.toString()) {
    override fun myCopy(
        id: Long,
        sort: Long,
        common: Long,
        name: String,
        namePlan: String,
        nameStap: String,
        vajn: Long,
        privplan: Long,
        stap_prpl: Long,
        sver: Boolean
    ): ItemNextAction = this.copy(
        id = id,
        sort = sort,
        common_id = common,
        name = name,
        namePlan = namePlan,
        nameStap = nameStap,
        vajn = vajn,
        privplan = privplan,
        stap_prpl = stap_prpl,
        sver = sver
    )
}
@Parcelize
data class ItemNextActionCommon(
    override val id: Long,
    override val sort: Long,
    override val common_id: Long,
    override val name: String,
    override val namePlan: String,
    override val nameStap: String,
    override val vajn: Long,
    override val privplan: Long,
    override val stap_prpl: Long,
    override val sver: Boolean = true
) : ItemNextAction(id.toString()) {
    override fun myCopy(
        id: Long,
        sort: Long,
        common: Long,
        name: String,
        namePlan: String,
        nameStap: String,
        vajn: Long,
        privplan: Long,
        stap_prpl: Long,
        sver: Boolean
    ): ItemNextAction = this.copy(
        id = id,
        sort = sort,
        common_id = common,
        name = name,
        namePlan = namePlan,
        nameStap = nameStap,
        vajn = vajn,
        privplan = privplan,
        stap_prpl = stap_prpl,
        sver = sver
    )
}
fun getItemNextAction(
    id: Long,
    sort: Long,
    common: Long,
    name: String,
    namePlan: String,
    nameStap: String,
    vajn: Long,
    privplan: Long,
    stap_prpl: Long,
    sver: Boolean = true
) = (if (common == 0L) ::ItemNextActionStap else ::ItemNextActionCommon).invoke(
        id,
        sort,
        common,
        name,
        namePlan,
        nameStap,
        vajn,
        privplan,
        stap_prpl,
        sver
)