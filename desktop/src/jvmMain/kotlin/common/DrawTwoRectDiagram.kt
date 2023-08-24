package common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.*
import org.jetbrains.skia.*
import org.jetbrains.skia.TextLine
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemTwoRectDiag
import ru.ragefalcon.sharedcode.models.data.ItemYearGrafTwoRect
import viewmodel.MainDB

class DrawTwoRectDiagram {

    var shir = 6.dp
    var shirBetween = 2.dp
    var otstup = 2.dp
    var otstupUp = 60.dp
    var otstupYear = 9.dp
    var otstupBottom = 0.dp
    var p1 = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Fill
        strokeWidth = 1f
        color = Color.White
    }
    val textSizeSp = 13


    fun calcWidth(countYear: Int): Dp = ((shir * 2f + shirBetween + otstup * 2) * 12 + otstupYear) * countYear

    fun getThisTextLine(text: String, textS: Float) = TextLine.make(text, Font(
        Typeface.makeFromName(
            "sadf",
            FontStyle.NORMAL
        )
    ).apply {
        size = textS
        this.edging = FontEdging.ANTI_ALIAS
    })

    fun getShirPodpis(item: ItemTwoRectDiag, textSize: Float): Float =
        getThisTextLine(") ${item.month}", textSize).width +
                getThisTextLine(" ${item.summarasx.roundToStringProb(1)} ", textSize).width +
                getThisTextLine("/", textSize).width +
                getThisTextLine(" ${item.summadox.roundToStringProb(1)} ", textSize).width +
                getThisTextLine("(", textSize).width

    private fun drawTwoRect(
        canvas: DrawScope,
        maxVal: Float,
        x: Float,
        isPressed: Boolean,
        item: ItemTwoRectDiag,
        density: Density
    ): Float {
        with(density) {

            val r1 = MyRectF(
                shir.toPx(),
                maxVal * item.procentdox.toFloat(),
                otstup.toPx() + x,
                16.dp.toPx() + otstupUp.toPx() + maxVal * (1 - item.procentdox.toFloat()),
            )
            val r2 = MyRectF(
                shir.toPx(),
                maxVal * item.procentrasx.toFloat(),
                otstup.toPx() + x + shir.toPx() + shirBetween.toPx(),
                16.dp.toPx() + otstupUp.toPx() + maxVal * (1 - item.procentrasx.toFloat()),
            )

            val select =
                r1.offsetX + yearOffset < cursorPosition.value.x && r1.offsetX + 2 * shir.toPx() + shirBetween.toPx() + yearOffset > cursorPosition.value.x

            canvas.apply {
                drawMyRoundRect(
                    CornerRadius(0F, 0F),
                    if (select && isPressed) styleRD.color_rect_select else styleRD.color_rect,
                    Fill,
                    r1
                )
                drawMyRoundRect(
                    CornerRadius(0F, 0F),
                    if (select && isPressed) styleRD.color_rect_select_2 else styleRD.color_rect_2,
                    Fill,
                    r2
                )
                drawMyRoundRect(CornerRadius(0F, 0F), styleRD.color_rect_stroke, Stroke(width = 1f), r1)
                drawMyRoundRect(CornerRadius(0F, 0F), styleRD.color_rect_stroke_2, Stroke(width = 1f), r2)

                val textSize = textSizeSp.sp.toPx()
                if (select) {
                    val tmp = yearOffset
                    drawCursorSum = {
                        drawIntoCanvas {
                            val textSumDox = TextLine.make(
                                item.summadox.roundToStringProb(1), Font(
                                    Typeface.makeFromName(
                                        "sadf",
                                        FontStyle.BOLD
                                    )
                                ).apply {
                                    size = 12.sp.toPx()
                                    this.edging = FontEdging.ANTI_ALIAS
                                })
                            val textSumRasx = TextLine.make(
                                item.summarasx.roundToStringProb(1), Font(
                                    Typeface.makeFromName(
                                        "sadf",
                                        FontStyle.BOLD
                                    )
                                ).apply {
                                    size = 12.sp.toPx()
                                    this.edging = FontEdging.ANTI_ALIAS
                                })
                            val hhh = minOf(r1.offsetY, r2.offsetY)
                            it.nativeCanvas.drawTextLine(
                                textSumDox,
                                12.sp.toPx() + x - textSumDox.width / 2 - shir.toPx() / 2 + tmp,
                                hhh - shir.toPx() - textSumRasx.height,
                                Paint().apply {
                                    isAntiAlias = true
                                    style = PaintingStyle.Stroke
                                    strokeWidth = 2.dp.toPx()
                                    color = styleRD.color_summa_stroke
                                }.asFrameworkPaint()
                            )
                            it.nativeCanvas.drawTextLine(
                                textSumDox,
                                12.sp.toPx() + x - textSumDox.width / 2 - shir.toPx() / 2 + tmp,
                                hhh - shir.toPx() - textSumRasx.height,
                                Paint().apply {
                                    isAntiAlias = true
                                    style = PaintingStyle.Fill
//                                    strokeWidth = 1f
                                    color = styleRD.color_summa
                                }.asFrameworkPaint()
                            )
                            it.nativeCanvas.drawTextLine(
                                textSumRasx,
                                12.sp.toPx() + x - textSumRasx.width / 2 - shir.toPx() / 2 + tmp,
                                hhh - shir.toPx(), // 24.dp.toPx() + otstupUp.toPx() + maxVal,
                                Paint().apply {
                                    isAntiAlias = true
                                    style = PaintingStyle.Stroke
                                    strokeWidth = 2.dp.toPx()
                                    color = styleRD.color_summa_stroke_2
                                }.asFrameworkPaint()
                            )
                            it.nativeCanvas.drawTextLine(
                                textSumRasx,
                                12.sp.toPx() + x - textSumRasx.width / 2 - shir.toPx() / 2 + tmp,
                                hhh - shir.toPx(), // 24.dp.toPx() + otstupUp.toPx() + maxVal,
                                Paint().apply {
                                    isAntiAlias = true
                                    style = PaintingStyle.Fill
//                                    strokeWidth = 1f
                                    color = styleRD.color_summa_2
                                }.asFrameworkPaint()
                            )
                        }
                    }
                }


                val textMonth = getThisTextLine(") ${item.month}", textSize)
                val textRasx = getThisTextLine(" ${item.summarasx.roundToStringProb(1)} ", textSize)
                val textDrob = getThisTextLine("/", textSize)
                val textDox = getThisTextLine(" ${item.summadox.roundToStringProb(1)} ", textSize)
                val textSkobka = getThisTextLine("(", textSize)

                val offset = Offset(textSize + x, 24.dp.toPx() + otstupUp.toPx() + maxVal)
                var coorX = offset.x

                fun thisDrawTExt90(text: TextLine, color: Color) = rotate(-90f, offset) {
                    coorX -= text.width
                    p1.color = color
                    drawIntoCanvas {
                        it.nativeCanvas.drawTextLine(
                            text,
                            coorX,
                            offset.y,
                            p1.asFrameworkPaint()
                        )
                    }
                }
                thisDrawTExt90(textMonth, styleRD.color_month)
                thisDrawTExt90(textRasx, styleRD.color_summa_os_2)
                thisDrawTExt90(textDrob, styleRD.color_month)
                thisDrawTExt90(textDox, styleRD.color_summa_os)
                thisDrawTExt90(textSkobka, styleRD.color_month)
            }
            return x + shir.toPx() * 2 + shirBetween.toPx() + 2 * otstup.toPx()
        }
    }

    private fun drawYear(
        canvas: DrawScope, height: Float, year: ItemYearGrafTwoRect, isPressed: Boolean,
        density: Density
    ): Float {
        with(density) {


            val p1 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Fill
//            strokeWidth = 1f
                color = Color.Black
            }
            val p2 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Stroke
                strokeWidth = 2.0.dp.toPx()
                color = Color.Black
            }

            var x = otstupYear.toPx()


            val maxVal: Float = height - otstupUp.toPx() - otstupBottom.toPx()



            canvas.apply {
                drawLine(
                    styleRD.color_razdelit,
                    Offset(4.5.dp.toPx(), 16.dp.toPx() + maxVal + otstupUp.toPx()),
                    Offset(4.5.dp.toPx(), 6.dp.toPx()),
                    1.dp.toPx()
                )
                p1.color = styleRD.color_year
                p2.color = styleRD.color_year_stroke
                drawIntoCanvas {
                    val textL = TextLine.make("${year.year}", Font(
                        Typeface.makeFromName(
                            "sadf",
                            FontStyle.BOLD
                        )
                    ).apply {
                        size = 20.sp.toPx()
                    })
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 24.dp.toPx(), p2.asFrameworkPaint())
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 24.dp.toPx(), p1.asFrameworkPaint())
                }
                p1.color = styleRD.color_summa
                p2.color = styleRD.color_summa_stroke
                drawIntoCanvas {
                    val textL = TextLine.make(year.month.firstOrNull()?.sumyeardox?.roundToStringProb(1), Font(
                        Typeface.makeFromName(
                            "sadf",
                            FontStyle.BOLD
                        )
                    ).apply {
                        size = 16.sp.toPx()
                    })
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 44.dp.toPx(), p2.asFrameworkPaint())
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 44.dp.toPx(), p1.asFrameworkPaint())
                }
                p1.color = styleRD.color_summa_2
                p2.color = styleRD.color_summa_stroke_2
                drawIntoCanvas {
                    val textL = TextLine.make(year.month.firstOrNull()?.sumyearrasx?.roundToStringProb(1), Font(
                        Typeface.makeFromName(
                            "sadf",
                            FontStyle.BOLD
                        )
                    ).apply {
                        size = 16.sp.toPx()
                    })
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 64.dp.toPx(), p2.asFrameworkPaint())
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 64.dp.toPx(), p1.asFrameworkPaint())
                }
                for (type in year.month.asReversed()) {
                    x = drawTwoRect(this, maxVal, x, isPressed, type, density)
                }
