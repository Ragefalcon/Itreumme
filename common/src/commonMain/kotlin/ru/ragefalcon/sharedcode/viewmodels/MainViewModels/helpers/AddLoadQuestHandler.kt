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

    fun completeInnerFinishTriggerAction(){
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
//        mdb.spisGovorunQuestQueries.selectOneQuest(id.toLong()).executeAsList().let { list ->
                FileMP.deleteDirectory(getPathWithSeparator(listOf(dirLoadedFiles,"Quest_$id")))
        mdb.spisIconNodeTreeSkillsQueries.selectQuest(id.toLong()).executeAsList().let {
            it.forEach { icon ->
                FileMP.deleteFile(getPathWithSeparator(listOf(dirIconFiles,"icon_${icon._id}.${icon.extension}")))
            }
        }
//            list.forEach { govorun ->
//                FileMP.deleteFile("${FileMP.getSystemDir()}${FileMP.getFileSeparator()}LoadQuestFiles${FileMP.getFileSeparator()}${govorun.image_file}")
//            }
//        }
        mdb.spisQuestsQueries.deleteFullQuest(
            id.toLong()
//            type_plan_element_code = TypeQuestElement.PLAN.code,
//            type_stap_plan_element_code = TypeQuestElement.PLANSTAP.code
        )
    }


/*
    private fun copyQuestBodyToMainDB(questDB: ObserveQuestBaseVM, innerDial: Boolean = false): Long {
        var quest_id = -1L
        questDB.questObjForSpis.spisMainParam.oneZapros {
            it.find { it.name == "name" }?.let {
                quest_id = mdb.spisQuestsQueries.transactionWithResult<Long> {
                    if (innerDial){
                        addQuestWithId(QUEST_ID_INNER_DIALOG,it.stringparam, DateTimeTz.nowLocal().minusTime().localUnix())
                    }   else    {
                        addQuest(it.stringparam, DateTimeTz.nowLocal().minusTime().localUnix())
                    }
                    mdb.spisQuestsQueries.lastInsertRowId().executeAsOne()
                }
            }
            if (quest_id != -1L) {
                it.filter { it.name != "name" }.let {
                    for (opis in it) {
                        mdb.questMainparamQueries.insertOrReplacePlan(
                            quest_id,
                            opis.name,
                            opis.intparam,
                            opis.stringparam
                        )
                    }
                }
            }
        }
        return quest_id
    }

    private fun copyGovorunAndAvaFilesToMainDB(questDB: ObserveQuestBaseVM, quest_id: Long, nameQuest: String, dirQuest: String, dirLoadedFiles: String) {
        questDB.questObjForSpis.spisGovorun.oneZapros { listGovorun ->
            listGovorun.forEach { govorun ->
                if (govorun.image_file != "") {
                    val ava = FileMP()
//                    ava.openFileInput("${FileMP.getSystemDir()}${FileMP.getFileSeparator()}Quests${FileMP.getFileSeparator()}${govorun.image_file}.jpg")
//                    ava.openFileInput(getPathWithSeparator(listOf(FileMP.getSystemDir(),"Quests","${govorun.image_file}.jpg")))
                    println("testPath: $dirQuest, ${govorun.image_file}.jpg")
                    println("testPath: ${getPathWithSeparator(listOf(dirQuest,"${govorun.image_file}.jpg"))}")
                    ava.openFileInput(getPathWithSeparator(listOf(dirQuest,"${govorun.image_file}.jpg")))
                    ava.getFileStream()?.let { byteArray ->
                        val loadAva = FileMP()
//                        loadAva.openFileOutput("${FileMP.getSystemDir()}${FileMP.getFileSeparator()}LoadQuestFiles${FileMP.getFileSeparator()}Quest_${quest_id}_gid_${govorun.id}.jpg")
                        loadAva.openFileOutput(getPathWithSeparator(listOf(dirLoadedFiles,"Quest_${quest_id}","Quest_${quest_id}_gid_${govorun.id}.jpg")))
                        loadAva.writeFile(byteArray)
                        loadAva.closeFile()
                    }
                    ava.closeFile()
                }
                mdb.spisGovorunQuestQueries.insertOrReplace(
                    quest_id,
                    govorun.id.toLong(),
                    govorun.name,
                    govorun.opis,
                    "Quest_${quest_id}_gid_${govorun.id}.jpg"
                )
            }
        }
    }

    private fun copyDialogAndOtvetToMainDB(questDB: ObserveQuestBaseVM, quest_id: Long) {
        questDB.questObjForSpis.spisDialog.oneZapros { listDial ->
            listDial.forEach { dial ->
                mdb.spisDialogQuestQueries.insertOrReplace(
                    quest_id,
                    dial.id.toLong(),
                    dial.name,
                    dial.maintext,
                    dial.govorun_name,
                    dial.govorun_id
                )
            }
        }
        questDB.questObjForSpis.spisOtvetDialog.oneZapros { listOtvetDial ->
            listOtvetDial.forEach { otvDial ->
                mdb.spisOtvetDialogQuestQueries.insertOrReplace(
                    quest_id,
                    otvDial.id.toLong(),
                    otvDial.dialog_id,
                    otvDial.text,
                    otvDial.order_number
                )
            }
        }
    }

    private fun copyPlanAndStapAndTriggerToMainDB(questDB: ObserveQuestBaseVM, quest_id: Long) {
        questDB.questObjForSpis.spisStapPlan.oneZapros { listStap ->
            questDB.questObjForSpis.spisPlan.oneZapros { listPlan ->
                var plan_id = -1L
                var stap_id = -1L
                val stapId_oldnew = mutableMapOf<String, Long>()
                val planId_oldnew = mutableMapOf<String, Long>()
                for (plan in listPlan) {
                    plan_id = mdb.spisPlanQueries.transactionWithResult<Long> {
                        mdb.spisPlanQueries.insertOrReplacePlan(
                            plan.vajn,
                            plan.name,
                            0.0,
                            0,
                            1,
                            plan.opis,
                            plan.commstat
                        )
                        mdb.spisQuestsQueries.lastInsertRowId().executeAsOne()
                    }
                    if (plan_id != -1L) {
                        planId_oldnew[plan.id] = plan_id
                        mdb.spisQuestElementQueries.insertOrReplacePlan(quest_id, plan_id, TypeQuestElement.PLAN.code)
                        for (stap in listStap.filter { it.idplan == plan.id.toLong() }) {
                            stap_id = mdb.spisStapPlanQueries.transactionWithResult<Long> {
                                mdb.spisStapPlanQueries.insertOrReplaceStapPlan(
                                    stap.parent_id.toLong(),
                                    stap.name,
                                    0.0,
                                    0,
                                    1,
                                    stap.opis,
                                    stap.commstat,
                                   stap.svernut.toString(),
                                    plan_id
                                )
                                mdb.spisQuestsQueries.lastInsertRowId().executeAsOne()
                            }
                            if (stap_id != -1L) {
                                stapId_oldnew[stap.id] = stap_id
                                mdb.spisQuestElementQueries.insertOrReplacePlan(
                                    quest_id,
                                    stap_id,
                                    TypeQuestElement.PLANSTAP.code
                                )
                            }
                        }
                    }
                }
                mdb.spisStapPlanQueries.transaction {
                    for (stap in listStap) {
                        stapId_oldnew[stap.id]?.let { idstap ->
                            stapId_oldnew[stap.parent_id]?.let { parentid ->
                                mdb.spisStapPlanQueries.updateParentPlanStap(parentid, idstap)
                            }
                        }
                    }
                }
                questDB.questObjForSpis.spisTrigger.oneZapros { listTrig ->
                    mdb.questCommonTriggerQueries.transaction {
                        listTrig.forEach { trig ->
                            when (trig.parent_type) {
                                TypeParentOfTrig.PLAN.code -> planId_oldnew[trig.parent_id.toString()]
                                TypeParentOfTrig.PLANSTAP.code -> stapId_oldnew[trig.parent_id.toString()]
                                else -> trig.parent_id
                            }?.let { newParId ->
                                when (trig.type_id) {
                                    TypeStartObjOfTrigger.STARTPLAN.id -> planId_oldnew[trig.child_id.toString()]
                                    TypeStartObjOfTrigger.STARTSTAP.id -> stapId_oldnew[trig.child_id.toString()]
                                    else -> trig.child_id
                                }?.let { newChildId ->
//                                        if (newParId != trig.parent_id || newChildId != trig.child_id)
                                    mdb.questCommonTriggerQueries.insertOrReplace(
                                        quest_id.toString(),
                                        trig.parent_type,
                                        newParId,
                                        trig.type_id,
                                        newChildId,
                                        trig.child_name,
                                        trig.act_code
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
*/

