package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.extensions.pxF

class EffektShkalDrawHelper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawViewHelper(context, attrs, defStyleAttr) {

    private var item: ItemEffekt = ItemEffekt("1", "Programm", 1234, 15.0, 8.0, 78.0, 350.0)//null
    private var updateNeed: Boolean = false

    fun setItemEffekt(itemm: ItemEffekt){
        item = itemm
        updateNeed = true
        invalidate()
    }

    override fun calculateFun(): Boolean {
        if (updateNeed) {
            updateNeed = false
            return true
        } else {
            return false
        }
    }

    private val otstupOut = 3.pxF
    private val otstupIn = 2.pxF
    private val otstupBetween = 2.pxF
    private val otstupSum = otstupOut + otstupIn
    private var p1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorEffektShkal_Year)
        setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)
    }
    private var pBackgb = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorEffektShkal_Back)
    }
    private var pRamk = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.pxF
        color = Color.WHITE
        setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)
    }

    private fun reverseSide(rect: RectF, pp: Paint){
        if (rect.left>rect.right){
            val tmp = rect.left
            rect.left = rect.right
            rect.right = tmp
            pp.color = ContextCompat.getColor(context, R.color.colorEffektShkal_BackRed)
        }
    }
    override fun drawFun(canvas: Canvas) {
        val bitmap = Bitmap.createBitmap(
            if (canvas.width != 0) canvas.width else 100,
            if (canvas.height != 0) canvas.height else 100,
            Bitmap.Config.ARGB_8888
        )
        val bmpCnvs = Canvas(bitmap)
        val rectRamk = RectF(0F,0F,canvas.width.toFloat(),canvas.height.toFloat()).apply {
            inset(otstupOut, otstupOut)
        }

        val nrm = if (item.norma>0) item.norma else -item.norma

        var koefMax = 1F
        val koefned: Float = (item.sumNedel / nrm).toFloat()
        val koefMonth: Float = (item.sumMonth / (nrm*4.286)).toFloat()
        val koefYear: Float = (item.sumYear / (nrm*52)).toFloat()

        if (koefned>koefMax) koefMax = koefned
        if (koefMonth>koefMax) koefMax = koefMonth
        if (koefYear>koefMax) koefMax = koefYear
        pBackgb.color = ContextCompat.getColor(context, R.color.colorEffektShkal_Back)
        if (item.norma>0){
            if (koefMonth+koefYear+koefned<1.5) pBackgb.color = ContextCompat.getColor(context, R.color.colorEffektShkal_BackRed)
        }   else    {
            if (koefMonth+koefYear+koefned>1.5) pBackgb.color = ContextCompat.getColor(context, R.color.colorEffektShkal_BackRed)
        }

        if (koefMax>1F){
                rectRamk.right = rectRamk.left + rectRamk.width()/koefMax
        }

        val innerRectHeight = canvas.height - 2 * otstupSum

        val rectNedelBack =RectF(rectRamk).apply {
            bottom = top + height()*3/5
        }
        val rectNedel =RectF(rectRamk).apply {
            inset(otstupIn, otstupIn)
            bottom = top + innerRectHeight*3/5 - otstupBetween/2
        }
        val rectMonthBack =RectF(rectRamk).apply {
            top += height()*3/5
            bottom = top + innerRectHeight*2/5*3/5
        }
        val rectMonth =RectF(rectMonthBack).apply {
            inset(otstupIn, otstupBetween/2)
        }
        val rectYearBack =RectF(rectRamk).apply {
            top += height()*3/5 + innerRectHeight*2/5*3/5
        }
        val rectYear =RectF(rectYearBack).apply {
            inset(otstupIn, 0F)
            top += otstupBetween/2
            bottom -= otstupIn
        }
        if (item.norma>0){
            rectNedel.right = rectNedel.left + rectNedel.width()*koefned
            rectMonth.right = rectMonth.left + rectMonth.width()*koefMonth
            rectYear.right = rectYear.left + rectYear.width()*koefYear
        }   else    {
            rectNedel.left = rectNedel.left + rectNedel.width()*koefned
            rectMonth.left = rectMonth.left + rectMonth.width()*koefMonth
            rectYear.left = rectYear.left + rectYear.width()*koefYear
        }

        bmpCnvs.drawRoundRect(
            rectRamk, 0F, 0F, pBackgb
        )
        bmpCnvs.drawRoundRect(
            rectRamk, 0F, 0F, pRamk
        )
        p1.color = ContextCompat.getColor(context, R.color.colorEffektShkal_Year)
        reverseSide(rectYear,p1)
        bmpCnvs.drawRoundRect(
            rectYear, 0F, 0F, p1
        )
        p1.color = ContextCompat.getColor(context, R.color.colorEffektShkal_Month)
        reverseSide(rectMonth,p1)
        bmpCnvs.drawRoundRect(
            rectMonth, 0F, 0F, p1//pBackgb
        )
        p1.color = ContextCompat.getColor(context, R.color.colorEffektShkal_Nedel)
        reverseSide(rectNedel,p1)
        bmpCnvs.drawRoundRect(
            rectNedel, 0F, 0F, p1
        )
        val paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmap, 0F, 0F, paintDst)
    }
}



