package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.concurrent.timerTask


abstract class DrawViewHelper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    protected var viewWidth: Int = 0
    protected var viewHeight: Int = 0

    abstract fun calculateFun(): Boolean
    abstract fun drawFun(canvas: Canvas)

    private var helper: DrawViewHelper? = null

    var timer = Timer()
    var task = timerTask {
        if (calculateFun()) {
            Thread(Runnable {
                postInvalidate()
            }).start()
        }
    }

    protected var periodUpdate = (1000 / 60).toLong()

    fun onStart() {
        timer.scheduleAtFixedRate(task, 0.toLong(), periodUpdate)
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

    override fun onDraw(canvas: Canvas) {
        drawFun(canvas)
    }


}