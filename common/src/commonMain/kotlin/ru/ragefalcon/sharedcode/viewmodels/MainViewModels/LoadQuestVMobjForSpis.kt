package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.unOffset
import ru.ragefalcon.sharedcode.models.data.ItemDialogLine
import ru.ragefalcon.sharedcode.models.data.ItemLoadQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemDialogQuestEvent
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemOtvetDialogQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemQuestTrigger
import ru.ragefalcon.sharedcode.quest.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerFinishTriggerEnum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.SpisQueryForListener
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeDialogMessage
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniConvertQueryAdapter

class LoadQuestVMobjForSpis(private val mDB: Database, private val spisQueryListener: SpisQueryForListener) {

    private var dialogEventValue: ItemDialogLine? = null
    private var updateDialogLine: ((ItemDialogLine?) -> Unit)? = null

    private fun callDialogEvent() {
        updateDialogLine?.invoke(dialogEventValue)
    }

    private fun setCurrentDialogEvent(newDialog: ItemDialogLine?) {
        dialogEventValue = newDialog
        newDialog?.let {
            if (it.type_message == TypeDialogMessage.QUESTDIALOG.code) {
//                val quest_id = newDialog?.subtype_id ?: -1
//                val dialog_id = newDialog?.key_id ?: -1
                spisOtvetDialogForEvent.updateQuery(
                    mDB.spisOtvetDialogQuestQueries.selectForDialog(
                        newDialog.subtype_id,
                        newDialog.key_id
                    )
                )
                dialogForEvent.updateQuery(
                    mDB.spisDialogQuestQueries.selectDialogEvent(
                        newDialog.subtype_id,
                        newDialog.key_id
                    )
                )
            }
        }
        callDialogEvent()
    }

    private fun setCallEventFun(ff: (ItemDialogLine?) -> Unit) {
        updateDialogLine = ff
        callDialogEvent()
    }

    var dialogEvent = MyObserveObj<ItemDialogLine?> { ff ->
        setCallEventFun(ff)
    }

    fun completeDialogEvent() {
        val aa = dialogEventValue
        aa?.let {
            dialogEventValue = null
            mDB.dialogLineQueries.delete(it.id.toLong())
        }
    }


    val spisOtvetDialogForEvent = UniConvertQueryAdapter<Spis_otvet_dialog_quest, ItemOtvetDialogQuest>() {
        ItemOtvetDialogQuest(
            id = it._id.toString(),
            quest_id = it.quest_id,
            key_id = it.key_id,
            dialog_id = it.dialog_id,
            text = it.text,
            order_number = it.order_number
        )
    }.apply {
        updateQuery(mDB.spisOtvetDialogQuestQueries.selectForDialog(-1, -1))
    }

    val dialogForEvent = UniConvertQueryAdapter<SelectDialogEvent, ItemDialogQuestEvent>() {
        ItemDialogQuestEvent(
            id = it._id.toString(),
            quest_id = it.quest_id,
            key_id = it.key_id,
            name = it.name,
            maintext = it.maintext,
            govorun_name = it.govorun_name,
            govorun_id = it.govorun_id,
            quest_name = it.quest_name,
            image_govorun = it.image_govorun
        )
    }.apply {
        updateQuery(mDB.spisDialogQuestQueries.selectDialogEvent(-1, -1))
    }

    val spisDialogActive = UniConvertQueryAdapter<Dialog_line, ItemDialogLine>() {
        ItemDialogLine(
            id = it._id.toString(),
            type_message = it.type_message,
            subtype_id = it.subtype_id,
            key_id = it.key_id,
            name = it.name,
            datetime = it.datetime.unOffset(),
            minimaze = it.minimaze
        )
    }.apply {
        updateQuery(mDB.dialogLineQueries.selectActiv())
    }

    init {
        spisDialogActive.getMyObserveObj().getObserve {
            dialogEventValue ?: run {
                setCurrentDialogEvent(it?.firstOrNull())
            }
        }
    }

    val spisDialogMinimaze = UniConvertQueryAdapter<Dialog_line, ItemDialogLine>() {
        ItemDialogLine(
            id = it._id.toString(),
            type_message = it.type_message,
            subtype_id = it.subtype_id,
            key_id = it.key_id,
            name = it.name,
            datetime = it.datetime.unOffset(),
            minimaze = it.minimaze
        )
    }.apply {
        updateQuery(mDB.dialogLineQueries.selectMinimaze())
    }


    val spisQuest = UniConvertQueryAdapter<Spis_quest, ItemLoadQuest>() {
        ItemLoadQuest(
            id = it._id.toString(),
            name = it.name,
            dateopen = it.dateopen.unOffset(),
            complete = it.complete
        )
    }.apply {
        mDB.spisQuestsQueries.selectOpenQuest().let {
            spisQueryListener.spisQuest = it
            updateQuery(it)
        }
    }


    val spisTrigger = UniConvertQueryAdapter<Quest_common_trigger, ItemQuestTrigger>() {
        ItemQuestTrigger(
            id = it._id.toString(),
            quest_id = it.quest_id,
            parent_type = it.parent_type_element,
            parent_id = it.parent_element_id,
            type_id = it.type_trig_id,
            child_id = it.child_id,
            child_name = it.child_name,
            act_code = it.act_code,
        )
    }.apply {
        this.updateQuery(mDB.questCommonTriggerQueries.selectAll())
    }


    var innerFinishTriggerAction = CommonObserveObj<InnerFinishTriggerEnum?>()

}