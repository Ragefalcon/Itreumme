package common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.*
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemOperWeek
import viewmodel.MainDB
import java.util.*

class DrawGrafik {

    var shir = 12.dp
    var otstup = 2.dp
    var otstupUp = 45.dp
    var otstupYear = 9.dp
    var otstupBottom = 100.dp
    var timeOneWeek = 7 * 24 * 60 * 60 * 1000
    var p1 = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Fill
        strokeWidth = 1f
        color = Color.Red.toMyColorARGB().plusWhite().plusWhite().toColor()
    }


    var shagBase = 2.dp
    var yearLast: Int = 0
    var yearFirst: Int = 0
    var yearCount = 1

    fun getWidthGraf(
        list: List<ItemOperWeek>, density: Density
    ): Float {
        with(density) {
            val shag = shagBase.toPx()
            if (list.isNotEmpty()) {
                val last = Date(list.lastOrNull()?.data ?: 0L).format("yyyy").toInt()
                val first = Date(list.firstOrNull()?.data ?: 0L).format("yyyy").toInt()

                yearLast = maxOf(last, first)
                yearFirst = minOf(last, first)

                yearCount = yearLast - yearFirst + 1
            }
            return yearCount * 52.1786F * shag + 20.dp.toPx()
        }
    }

    fun drawGraf(
        canvas: DrawScope,
        list: List<ItemOperWeek>,
        minSumOperWeek: Float,
        maxSumOperWeek: Float,
        isPressed: Boolean,
        density: Density
    ) {
        with(density) {
            val shag = shagBase.toPx()
            val maxVal = canvas.size.height * 2 / 3
            val graf = Path()
            val otstupVerx = 80.dp.toPx()
            val otstupNiz = 20.dp.toPx()
            val hh = canvas.size.height.toFloat()
            val hhGraph = hh - otstupVerx - otstupNiz
            val diap = if (minSumOperWeek < 0) maxSumOperWeek - minSumOperWeek else maxSumOperWeek
            val minusMin = if (minSumOperWeek < 0) minSumOperWeek else 0.0F

            val pY = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Fill
                color = Color.Yellow
            }
            val pY2 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Fill
                color = Color.Green
            }
            val p2 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Stroke
                strokeWidth = 2f
                color = Color.Green.toMyColorARGB().toColor()
            }

            canvas.apply {
                /**
                 * Отрисовка полосок между годами
                 * */
                var date = list.firstOrNull()?.data?.let { Date(it).format("yyyy").toLong() } ?: 0L
                var numYear = 0
                val shagYear = 52.1786F * shag
                var key = false
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = list.firstOrNull()?.data ?: 0L
                }

                val weekOfYear = calendar[Calendar.WEEK_OF_YEAR]
                drawLine(
                    styleRD.color_razdelit,
                    Offset(numYear * shagYear - 1.0F, hh),
                    Offset(numYear * shagYear - 1.0F, otstupVerx / 2 - otstupNiz * 3 / 4),
                    1f
                )
                drawIntoCanvas {
                    val textL = TextLine.make(date.toString(), Font().apply {
                        size = 20.sp.toPx()
                    })
                    p2.color = styleRD.color_year_stroke
                    it.nativeCanvas.drawTextLine(
                        textL,
                        10.dp.toPx() + numYear * shagYear,
                        24.dp.toPx(),
                        p2.asFrameworkPaint()
                    )
                    pY.color = styleRD.color_year
                    it.nativeCanvas.drawTextLine(
                        textL,
                        10.dp.toPx() + numYear * shagYear,
                        24.dp.toPx(),
                        pY.asFrameworkPaint()
                    )
                }

                for (item in list) {
                    val delta = Date(item.data).format("yyyy").toLong() - date
                    if (delta > 0) {
                        for (i in 1..delta) {
                            numYear++
                            date++
                            drawLine(
                                styleRD.color_razdelit,
                                Offset(numYear * shagYear - 1.0F, hh),
                                Offset(numYear * shagYear - 1.0F, otstupVerx / 2 - otstupNiz * 3 / 4),
                                1f
                            )
                            drawIntoCanvas {
                                val textL = TextLine.make(date.toString(), Font().apply {
                                    size = 20.sp.toPx()
                                })
                                p2.color = styleRD.color_year_stroke
                                it.nativeCanvas.drawTextLine(
                                    textL,
                                    10.dp.toPx() + numYear * shagYear,
                                    24.dp.toPx(),
                                    p2.asFrameworkPaint()
                                )
                                pY.color = styleRD.color_year
                                it.nativeCanvas.drawTextLine(
                                    textL,
                                    10.dp.toPx() + numYear * shagYear,
                                    24.dp.toPx(),
                                    pY.asFrameworkPaint()
                                )
                            }

                            drawIntoCanvas {
                                val textL = TextLine.make(item.sumCap.roundToStringProb(2), Font().apply {
                                    size = 12.sp.toPx()
                                })
                                pY2.color = styleRD.color_summa
                                it.nativeCanvas.drawTextLine(
                                    textL,
                                    10.dp.toPx() + numYear * shagYear,
                                    44.dp.toPx(),
                                    pY2.asFrameworkPaint()
                                )
                            }
                        }
                    }

                }

                /**
                 * Отрисовка горизонтальной оси нулевого баланса
                 * */
                drawLine(
                    styleRD.color_os,
                    Offset(
                        0.0F,
                        hh - otstupNiz + minusMin / diap * hhGraph
                    ),
                    Offset(
                        yearCount * 52.1786F * shag + 10.dp.toPx(),
                        hh - otstupNiz + minusMin / diap * hhGraph
                    ),
                    1f
                )

                var i = weekOfYear

                val xcur = ((cursorPosition.value.x + shag / 2) / shag).toInt()
                var hcur: Float? = null
                var curSum = 0.0
                graf.moveTo(i * shag, hh - otstupNiz + minusMin / diap * hhGraph)
                var lastDate: Long = list.firstOrNull()?.data ?: 0L
                for (item in list) {
                    i += ((item.data - lastDate) / timeOneWeek).toInt()
                    graf.lineTo(i * shag, hh - otstupNiz - (item.sumCap.toFloat() - minusMin) / diap * hhGraph)
                    if (isPressed) if (cursorPosition.value.y > hh - otstupNiz - hhGraph) if (i == xcur) {
                        hcur = hh - otstupNiz - (item.sumCap.toFloat() - minusMin) / diap * hhGraph
                        curSum = item.sumCap
                    }
                    lastDate = item.data
                }
                hcur?.let {
                    drawLine(
                        styleRD.color_os_cursor,
                        Offset(
                            0.0F,
                            it
                        ),
                        Offset(
                            yearCount * 52.1786F * shag + 10.dp.toPx(),
                            it
                        ),
                        1f
                    )
                    drawCircle(styleRD.color_cursor_stroke, 4.dp.toPx(), center = Offset(xcur * shag, it))
                    drawCircle(styleRD.color_cursor, 3.dp.toPx(), center = Offset(xcur * shag, it))
                    drawIntoCanvas {
                        val textL = TextLine.make(curSum.roundToStringProb(2), Font().apply {
                            size = 12.sp.toPx()
                        })
                        it.nativeCanvas.drawTextLine(
                            textL,
                            xcur * shag - textL.width / 2,
                            64.dp.toPx(),
                            Paint().apply {
                                isAntiAlias = true
                                style = PaintingStyle.Fill
                                color = styleRD.color_summa_cursor
                            }.asFrameworkPaint()
                        )
                    }
                }
                drawPath(graf, styleRD.color_graf_stroke, style = Stroke(2.5.dp.toPx()))
                drawPath(graf, styleRD.color_graf, style = Stroke(1.dp.toPx()))
                hcur?.let {
                    drawCircle(styleRD.color_cursor.copy(0.2f), 3.dp.toPx(), center = Offset(xcur * shag, it))
                }
            }
        }

    }

    val cursorPosition = mutableStateOf(Offset(0f, 0f))
    var yearOffset = 0f

    var darkBack = true
    var styleRD: GrafikColorStyleState =
        GrafikColorStyleState(MainDB.styleParam.finParam.doxodParam.GrafColor)

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun drawDiagram(
        modifier: Modifier = Modifier,
        list: List<ItemOperWeek>,
        minSumOperWeek: Float,
        maxSumOperWeek: Float,
        darkBackground: Boolean = true,
        styleDiag: GrafikColorStyleState?
    ) {
        darkBack = darkBackground
        styleDiag?.let { styleRD = it }

        val interactionSource = remember { MutableInteractionSource() }
        val isPressedBy by interactionSource.collectIsPressedAsState()

        with(LocalDensity.current) {
            BoxWithHScrollBar(
                modifier, scroll = rememberScrollState(0), dark = darkBackground, reverse = true
            ) { scrollSt ->
                Row(
                    Modifier.horizontalScroll(scrollSt, reverseScrolling = true)
                ) {
                    Surface(onClick = {},
                        interactionSource = interactionSource,
                        color = Color.Transparent,
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                            .padding(bottom = 10.dp)
                            .pointerMoveFilter(onMove = {
                                cursorPosition.value = it
                                false
                            }
                            )
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(getWidthGraf(list, this@with).toDp() + 26.dp)
                                .scrollVerticalToHorizontal(scrollSt)
                        ) {
                            drawGraf(this, list, minSumOperWeek, maxSumOperWeek, isPressedBy, this@with)
                        }
                    }
                }
            }
        }
    }
}