package extensions

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MyTypeCorner

class CustomShape(
    val topStartShape: MyTypeCorner,
    val topEndShape: MyTypeCorner,
    val bottomStartShape: MyTypeCorner,
    val bottomEndShape: MyTypeCorner,
    topStart: CornerSize,
    topEnd: CornerSize,
    bottomStart: CornerSize,
    bottomEnd: CornerSize,
    val hole: Boolean = false
) : //Shape
    CornerBasedShape(
    topStart,
            topEnd,
        bottomEnd,
        bottomStart
    )
{

    constructor(typeCorner: MyTypeCorner, radius: CornerSize, hole: Boolean = false) : this(
        typeCorner,
        typeCorner,
        typeCorner,
        typeCorner,
        radius,
        radius,
        radius,
        radius,
        hole
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomShape) return false

        if (topStartShape != other.topStartShape) return false
        if (topEndShape != other.topEndShape) return false
        if (bottomStartShape != other.bottomStartShape) return false
        if (bottomEndShape != other.bottomEndShape) return false
        if (topStart != other.topStart) return false
        if (topEnd != other.topEnd) return false
        if (bottomStart != other.bottomStart) return false
        if (bottomEnd != other.bottomEnd) return false
        if (hole != other.hole) return false

        return true
    }
    override fun hashCode(): Int {
        var result = topStart.hashCode()
        result = 31 * result + topEnd.hashCode()
        result = 31 * result + bottomEnd.hashCode()
        result = 31 * result + bottomStart.hashCode()
        result = 31 * result + topStartShape.hashCode()
        result = 31 * result + topEndShape.hashCode()
        result = 31 * result + bottomStartShape.hashCode()
        result = 31 * result + bottomEndShape.hashCode()
        result = 31 * result + hole.hashCode()
        return result
    }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ): CornerBasedShape = CustomShape(
    topStartShape,
    topEndShape,
    bottomStartShape,
    bottomEndShape,
    topStart,
    topEnd,
    bottomStart,
    bottomEnd,
    hole
    )

/*
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        with(density) {
            return Outline.Generic(
                // Draw your custom path here
                path = drawTicketPath(
                    size = size,
                    topStartShape = topStartShape,
                    topEndShape = topEndShape,
                    bottomStartShape = bottomStartShape,
                    bottomEndShape = bottomEndShape,
                    topStart = topStart.toPx(size,density),
                    topEnd = topEnd.toPx(size,density),
                    bottomStart = bottomStart.toPx(size,density),
                    bottomEnd = bottomEnd.toPx(size,density),
                    hole = hole
                )
            )
        }
    }
*/

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ): Outline = Outline.Generic(
        // Draw your custom path here
        path = drawTicketPath(
            size = size,
            topStartShape = topStartShape,
            topEndShape = topEndShape,
            bottomStartShape = bottomStartShape,
            bottomEndShape = bottomEndShape,
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd,
            hole = hole
        )
    )
}

class CustomShapeForShadow(
    private val path: Path,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val p2 = Path().apply {
            addRect(Rect(Offset(-1000000f, -1000000f), Size(10f, 10f)))
        }
        val pathRez = Path()
        pathRez.op(path, p2, PathOperation.Union)
        return Outline.Generic(path = pathRez)
    }
}


