package MainTabs.Time.Elements


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyTextButtStyle2
import common.MyToggleButtIconStyle1
import extensions.*
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

class TimeSelectSlider2(dtStart: Date, dtEnd: Date) {

    private fun DateToInt(date: Date): Int {
//        return ((date.withOffset().time/60f/1000f).toInt()%(24*60+1))/5f
        return (date.format("HH").toFloat() * 12f + date.format("mm").toFloat() / 5f).toInt()
    }

    private fun IntToDate(value: Int): Date {
        return Date().apply {
            time = value.toLong() * 5 * 60 * 1000

        }.minusOffset()
    }

    private val timeStart = mutableStateOf(DateToInt(dtStart))
    private val timeEnd = mutableStateOf(DateToInt(dtEnd))

    fun getTimeStart(): Date {
        return IntToDate(timeStart.value)
    }

    fun getTimeEnd(): Date {
        return IntToDate(timeEnd.value)
    }

    fun setTimeStart(date: Date) {
        timeStart.value = DateToInt(date)
    }

    fun setTimeEnd(date: Date) {
        timeEnd.value = DateToInt(date)
    }

    private val fixDiap = mutableStateOf(false)
    private var widthDiap = 20

    private val maxValSlider = 24 * 12

    fun countToStr(countFive: Int): String = "${(countFive * 5 / 60F).toInt()} ч. ${(countFive * 5 % 60).toInt()} мин."

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @Composable
    private fun timeSelect(modifier: Modifier = Modifier, time: MutableState<Int>) {

        val interactionSource = remember { MutableInteractionSource() }
        var cursorPositionL by remember { mutableStateOf(Offset(0f, 0f)) }
        var sectorAdd by remember { mutableStateOf(0) }
        var zoomEnable by remember { mutableStateOf(false) }
        var dragNow by remember { mutableStateOf(false) }

        with(LocalDensity.current) {
            val padding = 5.dp
            val between = 2.dp.toPx()
            val shirDp = 30.dp
            val shir = shirDp.toPx()

            val listColor = listOf(
                0f..1f to MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_TIME_PERIOD_1.getValue().toColor(),
                1f..2f to MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_TIME_PERIOD_2.getValue().toColor(),
                2f..3f to MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_TIME_PERIOD_3.getValue().toColor(),
                3f..4f to MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_TIME_PERIOD_4.getValue().toColor(),
                4f..5f to MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_TIME_PERIOD_5.getValue().toColor(),
                5f..6f to MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_TIME_PERIOD_6.getValue().toColor(),
            )

            val pY = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Fill
                color = MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_NUMBER.getValue().toColor()
            }
            val p2 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Stroke
                strokeWidth = 2f
                color = MainDB.styleParam.timeParam.denPlanTab.timeSelector.COLOR_NUMBER_BORDER.getValue().toColor()
            }
            val font = Font().apply {
                size = 17.sp.toPx()
            }
            var position by remember { mutableStateOf(0f) }
// val sdfs: ClosedFloatingPointRange<Float> = 5f..6f
            var colorRect by remember { mutableStateOf(Color.Blue) }

