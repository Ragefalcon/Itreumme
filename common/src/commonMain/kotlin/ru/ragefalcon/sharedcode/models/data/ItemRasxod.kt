package ru.ragefalcon.sharedcode.models.data


import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemRasxod    (
    val id: String,
    val name: String,
    val typeid: String,
    val type: String,
    val summa: Double,
    val data: Long,
    val schetid:String,
    val schet:String,
    val typerasxod_open:Boolean,
    val schet_open:Boolean,
    val schpl_open:Boolean,
    val schpl_id:Long
): Id_class (id_main = id), Parcelable {

    fun mayChange():String? = if (this.typerasxod_open && this.schet_open && this.schpl_open) {
        null
    } else {
        var str = ""
        if (!this.typerasxod_open) str += "Тип расхода используемый в данной операции уже закрыт, поэтому изменение|удаление невозможно.\n\n"
        if (!this.schet_open) str += "Счет используемый в данной операции уже закрыт, поэтому изменение|удаление невозможно.\n\n"
        if (!this.schpl_open) str += "Счет-план используемый в данной операции уже закрыт, поэтому изменение|удаление невозможно.\n\n"
        str
    }
}

