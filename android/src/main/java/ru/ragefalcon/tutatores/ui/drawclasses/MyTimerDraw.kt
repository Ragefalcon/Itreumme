package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import ru.ragefalcon.sharedcode.models.commonfun.vozrast
import ru.ragefalcon.tutatores.extensions.pxF
import ru.ragefalcon.tutatores.extensions.toIntColor
import ru.ragefalcon.tutatores.extensions.toMyColorARGB

class MyTimerDraw  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawViewHelper(context, attrs, defStyleAttr) {

    private var birthday: Long = 5602600000
    private var updateNeed: Boolean = false

    fun setBirthday(date: Long) {
        birthday = date
        updateNeed = true
        invalidate()
    }

    init {
        periodUpdate = 1000
        onStart()
    }

    override fun calculateFun(): Boolean {
            return true
    }

    var pT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = 24.pxF
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
        setShadowLayer(2.pxF,1.pxF,1.pxF, Color.BLACK.toMyColorARGB().apply { A = 170 }.toIntColor())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        minimumHeight = (pT.textSize*2).toInt()
    }

    override fun drawFun(canvas: Canvas) {
        canvas.drawText(
            "${vozrast(birthday)}",
            canvas.width/2F,
            height/2F+pT.textSize/3F,
            pT
        )
    }
}