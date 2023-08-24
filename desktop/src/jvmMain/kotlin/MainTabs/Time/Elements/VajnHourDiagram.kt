package MainTabs.Time.Elements

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.MyRectF
import extensions.toColor
import org.jetbrains.skia.RRect
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemHourVajn

@Composable
fun VajnHourDiagram(hours: List<ItemHourVajn>, modifier: Modifier = Modifier) {
    val otstupOut = 3.dp
    val otstupIn = 2.dp
    val otstupBetween = Dp.Hairline // 1.5.dp
    val otstupSum = otstupOut + otstupIn
    with(LocalDensity.current) {
        Canvas(
            modifier = modifier
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            var boxFr = 10F
            var p1 = Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Stroke
                strokeWidth = 1f
                color = Color.Red
            }
            if (canvasHeight < canvasWidth) {
                boxFr = canvasHeight
            } else {
                boxFr = canvasWidth
            }
//                            val area = MyAreaCanvas(fizAreaSize.value, fizAreaSize.value, boxFr)
/*
                            drawRect(
                                color = MyColorARGB.colorEffektShkal_Back.toColor(),// Color(0xFFefeb85),//fff589),
                                size = Size(canvasWidth, canvasHeight),
                            )
*/

            val rectRamk = MyRectF(canvasWidth, canvasHeight, 0F, 0F).apply {
                inset(otstupOut.toPx(), otstupOut.toPx())
            }

            val nrm = 24f

            var sumNedel = 0f
            var sumMonth = 0f
            var sumYear = 0f

            for (item in hours) {
                sumNedel += item.sumNedel.toFloat()
                sumMonth += item.sumMonth.toFloat()
                sumYear += item.sumYear.toFloat()
            }

            var koefMax = 1F
            val koefned: Float = (sumNedel / (nrm * 7))
            val koefMonth: Float = (sumMonth / (nrm * 30))
            val koefYear: Float = (sumYear / (nrm * 365))

            if (koefned > koefMax) koefMax = koefned
            if (koefMonth > koefMax) koefMax = koefMonth
            if (koefYear > koefMax) koefMax = koefYear
            var pBackgbColor = MyColorARGB.colorEffektShkal_Back
//        if (koefMonth + koefYear + koefned > 1.5) pBackgbColor = MyColorARGB.colorEffektShkal_BackRed

            if (koefMax > 1F) {
//            if (item.norma>0){
                rectRamk.width = rectRamk.width / koefMax
//            }   else    {
//                rectRamk.left = rectRamk.right - rectRamk.width()/koefMax
//            }
            }

            val innerRectHeight = canvasHeight - 2 * otstupSum.toPx()

            val rectNedelBack = MyRectF(rectRamk).apply {
                height = height * 3 / 5
            }


            fun drawMyRect(rectF: MyRectF, color: Color, stroke: Boolean = false) = drawIntoCanvas {
                val rr = RRect.makeLTRB(
                    l = rectF.offsetX,
                    t = rectF.offsetY,
                    r = rectF.offsetX + rectF.width,
                    b = rectF.offsetY + rectF.height,
                    3.dp.value
                )
                it.nativeCanvas.drawRRect(
                    rr,
                    p1.apply {
                        this.color = color; style = if (stroke) PaintingStyle.Stroke else PaintingStyle.Fill
                    }.asFrameworkPaint()
                )
            }
            drawMyRect(rectRamk, pBackgbColor.plusWhite(4f).toColor())
            drawRoundRect(
                color = Color.Black,
                size = Size(canvasWidth, canvasHeight),
                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                style = Stroke(
                    width = 3.dp.toPx(),
//                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            )
            drawRoundRect(
                color = Color.White,
                size = Size(canvasWidth, canvasHeight),
                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                style = Stroke(
                    width = 2.dp.toPx(),
//                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            )
            val rectSleep = MyRectF(
                rectRamk.width * (2f / 24f),
                canvasHeight,
                otstupIn.toPx() + rectRamk.width * (7f / 24f),
                0F
            ).apply {
//            inset(0f, 0)
            }
            drawMyRect(rectSleep, MyColorARGB.colorEffektShkal_Month.toColor())
            /*drawLine(
                Color.Red,
                Offset(otstupIn.toPx() + rectRamk.width * (8f/24f),0f),
                Offset(otstupIn.toPx() + rectRamk.width * (8f/24f),this.size.height), // 2*otstupIn.toPx() + rectRamk.height
                4f
            )*/
//                        drawRoundRect(
//                            color = pBackgbColor.plusWhite(4f).toColor(),
//                            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
//                            topLeft = rectRamk.offset,
//                            size = rectRamk.size,
//                        )

            var otstupNedel = otstupIn.toPx()
            var otstupMonth = otstupIn.toPx()
            var otstupYear = otstupIn.toPx()
            for (vajnHour in hours.sortedBy { it.vajn }) {
                val rectNedel = MyRectF(rectRamk).apply {
                    inset(otstupNedel, otstupIn.toPx())
                    height = innerRectHeight * 3 / 5 - otstupBetween.toPx() / 2
                }
                rectNedel.width = rectRamk.width * (vajnHour.sumNedel.toFloat() / (24f * 7f))
                val colorHour = when (vajnHour.vajn) {
                    -1L -> MyColorARGB.colorSleep.toColor()
                    0L -> Color(0xFFFFF42B)
                    1L -> Color(0xFFFFFFFF)
                    2L -> Color(0xFF7FFAF6)
                    3L -> Color(0xFFFF5858)
                    else -> Color(0xFFFF5858)
                }
                drawRoundRect(
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    color = colorHour,
                    topLeft = rectNedel.offset,
                    size = rectNedel.size,
                )
                drawRoundRect(
                    color = Color(0xAA000000),
                    topLeft = rectNedel.offset,
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    size = rectNedel.size,
                    style = Stroke(
                        width = 1.dp.toPx(),
                    )
                )
                otstupNedel += rectNedel.width

                val rectMonthBack = MyRectF(rectRamk).apply {
                    offsetY += height * 3 / 5
                    height = innerRectHeight * 2 / 5 * 3 / 5
                }
                val rectMonth = MyRectF(rectMonthBack).apply {
                    inset(otstupMonth, otstupBetween.toPx() / 2)
                }
                rectMonth.width = rectRamk.width * (vajnHour.sumMonth.toFloat() / (24f * 30f))
                drawRoundRect(
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    color = colorHour,
                    topLeft = rectMonth.offset,
                    size = rectMonth.size,
                )
                drawRoundRect(
                    color = Color(0xAA000000),
                    topLeft = rectMonth.offset,
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    size = rectMonth.size,
                    style = Stroke(
                        width = 1.dp.toPx(),
                    )
                )
                otstupMonth += rectMonth.width

                val rectYearBack = MyRectF(rectRamk).apply {
                    offsetY += height * 3 / 5 + innerRectHeight * 2 / 5 * 3 / 5
                    height -= height * 3 / 5 + innerRectHeight * 2 / 5 * 3 / 5
                }
                val rectYear = MyRectF(rectYearBack).apply {
                    inset(otstupYear, 0F)
                    offsetY += otstupBetween.toPx() / 2
                    height -= otstupBetween.toPx() / 2
                    height -= otstupIn.toPx()
                }
                rectYear.width = rectRamk.width * (vajnHour.sumYear.toFloat() / (24f * 365f))
                drawMyRect(rectYear, colorHour)
                drawMyRect(rectYear, Color(0xAA000000), true)
                otstupYear += rectYear.width


            }
        }
    }

}