            Column(modifier) {
                RowVA(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    MyTextButtStyle2(
                        "❮",
                        modifier = Modifier.width(30.dp).height(30.dp),
                        fontSize = 12.sp
                    ) {
                        time.value -= 1
                    }
                    Text(
                        countToStr(time.value),
                        color = Color.White
                    )
                    MyTextButtStyle2(
                        "❯",
                        modifier = Modifier.width(30.dp).height(30.dp),
                        fontSize = 12.sp
                    ) {
                        time.value += 1
                    }
                }
                BoxWithConstraints(Modifier.fillMaxWidth()) {

                    val modifierCanvas by remember {
                        mutableStateOf<Modifier>(Modifier.width(this.maxWidth).height(shirDp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
//                                println("clickkkk ====")
//                    lColor.value = maxOf(0f, minOf(cursorPositionL.value.x / 1.dp.toPx(), 255f)) // - padding.toPx()
//                    changeFromHSL()
                            }
                            .pointerMoveFilter(onMove = {
                                if (zoomEnable) {
                                    position =
                                        if (cursorPositionL.x < 0f) 0f else if (cursorPositionL.x > this.maxWidth.toPx()) this.maxWidth.toPx() else cursorPositionL.x
                                    time.value = sectorAdd * 12 + (position / this.maxWidth.toPx() * 4 * 12).toInt()
                                }
                                cursorPositionL = it
//                                println("cursorX = ${it.x}")
                                false
                            })
                            .draggable(DraggableState { delta ->
//                        println("delta = $delta")
                            }, Orientation.Horizontal, interactionSource = interactionSource)
                        )
                    }

                    fun getColor(opor: Float): Color = when (opor / this.maxWidth.toPx() * 6) {
                        in listColor[0].first -> listColor[0].second
                        in listColor[1].first -> listColor[1].second
                        in listColor[2].first -> listColor[2].second
                        in listColor[3].first -> listColor[3].second
                        in listColor[4].first -> listColor[4].second
                        in listColor[5].first -> listColor[5].second
                        else -> Color.White
                    }

                    fun setColor() {
                        colorRect = getColor(cursorPositionL.x)
                        sectorAdd = when (cursorPositionL.x / this.maxWidth.toPx() * 6) {
                            in listColor[0].first -> 0
                            in listColor[1].first -> 4
                            in listColor[2].first -> 8
                            in listColor[3].first -> 12
                            in listColor[4].first -> 16
                            in listColor[5].first -> 20
                            else -> 0
                        }
                    }

                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is DragInteraction.Start -> {
                                    if (!zoomEnable) setColor()
                                    zoomEnable = true
                                    dragNow = true
//                                    println("Drag Start")
                                }
                                is DragInteraction.Stop -> {
                                    colorRect = Color.Blue
                                    dragNow = false
                                    zoomEnable = false
//                                    println("Drag Stop")
                                }
                                is DragInteraction.Cancel -> {
                                    colorRect = Color.Blue
                                    dragNow = false
                                    zoomEnable = false
//                                    println("Drag Cancel")
                                }
                                is PressInteraction.Press -> {
                                    if (!zoomEnable) setColor()
                                    zoomEnable = true
//                                    println("PressInteraction.Press")
                                }
                                is PressInteraction.Release -> {
                                    colorRect = Color.Blue
                                    dragNow = false
                                    zoomEnable = false
//                                    println("PressInteraction.Release")
                                }
                                is PressInteraction.Cancel -> {
                                    if (dragNow.not()) zoomEnable = false
//                                    println("PressInteraction.Cancel")
                                }
                            }
                        }
                    }
                    Canvas(
                        modifier = modifierCanvas
/*
                    .pointerInput(Unit) {
                        detectDragGestures() { change, dragAmount ->
//                        lColor.value =
//                            maxOf(0f, minOf(lColor.value + dragAmount.x / 1.dp.toPx(), 255f)) // - padding.toPx()
//                        changeFromHSL()
                        }
                    }
*/
                    ) {
                        if (zoomEnable) {
                            for (i in 0..3) {
//                    val rgb = hslToRgb(hColor.value / 255f, sColor.value / 255f, iw.toFloat() / 254f)
                                drawRect(
                                    colorRect,
                                    Offset((this.size.width - between) / 4f * i + between, 0f),
                                    Size((this.size.width - between) / 4f - between, shir)
                                )
                                drawIntoCanvas {
                                    val textL = TextLine.make((sectorAdd + i).toString(), font)
                                    it.nativeCanvas.drawTextLine(
                                        textL,
                                        (this.size.width - between) / 4f * i + between,
                                        textL.capHeight + 2.dp.toPx(),
                                        p2.asFrameworkPaint()
                                    )
                                    it.nativeCanvas.drawTextLine(
                                        textL,
                                        (this.size.width - between) / 4f * i + between,
                                        textL.capHeight + 2.dp.toPx(),
                                        pY.asFrameworkPaint()
                                    )
                                }
                            }
                        } else {
                            for (i in 0..5) {
//                    val rgb = hslToRgb(hColor.value / 255f, sColor.value / 255f, iw.toFloat() / 254f)
                                listColor.getOrNull(i)?.let {
                                    drawRect(
                                        it.second,
                                        Offset(this.size.width / 6f * i, 0f), Size(this.size.width / 6f, shir)
                                    )
                                }
                                drawIntoCanvas {
                                    val textL = TextLine.make((i * 4).toString(), font)
                                    it.nativeCanvas.drawTextLine(
                                        textL,
                                        this.size.width / 6f * i + 2.dp.toPx(),
                                        textL.capHeight + 2.dp.toPx(),
                                        p2.asFrameworkPaint()
                                    )
                                    it.nativeCanvas.drawTextLine(
                                        textL,
                                        this.size.width / 6f * i + 2.dp.toPx(),
                                        textL.capHeight + 2.dp.toPx(),
                                        pY.asFrameworkPaint()
                                    )
                                }
                            }
                        }
/*
                    drawRect(
                        colorRect,//if (isPressedBy ) Color.Red else if (isDragBy) Color.Green else Color.White, //.copy(0.5f),
                        Offset(0f, 0f),
                        this.size,
                        style = Stroke(1.5.dp.toPx())
                    )
*/
                        if (zoomEnable) {
//                            val position =
//                                if (cursorPositionL.x < 0f) 0f else if (cursorPositionL.x > this.size.width) this.size.width else cursorPositionL.x
/*
                            drawIntoCanvas {
                                val textL = TextLine.make("${(time.value * 5 / 60F).toInt()} ч. ${(time.value * 5 % 60).toInt()} мин.", font)
                                it.nativeCanvas.drawTextLine(
                                    textL,
                                    2.dp.toPx(),
                                    shir - 2.dp.toPx() ,
                                    p2.asFrameworkPaint()
                                )
                                it.nativeCanvas.drawTextLine(
                                    textL,
                                    2.dp.toPx(),
                                    shir - 2.dp.toPx() ,
                                    pY.asFrameworkPaint()
                                )
                            }
*/
                            drawRect(
                                Color.White, //.copy(0.5f),
                                Offset((position.dp - 1.5.dp).toPx(), -5.dp.toPx()),
                                Size(3.dp.toPx(), shir + 10.dp.toPx()),
                                style = Stroke(1.5.dp.toPx())
                            )
                        }
                    }
                }
            }
        }
