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

    //    init {
//        p = Paint()
//        rect = Rect()
//    }
    var timer = Timer()
    var task = timerTask {
        if (count < 60 * 5) {
            count++
            invalidateStars()
        }
    }

    fun onStart() {


        // set timer to run every 16 milliseconds (fps = 1000 / 60)
        timer.scheduleAtFixedRate(task, 0.toLong(), (1000 / 60).toLong())
    }

    fun onRestartAnimation() {
        count = 0
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        viewWidth = w
        viewHeight = h

        if (viewWidth > 0 && viewHeight > 0) {
            // init stars every time the size of the view has changed
            //    initStars()
        }
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

//            stars.forEach { it.calculateFrame(viewWidth, viewHeight) }
//            starsCalculatedFlag = true
            postInvalidate()
        }).start()

    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.GREEN)
//        canvas.drawARGB(80, 102, 204, 255);

        // настройка кисти
        // красный цвет
        p.setColor(Color.WHITE);
        // толщина линии = 10
        p.setStrokeWidth(3.0F);

        pT.setColor(Color.GREEN);
        pT.setStrokeWidth(3.0F);

        // рисуем точку (50,50)
        canvas.drawPoint(50F, 50F, p);


        // рисуем круг с центром в (100,200), радиус = 50
        canvas.drawCircle(viewWidth / 2F, viewHeight / 2F, dlina / 2 - 100F, p);

        // рисуем прямоугольник
        // левая верхняя точка (200,150), нижняя правая (400,200)
        canvas.drawRect(500F, 150F, dlina, 200F, p);

        /**        // настройка объекта Rect
        // левая верхняя точка (250,300), нижняя правая (350,500)
        rect.set(250, 300, 350, 500);
        // рисуем прямоугольник из объекта rect
        //       canvas.drawRect(rect, p);
        var rectf= RectF(200F, 200F, 400F, 400F)
        pT.style=Paint.Style.STROKE
        pT.strokeWidth=10F
        //        pT.color = Color.TRANSPARENT
        pT.style=Paint.Style.FILL
        p.style=Paint.Style.STROKE
        pT.strokeWidth=3F
        //        pT.setXfermode(Xfermode())//PorterDuffXfermode(PorterDuff.Mode.DST_OUT))
        canvas.drawArc(rectf, 135F, 270F, true, pT)
        p.setStrokeWidth(10.0F);
        //        canvas.drawArc(rectf, 135F, 270F, false, p)
        canvas.drawArc(rectf, 135F, 270F, true, p)
        canvas.drawArc(rectf, 135F, 270F, true, pT)
        p.style=Paint.Style.FILL_AND_STROKE
        var diam = 100F
        rectf= RectF(300F-diam/2, 300F-diam/2, 300F+diam/2, 300F+diam/2)
        canvas.drawArc(rectf, 135F, 270F, true, p)
        diam = 90F
        p.color=getColor(context,R.color.colorMyMainTheme)
        rectf= RectF(300F-diam/2, 300F-diam/2, 300F+diam/2, 300F+diam/2)
        canvas.drawCircle(300F,300F,diam/2,p)
        //        canvas.drawArc(rectf, 0F, 0F, true, p)
        var otstup = 5F
        var angleOts = 360F*otstup/(PI*diam)
        //        pT.color = Color.BLACK
        //        pT.strokeWidth = 1F//pT.strokeWidth - 2 * otstup
        //        for (i in 110..290 step 10) {
        //            diam = i*1F
        //            angleOts = 360F*otstup/(PI*diam)
        //            rectf= RectF(300F-diam/2, 300F-diam/2, 300F+diam/2, 300F+diam/2)
        //            canvas.drawArc(rectf, 135F + angleOts.toFloat(), 270F - 2 * angleOts.toFloat(), false, pT)
        //        }
        // рисуем линию от (100,100) до (500,50)
        //        canvas.drawLine(200F, 300F, 400F, 300F, p);*/
