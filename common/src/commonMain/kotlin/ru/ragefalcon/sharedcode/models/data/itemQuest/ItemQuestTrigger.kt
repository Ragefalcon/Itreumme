package ru.ragefalcon.sharedcode.models.data.itemQuest

import ru.ragefalcon.sharedcode.extensions.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class


    @Parcelize
    class ItemQuestTrigger (
        val id: String,
        val quest_id: String,
        val parent_type: String,
        val parent_id: Long,
        val type_id: Long,
        val child_id: Long,
        var child_name: String,
        val act_code: Long,

        ): Id_class(id_main = id)
    {
    }