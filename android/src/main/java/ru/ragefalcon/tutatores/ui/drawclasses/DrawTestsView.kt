package ru.ragefalcon.tutatores.ui.drawclasses

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import ru.ragefalcon.tutatores.R
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


internal class DrawTestsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var p: Paint = Paint().apply { isAntiAlias = true }
    var pT: Paint = Paint().apply { isAntiAlias = true }
    var rect: Rect = Rect()
    var dlina: Float = 500F
    var koor: Float = 0F
    var koef = 1
    var count = 0
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    var timer = Timer()
    var task = timerTask {
        if (count < 60 * 5) {
            count++
            invalidateStars()
        }
    }

    fun onStart() {
        timer.scheduleAtFixedRate(task, 0.toLong(), (1000 / 60).toLong())
    }

    fun onRestartAnimation() {
        count = 0
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

    private fun invalidateStars() {
        Thread(Runnable {
            if (koor > 300) koef = -1
            if (koor < -300) koef = 1
            koor = koor + koef * 10F
            dlina = 500F + koef * sqrt(300F * 300F - koor * koor)

            postInvalidate()
        }).start()
    }

    override fun onDraw(canvas: Canvas) {
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3.0F);
        pT.setColor(Color.GREEN);
        pT.setStrokeWidth(3.0F);
        canvas.drawPoint(50F, 50F, p);
        canvas.drawCircle(viewWidth / 2F, viewHeight / 2F, dlina / 2 - 100F, p);
        canvas.drawRect(500F, 150F, dlina, 200F, p);
        drawSector2(canvas, Color.rgb(255, 0, 0), 300F, 300F, 400F, 300F, 240F, 120F)
        drawSector2(canvas, Color.rgb(0, 0, 255), 300F, 300F, 300F, 200F, 0F, 120F)
        drawSector2(canvas, Color.rgb(0, 255, 0), 300F, 300F, 350F, 150F, 120F, 120F)
    }

    fun drawSector(
        canvas: Canvas,
        cent: PointF,
        diamOut: Float,
        diamIn: Float,
        kontur: Float,
        angSt: Float,
        angEnd: Float,
        color: Int
    ) {

        var rectf = RectF(cent.x - diamOut / 2, cent.y - diamOut / 2, cent.x + diamOut / 2, cent.y + diamOut / 2)

        pT.style = Paint.Style.FILL
        pT.color = color
        p.color = Color.WHITE
        p.style = Paint.Style.STROKE

        pT.strokeWidth = 3F
        p.setStrokeWidth(kontur * 2);
        canvas.drawArc(rectf, angSt, angEnd, true, pT)
        canvas.drawArc(rectf, angSt, angEnd, true, p)
        p.style = Paint.Style.FILL_AND_STROKE
        var diam = diamIn
        p.color = getColor(context, R.color.colorMyMainTheme)
        p.alpha = 255
        rectf = RectF(cent.x - diam / 2, cent.y - diam / 2, cent.x + diam / 2, cent.y + diam / 2)
        canvas.drawArc(rectf, angSt, angEnd, true, p)
        p.color = Color.WHITE

        rectf = RectF(cent.x - diam / 2, cent.y - diam / 2, cent.x + diam / 2, cent.y + diam / 2)
        canvas.drawArc(rectf, angSt, angEnd, true, p)
        diam = diamIn - kontur * 2
        p.color = getColor(context, R.color.colorMyMainTheme)
        p.alpha = 255
        rectf = RectF(cent.x - diam / 2, cent.y - diam / 2, cent.x + diam / 2, cent.y + diam / 2)
        canvas.drawCircle(cent.x, cent.y, diam / 2, p)
    }

    fun drawPorterDuff(canvas: Canvas) {
        val paintSrc: Paint
        val paintDst: Paint
        var paintBorder: Paint
        val pathSrc: Path
        val pathDst: Path
        val bitmapSrc: Bitmap
        val bitmapDst: Bitmap
        val mode = PorterDuff.Mode.XOR

        val colorDst = Color.BLUE
        val colorSrc = Color.YELLOW
        pathDst = Path()
        pathDst.moveTo(0f, 0f)
        pathDst.lineTo(500f, 0f)
        pathDst.lineTo(500f, 500f)
        pathDst.close()
        bitmapDst = createBitmap(pathDst, colorDst)!!
        paintDst = Paint()
        pathSrc = Path()
        pathSrc.moveTo(0f, 0f)
        pathSrc.lineTo(500f, 0f)
        pathSrc.lineTo(0f, 500f)
        pathSrc.close()
        bitmapSrc = createBitmap(pathSrc, colorSrc)!!
        paintSrc = Paint(Paint.ANTI_ALIAS_FLAG)
        paintSrc.xfermode = PorterDuffXfermode(mode)
        canvas.translate(390F, 80F);
        val bitmap = Bitmap.createBitmap(
            500, 500,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawBitmap(bitmapDst, 0F, 0F, paintDst);

        bitmapCanvas.drawBitmap(bitmapSrc, 0F, 0F, paintSrc);
        canvas.drawBitmap(bitmap, 0F, 0F, paintDst);
    }

    private fun createBitmap(path: Path, color: Int): Bitmap? {

        val bitmap = Bitmap.createBitmap(
            500, 500,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color

        bitmapCanvas.drawPath(path, paint)
        return bitmap
    }

    fun drawSector2(
        canvas: Canvas,
        color: Int,
        centX: Float,
        centY: Float,
        diam: Float,
        shir: Float,
        angSt: Float,
        angEnd: Float
    ) {
        var pT: Paint = Paint().apply { isAntiAlias = true }
        var path = sectorPath(diam / 2, diam / 2, diam, shir, angSt, angEnd)
        pT.color = color
        pT.style = Paint.Style.FILL

        val bitmap1 = Bitmap.createBitmap(
            diam.toInt() + 1, diam.toInt() + 1,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)

        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        var paintSrc = Paint(Paint.ANTI_ALIAS_FLAG)
        paintSrc.style = Paint.Style.STROKE
        paintSrc.color = color
        bitmapCanvas1.drawPath(path, pT)

        paintSrc.strokeWidth = 6F
        paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        bitmapCanvas1.drawPath(path, paintSrc)

        paintSrc.alpha = 128
        paintSrc.strokeWidth = 12F
        paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        bitmapCanvas1.drawPath(path, paintSrc)

        canvas.drawBitmap(bitmap1, centX - diam / 2, centY - diam / 2, paintDst)
    }

    fun sectorPath(
        centX: Float,
        centY: Float,
        diam: Float,
        shir: Float,
        angSt: Float,
        angEnd: Float
    ): Path {
        val path = Path()
        var rectf = RectF(centX - diam / 2, centY - diam / 2, centX + diam / 2, centY + diam / 2)
        val diam2 = diam - shir
        val p1 = fromPolar(angSt, diam / 2, PointF(centX, centY))
        val p4 = fromPolar(angSt + angEnd, diam2 / 2, PointF(centX, centY))

        path.arcTo(rectf, angSt, angEnd)
        path.lineTo(p4.x, p4.y)
        rectf = RectF(centX - diam2 / 2, centY - diam2 / 2, centX + diam2 / 2, centY + diam2 / 2)
        path.arcTo(rectf, angSt + angEnd, -angEnd)
        path.lineTo(p1.x, p1.y)
        path.close()
        return path
    }

    fun fromPolar(ang: Float, rad: Float, cent: PointF): PointF {
        val ang1 = ((360 - ang) * PI / 180).toFloat()
        return PointF(rad * cos(ang1) + cent.x, cent.y - rad * sin(ang1))
    }
}