package MyDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import kotlinx.coroutines.launch
import viewmodel.MainDB
import java.time.YearMonth
import java.util.*

@Composable
fun buttDatePickerWithButton(
    dialPan: MyDialogLayout,
    changeDate: MutableState<Date>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
    myStyleTextDate: TextButtonStyleState? = null,
    myStyleTextArrow: TextButtonStyleState? = null,
    shagDate: Int = 1,
    format: String = "dd MMMM yyyy (EEE)",
    funRez: (Date) -> Unit = {}
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

        MyTextButtStyle1("<", fontSize = fontSize, myStyleTextButton = myStyleTextArrow ?: myStyleTextDate) {
            changeDate.value = Date(changeDate.value.time).add(-shagDate, TimeUnits.DAY)
            funRez(changeDate.value)
        }
        MyTextButtStyle1(
            changeDate.value.format(format),
            Modifier.padding(3.dp),
            fontSize = fontSize,
            myStyleTextButton = myStyleTextDate ?: myStyleTextArrow
        ) {
            myDatePicker(dialPan, changeDate, {}, funRez)
        }
//        buttDatePicker(dialPan, changeDate, funRez = funRez)
        MyTextButtStyle1(">", fontSize = fontSize, myStyleTextButton = myStyleTextArrow ?: myStyleTextDate) {
            changeDate.value = Date(changeDate.value.time).add(shagDate, TimeUnits.DAY)
            funRez(changeDate.value)
        }
    }
}

@Composable
fun buttDatePicker(
    dialPan: MyDialogLayout,
    changeDate: MutableState<Date>,
    modifier: Modifier = Modifier,
    funRez: (Date) -> Unit = {}
) {
    Button(
        modifier = modifier.padding(3.dp),
        border = myButtBorder,//BorderStroke(1.dp,Color(0xFFFFF7D9)),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF464D45)),
//        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        onClick = {
            myDatePicker(dialPan, changeDate, {}, funRez)
//                        myShowMessage(dialLay, "Test Message",)
//                        db.ObserFM.selPer.SetPeriodYear()
        }
    ) {
        Text(
            changeDate.value.format("dd MMMM yyyy (EEE)"),
            style = TextStyle(
                color = Color(0xFFFFF7D9),
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
//                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 4f
                ),
//                textGeometricTransform = TextGeometricTransform(
//                    scaleX = 2.5f,
//                    skewX = 1f
//                )
            ),
//        color = Color(0xFFFFF7D9),

        )
    }
}

