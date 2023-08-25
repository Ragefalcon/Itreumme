package MainTabs.Time.Elements


import MainTabs.Time.Items.ComItemHistoryPlan
import MyDialog.MyDialogLayout
import MyList
import adapters.MyComboBox
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB
import java.util.*

fun PanViewPlanHistory(
    dialPan: MyDialogLayout,
    item: ItemPlan
) {
    PanViewPlanHistory(dialPan, item.id.toLong(), item.name, false)
}

fun PanViewPlanHistory(
    dialPan: MyDialogLayout,
    item: ItemPlanStap
) {
    PanViewPlanHistory(dialPan, item.id.toLong(), item.name, true)
}

private enum class HistoryPlanTabsEnum(override val nameTab: String) : tabElement {
    Spisok("Список"),
    Diagram("Диаграмма");
}

fun PanViewPlanHistory(
    dialPan: MyDialogLayout,
    id: Long,
    name: String,
    stapId: Boolean
) {
    val dialLayInner = MyDialogLayout()
    if (!stapId) MainDB.timeFun.setPlanForHistory(id) else MainDB.timeFun.setStapPlanForHistory(id)
    val rectDiagram = DrawRectDiagram()

    val seekBar =
        EnumDiskretSeekBar(HistoryPlanTabsEnum::class, HistoryPlanTabsEnum.Spisok)

    val inYear = mutableStateOf(false)

    dialPan.dial = @Composable {
        with(MainDB.styleParam.timeParam.planTab.panHistory) {
            BackgroungPanelStyle1(
                style = SimplePlateStyleState(platePanel),
                vignette = VIGNETTE.getValue()
            ) {
                Column(
                    Modifier.padding(15.dp)
                        .heightIn(0.dp, dialPan.layHeight.value * 0.8F)
                        .fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = name,
                        style = textNameElem.getValue()
                    )
                    seekBar.show(style = seekBarHistory)
                    when (seekBar.active) {
                        HistoryPlanTabsEnum.Spisok -> {
                            (if (!stapId) MainDB.timeSpis.spisDenPlanForHistoryPlan else MainDB.timeSpis.spisDenPlanForHistoryStapPlan).let { spisHistory ->
                                spisHistory.getState().value?.groupBy { Date(it.data).format("yyyy") }
                                    ?.let { historyMap ->
                                        val comboBoxList = remember {
                                            MyComboBox(historyMap.map { it.key },
                                                nameItem = { it }, width = 60.dp
                                            )
                                        }
                                        LaunchedEffect(historyMap) {
                                            if (comboBoxList.getListItem.value != historyMap.map { it.key })
                                                comboBoxList.getListItem.value = historyMap.map { it.key }
                                        }
                                        RowVA(
                                            Modifier.padding(5.dp).fillMaxWidth(0.7f)
                                        ) {
                                            MyCheckbox(
                                                inYear,
                                                "по годам",
                                                Modifier.padding(horizontal = 15.dp),
                                                style = CheckboxStyleState(checkBoxYear)
                                            )
                                            Spacer(Modifier.weight(1f))
                                            if (inYear.value) comboBoxList.show(
                                                Modifier.padding(horizontal = 15.dp),
                                                style = ComboBoxStyleState(cb_years)
                                            )
                                        }
                                        (if (inYear.value) comboBoxList.getSelected()
                                            ?.let { historyMap[it] } else spisHistory.getState().value)?.let {
                                            MyList(
                                                it,
                                                Modifier.weight(1f).padding(vertical = 10.dp),
                                                reverse = true
                                            ) { ind, item ->
                                                ComItemHistoryPlan(item, stapId, ind == it.size - 1, dialLayInner)
                                            }
                                        }
                                    }
                            }
                        }

                        HistoryPlanTabsEnum.Diagram -> {
                            (if (!stapId) MainDB.timeSpis.spisSumHourForHistoryPlanDiag else MainDB.timeSpis.spisSumHourForHistoryStapPlanDiag).getState().value?.let {
                                rectDiagram.drawDiagram(
                                    Modifier.weight(1f).padding(15.dp),
                                    it,
                                    darkBackground = true,
                                    MyColorARGB.colorSchetItemText.toColor(),
                                    styleState = RectDiagramColorStyleState(rectDiagColor)
                                )
                            }
                        }

                        else -> {}
                    }
                    Row {
                        MyTextButtStyle1("Скрыть", myStyleTextButton = TextButtonStyleState(buttHide)) {
                            dialPan.close()
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}

