package MyDialog

import MainTabs.Journal.Element.PanAddIdeaStapFromVxod
import MainTabs.Journal.Items.ComItemBloknot
import MainTabs.Time.Elements.PanAddDenPlan
import MainTabs.Time.Elements.PanAddPlan
import MainTabs.Time.Elements.PanAddPlanStap
import MainTabs.Time.Items.ComItemVxodPlate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import common.PanSelectOneItem
import common.tests.PanDeleteItem
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.extensions.unOffset
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerFinishTriggerEnum
import viewmodel.MainDB
import viewmodel.StateVM
import java.util.*

sealed class InnerFinishAction(private var autoClear: Boolean) {

    @Composable
    open fun objectForAction() {
    }

    fun finishAction(type: InnerFinishTriggerEnum, dialPan: MyDialogLayout? = null): Unit {
        println("finishActionPriv")
        println(type)
        println("dialPan: $dialPan")
        if (type != InnerFinishTriggerEnum.Cancel) finishActionPriv(type, dialPan) else {
            StateVM.innerFinishAction.value = null
        }
        if (autoClear) StateVM.innerFinishAction.value = null
        MainDB.addQuest.completeInnerFinishTriggerAction()
    }

    protected abstract fun finishActionPriv(type: InnerFinishTriggerEnum, dialPan: MyDialogLayout? = null): Unit
}

class InnerFinishBirthdayAction() : InnerFinishAction(true) {
    override fun finishActionPriv(type: InnerFinishTriggerEnum, dialPan: MyDialogLayout?) {
        when (type) {
            InnerFinishTriggerEnum.AddBirthday -> {
                println("InnerFinishTriggerEnum.AddBirthday_0")
                dialPan?.let {
                    println("InnerFinishTriggerEnum.AddBirthday_1")
                    val dateBirthday =
                        MainDB.avatarSpis.spisMainParam.getState().value?.find { it.name == "Birthday" }?.stringparam?.toLong()
                            ?.run { Date(this) } ?: Date()
                    println("InnerFinishTriggerEnum.AddBirthday_2")
                    myDatePicker(dialPan, mutableStateOf(dateBirthday), {
                        if (MainDB.addAvatar.checkEmptyBirthday()) MainDB.addAvatar.addBirthday(Date().time)
                    }) {
                        MainDB.addAvatar.addBirthday(it.time)
                    }
                }
            }
            InnerFinishTriggerEnum.ScipAddBirthday -> {
                dialPan?.let {
                    if (MainDB.addAvatar.checkEmptyBirthday()) MainDB.addAvatar.addBirthday(Date().time)
                }
            }
            else -> {}
        }
    }
}

class InnerFinishVxodAction(val item: ItemVxod) : InnerFinishAction(false) {

    @Composable
    override fun objectForAction() {
        ComItemVxodPlate(item)
    }

    fun deleteVxod(){
        MainDB.addTime.delVxod(item.id.toLong()){
            MainDB.complexOpisSpis.spisComplexOpisForVxod.delAllImageForItem(it)
        }
    }

    override fun finishActionPriv(type: InnerFinishTriggerEnum, dialPan: MyDialogLayout?) {
        when (type) {
//            InnerFinishTriggerEnum.AddBirthday -> TODO()
            InnerFinishTriggerEnum.DeleteVxod -> dialPan?.let {
                PanDeleteItem(it, item = {
                    ComItemVxodPlate(item)
                }, cancelListener = { StateVM.innerFinishAction.value = null }
                ) {
                    deleteVxod()
                    StateVM.innerFinishAction.value = null
                }
            }

            InnerFinishTriggerEnum.VxodToPlan -> dialPan?.let {
                PanAddPlan(
                    dialPan,
                    ItemPlan(
                        "-25",
                        item.name,
                        if (item.stat > 0) item.stat - 1 else item.stat,
                        0.0,
                        0L.unOffset(),
                        1L,
                        item.opis,
                        TypeStatPlan.VISIB,
                        0.0,
                        0,
                        0,
                        false,
                        0L
                    ),
                    cancelListener = { StateVM.innerFinishAction.value = null },
                    listOp = MainDB.complexOpisSpis.spisComplexOpisForVxod.getState().value?.get(item.id.toLong())
                        ?.clearSourceList(TableNameForComplexOpis.spisPlan)
                ) {
                    deleteVxod()
                    StateVM.innerFinishAction.value = null
                }
            }

            InnerFinishTriggerEnum.VxodToStapPlan -> dialPan?.let {
                PanAddPlanStap(
                    dialPan,
                    item = ItemPlanStap(
                        0L,
                        "-25",
                        "-1",
                        item.name,
                        0.0,
                        0L.unOffset(),
                        1L,
                        item.opis,
                        false,
                        TypeStatPlanStap.VISIB,
                        0.0,
                        0,
                        0L,
                        "",
                        0L
                    ),
                    cancelListener = { StateVM.innerFinishAction.value = null },
                    listOp = MainDB.complexOpisSpis.spisComplexOpisForVxod.getState().value?.get(item.id.toLong())
                        ?.clearSourceList(TableNameForComplexOpis.spisPlanStap)
                ) {
                    deleteVxod()
                    StateVM.innerFinishAction.value = null
                }
            }

            InnerFinishTriggerEnum.VxodToIdea -> dialPan?.let {
                PanSelectOneItem(
                    it, MainDB.journalSpis.spisBloknot,
                    listener = { itemBloknot ->
                        PanAddIdeaStapFromVxod(
                            dialPan, item, itemBloknot,
                            cancelListener = { StateVM.innerFinishAction.value = null },
                            listOp = MainDB.complexOpisSpis.spisComplexOpisForVxod.getState().value?.get(item.id.toLong())
                                ?.clearSourceList(TableNameForComplexOpis.spisIdeaStap)
                        ) {
                            deleteVxod()
                            StateVM.innerFinishAction.value = null
                        }
                    },
                    cancelListener = { StateVM.innerFinishAction.value = null },
                    stylePanelItem = MainDB.styleParam.finParam.doxodParam.panAddDoxod.plateShablon,
                ) { item, selection, dialL, listener ->
                    MainDB.styleParam.journalParam.itemBloknot.getComposable(::ItemBloknotStyleState) { itemBloknotStyle ->
                        ComItemBloknot(
                            item,
                            selection,
                            false,
                            {},
                            itemBloknotStyleState = itemBloknotStyle
                        )
                    }
                }
            }

            InnerFinishTriggerEnum.VxodToDenPlan -> dialPan?.let {
                PanAddDenPlan(it, ItemDenPlan(
                    "-25",
                    item.name,
                    Date().format("HH:mm:ss"),
                    Date().add(2, TimeUnits.HOUR).format("HH:mm:ss"),
                    0.0,
                    if (item.stat > 0) item.stat - 1 else item.stat,
                    0.0,
                    Date().time,
                    item.opis,
                    -1L,
                    -1L,
                    "",
                    ""
                ),
                    cancelListener = { StateVM.innerFinishAction.value = null },
                    listOp = MainDB.complexOpisSpis.spisComplexOpisForVxod.getState().value?.get(item.id.toLong())
                        ?.clearSourceList(TableNameForComplexOpis.spisDenPlan)
                ) {
                    deleteVxod()
                    StateVM.innerFinishAction.value = null
                }
            }

            else -> {
                StateVM.innerFinishAction.value = null
            }
        }
    }

}