//            for (i in year.month.count() + 1..12) {
//                x = drawTwoRect(this, maxVal, x, ItemRectDiag("asdf", "sdfsd", 0.0, 0.0, 0.0))
//            }
            }
            return x
        }
    }

    val cursorPosition = mutableStateOf(Offset(0f, 0f))
    var yearOffset = 0f
    var drawCursorSum: DrawScope.() -> Unit = {}

    var darkBack = true
    var styleRD: TwoRectDiagramColorStyleState =
        TwoRectDiagramColorStyleState(MainDB.styleParam.finParam.doxodParam.twoRectDiagColor)

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun drawDiagram(
        modifier: Modifier = Modifier,
        yearDiag: List<ItemYearGrafTwoRect>,
        darkBackground: Boolean = true,
        styleDiag: TwoRectDiagramColorStyleState? = null
//        density: Density
    ) {

        darkBack = darkBackground
        styleDiag?.let { styleRD = it }

        val interactionSource = remember { MutableInteractionSource() }
        val isPressedBy = remember { mutableStateOf(false) } // by interactionSource.collectIsPressedAsState()

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        isPressedBy.value = true
                    }
                    is PressInteraction.Release -> {
                        isPressedBy.value = false
                    }
                    is PressInteraction.Cancel -> {
                        isPressedBy.value = false
                    }
                }
            }
        }

        with(LocalDensity.current) {
            yearDiag.forEach {
                it.month.forEach {
                    getShirPodpis(it, textSizeSp.sp.toPx()).toDp().let {
                        if (it > otstupBottom - 25.dp) otstupBottom = it + 25.dp
                    }
                }
            }

            BoxWithHScrollBar(
                modifier, scroll = rememberScrollState(0), dark = darkBack, reverse = true
            ) { scrollSt ->
                Row(
                    Modifier.horizontalScroll(scrollSt, reverseScrolling = true)
                ) {
                    Surface(onClick = {},
                        interactionSource = interactionSource,
                        indication = null,
                        color = Color.Transparent,
                        modifier = Modifier//.matchParentSize()
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
                                .scrollVerticalToHorizontal(scrollSt)
                                .width(calcWidth(yearDiag.size) + 26.dp)
                        ) {
                            yearOffset = 0f
                            drawCursorSum = {}
                            if (yearDiag.size > 0) for (year in yearDiag) {
                                translate(left = yearOffset, top = 0f) {
                                    yearOffset += drawYear(
                                        this,
                                        size.height,
                                        year,
                                        isPressedBy.value,
                                        this@with//density
                                    )
                                }
                            }
                            if (isPressedBy.value) drawCursorSum.invoke(this)
                        }
                    }
                }
            }
        }
    }
}
