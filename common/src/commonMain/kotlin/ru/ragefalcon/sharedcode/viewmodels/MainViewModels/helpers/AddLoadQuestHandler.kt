package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.models.data.ItemMainParam
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemOtvetDialogQuest
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.source.disk.FileMP
import ru.ragefalcon.sharedcode.source.disk.getPathWithSeparator
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.QUEST_ID_INNER_DIALOG
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.LoadQuestVMobjForSpis

class AddLoadQuestHandler(
    private var mdb: Database,
    private val spis: LoadQuestVMobjForSpis,
    private val commonFun: PrivateCommonFun,
    private val dbArgs: DbArgs
) {

    fun getQuestMainParam(id: Long): List<ItemMainParam> {
        return mdb.questMainparamQueries.getQuestParam(id).executeAsList().map {
            ItemMainParam(
                it._id.toString(),
                it.name,
                it.intparam,
                it.stringparam
            )
        }
    }

    fun completeInnerFinishTriggerAction() {
        spis.innerFinishTriggerAction.setValue(null)
    }

    fun completeDialogEvent() = spis.completeDialogEvent()
    fun completeDialogEvent(otvetDialog: ItemOtvetDialogQuest) {
        val date = spis.dialogEvent.getValue()?.let { it.datetime - 1 }
        commonFun.runTriggerReact(
            quest_id = otvetDialog.quest_id.toString(),
            TypeParentOfTrig.OTVDIALOG,
            otvetDialog.key_id,
            date
        )
        spis.completeDialogEvent()
    }

    private fun addQuest(
        name: String,
        dateopen: Long,
    ) {
        mdb.spisQuestsQueries.insertOrReplace(
            name = name,
            dateopen = dateopen,
            complete = 0
        )
    }

    private fun addQuestWithId(
        id: Long,
        name: String,
        dateopen: Long,
    ) {
        mdb.spisQuestsQueries.insertOrReplaceWithId(
            _id = id,
            name = name,
            dateopen = dateopen,
            complete = 0
        )
    }

    fun getOtvetDial(quest_id: Long, key_id: Long): List<ItemOtvetDialogQuest> {
        return mdb.spisOtvetDialogQuestQueries.selectForDialog(quest_id, key_id).executeAsList().map {
            ItemOtvetDialogQuest(
                id = it._id.toString(),
                quest_id = it.quest_id,
                key_id = it.key_id,
                dialog_id = it.dialog_id,
                text = it.text,
                order_number = it.order_number
            )
        }
    }

    fun addDialogInLine(
        type_message: String,
        subtype_id: Long,
        key_id: Long,
        name: String,
        datetime: Long,
        minimaze: Long
    ) {
        mdb.dialogLineQueries.insertOrReplace(
            type_message,
            subtype_id,
            key_id,
            name,
            datetime,
            minimaze
        )
    }

    fun deleteDialogLine(id: String) {
        mdb.dialogLineQueries.delete(id.toLong())
    }

    fun minimazeDialogLine(id: String, minimaze: Long) {
        mdb.dialogLineQueries.setMinimaze(minimaze, id.toLong())
    }

    fun deleteQuest(id: String) {
        mdb.spisQuestsQueries.deleteQuest(id.toLong())
    }

    fun deleteFullQuest(id: String, dirLoadedFiles: String, dirIconFiles: String) {
        FileMP.deleteDirectory(getPathWithSeparator(listOf(dirLoadedFiles, "Quest_$id")))
        mdb.spisIconNodeTreeSkillsQueries.selectQuest(id.toLong()).executeAsList().let {
            it.forEach { icon ->
                FileMP.deleteFile(getPathWithSeparator(listOf(dirIconFiles, "icon_${icon._id}.${icon.extension}")))
            }
        }
        mdb.spisQuestsQueries.deleteFullQuest(
            id.toLong()
        )
    }


    fun loadCheckInnerDialog() = mdb.spisQuestsQueries.checkInnerQuest(QUEST_ID_INNER_DIALOG).executeAsOne() > 0
}