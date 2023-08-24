package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
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
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemRectDiag
import ru.ragefalcon.sharedcode.models.data.ItemTwoRectDiag
import ru.ragefalcon.sharedcode.models.data.ItemYearGraf

class DrawRectDiagram {

    var shir = 12.dp
    var otstup = 2.dp
    var otstupUp = 45.dp
    var otstupYear = 9.dp
    var otstupBottom = 0.dp

    fun calcWidth(countYear: Int): Dp = ((shir + otstup * 2) * 12 + otstupYear) * countYear

    var drawCursorSum: DrawScope.() -> Unit = {}

    val textSizeSp = 13

    fun getThisTextLine(text: String, textS: Float) = TextLine.make(text, Font(
        Typeface.makeFromName(
            "sadf",
            FontStyle.NORMAL
        )
    ).apply {
        size = textS
        this.edging = FontEdging.ANTI_ALIAS
    })

    fun getShirPodpis(item: ItemRectDiag, textSize: Float): Float =
        getThisTextLine(") ${item.month}", textSize).width +
                getThisTextLine(" ${item.summa.roundToStringProb(1)} ", textSize).width +
                getThisTextLine("(", textSize).width

    private fun drawRect(
        canvas: DrawScope, maxVal: Float, x: Float, item: ItemRectDiag,
        isPressed: Boolean,
        density: Density
    ): Float {
        with(density) {
            val rr = MyRectF(
                shir.toPx(),
                maxVal * item.procent.toFloat(),
                otstup.toPx() + x,
                16.dp.toPx() + otstupUp.toPx() + maxVal * (1 - item.procent.toFloat()),
            )

            val select = //false
                rr.offsetX + yearOffset < cursorPosition.value.x && rr.offsetX + rr.width + yearOffset > cursorPosition.value.x

            canvas.apply {
                drawMyRoundRect(
                    CornerRadius(0F, 0F),
                    if (select && isPressed) colorSelect else colorDiagLite,
                    Fill,
                    rr
                )

                drawMyRoundRect(
                    CornerRadius(0F, 0F),
                    colorDiag,
                    Stroke(width = 1f), rr
                )
                val textSize = textSizeSp.sp.toPx()
                if (select && isPressed) {
                    val tmp = yearOffset
                    val textSize1 = 12.sp.toPx()
                    drawCursorSum = {
                        drawIntoCanvas {
                            val textL = TextLine.make(
                                item.summa.roundToStringProb(1), Font(
                                    Typeface.makeFromName(
                                        "sadf",
                                        FontStyle.BOLD
                                    )
                                ).apply {
                                    size = textSize1
                                    this.edging = FontEdging.ANTI_ALIAS
                                })
                            it.nativeCanvas.drawTextLine(
                                textL,
                                textSize1 + x - textL.width / 2 - shir.toPx() / 2 + tmp,
                                rr.offsetY - shir.toPx() / 2, // 24.dp.toPx() + otstupUp.toPx() + maxVal,
                                Paint().apply {
                                    isAntiAlias = true
                                    style = PaintingStyle.Stroke
                                    strokeWidth = 2.dp.toPx()
                                    color = colorDiagHourStroke
                                }.asFrameworkPaint()
                            )
                            it.nativeCanvas.drawTextLine(
                                textL,
                                textSize1 + x - textL.width / 2 - shir.toPx() / 2 + tmp,
                                rr.offsetY - shir.toPx() / 2, // 24.dp.toPx() + otstupUp.toPx() + maxVal,
                                Paint().apply {
                                    isAntiAlias = true
                                    style = PaintingStyle.Fill
//                                    strokeWidth = 1f
                                    color = colorDiagHour
                                }.asFrameworkPaint()
                            )
                        }
                    }
                }

                val textMonth = getThisTextLine(") ${item.month}", textSize)
                val textRasx = getThisTextLine(" ${item.summa.roundToStringProb(1)} ", textSize)
                val textSkobka = getThisTextLine("(", textSize)

                val offset = Offset(textSize + x, 24.dp.toPx() + otstupUp.toPx() + maxVal)
                var coorX = offset.x

                fun thisDrawTExt90(text: TextLine, color1: Color) = rotate(-90f, offset) {
                    coorX -= text.width
                    drawIntoCanvas {
                        it.nativeCanvas.drawTextLine(
                            text,
                            coorX,
                            offset.y,
                            Paint().apply {
                                isAntiAlias = true
                                style = PaintingStyle.Fill
                                color = color1
                            }.asFrameworkPaint()
                        )
                    }
                }
                thisDrawTExt90(textMonth, colorDiagTextMonthBelow)
                thisDrawTExt90(textRasx, colorDiagTextHourBelow)
                thisDrawTExt90(textSkobka, colorDiagTextMonthBelow)

/*
                rotate(-90f, Offset(textSize + x, 24.dp.toPx() + otstupUp.toPx() + maxVal)) {
                    drawIntoCanvas {
                        val textL = TextLine.make("( ${item.summa.roundToStringProb(1)} ) ${item.month}", Font(
                            Typeface.makeFromName(
                                "sadf",
                                FontStyle.BOLD
                            )
                        ).apply {
                            size = textSize
                            this.edging = FontEdging.ANTI_ALIAS
                        })
                        it.nativeCanvas.drawTextLine(
                            textL,
                            textSize + x - textL.width,
                            24.dp.toPx() + otstupUp.toPx() + maxVal,
                            p_text_stroke.asFrameworkPaint()
                        )
                        it.nativeCanvas.drawTextLine(
                            textL,
                            textSize + x - textL.width,
                            24.dp.toPx() + otstupUp.toPx() + maxVal,
                            p_text_fill.asFrameworkPaint()
                        )
                    }
                }
*/
            }
            return x + shir.toPx() + 2 * otstup.toPx()
        }
    }

