package MainTabs.Time.Elements

import MainTabs.Time.Items.ComItemTimeline
import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import MyList
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.models.data.ItemSrokPlanAndStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB
import java.util.*

enum class TimelinePanEnum(override val nameTab: String) : tabElement {
    Nears("Ближайшие"),
    ForPlans("Для проекта");
}

fun panTimeline(
    dialPan: MyDialogLayout
) {
    val dialLayInner = MyDialogLayout()

    val selection = SingleSelectionType<ItemSrokPlanAndStap>()
    val dateCurr = mutableStateOf(Date())
    val year = mutableStateOf(false)

    val boxSelectPlan = BoxSelectParentPlan(onlyPlan = true).apply {
        MainDB.timeSpis.spisPlan.getState().firstOrNull()?.let {
            this.selectionPlanParent.selected = it
            MainDB.timeFun.setPlanForTimeline(it.id.toLong())
        }
    }

    val timelineSeekBar = EnumDiskretSeekBar(TimelinePanEnum::class, TimelinePanEnum.Nears)

    dialPan.dial = @Composable {
        with(MainDB.styleParam.timeParam.timelineCommonParam) {
            LaunchedEffect(boxSelectPlan.selectionPlanParent.selected) {
                boxSelectPlan.selectionPlanParent.selected?.let {
                    MainDB.timeFun.setPlanForTimeline(it.id.toLong())
                }
            }
            BackgroungPanelStyle1(style = SimplePlateStyleState(plateSrokPanel)) {
                Column(
                    Modifier.padding(15.dp)
                        .fillMaxHeight(0.85f)
                        .fillMaxWidth(0.85f), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    timelineSeekBar.show(style = timelineSeekBarStyle)
                    when (timelineSeekBar.active) {
                        TimelinePanEnum.Nears -> {}
                        TimelinePanEnum.ForPlans -> boxSelectPlan.getComposable()
                    }
                    Column(Modifier.weight(1f)) {
                        Box {
                            RowVA {
                                Spacer(Modifier.width(260.dp))
                                ShkalSrokPodpis(
                                    TimelineDiagramColorsState(timelineColors),
                                    modifier = Modifier.height(40.dp).weight(1f),
                                    dateOpor = dateCurr.value.time,
                                    year = year.value
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Column(Modifier.fillMaxWidth().padding(end = 8.dp)) {
                                Spacer(
                                    Modifier.height(39.dp)
                                        .fillMaxWidth()
                                )
                                Box(
                                    Modifier
                                        .height(1.dp)
                                        .fillMaxWidth()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    timelineColors.COLOR_RAMK.getValue().toColor()
                                                )
                                            )
                                        )
                                )
                            }
                        }
                        when (timelineSeekBar.active) {
                            TimelinePanEnum.Nears -> Box(Modifier.weight(1f, false)) {
                                MyList(
                                    MainDB.timeSpis.spisSrokForPlanAndStap,
                                    Modifier
                                ) { ind, item ->
                                    ComItemTimeline(item, selection, dateCurr.value.time, year.value)
                                }
                            }

                            TimelinePanEnum.ForPlans -> {
                                MainDB.timeSpis.spisTimelineForPlan.getState().value?.let { listT ->
                                    Column(Modifier.padding(end = 8.dp)) {
                                        listT.firstOrNull()?.let {
                                            ComItemTimeline(it, selection, dateCurr.value.time, year.value)
                                        }
                                    }
                                    Box(Modifier.weight(1f, false)) {
                                        MyList(listT.filter { it.stap_id != 0L }, Modifier) { ind, item ->
                                            ComItemTimeline(item, selection, dateCurr.value.time, year.value)
                                        }
                                    }
                                }
                            }
                        }
                        Box {
                            RowVA {
                                Spacer(Modifier.width(260.dp))
                                ShkalSrokPodpis(
                                    TimelineDiagramColorsState(timelineColors),
                                    true,
                                    modifier = Modifier.height(40.dp).weight(1f),
                                    dateOpor = dateCurr.value.time,
                                    year = year.value
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Column(Modifier.fillMaxWidth().padding(end = 8.dp)) {
                                Box(
                                    Modifier
                                        .height(1.dp)
                                        .fillMaxWidth()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    timelineColors.COLOR_RAMK.getValue().toColor()
                                                )
                                            )
                                        )
                                )
                                Spacer(
                                    Modifier.height(39.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MyToggleButtIconStyle1(
                            "ic_baseline_expand_24.xml",
                            value = year,
                            sizeIcon = 30.dp,
                            myStyleToggleButton = ToggleButtonStyleState(buttYear),
                            rotateIcon = true
                        )
                        RowVA(horizontalArrangement = Arrangement.Center) {
                            MyTextButtStyle1("<<<", myStyleTextButton = TextButtonStyleState(buttArrow3)) {
                                dateCurr.value = Date(dateCurr.value.time).add(-30, TimeUnits.DAY)
                            }
                            MyTextButtStyle1("<<", myStyleTextButton = TextButtonStyleState(buttArrow2)) {
                                dateCurr.value = Date(dateCurr.value.time).add(-5, TimeUnits.DAY)
                            }
                            buttDatePickerWithButton(
                                dialLayInner,
                                dateCurr,
                                myStyleTextDate = TextButtonStyleState(buttDate),
                                myStyleTextArrow = TextButtonStyleState(buttArrow1)
                            )
                            MyTextButtStyle1(">>", myStyleTextButton = TextButtonStyleState(buttArrow2)) {
                                dateCurr.value = Date(dateCurr.value.time).add(5, TimeUnits.DAY)
                            }
                            MyTextButtStyle1(">>>", myStyleTextButton = TextButtonStyleState(buttArrow3)) {
                                dateCurr.value = Date(dateCurr.value.time).add(30, TimeUnits.DAY)
                            }
                        }
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

