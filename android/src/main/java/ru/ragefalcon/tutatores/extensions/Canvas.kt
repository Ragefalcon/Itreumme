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
    val diam = radOut*2
    val shir = radOut - radIn
    val centX = centr.x
    val centY = centr.y
    val pT: Paint = Paint().apply { isAntiAlias = true }
    val path = sectorPath(diam/2, diam/2, diam, shir, angSt, angValue)
    pT.color = color
    pT.style= Paint.Style.FILL

    val bitmap1 = Bitmap.createBitmap(
        diam.toInt()+1, diam.toInt()+1,
        Bitmap.Config.ARGB_8888
    )
    val bitmapCanvas1 = Canvas(bitmap1)

    val paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintSrc = Paint(Paint.ANTI_ALIAS_FLAG)
    paintSrc.style= Paint.Style.STROKE
    paintSrc.color = Color.GREEN

    bitmapCanvas1.drawPath(path,pT)

    paintSrc.strokeWidth=6F
    paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    bitmapCanvas1.drawPath(path,paintSrc)


    paintSrc.color = colorStroke
    paintSrc.strokeWidth=12F
    paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    bitmapCanvas1.drawPath(path,paintSrc)

    this.drawBitmap(bitmap1, centX-diam/2, centY-diam/2, paintDst)
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
    val path = Path()
    var rectf= RectF(centX-diam/2, centY-diam/2, centX+diam/2, centY+diam/2)

    val diam2 = diam - 2*shir
    val p1 = fromPolar(angSt,diam/2, PointF(centX,centY))
    val p4 = fromPolar(angSt+angEnd,diam2/2, PointF(centX,centY))

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