/*
    fun loadQuest(questDB: ObserveQuestBaseVM, nameQuest: String, dirQuest: String, dirLoadedFiles: String, listener: () -> Unit): Long {

        val quest_id = copyQuestBodyToMainDB(questDB)
        if (quest_id != -1L) {
            copyPlanAndStapAndTriggerToMainDB(questDB, quest_id)
            copyDialogAndOtvetToMainDB(questDB, quest_id)
            copyGovorunAndAvaFilesToMainDB(questDB, quest_id, nameQuest, dirQuest, dirLoadedFiles)
        }
        commonFun.runTriggerReact(quest_id.toString(), TypeParentOfTrig.STARTQUESTDIALOG,-25)
        return quest_id
    }
*/
    fun loadCheckInnerDialog()  = mdb.spisQuestsQueries.checkInnerQuest(QUEST_ID_INNER_DIALOG).executeAsOne() > 0
/*
    fun loadInnerDialog(questDB: ObserveQuestBaseVM, nameQuest: String, dirQuest: String, dirLoadedFiles: String, overwrite: Boolean, listener: () -> Unit) {
        val check = mdb.spisQuestsQueries.checkInnerQuest(QUEST_ID_INNER_DIALOG).executeAsOne() > 0
        if (overwrite || !check) {
            if (check) deleteFullQuest(QUEST_ID_INNER_DIALOG.toString(), dirLoadedFiles)
            val quest_id = copyQuestBodyToMainDB(questDB, true)
//        val quest_id = QUEST_ID_INNER_DIALOG
            if (quest_id != -1L) {
                copyPlanAndStapAndTriggerToMainDB(questDB, quest_id)
                copyDialogAndOtvetToMainDB(questDB, quest_id)
                copyGovorunAndAvaFilesToMainDB(questDB, quest_id, nameQuest, dirQuest, dirLoadedFiles)
            }
        }
    }
*/
}