    private fun drawYear(
        canvas: DrawScope, height: Float, year: ItemYearGraf,
        isPressed: Boolean,
        density: Density
    ): Float {

        with(density) {
            val p1 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Fill
                color = colorFillText //Color.White//.copy(0.8f)
//            strokeWidth = 1f
            }
            val p2 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Stroke
                strokeWidth = 2.dp.toPx()
                color = colorStrokeText // Color.Black
            }

            var x = otstupYear.toPx()

            val maxVal: Float = height - otstupUp.toPx() - otstupBottom.toPx()



            canvas.apply {
                drawLine(
                    colorRazdelit,// colorDiag.toMyColorARGB().plusWhite().toColor().copy(alpha = 0.7f),
                    Offset(4.5.dp.toPx(), 16.dp.toPx() + maxVal + otstupUp.toPx()),
                    Offset(4.5.dp.toPx(), 6.dp.toPx()),
                    1f
                )
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
                p1.color = colorDiagHour
                drawIntoCanvas {
                    val textL = TextLine.make(year.month.firstOrNull()?.sumyear?.roundToStringProb(1), Font(
                        Typeface.makeFromName(
                            "sadf",
                            FontStyle.BOLD
                        )
                    ).apply {
                        size = 16.sp.toPx()
                    })
                    p2.color = colorDiagHourStroke
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 43.dp.toPx(), p2.asFrameworkPaint())
                    it.nativeCanvas.drawTextLine(textL, 20.dp.toPx(), 43.dp.toPx(), p1.asFrameworkPaint())
                }
                for (type in year.month.asReversed()) {
                    x = drawRect(this, maxVal, x, type, isPressed, density)
                }
            }
            return x
        }
    }

    val cursorPosition = mutableStateOf(Offset(0f, 0f))
    var yearOffset = 0f
    var darkBack = true
    var colorFillText = Color.White
    var colorStrokeText = Color.Black
    var colorDiag = Color.Red
    var colorDiagLite = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()
    var colorDiagHour = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()

    var colorDiagHourStroke = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()
    var colorDiagTextMonthBelow = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()
    var colorDiagTextHourBelow = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()
    var colorRazdelit = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()
    var colorSelect = Color.Red.toMyColorARGB().plusWhite(1.3f).toColor()

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @Composable
    fun drawDiagram(
        modifier: Modifier = Modifier,
        yearDiag: List<ItemYearGraf>,
        darkBackground: Boolean = true,
        colorDiagram: Color = Color.Red,
        styleState: RectDiagramColorStyleState? = null
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressedBy = remember { mutableStateOf(false) } // by interactionSource.collectIsPressedAsState()
        val interactions = remember { mutableStateListOf<Interaction>() }

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
//                        println("PressInteraction.Press")
                        isPressedBy.value = true
                        interactions.add(interaction)
                    }
                    is PressInteraction.Release -> {
                        isPressedBy.value = false
//                        println("PressInteraction.Release")
                        interactions.remove(interaction.press)
                    }
                    is PressInteraction.Cancel -> {
                        isPressedBy.value = false
//                        println("PressInteraction.Cancel")
                        interactions.remove(interaction.press)
                    }
                    is DragInteraction.Start -> {
//                        println("PressInteraction.Start")
                        interactions.add(interaction)
                    }
                    is DragInteraction.Stop -> {
//                        println("PressInteraction.Stop")
                        interactions.remove(interaction.start)
                    }
                    is DragInteraction.Cancel -> {
//                        println("PressInteraction.Cancel")
                        interactions.remove(interaction.start)
                    }
                }
            }
        }

        darkBack = darkBackground
        colorDiag = styleState?.color_rect_border ?: colorDiagram
        colorDiagLite = styleState?.color_rect ?: colorDiagram.toMyColorARGB().plusWhite(1.3f).toColor()
        colorDiagHour = styleState?.color_hour ?: if (darkBack) colorDiagram.toMyColorARGB().plusWhite(1.3f)
            .toColor() else colorDiagram
        colorFillText = styleState?.color_year ?: if (darkBack) Color.White else Color.Black
        colorStrokeText = styleState?.color_year_border ?: if (darkBack) Color.Black else Color.White

        colorDiagHourStroke = styleState?.color_hour_border ?: if (darkBack) Color.Black else Color.White
        colorDiagTextMonthBelow = styleState?.color_month ?: colorFillText
        colorDiagTextHourBelow = styleState?.color_hour_2 ?: colorDiag
        colorRazdelit = styleState?.color_razdelit ?: colorDiag
        colorSelect = styleState?.color_rect_select ?: Color.White
        BoxWithHScrollBar(
            modifier, scroll = rememberScrollState(0), dark = !darkBack, reverse = true
        ) { scrollSt ->
            Row(
                Modifier.horizontalScroll(scrollSt, reverseScrolling = true)
            ) {
                with(LocalDensity.current) {
//                    Box{
                    yearDiag.forEach {
                        it.month.forEach {
                            getShirPodpis(it, textSizeSp.sp.toPx()).toDp().let {
                                if (it > otstupBottom - 25.dp) otstupBottom = it + 25.dp
                            }
                        }
                    }

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
                                        this@with
                                    )
                                }
                            }
                            if (isPressedBy.value) drawCursorSum.invoke(this)
                        }
                    }
//                    }
                }
            }
        }
    }
}



