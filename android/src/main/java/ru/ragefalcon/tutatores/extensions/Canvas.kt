package ru.ragefalcon.tutatores.extensions

import android.graphics.*
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun MyColorARGB.toIntColor(): Int{
    return Color.argb(this.A,this.R,this.G,this.B)
}
fun Int.toMyColorARGB():MyColorARGB{
    return MyColorARGB((Color.alpha(this)),
                       (Color.red(this)),
                       (Color.green(this)),
                       (Color.blue(this)))
}

fun Canvas.drawSectorWhiter(
    color: Int,
    centr: PointF,
    radOut: Float,
    radIn: Float,
    angSt: Float,
    angValue: Float
){
    this.drawSector(color.toMyColorARGB().plusWhite().toIntColor(),
            color,
            centr,
            radOut,
            radIn,
            angSt,
            angValue)
}

fun Canvas.drawSector(
    color: Int,
    colorStroke: Int,
    centr: PointF,
    radOut: Float,
    radIn: Float,
    angSt: Float,
    angValue: Float
){
    var diam = radOut*2
    var shir = radOut - radIn
    var centX = centr.x
    var centY = centr.y
    var pT: Paint = Paint().apply { isAntiAlias = true }
    var path = sectorPath(diam/2, diam/2, diam, shir, angSt, angValue)
    pT.color = color//Color.GREEN
    pT.style= Paint.Style.FILL

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
    paintSrc.style= Paint.Style.STROKE
    paintSrc.color = Color.GREEN


    bitmapCanvas1.drawPath(path,pT)

    paintSrc.strokeWidth=6F
    paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)//DST_OUT//DST_ATOP//DST_IN//SRC_IN//SRC_OVER// SRC
    bitmapCanvas1.drawPath(path,paintSrc)

//    paintSrc.alpha = 128
    paintSrc.color = colorStroke
    paintSrc.strokeWidth=12F
    paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    bitmapCanvas1.drawPath(path,paintSrc)

    this.drawBitmap(bitmap1, centX-diam/2, centY-diam/2, paintDst)
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
    angEnd0: Float
): Path {
    var angEnd = angEnd0
    if (angEnd==360F) angEnd=359.5F
    var path = Path()
    var rectf= RectF(centX-diam/2, centY-diam/2, centX+diam/2, centY+diam/2)

    var diam2 = diam - 2*shir
    var p1 = fromPolar(angSt,diam/2, PointF(centX,centY))
//        var p2 = fromPolar(angSt,diam2/2,PointF(centX,centY))
//        var p3 = fromPolar(angSt+angEnd,diam/2, PointF(centX,centY))
    var p4 = fromPolar(angSt+angEnd,diam2/2, PointF(centX,centY))

    path.arcTo(rectf, angSt, angEnd)
    path.lineTo(p4.x,p4.y)
    rectf = RectF(centX-diam2/2, centY-diam2/2, centX+diam2/2, centY+diam2/2)
    path.arcTo(rectf, angSt+angEnd, - angEnd)
    path.lineTo(p1.x,p1.y)
    path.close()
    return path
}

fun fromPolar(ang: Float, rad: Float,cent: PointF): PointF{
    val ang1=((360-ang)* PI /180).toFloat()
    return PointF(rad* cos(ang1) +cent.x,cent.y-rad* sin(ang1))
}
