package MainTabs.Time.Items

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import common.MyButtDropdownMenuStyle2
import common.MyCardStyle1
import common.SingleSelectionType
import extensions.ItemEffectStyleState
import extensions.MyRectF
import extensions.RowVA
import extensions.toColor
import org.jetbrains.skia.RRect
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemEffekt

class ComItemEffekt(
    val item: ItemEffekt,
    val selection: SingleSelectionType<ItemEffekt>,
    val selFun: (ItemEffekt) -> Unit = {},
    val editable: Boolean = true,
    val itemEffectStyleState: ItemEffectStyleState,
    val dropMenu: @Composable ColumnScope.(ItemEffekt, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expanded = mutableStateOf(false)

    fun isActive(): Boolean {
        selection.selected?.let {
            if (it.id == item.id) return true
        }
        return false
    }

    private val otstupOut = 3.dp
    private val otstupIn = 2.dp
    private val otstupBetween = Dp.Hairline
    private val otstupSum = otstupOut + otstupIn

    @Composable
    fun getComposable() {
        with(itemEffectStyleState) {
            MyCardStyle1(
                selection.isActive(item), 0, {
                    selection.selected = item
                    selFun(item)
                },
                styleSettings = itemEffectStyleState,
                dropMenu = { exp -> dropMenu(item, exp) }
            ) {
                with(LocalDensity.current) {
                    Column {

                        RowVA {
                            Column(Modifier.weight(1F)) {
                                Text(
                                    modifier = Modifier.padding(top = 5.dp).padding(start = 15.dp),
                                    text = item.name,
                                    style = mainTextStyle
                                )
                                val nr = item.norma
                                Text(
                                    modifier = Modifier.padding(0.dp).padding(start = 15.dp).padding(bottom = 5.dp),
                                    text = "Н: ${item.sumNedel.roundToString(1)}/${nr.roundToString(1)} " +
                                            "  М: ${item.sumMonth.roundToString(1)}/${(nr * 4.286).roundToString(1)} " +
                                            "  Г: ${item.sumYear.roundToString(1)}/${(nr * 52).roundToString(1)}",
                                    style = hourTextStyle
                                )
                            }
                            Box {
                                if (editable && selection.isActive(item)) MyButtDropdownMenuStyle2(
                                    Modifier.padding(end = 15.dp).padding(vertical = 5.dp), expanded,
                                    buttMenu
                                ) {
                                    dropMenu(item, expanded)
                                }
                            }
                        }
                        Canvas(
                            modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 10.dp)
                                .padding(bottom = 10.dp)
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

                            val rectRamk = MyRectF(canvasWidth, canvasHeight, 0F, 0F).apply {
                                inset(otstupOut.toPx(), otstupOut.toPx())
                            }

                            val nrm = if (item.norma > 0) item.norma else -item.norma

                            var koefMax = 1F
                            val koefned: Float = (item.sumNedel / nrm).toFloat()
                            val koefMonth: Float = (item.sumMonth / (nrm * 4.286)).toFloat()
                            val koefYear: Float = (item.sumYear / (nrm * 52)).toFloat()

                            if (koefned > koefMax) koefMax = koefned
                            if (koefMonth > koefMax) koefMax = koefMonth
                            if (koefYear > koefMax) koefMax = koefYear
                            var pBackgbColor = MyColorARGB.colorEffektShkal_Back
                            if (item.norma > 0) {
                                if (koefMonth + koefYear + koefned < 1.5) pBackgbColor =
                                    MyColorARGB.colorEffektShkal_BackRed
                            } else {
                                if (koefMonth + koefYear + koefned > 1.5) pBackgbColor =
                                    MyColorARGB.colorEffektShkal_BackRed
                            }

                            if (koefMax > 1F) {
                                rectRamk.width = rectRamk.width / koefMax
                            }

                            val innerRectHeight = canvasHeight - 2 * otstupSum.toPx()

                            val rectNedelBack = MyRectF(rectRamk).apply {
                                height = height * 3 / 5
                            }
                            val rectNedel = MyRectF(rectRamk).apply {
                                inset(otstupIn.toPx(), otstupIn.toPx())
                                height = innerRectHeight * 3 / 5 - otstupBetween.toPx() / 2
                            }
                            val rectMonthBack = MyRectF(rectRamk).apply {
                                offsetY += height * 3 / 5
                                height = innerRectHeight * 2 / 5 * 3 / 5
                            }
                            val rectMonth = MyRectF(rectMonthBack).apply {
                                inset(otstupIn.toPx(), otstupBetween.toPx() / 2)
                            }
                            val rectYearBack = MyRectF(rectRamk).apply {
                                offsetY += height * 3 / 5 + innerRectHeight * 2 / 5 * 3 / 5
                                height -= height * 3 / 5 + innerRectHeight * 2 / 5 * 3 / 5
                            }
                            val rectYear = MyRectF(rectYearBack).apply {
                                inset(otstupIn.toPx(), 0F)
                                offsetY += otstupBetween.toPx() / 2
                                height -= otstupBetween.toPx() / 2
                                height -= otstupIn.toPx()
                            }
                            if (item.norma > 0) {
                                rectNedel.width = rectNedel.width * koefned
                                rectMonth.width = rectMonth.width * koefMonth
                                rectYear.width = rectYear.width * koefYear
                            } else {
                                rectNedel.offsetX += rectNedel.width * koefned
                                rectNedel.width -= rectNedel.width * koefned
                                rectMonth.offsetX += rectMonth.width * koefMonth
                                rectMonth.width -= rectMonth.width * koefMonth
                                rectYear.offsetX += rectYear.width * koefYear
                                rectYear.width -= rectYear.width * koefYear
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
                                        this.color = color; style =
                                        if (stroke) PaintingStyle.Stroke else PaintingStyle.Fill
                                    }.asFrameworkPaint()
                                )
                            }
                            drawMyRect(rectRamk, pBackgbColor.plusWhite(4f).toColor())
                            drawMyRect(rectYear, MyColorARGB.colorEffektShkal_Year.toColor())
                            drawMyRect(rectYear, Color(0xAA000000), true)
                            drawRoundRect(
                                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                                color = MyColorARGB.colorEffektShkal_Month.toColor(),
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
                            drawRoundRect(
                                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                                color = MyColorARGB.colorEffektShkal_Nedel.toColor(),
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

                            drawRoundRect(
                                color = Color.Black,
                                size = Size(canvasWidth, canvasHeight),
                                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                )
                            )
                            drawRoundRect(
                                color = Color.White,
                                size = Size(canvasWidth, canvasHeight),
                                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}