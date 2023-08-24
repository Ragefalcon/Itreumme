package MainTabs.Time.Elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import extensions.*
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine
import ru.ragefalcon.sharedcode.extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemSrokPlanAndStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import java.util.*

//private val countBlock = 35
private val beforeDayPriv = 5
private val beforeDayYearPriv = 65
private val paddingPriv = 10.dp
private val widthDayPriv = 16.dp
private val widthDayYearPriv = 2.dp

@Composable
fun ShkalSrok(
    colorsState: TimelineDiagramColorsState,
    item: ItemSrokPlanAndStap,
    modifier: Modifier = Modifier.width(150.dp).height(15.dp),
    dateOpor: Long = Date().time.longMinusTimeLocal(),
    year: Boolean = true
) {
    val date1 = Date(item.data1)
    val date2 = Date(item.data2)
    val listDate = item.listDate

    with(colorsState) {
        with(LocalDensity.current) {

            val beforeDay = if (year) beforeDayYearPriv else beforeDayPriv

            val startTime: Int =
                ((date1.time.unOffset() - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay) / TimeUnits.DAY.milliseconds).toInt()
//        println("startTime = ${(date1.time - dateCurr.minusTime() + TimeUnits.DAY.milliseconds * beforeDay).toFloat() / TimeUnits.DAY.milliseconds.toFloat()}")
            val endTime: Int =
                ((date2.time.unOffset() - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay) / TimeUnits.DAY.milliseconds).toInt()
            val dateCurr: Long = Date().time.longMinusTimeUTC()
//            println("date1 = ${Date(date1.time.unOffset()).format("dd MM yyyy HH mm")}")
//            println("date1 = ${date1.time}") // вот этот вариант будет равен 0 если даты не было
//            println("date1_1 = ${date1.time.unOffset()}")
//            println("dateCurr = ${Date(dateCurr).format("dd MM yyyy HH mm")}")
//            println("dateOpor1 = ${Date(dateOpor.longMinusTimeLocal().unOffset()).format("dd MM yyyy HH mm")}")
//            println("dateOpor2 = ${Date(dateOpor.unOffset()).format("dd MM yyyy HH mm")}")
//            println("dateOpor3 = ${Date(dateOpor).format("dd MM yyyy HH mm")}")
//            println("dateOpor4 = ${Date(dateOpor.longMinusTimeUTC()).format("dd MM yyyy HH mm")}")
//            println("dateOpor5 = ${Date(dateOpor.longMinusTimeUTC().unOffset()).format("dd MM yyyy HH mm")}")
            /**
             * ...minusTime(): Long {
            return DateTimeTz.fromUnixLocal( ...
            dateOpor1 = 04 09 2022 00 00
            dateOpor2 = 04 09 2022 23 15
            dateOpor3 = 05 09 2022 03 15
            dateOpor4 = 04 09 2022 04 00
            dateOpor5 = 04 09 2022 04 00
             * */
            val currTime: Int =
                ((dateCurr - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay) / TimeUnits.DAY.milliseconds).toInt()
//            println("currTime = ${currTime}")
//            println("currTimeFloat = ${(dateCurr - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay) / TimeUnits.DAY.milliseconds}")
//            println("startTime = ${startTime}")

            val color =
                if ((item.stap_id == 0L && item.stat == TypeStatPlan.COMPLETE.codValue) || (item.stap_id > 0L && item.stat == TypeStatPlanStap.COMPLETE.codValue)) color_background_complete
                else {
                    if ((item.stap_id == 0L && item.stat == TypeStatPlan.CLOSE.codValue) || (item.stap_id > 0L && item.stat == TypeStatPlanStap.CLOSE.codValue)) color_background_close
                    else {
                        if (currTime < startTime) color_range_future else if (currTime > endTime) color_range_past else color_range_present
                    }
                }
            val colorBack =
                if ((item.stap_id == 0L && TypeStatPlan.getCloseSelectList().contains(TypeStatPlan.getType(item.stat)))
                    || (item.stap_id > 0L && TypeStatPlanStap.getCloseSelectList().contains(TypeStatPlanStap.getType(item.stat)))
                ) color_background
                else {
                    if (date1.time.unOffset() < TimeUnits.DAY.milliseconds) color_background
                    else if (currTime < startTime && startTime - currTime < 3) color_background_near
                    else if (currTime > endTime) color_background_end else color_background
                }
            Canvas(
                modifier = modifier.background(colorBack)
            ) {
                with(this@with) {

                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    val dd = Calendar.getInstance().apply {
                        time = Date(dateOpor).add(-beforeDay, TimeUnits.DAY)
                    }

                    val padding = paddingPriv.toPx()
                    val countBlock = if (year) ((canvasWidth - padding * 2) / widthDayYearPriv.toPx() / 30f).toInt() + 3
                    else ((canvasWidth - padding * 2) / widthDayPriv.toPx()).toInt()
                    val widthDay = if (year) widthDayYearPriv.toPx() else widthDayPriv.toPx()

                    val dateStart = dateOpor.longMinusTimeUTC() - TimeUnits.DAY.milliseconds * beforeDay
                    val dateEnd = dateStart + TimeUnits.DAY.milliseconds * countBlock * (if (year) 30 else 1)

                    if (!year) for (i in 0 until countBlock) {
                        dd.get(Calendar.DAY_OF_WEEK).let { dayWeek ->
                            if (dayWeek == Calendar.SATURDAY || dayWeek == Calendar.SUNDAY) drawRect(
                                color_weekend,
                                Offset(i * widthDay + padding, 0f),
                                Size(widthDay, canvasHeight)
                            )
                        }
                        dd.add(Calendar.DATE, 1)
                    }
                    drawRect(
                        if (year) color_current.copy(alpha = 1.0f) else color_current.copy(alpha = 0.3f),
                        Offset(currTime * widthDay + padding, 0f),
                        Size(widthDay, canvasHeight)
                    )
                    drawRect(
                        color.copy(alpha = 0.5f),
                        Offset(startTime * widthDay + padding, 0f),
                        Size((endTime - startTime + 1) * widthDay, canvasHeight)
                    )
                    val ramk = Path()
                    if (!year) for (i in 1 until countBlock) {
                        ramk.moveTo(padding + widthDay * i, 0f)
                        ramk.lineTo(padding + widthDay * i, canvasHeight)
                    } else {
                        dd.add(Calendar.DATE, -dd.get(Calendar.DAY_OF_MONTH) + 1)
                        for (i in 1 until countBlock) {
                            val tmpTime: Int =
                                ((dd.time.time.longMinusTimeUTC() - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay).toFloat() / TimeUnits.DAY.milliseconds.toFloat()).toInt()
                            ramk.moveTo(padding + widthDay * tmpTime, 0f)
                            ramk.lineTo(padding + widthDay * tmpTime, canvasHeight)
                            dd.add(Calendar.MONTH, 1)
                        }
                    }
//            ramk.addRect(Rect(Offset(0f, 0f), Size(canvasWidth, canvasHeight)))
                    drawPath(
                        ramk,
                        if (year) color_shkala.copy(alpha = 0.5f) else color_between_days,
                        style = Stroke(Hairline.toPx())
                    )
                    if (!year) {
                        dd.add(Calendar.DATE, -countBlock)
                        for (i in 0 until countBlock) {
                            dd.get(Calendar.DAY_OF_MONTH).let { dayWeek ->
                                if (dayWeek == 1) drawLine(
                                    color_shkala,//.copy(alpha = 0.2f),
                                    Offset(i * widthDay + padding, 0f),
                                    Offset(i * widthDay + padding, canvasHeight),
//                            Size(widthDay, canvasHeight)
                                )
                                if (dayWeek % 5 == 1) {
                                    if (dayWeek != 1) drawLine(
                                        color_shkala,//.copy(alpha = 0.5f),
                                        Offset(i * widthDay + padding, 0f),
                                        Offset(i * widthDay + padding, canvasHeight)
//                            Size(widthDay, canvasHeight)
                                    )
                                }
                            }
                            dd.add(Calendar.DATE, 1)
                        }
                    }

                    val strokeWidth = 2.dp.toPx()
                    drawRect(
                        color,
                        Offset(startTime * widthDay + padding, strokeWidth),
                        Size(
                            if ((endTime - startTime + 1) * widthDay > 0) (endTime - startTime + 1) * widthDay else 1f,
                            if (canvasHeight - 2 * strokeWidth > 0) canvasHeight - 2 * strokeWidth else 1f
                        ),
                        style = Stroke(strokeWidth)
                    )
                    listDate.filter { it >= dateStart && it <= dateEnd }.forEach {
                        val posDate: Int =
                            ((it.unOffset() - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay) / TimeUnits.DAY.milliseconds).toInt()
                        if (year) drawOval(
                            color_point_work,
                            Offset(posDate * widthDay + padding, canvasHeight / 3f),
                            Size(widthDay, canvasHeight / 3f)
                        ) else drawCircle(
                            color_point_work,
                            widthDay / 3f,
                            Offset((posDate + 0.5f) * widthDay + padding, canvasHeight / 2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShkalSrokPodpis(
    colorsState: TimelineDiagramColorsState,
    bottom: Boolean = false,
    dateOpor: Long = Date().time,
    modifier: Modifier = Modifier.width(150.dp).height(15.dp),
    color: Color = Color.Green,
    year: Boolean = true
) {
    with(colorsState) {
        with(LocalDensity.current) {

            Canvas(
                modifier = modifier.clip(RectangleShape)
            ) {
                with(this@with) {

                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val dateCurr: Long = Date().time.longMinusTimeUTC()
                    val beforeDay = if (year) beforeDayYearPriv else beforeDayPriv
                    val currTime: Int =
                        ((dateCurr - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay).toFloat() / TimeUnits.DAY.milliseconds.toFloat()).toInt()

                    val dd = Calendar.getInstance().apply {
                        time = Date(dateOpor).add(-beforeDay, TimeUnits.DAY)
                    }//.apply { set(Calendar.DAY_OF_MONTH,1) }

                    val padding = paddingPriv.toPx()
                    val countBlock = if (year) ((canvasWidth - padding * 2) / widthDayYearPriv.toPx() / 30f).toInt() + 3
                    else ((canvasWidth - padding * 2) / widthDayPriv.toPx()).toInt()
                    val widthDay = if (year) widthDayYearPriv.toPx() else widthDayPriv.toPx()

                    val sizeText = canvasHeight / 2f // 20.sp.toPx()
                    val heightKray = canvasHeight / 5f

                    if (!year) for (i in 0 until countBlock) {
                        dd.get(Calendar.DAY_OF_WEEK).let { dayWeek ->
                            if (dayWeek == Calendar.SATURDAY || dayWeek == Calendar.SUNDAY) drawRect(
                                color_weekend,
                                Offset(i * widthDay + padding, if (bottom) 0f else canvasHeight - heightKray),
                                Size(widthDay, heightKray)
                            )
                        }
                        dd.add(Calendar.DATE, 1)
                    }

                    drawRect(
                        if (year) color_current.copy(alpha = 1.0f) else color_current.copy(alpha = 0.3f),
                        Offset(currTime * widthDay + padding, if (bottom) 0f else canvasHeight - heightKray),
                        Size(widthDay, heightKray)
                    )

                    val p2 = Paint().apply {
                        style = PaintingStyle.Fill
                        this.color = color_year
                    }
                    dd.time = Date(dateOpor).add(-beforeDay, TimeUnits.DAY)
                    if (year) {
                        if (dd.get(Calendar.MONTH) < 11) drawIntoCanvas { // && dd.get(Calendar.MONTH) != 0
                            val textL = TextLine.make(dd.time.format("yyyy"), Font().apply {
                                size = sizeText
                            })
                            textL.width
                            it.nativeCanvas.drawTextLine(
                                textL,
                                0.5f * widthDay + padding,
                                if (bottom) canvasHeight - sizeText / 4f else canvasHeight / 2f - sizeText / 4f, // (canvasHeight - sizeText)/2f + sizeText,
                                p2.asFrameworkPaint()
                            )
                        }
                    } else if (dd.get(Calendar.DAY_OF_MONTH) < 21 && dd.get(Calendar.DAY_OF_MONTH) != 1) drawIntoCanvas {
                        val textL = TextLine.make(dd.time.format("LLLL yyyy"), Font().apply {
                            size = sizeText
                        })
                        textL.width
                        it.nativeCanvas.drawTextLine(
                            textL,
                            0.5f * widthDay + padding,
                            if (bottom) canvasHeight - sizeText / 4f else canvasHeight / 2f - sizeText / 4f, // (canvasHeight - sizeText)/2f + sizeText,
                            p2.asFrameworkPaint()
                        )
                    }
                    if (year) {
//                    val ramk = Path()
                        dd.add(Calendar.DATE, -dd.get(Calendar.DAY_OF_MONTH) + 1)
                        for (i in 1 until countBlock) {
                            val tmpTime: Int =
                                ((dd.time.time.longMinusTimeUTC() - dateOpor.longMinusTimeUTC() + TimeUnits.DAY.milliseconds * beforeDay).toFloat() / TimeUnits.DAY.milliseconds.toFloat()).toInt()
//                        ramk.moveTo(padding + widthDay * tmpTime, 0f)
//                        ramk.lineTo(padding + widthDay * tmpTime, canvasHeight)

                            drawIntoCanvas {
                                val textL = TextLine.make(dd.time.format("MMM"), Font().apply {
                                    size = sizeText * 2f / 3f
                                })
                                textL.width
                                it.nativeCanvas.drawTextLine(
                                    textL,
                                    (tmpTime + 2) * widthDay + padding,
                                    if (!bottom) canvasHeight - sizeText / 4f else canvasHeight / 2f - sizeText / 4f, // (canvasHeight - sizeText)/2f + sizeText,
                                    Paint().apply {
                                        style = PaintingStyle.Fill
                                        this.color = color_months_days// MyColorARGB.colorMyBorderStroke.toColor()
                                    }.asFrameworkPaint()
                                )
                            }
                            dd.get(Calendar.MONTH).let { month ->
                                if (month == 0) {
                                    drawLine(
                                        color_shkala,//.copy(alpha = 0.2f),
                                        Offset(tmpTime * widthDay + padding, 0f),
                                        Offset(tmpTime * widthDay + padding, canvasHeight),
//                            Size(widthDay, canvasHeight)
                                    )
                                    if (countBlock - i > 2 && i > 1) drawIntoCanvas {
                                        val textL = TextLine.make(dd.time.format("yyyy"), Font().apply {
                                            size = sizeText
                                        })
                                        textL.width
                                        it.nativeCanvas.drawTextLine(
                                            textL,
                                            (tmpTime + 2) * widthDay + padding,
                                            if (bottom) canvasHeight - sizeText / 4f else canvasHeight / 2f - sizeText / 4f, // (canvasHeight - sizeText)/2f + sizeText,
                                            p2.asFrameworkPaint()
                                        )
                                    }
                                } else {
                                    drawLine(
                                        color_shkala,//.copy(alpha = 0.2f),
                                        Offset(
                                            tmpTime * widthDay + padding,
                                            if (bottom) 0f else canvasHeight - heightKray
                                        ),
                                        Offset(tmpTime * widthDay + padding, if (bottom) heightKray else canvasHeight)
//                            Size(widthDay, canvasHeight)
                                    )
                                }
                            }
                            dd.add(Calendar.MONTH, 1)
                        }
/*
                    drawPath(
                        ramk,
                        if (year) Color.White.copy(alpha = 0.5f) else Color.Black.copy(0.7f),
                        style = Stroke(Hairline.toPx())
                    )
*/
                    } else for (i in 0 until countBlock) {
                        dd.get(Calendar.DAY_OF_MONTH).let { dayWeek ->
                            if (dayWeek == 1) {
                                drawLine(
                                    color_shkala,//.copy(alpha = 0.2f),
                                    Offset(i * widthDay + padding, 0f),
                                    Offset(i * widthDay + padding, canvasHeight),
//                            Size(widthDay, canvasHeight)
                                )
                                if (countBlock - i > 7) drawIntoCanvas {
                                    val textL = TextLine.make(dd.time.format("LLLL yyyy"), Font().apply {
                                        size = sizeText
                                    })
                                    textL.width
                                    it.nativeCanvas.drawTextLine(
                                        textL,
                                        (i + 0.5f) * widthDay + padding,
                                        if (bottom) canvasHeight - sizeText / 4f else canvasHeight / 2f - sizeText / 4f, // (canvasHeight - sizeText)/2f + sizeText,
                                        p2.asFrameworkPaint()
                                    )
                                }

                            }
                            if (dayWeek % 5 == 1) {
                                if (dayWeek != 1) drawLine(
                                    color_shkala,//.copy(alpha = 0.2f),
                                    Offset(i * widthDay + padding, if (bottom) 0f else canvasHeight - heightKray),
                                    Offset(i * widthDay + padding, if (bottom) heightKray else canvasHeight)
//                            Size(widthDay, canvasHeight)
                                )
                                if (dayWeek != 31) drawIntoCanvas {
                                    val textL = TextLine.make(dd.time.format("dd"), Font().apply {
                                        size = sizeText * 2f / 3f
                                    })
                                    textL.width
                                    it.nativeCanvas.drawTextLine(
                                        textL,
                                        (i + 0.5f) * widthDay + padding,
                                        if (!bottom) canvasHeight - sizeText / 4f else canvasHeight / 2f - sizeText / 4f, // (canvasHeight - sizeText)/2f + sizeText,
                                        Paint().apply {
                                            style = PaintingStyle.Fill
                                            this.color = color_months_days// MyColorARGB.colorMyBorderStroke.toColor()
                                        }.asFrameworkPaint()
                                    )
                                }
                            }
                        }
                        dd.add(Calendar.DATE, 1)
                    }
                }
            }
        }
    }
}
