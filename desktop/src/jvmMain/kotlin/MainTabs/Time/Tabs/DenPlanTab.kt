package MainTabs.Time.Tabs

import MainTabs.Time.Elements.PanAddDenPlan
import MainTabs.Time.Elements.PanAddNapom
import MainTabs.Time.Elements.PanCalendar
import MainTabs.Time.Elements.PanSelectShablonDenPlan
import MainTabs.Time.Items.ComItemDenPlan
import MainTabs.Time.Items.ComItemNapom
import MainTabs.Time.Items.DropdownMenuDenPlan
import MainTabs.Time.Items.DropdownMenuNapom
import MyDialog.MyDialogLayout
import MyDialog.MyFullScreenPanel
import MyDialog.MyOneVopros
import MyDialog.buttDatePickerWithButton
import MyListItems
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soywiz.korio.async.async
import com.sun.tools.javac.Main
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.models.data.ItemNextActionStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB
import viewmodel.StateVM
import java.util.*


class DenPlanTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelection()
    val pamDayKey = mutableStateOf(false)
    val updateKey = mutableStateOf(false)

    val listState: LazyListState = LazyListState(0)

    @Composable
    fun spiski(modifier: Modifier, upd: Boolean) {
        val scopeR = rememberCoroutineScope()
        BoxWithVScrollBarLazyList(modifier, listState) { scrollState ->
            MainDB.styleParam.timeParam.denPlanTab.itemNapom.getComposable(::ItemNapomStyleState) { itemNapomStyle ->
                MainDB.styleParam.timeParam.denPlanTab.itemDenPlan.getComposable(::ItemDenPlanStyleState) { itemDenPlanStyle ->
                    LazyColumn(state = scrollState) {
                        MyListItems(MainDB.timeSpis.spisNapom, scope = this) { itemNap ->
                            ComItemNapom(itemNap, selection, vypFun = { gotov ->
                                MainDB.addTime.setVypNapom(gotov, MainDB.timeFun.getDay(), itemNap.id.toLong())
                            }, itemNapomStyle, dialLay = dialLay) { item, expanded ->
                                DropdownMenuNapom(item, expanded, dialLay)
                            }
                        }
                        MyListItems(MainDB.timeSpis.spisDenPlan, scope = this) {
                            ComItemDenPlan(it, selection, changeGotov = { item, progress ->
                                item.setGotov(progress.toDouble() * 100) { hour, gotov, exp ->
                                    MainDB.timeSpis.spisDenPlan.updateElem(
                                        item,
                                        item.copy(sum_hour = hour, gotov = gotov)
                                    )
                                }
//                                scopeR.async {
                                MainDB.addTime.updGotovDenPlan(
                                    item,
                                    progress.toDouble() * 100,
                                )
//                                }
                            }, itemDenPlanStyleState = itemDenPlanStyle, dialLay = dialLay) { item, expanded ->
                                DropdownMenuDenPlan(item, expanded, dialLay, itemDenPlanStyle)
                            }.getComposable()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun topPanel() {
        RowVA(Modifier.padding(vertical = 8.dp)) {
            MyToggleButtIconStyle1(
                "ic_baseline_turned_in_not_24.xml", "ic_round_turned_in_24.xml",
                twoIcon = true,
                value = pamDayKey, sizeIcon = 40.dp, modifier = Modifier.height(45.dp),
                myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttBestDays)
            ) {
                if (!it) {
                    MainDB.avatarSpis.spisBestDays.getState().value?.find {
                        it.data == MainDB.denPlanDate.value.time
                    }?.id?.let {
                        MainDB.addAvatar.delBestDay(it.toLong())
                    }
                } else {
                    MyOneVopros(
                        dialLay,
                        "Если хотите добавить этот день как памятный, введите его название:",
                        "Добавить",
                        "Название",
                        cancelListener = { pamDayKey.value = false }
                    ) {
                        if (it != "") {
                            MainDB.addAvatar.addBestDay(it, MainDB.denPlanDate.value.time)
                        } else {
                            pamDayKey.value = false
                        }
                    }
                }
            }
            MainDB.styleParam.timeParam.denPlanTab.today.getComposable(::TodayPlateStyleState) { todayState ->
                MainDB.timeFun.updateTextDateBetweenWithColor()
                MyShadowBox(todayState.shadow, Modifier.weight(1f)) {
                    Text(
                        MainDB.timeSpis.textAboveSelectDenPlan.getState().value ?: "",// text_addDay.value.text,
                        modifier = Modifier
                            .padding(horizontal = 9.dp)
                            .padding(end = 5.dp)
                            .clickable {
                                MainDB.timeFun.setToday()
                            }
                            .border(todayState.borderWidth, todayState.borderBrush, todayState.shape)
                            .background(
                                (MainDB.timeSpis.textColorAboveSelectDenPlan.getState().value
                                    ?: MyColorARGB.colorEffektShkal_Month).toColor(), todayState.shape
                            )
                            .then(todayState.padingInner)
//                            .padding(start = 5.dp)
//                            .padding(3.dp)
                            .fillMaxWidth()
//                            .alpha(0.7f)
                        ,
                        style = todayState.textStyle
                    )
                }
            }
            MyTextButtStyle1(
                "ᐁᐁ",
                modifier = Modifier.height(35.dp).padding(horizontal = 0.dp),
                height = 40.dp,
                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttSverDenPlan)
            ) {
                MainDB.timeSpis.spisDenPlan.getState().toList().let { list ->
                    for (item in list) {
                        MainDB.timeSpis.spisDenPlan.sverOpisElem(item, false)
                    }
                }
                MainDB.timeSpis.spisNapom.getState().toList().let { list ->
                    for (item in list) {
                        MainDB.timeSpis.spisNapom.sverOpisElem(item, false)
                    }
                }
                updateKey.value = updateKey.value.not()
            }
            MyTextButtStyle1(
                "ᐃᐃ",
                modifier = Modifier.height(35.dp),
                height = 40.dp,
                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttSverDenPlan)
            ) {
                MainDB.timeSpis.spisDenPlan.getState().toList().let { list ->
                    for (item in list) {
                        MainDB.timeSpis.spisDenPlan.sverOpisElem(item, true)
                    }
                }
                MainDB.timeSpis.spisNapom.getState().toList().let { list ->
                    for (item in list) {
                        MainDB.timeSpis.spisNapom.sverOpisElem(item, true)
                    }
                }
                updateKey.value = updateKey.value.not()
            }
        }
    }

    @Composable
    fun bottomPanel() {
        RowVA(
            Modifier.padding(bottom = 0.dp, top = 10.dp).padding(horizontal = 0.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LaunchedEffect(MainDB.denPlanDate.value) {
                pamDayKey.value = MainDB.avatarSpis.spisBestDays.getState().value?.find {
                    it.data == MainDB.denPlanDate.value.time
                }?.let { true } ?: false
            }
            LaunchedEffect(MainDB.denPlanDateForCalendar.value) {
                if (MainDB.timeFun.getCalendarDay() != MainDB.denPlanDateForCalendar.value.time) MainDB.timeFun.setCalendarDate(
                    MainDB.denPlanDateForCalendar.value.time
                )
            }
//            Column(modifier = Modifier.padding(end = 10.dp)) {
            MyIconButtStyle(
                "ic_round_access_time_24.xml", sizeIcon = 30.dp, width = 70.dp, height = 35.dp,
                myStyleButton = IconButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttIconCalendar)
            ) {
                MainDB.denPlanDateForCalendar.value = MainDB.denPlanDate.value
                MyFullScreenPanel(dialLay, showHideButton = true) { dialLayInner, closeFun ->
                    PanCalendar(MainDB.denPlanDateForCalendar, this, dialLayInner, closeFun)
                }
            }
//            }
            buttDatePickerWithButton(
                dialLay, MainDB.denPlanDate, Modifier.weight(1f), fontSize = 17.sp,
                myStyleTextDate = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttDate),
                myStyleTextArrow = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttDateArrow),
                format = "dd MMM yyyy (EEE)"
            )
//            Column(modifier = Modifier.padding(start = 10.dp)) {
            MyIconButtStyle(
                "ic_baseline_cloud_upload_24.xml", sizeIcon = 30.dp, width = 70.dp, height = 35.dp,
//                    modifier = Modifier.padding(bottom = 10.dp),
                myStyleButton = IconButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttIconShablon)
            ) {
                PanSelectShablonDenPlan(dialLay,
                    listenerShablon = { itShab, loadPovt, loadTime ->
                        PanAddDenPlan(dialLay, shablonLoad = Triple(itShab, loadPovt, loadTime))
                    },
                    listenerNextAction = { nextAction, listOpis, loadNameFromStap, loadOpis, finListener ->
                        PanAddDenPlan(
                            dialLay, ItemDenPlan(
                                "-25",
                                if (!loadNameFromStap && nextAction is ItemNextActionStap) nextAction.namePlan else nextAction.name,
                                Date().format("HH:mm:ss"),
                                Date().add(2, TimeUnits.HOUR).format("HH:mm:ss"),
                                0.0,
                                nextAction.vajn,
                                0.0,
                                Date().time,
                                "item.opis",
                                nextAction.privplan,
                                nextAction.stap_prpl,
                                "",
                                ""
                            ),
                            listOp = if (loadOpis && listOpis.isNotEmpty()) listOpis.clearSourceList(TableNameForComplexOpis.spisDenPlan) else null,
                            finListener = finListener
                        )
                    })
            }

            MyTextButtStyle1(
                "+",
//                    modifier = Modifier.padding(bottom = 10.dp),
                modifier = Modifier.padding(start = 5.dp),
                width = 70.dp,
                height = 35.dp,
                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttAddNapom)
            ) { // ❗
                PanAddNapom(dialLay)
            }
            MyTextButtStyle1(
                "+",
                modifier = Modifier.padding(start = 5.dp),
                width = 70.dp,
                height = 35.dp,
                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttAddDenPlan)
            ) {
                PanAddDenPlan(dialLay)
            }
//            }
        }
    }

    @Composable
    fun show() {
//        println(MainDB.styleParam.OLD_PAPER.code)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (((MainDB.timeSpis.spisNapom.getState().size
                    ?: 0) > 0) || ((MainDB.timeSpis.spisDenPlan.getState().size
                    ?: 0) > 0)
            ) {
                topPanel()
                spiski(Modifier.weight(1f), updateKey.value)
            } else {
                Spacer(Modifier.weight(1f))
            }
            bottomPanel()
        }
    }
}

