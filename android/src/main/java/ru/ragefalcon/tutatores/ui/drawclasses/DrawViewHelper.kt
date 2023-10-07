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
    protected var manualStart: Boolean = false

    abstract fun calculateFun(): Boolean
    abstract fun drawFun(canvas: Canvas)

    private var helper: DrawViewHelper? = null

    var timer: Timer? = null
    var task: TimerTask? = null

    protected var periodUpdate = (1000 / 60).toLong()

    fun onStart() {
        if (task == null && timer == null) {
            task = timerTask {
                if (calculateFun()) {
                    postInvalidate()
                }
            }
            timer = Timer()
            task?.let { timer?.scheduleAtFixedRate(it, 0.toLong(), periodUpdate) }
            manualStart = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

    }

    fun onStop() {
        task?.cancel()
        timer?.cancel()
        task = null
        timer = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onStop()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (manualStart) {
            onStart()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawFun(canvas)
    }

}