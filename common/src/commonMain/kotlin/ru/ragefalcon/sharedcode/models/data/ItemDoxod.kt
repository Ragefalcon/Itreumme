package ru.ragefalcon.sharedcode.models.data


import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemDoxod(
    val id: String,
    val name: String,
    val typeid: String,
    val type: String,
    val summa: Double,
    val data: Long,
    val schetid: String,
    val schet: String,
    val typedoxod_open: Boolean,
    val schet_open: Boolean,
) : Id_class(id_main = id), Parcelable {

    fun mayChange(): String? = if (this.typedoxod_open && this.schet_open) { // && item.schpl_open
        null
    } else {
        var str = ""
        if (!this.typedoxod_open) str += "Тип дохода используемый в данной операции уже закрыт, поэтому изменение|удаление невозможно.\n\n"
        if (!this.schet_open) str += "Счет используемый в данной операции уже закрыт, поэтому изменение|удаление невозможно.\n\n"
        str
    }
}