//        drawSector(canvas,PointF(400F,300F),300F, 200F, 10F, 240F,120F, Color.rgb(255,0,0))
//        drawSector(canvas, PointF(400F,300F),200F, 150F, 10F, 0F,120F, Color.rgb(0,0,255))
//        drawSector(canvas, PointF(400F,300F),250F, 100F, 10F, 120F,120F, Color.rgb(0,255,0))
//        drawPorterDuff(canvas)
        drawSector2(canvas, Color.rgb(255,0,0),300F,300F,400F,300F,240F,120F)
        drawSector2(canvas, Color.rgb(0,0,255),300F,300F,300F,200F,0F,120F)
        drawSector2(canvas, Color.rgb(0,255,0),300F,300F,350F,150F,120F,120F)
    }

    fun drawSector(canvas: Canvas, cent: PointF, diamOut: Float, diamIn: Float, kontur: Float, angSt: Float, angEnd: Float, color: Int) {

        var rectf= RectF(cent.x - diamOut/2, cent.y - diamOut/2, cent.x  + diamOut/2, cent.y + diamOut/2)
//        pT.style=Paint.Style.STROKE
//        pT.strokeWidth=10F
        pT.style=Paint.Style.FILL
        pT.color=color
        p.color=Color.WHITE//color
        p.style=Paint.Style.STROKE

//        p.alpha= 128
        pT.strokeWidth=3F
        p.setStrokeWidth(kontur*2);
        canvas.drawArc(rectf, angSt, angEnd, true, pT)
        canvas.drawArc(rectf, angSt, angEnd, true, p)
        p.style=Paint.Style.FILL_AND_STROKE
        var diam = diamIn
        p.color=getColor(context,R.color.colorMyMainTheme)
        p.alpha= 255
        rectf= RectF(cent.x-diam/2, cent.y-diam/2, cent.x+diam/2, cent.y+diam/2)
        canvas.drawArc(rectf, angSt, angEnd, true, p)
//                    canvas.drawCircle(300F,300F,diam/2,p)
        p.color=Color.WHITE//color
//        p.alpha= 128

        rectf= RectF(cent.x-diam/2, cent.y-diam/2, cent.x+diam/2, cent.y+diam/2)
        canvas.drawArc(rectf, angSt, angEnd, true, p)
        diam = diamIn-kontur*2
        p.color=getColor(context,R.color.colorMyMainTheme)
        p.alpha= 255
        rectf= RectF(cent.x-diam/2, cent.y-diam/2, cent.x+diam/2, cent.y+diam/2)
        canvas.drawCircle(cent.x,cent.y,diam/2,p)

    }

    fun drawPorterDuff(canvas: Canvas){
        var paintSrc: Paint
        var paintDst: Paint
        var paintBorder: Paint

        var pathSrc: Path
        var pathDst: Path

        var bitmapSrc: Bitmap
        var bitmapDst: Bitmap

        // PorterDuff режим

        // PorterDuff режим
        val mode = PorterDuff.Mode.XOR//DST_ATOP//DST_IN//DST_IN//SRC_IN//SRC_OVER// SRC

        val colorDst = Color.BLUE
        val colorSrc = Color.YELLOW

        // DST фигура

        // DST фигура
        pathDst = Path()
        pathDst.moveTo(0f, 0f)
        pathDst.lineTo(500f, 0f)
        pathDst.lineTo(500f, 500f)
        pathDst.close()

        // создание DST bitmap

        // создание DST bitmap
        bitmapDst = createBitmap(pathDst, colorDst)!!

        // кисть для вывода DST bitmap

        // кисть для вывода DST bitmap
        paintDst = Paint()

        // SRC фигура

        // SRC фигура
        pathSrc = Path()
        pathSrc.moveTo(0f, 0f)
        pathSrc.lineTo(500f, 0f)
        pathSrc.lineTo(0f, 500f)
        pathSrc.close()

        // создание SRC bitmap

        // создание SRC bitmap
        bitmapSrc = createBitmap(pathSrc, colorSrc)!!

        // кисть для вывода SRC bitmap

        // кисть для вывода SRC bitmap
        paintSrc = Paint(Paint.ANTI_ALIAS_FLAG)
//        paintSrc.color=Color.WHITE
//        paintSrc.style = Paint.Style.FILL_AND_STROKE

        paintSrc.xfermode = PorterDuffXfermode(mode)

        // кисть для рамки

        // кисть для рамки
//        paintBorder = Paint()
//        paintBorder.style = Paint.Style.STROKE
//        paintBorder.strokeWidth = 3f
//        paintBorder.color = Color.BLACK
//

        // DST bitmap
        canvas.translate(390F, 80F);
        val bitmap = Bitmap.createBitmap(
            500, 500,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawBitmap(bitmapDst, 0F, 0F, paintDst);

        // SRC bitmap
        bitmapCanvas.drawBitmap(bitmapSrc, 0F, 0F, paintSrc);
        canvas.drawBitmap(bitmap, 0F, 0F, paintDst);

//        canvas.drawBitmap(bitmapDst, 0F, 0F, paintSrc);
//         рамка
        //       canvas.drawRect(0F, 0F, 500F, 500F, paintBorder);

    }

    private fun createBitmap(path: Path, color: Int): Bitmap? {
        // создание bitmap и канвы для него
        val bitmap = Bitmap.createBitmap(
            500, 500,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap)

        // создание кисти нужного цвета
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color

        // рисование фигуры на канве bitmap
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
    ){
        var pT: Paint = Paint().apply { isAntiAlias = true }
        var path = sectorPath(diam/2, diam/2, diam, shir, angSt, angEnd)
        pT.color = color//Color.GREEN
        pT.style=Paint.Style.FILL

//        pT2.color = Color.RED
//        pT2.style=Paint.Style.STROKE
//        pT2.strokeWidth=12F
//
//        pT3.color = Color.RED
//        pT3.style=Paint.Style.STROKE
//        pT3.strokeWidth=6F


        val bitmap1 = Bitmap.createBitmap(
            diam.toInt()+1, diam.toInt()+1,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)

        // кисть для вывода DST bitmap
        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        var paintSrc = Paint(Paint.ANTI_ALIAS_FLAG)
        paintSrc.style=Paint.Style.STROKE
        paintSrc.color = color//Color.GREEN


        bitmapCanvas1.drawPath(path,pT)

        paintSrc.strokeWidth=6F
        paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)//DST_OUT//DST_ATOP//DST_IN//SRC_IN//SRC_OVER// SRC
        bitmapCanvas1.drawPath(path,paintSrc)

        paintSrc.alpha = 128
        paintSrc.strokeWidth=12F
        paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        bitmapCanvas1.drawPath(path,paintSrc)

        canvas.drawBitmap(bitmap1, centX-diam/2, centY-diam/2, paintDst)
//        bitmapCanvas1.drawPath(path,pT)
//        bitmapCanvas1.drawPath(path,pT2)
//        bitmapCanvas2.drawPath(path,pT)
//        bitmapCanvas3.drawPath(path,pT3)
//        bitmapCanvas4.drawBitmap(bitmap1, 0F, 0F, paintDst)
//        bitmapCanvas4.drawBitmap(bitmap2, 0F, 0F, paintSrc)
//        bitmapCanvas5.drawBitmap(bitmap4, 0F, 0F, paintDst)
//        mode = PorterDuff.Mode.DST_OUT//DST_ATOP//DST_IN//DST_IN//SRC_IN//SRC_OVER// SRC
//        paintSrc.xfermode = PorterDuffXfermode(mode)
//        bitmapCanvas5.drawBitmap(bitmap3, 0F, 0F, paintSrc)
//        canvas.drawBitmap(bitmap5, 0F, 0F, paintDst)
    }
    fun sectorPath(
        centX: Float,
        centY: Float,
        diam: Float,
        shir: Float,
        angSt: Float,
        angEnd: Float
    ):Path{
        var path = Path()
        var rectf= RectF(centX-diam/2, centY-diam/2, centX+diam/2, centY+diam/2)

        var diam2 = diam - shir
        var p1 = fromPolar(angSt,diam/2, PointF(centX,centY))
//        var p2 = fromPolar(angSt,diam2/2,PointF(centX,centY))
//        var p3 = fromPolar(angSt+angEnd,diam/2, PointF(centX,centY))
        var p4 = fromPolar(angSt+angEnd,diam2/2,PointF(centX,centY))

        path.arcTo(rectf, angSt, angEnd)
        path.lineTo(p4.x,p4.y)
        rectf = RectF(centX-diam2/2, centY-diam2/2, centX+diam2/2, centY+diam2/2)
        path.arcTo(rectf, angSt+angEnd, - angEnd)
        path.lineTo(p1.x,p1.y)
        path.close()
        return path
    }
    fun fromPolar(ang: Float, rad: Float,cent: PointF): PointF{
        val ang1=((360-ang)*PI/180).toFloat()
        return PointF(rad*cos(ang1)+cent.x,cent.y-rad* sin(ang1))
    }
}