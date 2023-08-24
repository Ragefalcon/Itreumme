package extensions

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import org.jetbrains.skia.Point
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun MyColorARGB.toColor(): Color {
    return Color(this.R, this.G, this.B, this.A)
}

fun DrawScope.drawMyRoundRect(
    cornerRadius: CornerRadius,
    color: Color,
    style: DrawStyle = Fill,
    rect: MyRectF
) {
    this.apply {
        drawRoundRect(
            cornerRadius = cornerRadius,
            color = color,
            topLeft = rect.offset,
            size = rect.size,
            style = style
        )
    }
}


fun DrawScope.drawSectorWhiter(
    color: Color,
    centr: Point,
    radOut: Float,
    radIn: Float,
    angSt: Float,
    angValue: Float,
    strokeWidth: Float,

    ){
    this.drawSector(color.toMyColorARGB().plusWhite().toColor(),
        color,
        strokeWidth,
        centr,
        radOut,
        radIn,
        angSt,
        angValue)
}

fun DrawScope.drawSector(
    color: Color,
    colorStroke: Color,
    strokeWidth: Float,
    centr: Point,
    radOut: Float,
    radIn: Float,
    angSt: Float,
    angValue: Float
){

    this.apply {
        val diam = radOut*2
        val shir = radOut - radIn
        val path = sectorPath(diam/2, diam/2, diam, shir, angSt, angValue)




//        val sm = 10f
//        val smX = sm*cos(toRad(angSt + ( angValue )/2  ))
//        val smY = sm*sin(toRad(angSt + ( angValue )/2  ))
//
//        println("angSt = ${angSt}")
//        println("angValue = ${angValue}")
//        println("ang = ${angSt + ( angValue )/2 }")
//        println("smX = ${smX}")
//        println("smY = ${smY}")
//
//
//
//        val size = 2.5f
//        val rr = Rect (Offset(0f,0f), Size(size,size))
//        val round = Path().apply {
//            arcTo(rr, 0f, PI.toFloat(), false)
//            lineTo(size, size)
//            arcTo(rr, PI.toFloat(), 2*PI.toFloat(), false)
//            lineTo(0f, size)
//            close()
//        }
//            val square = Path().apply {
//
//                lineTo(size, 0f)
//                lineTo(size, size)
//                lineTo(0f, size)
//                close()
//            }
//            val size2 = 5f
//            val rr2 = Rect (Offset(0f,0f), Size(size2,size2))
//            val square2 = Path().apply {
//                lineTo(size2, 0f)
//                lineTo(size2, size2)
//                lineTo(0f, size2)
//                close()
//            }

        translate(centr.x - radOut ,   + 0f) { //+ smX -smY


            drawPath(path,color = color)

//            paintSrc.strokeWidth=6F
//            paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)//DST_OUT//DST_ATOP//DST_IN//SRC_IN//SRC_OVER// SRC
//            bitmapCanvas1.drawPath(path,paintSrc)
//
////    paintSrc.alpha = 128
//            paintSrc.color = colorStroke
//            paintSrc.strokeWidth=12F
//            paintSrc.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//            bitmapCanvas1.drawPath(path,paintSrc)

//            drawPath(path,color = Color.White, style = Stroke(width = 2f, miter = 50F, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.stampedPathEffect( //, miter = 5F, cap = StrokeCap.Round, join = StrokeJoin.Bevel
//                shape = square2,
//                style = StampedPathEffectStyle.Morph,
//                phase = 0f,
//                advance = 5f
//            ))) //, blendMode = BlendMode.SrcIn
//            drawPath(path,color = colorStroke, style = Stroke(width = 2f, pathEffect = PathEffect.stampedPathEffect( //, miter = 5F, cap = StrokeCap.Round, join = StrokeJoin.Bevel
//                shape = square,
//                style = StampedPathEffectStyle.Morph,
//                phase = 0f,
//                advance = 2f
//            ))) //, blendMode = BlendMode.SrcIn

//            drawPath(path,color = Color(0x01000000), style = Stroke(width = 6f, miter = 5F, cap = StrokeCap.Round, join = StrokeJoin.Bevel), blendMode = BlendMode.Xor)
//            drawPath(path,color = colorStroke, style = Stroke(width = 6f, miter = 50F, cap = StrokeCap.Round, join = StrokeJoin.Bevel), blendMode = BlendMode.DstOut)
//            drawPath(path,color = colorStroke, style = Stroke(width = 12f, miter = 5F, cap = StrokeCap.Round, join = StrokeJoin.Bevel))
            drawPath(path,color = Color(0xFF576350), style = Stroke(width = strokeWidth), blendMode = BlendMode.SrcIn)
        }

    }
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
    var rectf= Rect(centX-diam/2, centY-diam/2, centX+diam/2, centY+diam/2)

    var diam2 = diam - 2*shir
    var p1 = fromPolar(angSt,diam/2, Point(centX,centY))
//        var p2 = fromPolar(angSt,diam2/2,PointF(centX,centY))
//        var p3 = fromPolar(angSt+angEnd,diam/2, PointF(centX,centY))
    var p4 = fromPolar(angSt+angEnd,diam2/2, Point(centX,centY))

//    val sm = 10f
//    val smX = sm*cos(toRad(angSt + ( angValue )/2  ))
//    val smY = sm*sin(toRad(angSt + ( angValue )/2  ))

    path.arcTo(rectf, angSt, angEnd, false)
    path.lineTo(p4.x,p4.y)
    rectf = Rect(centX-diam2/2, centY-diam2/2, centX+diam2/2, centY+diam2/2)
    path.arcTo(rectf, angSt+angEnd, - angEnd, false)
    path.lineTo(p1.x,p1.y)
    path.close()
    return path
}

fun fromPolar(ang: Float, rad: Float,cent: Point): Point{
    val ang1=((360-ang)* PI /180).toFloat()
    return Point(rad* cos(ang1) +cent.x,cent.y-rad* sin(ang1))
}

fun toRad(ang: Float) = ((360-ang)* PI /180).toFloat()

fun Color.toMyColorARGB(): MyColorARGB = MyColorARGB((this.alpha*255).toInt(), (this.red*255).toInt(), (this.green*255).toInt(), (this.blue*255).toInt())

class MyRectF(
    var width: Float,
    var height: Float,
    var offsetX: Float,
    var offsetY: Float
) {
    constructor(size: Size, offset: Offset) : this(size.width, size.height, offset.x, offset.y)
    constructor(rect: MyRectF) : this(rect.width, rect.height, rect.offsetX, rect.offsetY)
    constructor(rect: Rect) : this(rect.width, rect.height, rect.left, rect.top)

    fun getRect() = Rect(
        this.offsetX,
        this.offsetY,
        this.offsetX + this.width,
        this.offsetY + this.height
    )


    var size: Size
        get() = Size(width, height)
        set(value) {
            size = value
            width = value.width
            height = value.height
        }
    var offset: Offset
        get() = Offset(offsetX, offsetY)
        set(value) {
            offset = value
            offsetX = value.x
            offsetY = value.y
        }

    fun inset(inX: Float, inY: Float) {
        width -= inX * 2
        height -= inY * 2
        offsetX += inX
        offsetY += inY
    }


}
