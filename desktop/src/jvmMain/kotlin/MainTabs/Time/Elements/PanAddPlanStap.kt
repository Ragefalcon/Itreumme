package MainTabs.Time.Elements

import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB
import java.util.*

fun PanAddPlanStap(
    dialPan: MyDialogLayout,
//    MainDatabase: MainDatabase,
    itemPlanStapParent: ItemPlanStap? = null,
    itemPlanParent: ItemPlan? = null,
    item: ItemPlanStap? = null,
    cancelListener: () -> Unit = {},
    listOp: List<ItemComplexOpis>? = null,
    finishListener: () -> Unit = {}
) {

    with(MainDB.styleParam.timeParam.planTab.panAddPlanStap) {
        val change = item?.let { item.id.toLong() > 0 } ?: false
        val dialLayInner = MyDialogLayout()
        val dateStart =
            mutableStateOf(item?.let { if (it.data1 > 1L && it.data2 > 1L) Date(it.data1) else null } ?: Date())
        val dateEnd =
            mutableStateOf(item?.let { if (it.data1 > 1L && it.data2 > 1L) Date(it.data2) else null } ?: Date().add(
                14,
                TimeUnits.DAY
            ))
        val expandedDate = mutableStateOf((item?.data1?.withOffset() ?: 0L) != 0L)

        val marker = MySelectStat(
            item?.marker ?: 0L, statNabor = MySelectStat.statNabor3, iconRes = "bookmark_01.svg", dropdownMenuStyle
        )
        val enabledGotov =
            mutableStateOf(item?.gotov?.let { it >= 0.0 } ?: MainDB.interfaceSpis.DefaultPercentForPlan.getValue())
//    val enabledGotov = mutableStateOf((item?.gotov ?: -1.0) >= 0.0)

        val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
        val complexOpis =
            MyComplexOpisWithNameBox(
                "Название этапа",
                text_name,
                "Описание этапа",
                item?.id?.toLong() ?: -1,
                TableNameForComplexOpis.spisPlanStap,
                MainDB.styleParam.timeParam.planTab.complexOpisForPlanStap,
                listOp?.toMutableStateList() ?: item?.let {
                    MainDB.complexOpisSpis.spisComplexOpisForStapPlan.getState().value?.get(it.id.toLong())
                        ?.toMutableStateList()
                },
                styleName = textName,
                leftNameComposable = { marker.show() }
            )

        val selParents = BoxSelectParentPlan(
            itemPlanParent == null,
            arrayIskl = item?.let { listOf(it.id.toLong()) } ?: listOf(),
            label = "Выберете родительский проект/этап").apply {
            selectionPlanParent.selected = itemPlanParent
            selectionPlanStapParent.selected = itemPlanStapParent ?: item?.let { currItem ->
                MainDB.timeSpis.spisPlanStap.getState().find {
                    it.id == currItem.parent_id
                }
            }
        }

        itemPlanParent?.let { planParent ->
            MainDB.timeFun.setPlanForSpisStapPlanForSelect(
                planParent.id.toLong(),
                item?.id?.toLong()?.let { listOf(it) } ?: listOf())
        }

        dialPan.dial = @Composable {
            BackgroungPanelStyle1(
                style = SimplePlateStyleState(platePanel),
                vignette = VIGNETTE.getValue()
            ) {
                Column(
                    Modifier.padding(15.dp).fillMaxWidth(0.8F).fillMaxHeight(0.8f), // dialPan.layHeight.value
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    selParents.getComposable(Modifier.fillMaxWidth(0.8F).padding(bottom = 5.dp))
                    if (!selParents.isExpanded()) {
                        complexOpis.show(this, dialLayInner)
                        RowVA(Modifier.padding(bottom = 5.dp)) {
//                        marker.show()
                            MyToggleButtIconStyle1(
                                "ic_round_percent_24.xml", value = enabledGotov, sizeIcon = 25.dp,
                                modifier = Modifier.padding(horizontal = 5.dp),
                                myStyleToggleButton = ToggleButtonStyleState(buttGotov)
                            )
                            MyToggleButtIconStyle1(
                                "ic_round_access_time_24.xml", value = expandedDate, sizeIcon = 25.dp,
                                modifier = Modifier.padding(horizontal = 5.dp),
                                myStyleToggleButton = ToggleButtonStyleState(buttSrok)
                            )
                        }
                        Column(Modifier.animateContentSize()) {
                            if (expandedDate.value) {
                                buttDatePickerWithButton(
                                    dialLayInner, dateStart,
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    myStyleTextDate = TextButtonStyleState(buttDateStart),
                                    myStyleTextArrow = TextButtonStyleState(buttDateArrowStart)
                                ) {
                                    if (it > dateEnd.value) dateEnd.value = it
                                }
                                buttDatePickerWithButton(
                                    dialLayInner, dateEnd,
                                    modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 5.dp),
                                    myStyleTextDate = TextButtonStyleState(buttDateEnd),
                                    myStyleTextArrow = TextButtonStyleState(buttArrowDateEnd)
                                ) {
                                    if (it < dateStart.value) dateStart.value = it
                                }
                            }
                        }
                        Row {
                            MyTextButtStyle1(
                                "Отмена",
                                myStyleTextButton = TextButtonStyleState(buttCancel)
                            ) {
                                cancelListener()
                                dialPan.close()
                            }
                            if (selParents.selectionPlanParent.selected != null) MyTextButtStyle1(
                                if (change) "Изменить" else "Добавить",
                                Modifier.padding(start = 5.dp),
                                myStyleTextButton = TextButtonStyleState(buttAdd)
                            ) {
                                val idplan = selParents.selectionPlanParent.selected?.id?.toLong()
                                    ?: itemPlanParent?.id?.toLong() ?: -1L
                                val idquest = selParents.selectionPlanParent.selected?.quest_id
                                    ?: itemPlanParent?.quest_id ?: 0L
                                if (idplan > 0 && text_name.value.text != "") {
                                    complexOpis.listOpis.addUpdList { opis ->
                                        return@addUpdList if (change) {
                                            item?.let {
                                                if (enabledGotov.value) {
                                                    if (item.gotov < 0) MainDB.addTime.updGotovPlanStap(
                                                        it.id.toLong(),
                                                        0.0
                                                    )
                                                } else {
                                                    if (item.gotov >= 0) MainDB.addTime.updGotovPlanStap(
                                                        it.id.toLong(),
                                                        -1.0
                                                    )
                                                }
                                                MainDB.addTime.updPlanStap(
                                                    id = it.id.toLong(),
                                                    name = text_name.value.text,
                                                    data1 = if (expandedDate.value) dateStart.value.time else 0,
                                                    data2 = if (expandedDate.value) dateEnd.value.time else 1,
                                                    opis = opis,//text_opis.value.text,
                                                    parent_id = selParents.selectionPlanStapParent.selected?.id?.toLong()
                                                        ?: -1L,
                                                    idplan = idplan,
                                                    marker = marker.value
                                                )
                                            } ?: listOf()
                                        } else {
                                            MainDB.addTime.addStapPlan(
                                                name = text_name.value.text,
                                                gotov = if (enabledGotov.value) 0.0 else -1.0,
                                                data1 = if (expandedDate.value) dateStart.value.time else 0,
                                                data2 = if (expandedDate.value) dateEnd.value.time else 1,
                                                opis = opis,//text_opis.value.text,
                                                parent_id = selParents.selectionPlanStapParent.selected?.id?.toLong()
                                                    ?: -1L,
                                                idplan = idplan,
                                                stat = TypeStatPlanStap.VISIB,
                                                svernut = "false",
                                                marker = marker.value,
                                                quest_id = idquest
                                            )
                                        }
                                    }
                                    finishListener()
                                    dialPan.close()
                                }
                            }
                        }
                    }
                }
            }
            dialLayInner.getLay()
        }
    }
    dialPan.show()
}

