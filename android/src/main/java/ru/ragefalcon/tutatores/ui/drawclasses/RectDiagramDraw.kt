package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemRectDiag
import ru.ragefalcon.sharedcode.models.data.ItemYearGraf
import ru.ragefalcon.tutatores.extensions.*

class RectDiagramDraw @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawViewHelper(context, attrs, defStyleAttr) {

    private var yearDiag: MutableList<ItemYearGraf> = mutableListOf(
        ItemYearGraf(
            2020, listOf(
                ItemRectDiag("2020", "янв", 123.0, 2030.0, 0.70),
                ItemRectDiag("2021", "ht", 123.0, 2030.0, 0.40),
                ItemRectDiag("2021", "jty", 123.0, 2030.0, 0.15),
                ItemRectDiag("2020", "янв", 123.0, 2030.0, 0.60),
                ItemRectDiag("2020", "янв", 123.0, 2030.0, 0.70),
                ItemRectDiag("2021", "ht", 123.0, 2030.0, 0.35),
                ItemRectDiag("2020", "sdf", 123.0, 2030.0, 0.50),
                ItemRectDiag("2021", "jty", 123.0, 2030.0, 0.20),
                ItemRectDiag("2020", "sdf", 123.0, 2030.0, 0.30),
                ItemRectDiag("2021", "jty", 123.0, 2030.0, 0.20)
            )
        ),
        ItemYearGraf(
            2020, listOf(
                ItemRectDiag("2020", "янв", 123.0, 2030.0, 1.0),
                ItemRectDiag("2021", "ht", 123.0, 2030.0, 0.40),
                ItemRectDiag("2021", "jty", 123.0, 2030.0, 0.15),
                ItemRectDiag("2020", "янв", 123.0, 2030.0, 1.0),
                ItemRectDiag("2020", "янв", 123.0, 2030.0, 0.70),
                ItemRectDiag("2021", "ht", 123.0, 2030.0, 0.35),
                ItemRectDiag("2020", "sdf", 123.0, 2030.0, 1.0),
                ItemRectDiag("2020", "sdf", 123.0, 2030.0, 0.30),
                ItemRectDiag("2021", "jty", 123.0, 2030.0, 0.20)
            )
        )
    )


    private var updateNeed: Boolean = false

    fun setItemsYears(itemm: List<ItemYearGraf>) {
        yearDiag.clear()
        yearDiag.addAll(itemm)
        updateNeed = true
        invalidate()
    }

    fun getWidthRectDiag(): Int {
        return ((12 + (12+4)*12)*yearDiag.count()).dpToPx
    }

    override fun calculateFun(): Boolean {
        if (updateNeed) {
            updateNeed = false
            return true
        } else {
            return false
        }
    }

    override fun drawFun(canvas: Canvas) {

        var paintDst =
            Paint(Paint.ANTI_ALIAS_FLAG)

        var x = 0
        for (year in yearDiag) {
            var bmp = drawYear(
                canvas.height,
                year
            )
            canvas.drawBitmap(
                bmp, x.toFloat(), 0F, paintDst
            )
            x += bmp.width //+ 3.dpToPx
        }
    }
}



class paintRect(val canvas: Canvas, val maxVal: Float) {
    var shir = 12
    var otstup = 2
    var otstupUp = 45
    var otstupBottom = 100
    var p1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()
    }
    var p2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.pxF
        color = Color.RED.toMyColorARGB().toIntColor()
    }
    var pT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = shir.pxF
        textAlign = Paint.Align.LEFT
        isFakeBoldText = true
        setShadowLayer(2F,2F,2F,Color.BLACK.toMyColorARGB().apply { A = 50 }.toIntColor())
    }
    var pTmonth = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 14.pxF
        textAlign = Paint.Align.RIGHT
        isFakeBoldText = true
        setShadowLayer(2F,2F,2F,Color.BLACK.toMyColorARGB().apply { A = 50 }.toIntColor())
    }

    fun drawRect(x: Int, item: ItemRectDiag): Int {
        val textPath = Path()
        textPath.reset()
        textPath.moveTo(otstup.pxF + shir.pxF  + x.pxF, -8.pxF + canvas.height * 1F)
        textPath.lineTo(otstup.pxF + shir.pxF  + x.pxF, 24.pxF + otstupUp.pxF + maxVal)
        val rr = RectF(
            otstup.pxF + x.pxF,
            16.pxF + otstupUp.pxF + maxVal * (1 - item.procent.toFloat()),
            otstup.pxF + x.pxF + shir.pxF,
            16.pxF + otstupUp.pxF + maxVal
        )
        canvas.drawRoundRect(rr, 0F, 0F, p1)
        canvas.drawRoundRect(rr, 0F, 0F, p2)
        canvas.drawTextOnPath(
            "( ${item.summa.roundToStringProb(1)} ) ${item.month}",
            textPath,
            00F,
            -1.pxF,
            pTmonth
        )
        canvas.drawPath(textPath, p1)
        return x + shir + 2 * otstup
    }

}

fun drawYear(height: Int, year: ItemYearGraf): Bitmap {
    var x = 9
    var shir = 12
    var otstup = 2
    val bitmap = Bitmap.createBitmap(
        (x + 3 + (shir+2*otstup)*12).pxF.toInt(),
        height,
        Bitmap.Config.ARGB_8888
    )
    val bitmapCanvas = Canvas(bitmap)
    val maxVal: Float = bitmapCanvas.height - 45.pxF - 100.pxF

    val pR = paintRect(bitmapCanvas, maxVal)

    var textPath = Path()

    bitmapCanvas.drawLine(3.pxF, 16.pxF + maxVal + pR.otstupUp.pxF, 3.pxF, 6.pxF, pR.p2)
    textPath.reset()
    textPath.moveTo(20.pxF + 6.pxF, 24.pxF)
    textPath.lineTo(70.pxF + 6.pxF, 24.pxF)
    bitmapCanvas.drawPath(textPath, pR.p1)
    bitmapCanvas.drawTextOnPath(
        "${year.year}",
        textPath,
        0F,
        0F,
        pR.pT
    )
    textPath.reset()
    textPath.moveTo(20.pxF + 6.pxF, 40.pxF)
    textPath.lineTo(100.pxF + 6.pxF, 40.pxF)
    bitmapCanvas.drawTextOnPath(
        "${year.month.firstOrNull()?.sumyear?.roundToStringProb(2)}",
        textPath,
        0F,
        0F,
        pR.pT
    )
    bitmapCanvas.drawPath(textPath, pR.p1)
    for (type in year.month.asReversed()) {
        x = pR.drawRect(x, type)
    }
    for (i in year.month.count() + 1..12) {
        x = pR.drawRect(x, ItemRectDiag("asdf", "sdfsd", 0.0, 0.0, 0.0))
    }

    return bitmap
}
