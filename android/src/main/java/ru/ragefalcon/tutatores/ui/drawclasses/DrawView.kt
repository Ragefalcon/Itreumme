package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.concurrent.timerTask


internal class DrawView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var calculateFun: (() -> Boolean)? = null
    private var drawFun: ((Canvas) -> Unit)? = null

    var timer = Timer()
    var task = timerTask {
        if (calculateFun?.invoke() ?: false) {
            Thread(Runnable {
                postInvalidate()
            }).start()
        }
    }

    fun onStart() {
        timer.scheduleAtFixedRate(task, 0.toLong(), (1000 / 60).toLong())
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    fun onStop() {

        task.cancel()
        timer.cancel()

    }

    fun setCalcFun(ff: () -> Boolean) {
        calculateFun = ff
    }

    fun setDrawFun(ff: (Canvas) -> Unit) {
        drawFun = ff
    }

    override fun onDraw(canvas: Canvas) {
        drawFun?.invoke(canvas)
    }

}