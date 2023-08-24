package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import ru.ragefalcon.sharedcode.models.commonfun.vozrast
import ru.ragefalcon.tutatores.extensions.*

class MyTimerDraw  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawViewHelper(context, attrs, defStyleAttr) {

    private var birthday: Long = 5602600000
//    private var birthday: Long = 560217600000

    private var updateNeed: Boolean = false

    fun setBirthday(date: Long) {
        birthday = date
        updateNeed = true
        invalidate()
    }

//    var i = 0
    init {
        periodUpdate = 1000
        onStart()
    }

    override fun calculateFun(): Boolean {
//        i++
//        Log.d("MyTut", "iii: $i");
//        if ((updateNeed)||(i>30)) {
//            updateNeed = false
//            i=0
            return true
//        } else {
//            return false
//        }
    }

    var pT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
//            ContextCompat.getColor(
//            context,
//            R.color.colorRasxodTheme0
//        )
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