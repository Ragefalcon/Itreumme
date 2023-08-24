package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize

@Parcelize
data class ItemCommonFinOper    (
    val id: String,
    val name: String,
    val typeid: String,
    val type: String,
    val summa: Double,
    val summa2: Double,
    val data: Long,
    val schetid:String,
    val schet:String,
    val second_schet_open:Boolean,
//    val schpl_id:Long
): Id_class (id_main = id), Parcelable{

    fun mayChange():String? = if (this.second_schet_open) null else  "Второй счет используемый в данной операции уже закрыт, поэтому изменение|удаление невозможно.\n\n"
}


