package MainTabs.Time.Elements

import MainTabs.Time.Items.ComItemCalendarDenPlan
import MainTabs.Time.Items.ComItemCalendarNapom
import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import MyList
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyTextButtStyle1
import common.SingleSelection
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

@Composable
fun PanCalendar(
    changeDate: MutableState<Date>,
    scope: ColumnScope,
    dialLayInner: MyDialogLayout,
    closeFun: () -> Unit
) {
    scope.apply {
        with(MainDB.styleParam.timeParam.denPlanTab.calendarPanel) {
            class daysItem(
                val dayOfWeek: Int,
                val dateItem: Long,
//        var selection: SingleSelection
            ) {
/*
        val isActive: Boolean
            get() = dateItem == selectionDate

        fun activate() {
            aa.time.time = dateItem
            date = Date(dateItem)
            selectionDate = dateItem
//            selection.selected = this
        }
*/
            }

            class LineDay(dateStart: Date) {
                val daysWeek = mutableListOf<daysItem>()

                init {
                    for (i in 1..7) {
                        daysWeek.add(
                            daysItem(
                                i,
                                dateStart.add(1, TimeUnits.DAY).time
                            )
                        )
                    }
                }
            }

            fun getLines(oporDate: Date): MutableList<LineDay> {
                val aa by mutableStateOf(Calendar.getInstance().apply {
                    time = oporDate
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.HOUR, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                })
                val date by mutableStateOf(Date().apply { time = aa.timeInMillis })
                val calendar = Calendar.getInstance().apply { time = date }
                val daysMonth = mutableListOf<LineDay>()
                val dayWeekFD = (calendar.get(Calendar.DAY_OF_WEEK) - 1).let {
                    if (it == 0) 7 else it
                } // воскресенье = 1, пн = 2, .. сб = 7 (до вычета 1 и перестановки вс)
                val startDate = calendar.time.add(-7 - dayWeekFD, TimeUnits.DAY)
                for (i in 1..MainDB.complexOpisSpis.countCalendarWeek) {
                    daysMonth.add(LineDay(startDate))
//            startDate.add(7,TimeUnits.DAY)
                }
                return daysMonth
            }


//            val daysMonth = getLines(changeDate.value)
            val selection = remember { SingleSelection() }

            @Composable
            fun RowScope.ComItemDay(
                item: daysItem,
                listNapom: List<ItemNapom>,
                listPlan: List<ItemDenPlan>,
                styleCDP: ItemCalendarDenPlanStyleState
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        Date(item.dateItem).format("dd.MM.yyyy"),
                        Modifier.paddingStyle(padding_date),
                        style = textDate.getValue()
                    )
                    listNapom.forEach {
                        ComItemCalendarNapom(it, selection) { dialLayInner }
                    }
                    listPlan.forEach {
                        ComItemCalendarDenPlan(it, selection, styleCDP) { dialLayInner }
                    }
                }
            }

            @Composable
            fun comLine(ind: Int, line: LineDay, listNapom: Map<Long, List<ItemNapom>>, listDenPlan: Map<Long, List<ItemDenPlan>>,styleCDP: ItemCalendarDenPlanStyleState){
                val colorBorder = Color.White.copy(0.5f)
                if (ind == 0) Box(Modifier.background(colorBorder).height(1.dp).fillMaxWidth())
                Box {
                    Row(Modifier) {
                        for (day in line.daysWeek) {
                            ComItemDay(
                                day,
                                listNapom.get(day.dateItem)
                                    ?: listOf(),// .filter { it.data == day.dateItem },
                                listDenPlan.get(day.dateItem) ?: listOf(),
                                styleCDP
                            )
                        }
                    }
                    Box(Modifier.matchParentSize()) {
                        Row {
                            Box(Modifier.background(colorBorder).fillMaxHeight().width(1.dp))
                            for (i in 1..7) {
                                Spacer(Modifier.weight(1f))
                                Box(Modifier.background(colorBorder).fillMaxHeight().width(1.dp))
                            }
                        }

                    }
                }
                Box(Modifier.background(colorBorder).height(1.dp).fillMaxWidth())
            }

            Column(Modifier.weight(1f)) {
                ItemCalendarDenPlanStyleState(
                    MainDB.styleParam.timeParam.denPlanTab.calendarPanel.itemCalendarDenPlan,
                    ItemDenPlanStyleState(MainDB.styleParam.timeParam.denPlanTab.itemDenPlan)
                ).also { styleCDP ->
                    MainDB.timeSpis.spisDenPlanForCalendar.getState().groupBy { it.data }.let { listDenPlan ->
                        (MainDB.timeSpis.spisNapomForCalendar.getState().value?.groupBy { it.data }
                            ?: mapOf()).let { listNapom ->

//                            println("ItemCalendarDenPlanStyleState")
                            getLines(changeDate.value).let { lines ->
                                MyList(lines, Modifier.weight(1f)) { ind, line ->
//                                println("DrawMyListCalendar")
                                    comLine(ind,line, listNapom, listDenPlan, styleCDP)
                                }
                            }
                        }
                        RowVA {
                            buttDatePickerWithButton(
                                dialLayInner, changeDate, Modifier.weight(1f), fontSize = 18.sp,
                                myStyleTextDate = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttDate),
                                myStyleTextArrow = TextButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttDateArrow),
                                shagDate = 7
                            )
                        }
                    }
                }
            }
//            }
        }
    }
}