//        }
    }


    @Composable
    fun getComposable(modifier: Modifier = Modifier) {
        LaunchedEffect(timeStart.value) {
            if (fixDiap.value) {
                var secondProg = timeStart.value + widthDiap
                if (secondProg > maxValSlider) secondProg -= maxValSlider
                if (secondProg < 0) secondProg += maxValSlider
                timeEnd.value = secondProg
            }
        }
        LaunchedEffect(timeEnd.value) {
            if (fixDiap.value) {
                if (fixDiap.value) {
                    var secondProg = timeEnd.value - widthDiap
                    if (secondProg > maxValSlider) secondProg -= maxValSlider
                    if (secondProg < 0) secondProg += maxValSlider
                    timeStart.value = secondProg
                }
            }
        }
        RowVA(
            modifier = modifier
//                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 7.dp)
            ,
        ) {
            timeSelect(Modifier.weight(1f), timeStart)
            Column(Modifier.width(80.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    countToStr(if (fixDiap.value) widthDiap else (timeEnd.value - timeStart.value).let{ if (it>0) it else it + 24*12}),
                    Modifier.padding(bottom = 5.dp),
                    color = Color.White,
                    fontSize = 14.sp
                )
                MyToggleButtIconStyle1(
                    "ic_round_repeat_24.xml",
//                    modifier = Modifier.padding(horizontal = 20.dp),
                    value = fixDiap, sizeIcon = 30.dp,
                    myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.buttSort)
                ){
                    widthDiap = (timeEnd.value - timeStart.value).let{ if (it>0) it else it + 24*12}
                }
            }
            timeSelect(Modifier.weight(1f), timeEnd)
        }

    }

}