package MainTabs.Time.Elements

import MainTabs.Time.Items.ComItemNextActionCommon
import MainTabs.Time.Items.ComItemNextActionStap
import MainTabs.Time.Items.ComItemShablonDenPlan
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

fun PanSelectShablonDenPlan(
    dialPan: MyDialogLayout,
    listenerShablon: (ItemShablonDenPlan, Boolean, Boolean) -> Unit,
    listenerNextAction: (ItemNextAction, List<ItemComplexOpis>, Boolean, Boolean, () -> Unit) -> Unit,
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<ItemShablonDenPlan>()
    val selectionNA = SingleSelectionType<ItemNextAction>()
    val loadPovtor = mutableStateOf(MainDB.interfaceSpis.timeServiceParam.shablonCheckRepeat.getValue())
    val loadTime = mutableStateOf(MainDB.interfaceSpis.timeServiceParam.shablonCheckTime.getValue())
    val loadNameFromStap = mutableStateOf(MainDB.interfaceSpis.timeServiceParam.shablonCheckStapName.getValue())
    val loadOpis = mutableStateOf(MainDB.interfaceSpis.timeServiceParam.shablonCheckStapOpis.getValue())
    val sortShabEnable = mutableStateOf(false)

    fun loadNextAction(itemNextAction: ItemNextAction, finishListener: () -> Unit) {
        listenerNextAction(
            itemNextAction, when (itemNextAction) {
                is ItemNextActionCommon -> MainDB.complexOpisSpis.spisComplexOpisForNextActionCommon.getState().value?.get(
                    itemNextAction.common_id
                ) ?: listOf()

                is ItemNextActionStap -> MainDB.complexOpisSpis.spisComplexOpisForNextActionStap.getState().value?.get(
                    itemNextAction.stap_prpl
                ) ?: listOf()
            },
            if (itemNextAction is ItemNextActionStap) loadNameFromStap.value else true,
            if (itemNextAction is ItemNextActionStap) loadOpis.value else true,
            finishListener
        )
    }

    with(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon) {

        dialPan.dial = @Composable {
            BackgroungPanelStyle1(
                style = SimplePlateStyleState(platePanel),
                vignette = VIGNETTE.getValue()
            ) {
                Column(
                    Modifier
                        .heightIn(0.dp, dialPan.layHeight.value * 0.8F)
                        .widthIn(0.dp, dialPan.layWidth.value * 0.9F)
                        .padding(10.dp)
                ) {

                    Row(Modifier.weight(1f)) {

                        Column(
                            modifier = Modifier.padding(bottom = 5.dp).weight(1f)
                                .withSimplePlate(SimplePlateWithShadowStyleState(plateShablon)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Шаблоны", Modifier.padding(5.dp), style = textTitleShablon.getValue())
                            MyList(
                                MainDB.timeSpis.spisShablonDenPlan.getState(),
                                Modifier.weight(1f).padding(bottom = 0.dp).padding(horizontal = 5.dp),
                                darkScroll = true
                            ) { ind, itemShablonDenPlan ->
                                ComItemShablonDenPlan(
                                    itemShablonDenPlan,
                                    selection,
                                    sortShabEnable,
                                    dialLay = dialLayInner,
                                    onClick = {
                                        selectionNA.selected = null
                                    },
                                    doubleClick = {
                                        dialPan.close()
                                        listenerShablon(it, loadPovtor.value, loadTime.value)
                                    }
                                )
                            }
                            Row(
                                Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                MyCheckbox(loadPovtor, "Повторы", style = CheckboxStyleState(checkRepeat))
                                MyCheckbox(loadTime, "Время", style = CheckboxStyleState(checkTime))
                            }
                        }
                        Spacer(Modifier.width(5.dp))
                        Column(
                            Modifier.padding(bottom = 5.dp).weight(1f)
                                .withSimplePlate(SimplePlateWithShadowStyleState(plateNextAction)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Следующие действия", Modifier.padding(5.dp), style = textTitleNextAction.getValue())
                            MyList(
                                MainDB.timeSpis.spisNextAction.getState(),
                                Modifier.weight(1f).padding(bottom = 0.dp).padding(horizontal = 5.dp),
                                darkScroll = true
                            ) { ind, itemNA ->
                                when (itemNA) {
                                    is ItemNextActionCommon -> ComItemNextActionCommon(
                                        itemNA,
                                        selectionNA,
                                        sortShabEnable,
                                        {
                                            selection.selected = null
                                        },
                                        doubleClick = { itemDC, finListener ->
                                            dialPan.close()
                                            loadNextAction(itemDC, finListener)
                                        },
                                        dialL = dialLayInner
                                    )

                                    is ItemNextActionStap -> ComItemNextActionStap(
                                        itemNA,
                                        selectionNA,
                                        sortShabEnable,
                                        {
                                            selection.selected = null
                                        },
                                        doubleClick = { itemDC, finListener ->
                                            dialPan.close()
                                            loadNextAction(itemDC, finListener)
                                        },
                                        dialL = dialLayInner
                                    )
                                }
                            }
                            RowVA(Modifier.padding(5.dp)) {

                                RowVA(Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceAround) {
                                    MyCheckbox(
                                        loadNameFromStap,
                                        "Имя этапа",
                                        style = CheckboxStyleState(checkNameFromStap)
                                    )
                                    MyCheckbox(loadOpis, "Описание", style = CheckboxStyleState(checkOpis))
                                }

                                MyTextButtStyle1(
                                    "+",
                                    width = 70.dp,
                                    height = 35.dp,
                                    myStyleTextButton = TextButtonStyleState(buttAddNextAction)
                                ) {
                                    PanAddNextAction(dialLayInner)
                                }
                            }
                        }
                    }
                    Row {
                        MyToggleButtIconStyle1(
                            "ic_round_swap_vert_24.xml",
                            twoIcon = false,
                            value = sortShabEnable,
                            sizeIcon = 35.dp,
                            modifier = Modifier.height(35.dp).padding(start = 15.dp),
                            width = 35.dp,
                            myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.buttSort)
                        )
                        Spacer(Modifier.weight(1F))
                        MyTextButtStyle1("Отмена", myStyleTextButton = TextButtonStyleState(buttCancel)) {
                            dialPan.close()
                        }
                        Spacer(Modifier.weight(1F))
                        selection.selected?.also { itemShab ->
                            MyTextButtStyle1(
                                "Загрузить",
                                Modifier.padding(start = 5.dp),
                                myStyleTextButton = TextButtonStyleState(buttLoadShablon)
                            ) {
                                dialPan.close()
                                listenerShablon(itemShab, loadPovtor.value, loadTime.value)
                            }
                            Spacer(Modifier.weight(1F))
                        }
                        selectionNA.selected?.also { itemNextAction ->
                            MyTextButtStyle1(
                                "Загрузить",
                                Modifier.padding(start = 5.dp),
                                myStyleTextButton = TextButtonStyleState(buttLoadShablon)
                            ) {
                                dialPan.close()
                                loadNextAction(itemNextAction) {}
                            }
                            Spacer(Modifier.weight(1F))
                        }
                    }
                }
            }
            dialLayInner.getLay()
        }
    }

    dialPan.show()
}