fun drawTicketPath(
    size: Size,
    topStartShape: MyTypeCorner,
    topEndShape: MyTypeCorner,
    bottomStartShape: MyTypeCorner,
    bottomEndShape: MyTypeCorner,
    topStart: Float,
    topEnd: Float,
    bottomStart: Float,
    bottomEnd: Float,
    hole: Boolean = false
): Path
{
    val rez = Path().apply {
//        addRoundRect(RoundRect(Rect(Offset(0f,0f),size),
//            CornerRadius(topStart,topStart),
//            CornerRadius(topEnd,topEnd),
//            CornerRadius(bottomEnd,bottomEnd),
//            CornerRadius(bottomStart,bottomStart)))
//        reset()
        // Top left arc
        var topSt = topStart
        var topEn = topEnd
        var bottomSt = bottomStart
        var bottomEn = bottomEnd

        if (bottomStart > 0) {
            if (topStart > 0) {
                if (bottomStart + topStart > size.height) {
                    val koefSt = size.height / (bottomStart + topStart)
                    bottomSt = bottomStart * koefSt
                    topSt = size.height - bottomSt
                } else {
                    bottomSt = bottomStart
                    topSt = topStart
                }
            } else {
                if (bottomStart > size.height) {
                    bottomSt = size.height
                } else {
                    bottomSt = bottomStart
                }
            }
        } else {
            if (topStart > size.height) {
                topSt = size.height
            } else {
                topSt = topStart
            }
        }

        if (bottomEnd > 0) {
            if (topEnd > 0) {
                if (bottomEnd + topEnd > size.height) {
                    val koefEn = size.height / (bottomEnd + topEnd)
                    bottomEn = bottomEnd * koefEn
                    topEn = size.height - bottomEn
                } else {
                    bottomEn = bottomEnd
                    topEn = topEnd
                }
            } else {
                if (bottomEnd > size.height) {
                    bottomEn = size.height
                } else {
                    bottomEn = bottomEnd
                }
            }
        } else {
            if (topEnd > size.height) {
                topEn = size.height
            } else {
                topEn = topEnd
            }
        }

        if (topSt < 0f) topSt = 0f
        if (topEn < 0f) topEn = 0f
        if (bottomSt < 0f) bottomSt = 0f
        if (bottomEn < 0f) bottomEn = 0f
//        moveTo(0f, topSt)
//        lineTo(topSt, 0f)
        when (topStartShape) {
            MyTypeCorner.Round -> {
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = 2 * topSt,
                        bottom = 2 * topSt
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            MyTypeCorner.Cut -> {
                moveTo(0f, topSt)
                lineTo(topSt, 0f)
            }
            MyTypeCorner.Ticket -> {
                arcTo(
                    rect = Rect(
                        left = -topSt,
                        top = -topSt,
                        right = topSt,
                        bottom = topSt
                    ),
                    startAngleDegrees = 90.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
            }
        }

        lineTo(x = size.width - topEn, y = 0f)

//        lineTo(size.width, topEn)
        when (topEndShape) {
            MyTypeCorner.Round -> {
                arcTo(
                    rect = Rect(
                        left = size.width - 2 * topEn,
                        top = 0f,
                        right = size.width,
                        bottom = 2 * topEn
                    ),
                    startAngleDegrees = 270.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            MyTypeCorner.Cut -> {
                lineTo(size.width, topEn)
            }
            MyTypeCorner.Ticket -> {
                arcTo(
                    rect = Rect(
                        left = size.width - topEn,
                        top = -topEn,
                        right = size.width + topEn,
                        bottom = topEn
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
            }
        }
        lineTo(x = size.width, y = size.height - bottomEn)
        // Bottom right arc
//        lineTo(size.width - bottomEn, size.height)
        when (bottomEndShape) {
            MyTypeCorner.Round -> {
                arcTo(
                    rect = Rect(
                        left = size.width - 2 * bottomEn,
                        top = size.height - 2 * bottomEn,
                        right = size.width,
                        bottom = size.height
                    ),
                    startAngleDegrees = 0.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            MyTypeCorner.Cut -> {
                lineTo(size.width - bottomEn, size.height)
            }
            MyTypeCorner.Ticket -> {
                arcTo(
                    rect = Rect(
                        left = size.width - bottomEn,
                        top = size.height - bottomEn,
                        right = size.width + bottomEn,
                        bottom = size.height + bottomEn
                    ),
                    startAngleDegrees = 270.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
            }
        }
        lineTo(x = bottomSt, y = size.height)
        // Bottom left arc
//        lineTo(0f, size.height - bottomSt)
        when (bottomStartShape) {
            MyTypeCorner.Round -> {
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = size.height - 2 * bottomSt,
                        right = 2 * bottomSt,
                        bottom = size.height
                    ),
                    startAngleDegrees = 90.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            MyTypeCorner.Cut -> {
                lineTo(0f, size.height - bottomSt)
            }
            MyTypeCorner.Ticket -> {
                lineTo(0f, size.height - bottomSt)
                arcTo(
                    rect = Rect(
                        left = -bottomSt,
                        top = size.height - bottomSt,
                        right = bottomSt,
                        bottom = size.height + bottomSt
                    ),
                    startAngleDegrees = 0.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
            }
        }
        lineTo(x = 0f, y = topSt)
        close()
        if (hole) {
//                addRect(Rect(Offset(size.width/2, size.height/2), Size(1f, 1f)))
                addRect(Rect(Offset(-5000f, -5000f), Size(2f, 2f)))
        }
    }
//    if (hole) {
//        val p2 = Path().apply {
////            addOval(Rect(size.width / 2, size.height / 2, size.width / 2 + 10f, size.height / 2 + 10f))
//            addRect(Rect(Offset(-10f, -10f), Size(10f, 10f)))
////            addOval(Rect(size.width * 2, size.height * 2, size.width * 2 + 10f, size.height * 2 + 10f))
//        }
//        val p3 = Path()
//        p3.op(rez, p2, PathOperation.Union)
//        return p3
//    } else
        return rez

}

/*
@Composable
fun TicketComposable(modifier: Modifier) {
    Text(
        text = "🎉 CINEMA TICKET 🎉",
        style = TextStyle(
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .wrapContentSize()
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = TicketShape(24.dp)
                clip = true
            }
            .background(color = Color.DarkGray)
            .drawBehind {
                scale(scale = 0.9f) {
                    drawPath(
                        path = drawTicketPath(size = size, cornerRadius = 24.dp.toPx()),
                        color = Color.Red,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    )
                }
            }
            .padding(start = 32.dp, top = 64.dp, end = 32.dp, bottom = 64.dp)
    )
}*/