fun myDatePicker(
    dialPan: MyDialogLayout,
    changeDate: MutableState<Date>,
    cancelFun: () -> Unit = {},
    funRez: (Date) -> Unit = {}
) {

    var styleDP = PanSelectDateState(MainDB.styleParam.commonParam.panSelectDate)

    val aa by mutableStateOf(Calendar.getInstance().apply {
        time = changeDate.value
        set(Calendar.MILLISECOND, 0)
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    })
    var date by mutableStateOf(Date().apply { time = aa.timeInMillis })
    var dateYear by mutableStateOf(aa.get(Calendar.YEAR))

    val selectionDay = SingleSelection()
    var selectionDate by mutableStateOf(date.time)

    val startYear = dateYear - 100
    val endYear = dateYear + 100

    class daysItem(
        val dayOfWeek: Int,
        val number: Int,
        val dateItem: Long,
//        var selection: SingleSelection
    ) {
        val isActive: Boolean
            get() = dateItem == selectionDate

        fun activate() {
            aa.time.time = dateItem
            date = Date(dateItem)
            selectionDate = dateItem
//            selection.selected = this
        }
    }

    @Composable
    fun comDayItem(day: daysItem, row: RowScope) {
        row.apply {
            MyTextButtStyle1(
                day.number.toString(), Modifier.alpha(if (day.number > 0) 1f else 0f),
                myStyleTextButton = if (day.isActive) styleDP.buttNumberActive else styleDP.buttNumber
            )

            Surface(
                modifier = Modifier.weight(1f).padding(2.dp),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                color = if (day.isActive) {
                    if (day.number > 0) Color(0xFF2B2B2B) else Color.Transparent
                } else {
                    if (day.number > 0) Color(0xFF464D45) else Color.Transparent
                }
            ) {
                if (day.number > 0) Row(
                    Modifier
                        .clickable(remember(::MutableInteractionSource), indication = null) {
                            day.activate()
                        }.border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(listOf(Color(0xFF888888), Color(0xFF888888))),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = day.number.toString(), //Date(day.dateItem).format("dd.MM.yy"),//
                        color = Color(0xFFFFF7D9),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth().padding(3.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @Composable
    fun comDayName(date: Long, row: RowScope, weekend: Boolean) {
        row.apply {
            MyShadowBox(if (weekend) styleDP.plateWeekend.shadow else styleDP.plateDayWeek.shadow) {
                Box(
                    Modifier.padding(2.dp).withSimplePlate(if (weekend) styleDP.plateWeekend else styleDP.plateDayWeek).width(40.dp).height(40.dp)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Date(date).format("EE").uppercase(),
                        style = (if (weekend) styleDP.textWeekend else styleDP.textDayWeek).copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }

    class LineDay(private var first: Int, dateStart: Date, private val last: Int) {
        val daysMonth = mutableListOf<daysItem>()

        init {
            for (i in 1..7) {
                daysMonth.add(
                    daysItem(
                        i,
                        if (first > last) -first else first,
                        dateStart.add( 1, TimeUnits.DAY).time
                    )
                ) //,selectionDay
                first++
            }
        }
    }


    fun getLines(date: Date): MutableList<LineDay> {
        val calendar = Calendar.getInstance().apply { time = date }
        val daysMonth = mutableListOf<LineDay>()
        val dayWeekFD = calendar.get(Calendar.DAY_OF_WEEK) - 1 // воскресенье = 1, пн = 2, .. сб = 7
        val firstDayOfMonth = (calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) - 1).let {
            if (it == 0) 7 else it
        }
        val yearMonthObject: YearMonth = YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
        val daysInMonth: Int = yearMonthObject.lengthOfMonth() //28
//        println("Дней в месяце: $daysInMonth")
        var start = 2 - firstDayOfMonth
        var startDate = calendar.time.add(start - 2, TimeUnits.DAY)
        while (start <= daysInMonth) {
            daysMonth.add(LineDay(start, startDate, daysInMonth))
            start += 7
//            startDate.add(7,TimeUnits.DAY)
        }
        return daysMonth
    }

    val yearOffsetUp = 4

    var keyFirstStart = true
    val height = mutableStateOf(250.dp)
    dialPan.dial = @Composable {
        styleDP = PanSelectDateState(MainDB.styleParam.commonParam.panSelectDate)

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        fun setYear(year: Int) {
            if (dateYear != year) {
                dateYear = year
                coroutineScope.launch {
                    // Animate scroll to the 10th item
                    val xT = dateYear - startYear - yearOffsetUp
                    if ((xT > yearOffsetUp) && (xT < 200)) listState.animateScrollToItem(xT)//,- height.value.value.toInt()/3)
//                                                    listState.animateScrollToItem(index = 10)
                }
            }
        }

        val daysMonth = getLines(date)
//        val dayWeekFD = aa.get(Calendar.DAY_OF_WEEK) - 1 // воскресенье = 1, пн = 2, .. сб = 7
//        val firstDayOfMonth = (aa.apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) - 1).let {
//            if (it == 0) 7 else it
//        }
//        val yearMonthObject: YearMonth = YearMonth.of(aa.get(Calendar.YEAR), aa.get(Calendar.MONTH))
//        val daysInMonth: Int = yearMonthObject.lengthOfMonth() //28
//        println("Дней в месяце: $daysInMonth")
//        var start = 2 - firstDayOfMonth
//        while (start < daysInMonth) {
//            daysMonth.add(LineDay(start, daysInMonth))
//            start += 7
//        }
        BackgroungPanelStyle1(
            vignette = styleDP.VIGNETTE,
            style = styleDP.platePanel,
            modifierContent = styleDP.inner_padding
        ) {
            Layout({
                LazyColumn(
                    state = listState,
                    modifier = Modifier
//                                .height(height.value)
                        .padding(end = 15.dp)
                ) {//height.value
                    for (i in startYear..endYear) {
                        item {
                            MyTextButtStyle1(
                                i.toString(),
                                Modifier.padding(vertical = 2.dp),
                                myStyleTextButton = if (i == dateYear) styleDP.buttYearActive else styleDP.buttYear
                            ) {
                                aa.set(Calendar.YEAR, i)
                                date = aa.time
                                setYear(i)
                            }
/*
                            Surface(
                                modifier = Modifier.padding(2.dp),
                                shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                                color = if (i == dateYear) Color(0xFF2B2B2B) else Color.Transparent
                            ) {
                                Row(
                                    Modifier
                                        .clickable(remember(::MutableInteractionSource), indication = null) {
                                            aa.set(Calendar.YEAR, i)
                                            date = aa.time
                                            setYear(i)
                                        }.border(
                                            width = 1.dp,
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    Color(0xFF888888),
                                                    Color(0xFF888888)
                                                )
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = i.toString(),//
                                        color = if (i == aa.get(Calendar.YEAR)) Color(0xFFFFF7D9) else Color.Black,
                                        fontSize = 22.sp,
                                        modifier = Modifier.padding(6.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
*/
                        }
                    }
                }.apply {
                    if (keyFirstStart) {
                        coroutineScope.launch {
                            val xT = dateYear - startYear - yearOffsetUp
                            if ((xT > yearOffsetUp) && (xT < 200)) listState.scrollToItem(xT)//,- height.value.value.toInt()/3)
                        }
                        keyFirstStart = false
                    }
                }
//                        with(LocalDensity.current) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        Date(selectionDate).format("dd MMMM yyyy (EEE)"),
                        style = styleDP.textDate.copy(
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    MeasureUnconstrainedViewWidth(viewToMeasure = {
                        val dd = Calendar.getInstance()//.apply { set(Calendar.DAY_OF_MONTH,1) }
                        val startDate = dd.time.add(2 - dd.get(Calendar.DAY_OF_WEEK), TimeUnits.DAY)
                        Row(
                            Modifier.padding(vertical = 5.dp) //.width(280.dp)
                        ) {
                            for (day in 1..7) {
                                comDayName(startDate.time, this, day == 6 || day == 7)
                                startDate.add(1, TimeUnits.DAY)
                            }
                        }
                    }) {
                        RowVA(Modifier.width(it).padding(vertical = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) { //(Modifier.width(280.dp), verticalAlignment = Alignment.CenterVertically) {
                            MyTextButtStyle1("<", myStyleTextButton = styleDP.buttArrow) {
                                aa.add(Calendar.MONTH, -1)
                                date = aa.time
                                setYear(aa.get(Calendar.YEAR))
                            }
                            Text(
                                date.format("LLLL yyyy"),
//                                modifier = Modifier.padding(horizontal = 20.dp),
                                style = styleDP.textMonth
                            )
                            MyTextButtStyle1(">", myStyleTextButton = styleDP.buttArrow) {
                                aa.add(Calendar.MONTH, 1)
                                date = aa.time
                                setYear(aa.get(Calendar.YEAR))
                            }
                        }
                    }
                    Column {
                        val dd = Calendar.getInstance()//.apply { set(Calendar.DAY_OF_MONTH,1) }
                        val startDate = dd.time.add(2 - dd.get(Calendar.DAY_OF_WEEK), TimeUnits.DAY)
                        Row(
                            Modifier.padding(vertical = 5.dp) //.width(280.dp)
                        ) {
                            for (day in 1..7) {
                                comDayName(startDate.time, this, day == 6 || day == 7)
                                startDate.add(1, TimeUnits.DAY)
                            }
                        }
                        for (line in daysMonth) {
                            Row {
                                for (day in line.daysMonth) {
                                    MyTextButtStyle1(
                                        day.number.toString(),
                                        Modifier.alpha(if (day.number > 0) 1f else 0f).padding(2.dp),
                                        width = 40.dp,
                                        height = 40.dp,
                                        myStyleTextButton = if (day.dayOfWeek == 6 || day.dayOfWeek == 7) {
                                            if (day.isActive) styleDP.buttNumberWeekendActive else styleDP.buttNumberWeekend
                                        }   else    {
                                            if (day.isActive) styleDP.buttNumberActive else styleDP.buttNumber
                                        }
                                    ) {
                                        day.activate()
                                    }
//                                                    comDayItem(day, this)
                                }
                            }
                        }
                        if (daysMonth.size < 6) Box(Modifier.padding(2.dp).width(40.dp).height(40.dp))
                        if (daysMonth.size < 5) Box(Modifier.padding(2.dp).width(40.dp).height(40.dp))
                    }

                    Row(Modifier.padding(top = 10.dp)) {
                        MyTextButtStyle1(
                            "Отмена",
                            modifier = Modifier.padding(end = 10.dp),
                            myStyleTextButton = styleDP.buttCancel
                        ) {
                            dialPan.close()
                            cancelFun()
                        }
                        MyTextButtStyle1("Ok", myStyleTextButton = styleDP.buttSelect) {
                            dialPan.close()
                            changeDate.value = date
                            funRez(date)
                        }
                    }
                }
//                        }
            }, Modifier.padding(15.dp), measurePolicy = { measurables, constraints ->
                require(measurables.size == 2)

                val firstPlaceable = measurables[1].measure(constraints.copy(minWidth = 0, minHeight = 0))
                val secondHeight = firstPlaceable.height
                val secondPlaceable = measurables[0].measure(
                    Constraints(
                        minHeight = secondHeight,
                        maxHeight = secondHeight
                    )
                )
                layout(secondPlaceable.width + firstPlaceable.width, secondHeight) {
                    firstPlaceable.place(secondPlaceable.width, 0)
                    secondPlaceable.place(0, 0)
                }
            })
//                }
//            }
        }
    }

    dialPan.show()
}