package extensions

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.awt.event.MouseEvent

/**
 * https://stackoverflow.com/questions/71054138/jetpack-compose-inner-shadow
 *
 * что то не особо гладко работает, потому что изначально немного под другие библиотеки заточено, но вообще думаю при желании можно и доработать.
 * */
fun Modifier.innerShadow(
    color: Color = Color.Black,
    cornersRadius: Dp = 0.dp,
    spread: Dp = 0.dp,
    blur: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    drawIntoCanvas {

        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)
        it.drawRoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )

        val frameworkPaint = paint //.asFrameworkPaint()
        frameworkPaint.blendMode = BlendMode.DstOut // .xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
//            frameworkPaint.shader = Shader.b .maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blur.toPx()) // BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black
        it.drawRoundRect(
            left = left + spread.toPx() / 2,
            top = top + spread.toPx() / 2,
            right = right - spread.toPx() / 2,
            bottom = bottom - spread.toPx() / 2,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        frameworkPaint.blendMode = BlendMode.Clear
//        frameworkPaint.maskFilter = null
    }
}


@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.scrollVerticalToHorizontal(scrollSt: ScrollState, deltaMultiplic: Int = 40): Modifier {
    return this.pointerInput(Unit) {
        while (true) {
            val event = awaitPointerEventScope { awaitPointerEvent() }
            when (event.type) {
//                                    PointerEventType.Enter -> println("Enter")
                PointerEventType.Scroll -> {
                    event.changes.forEach {
                        if (it.scrollDelta.y != 0f) scrollSt.scrollTo(scrollSt.value + deltaMultiplic * it.scrollDelta.y.toInt())
//                                            println("Scroll: ${it.scrollDelta}")
                    }
                }
//                                    PointerEventType.Exit -> println("Left")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.scrollVerticalToHorizontal(scrollSt: LazyListState, deltaMultiplic: Int = 40): Modifier {
    return this.pointerInput(Unit) {
        while (true) {
            val event = awaitPointerEventScope { awaitPointerEvent() }
            when (event.type) {
//                                    PointerEventType.Enter -> println("Enter")
                PointerEventType.Scroll -> {
                    event.changes.forEach {
                        if (it.scrollDelta.y != 0f) scrollSt.scrollBy(deltaMultiplic * it.scrollDelta.y)// .scrollTo(scrollSt.value + deltaMultiplic*it.scrollDelta.y.toInt())
//                                            println("Scroll: ${it.scrollDelta}")
                    }
                }
//                                    PointerEventType.Exit -> println("Left")
            }
        }
    }
}

fun modLigthBack() = Modifier.border(
    width = 1.dp,
    brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
    shape = RoundedCornerShape(10.dp)
).background(
    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
    color = Color(0xFFE4E0C7),
)
fun Modifier.withSimplePlate(styleState: SimplePlateWithShadowStyleState): Modifier = this
    .clip(styleState.shape)
    .background(styleState.BACKGROUND, styleState.shape)
    .border(
        width = styleState.BORDER_WIDTH,
        brush = styleState.BORDER,
        shape = styleState.shape
    )

fun Modifier.withSimplePlate(styleState: SimplePlateStyleState): Modifier = this
    .clip(styleState.shape)
    .background(styleState.BACKGROUND, styleState.shape)
    .border(
        width = styleState.BORDER_WIDTH,
        brush = styleState.BORDER,
        shape = styleState.shape
    )

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Modifier.mouseDoubleClick(
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    rightClick: () -> Unit = {}
): Modifier {
//    var timePause = remember { Date().time }
//    var timePause = Date().time

    return this
        .onPointerEvent(PointerEventType.Press) {
            when (it.awtEventOrNull?.button) {
                MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount) {
                    1 -> {
                        onClick()
                    }
                    2 -> {
                        onDoubleClick()
                    }
                }
                MouseEvent.BUTTON2 -> {
                }
                MouseEvent.BUTTON3 -> {
                    onClick()
                    rightClick()
                }
            }
        }
/*
        .mouseClickable(
        onClick = {
            if (this.buttons.isSecondaryPressed) rightClick() else {
                val tmpTime = Date().time
                val tmpTime2 = timePause
                timePause = tmpTime
                    println("tmpTime = ${tmpTime}")
                    println("tmpTime2 = ${tmpTime2}")
                    println("timePause = ${timePause}")
                    println("tmpTime1-2 = ${tmpTime - tmpTime2}")
                if (tmpTime - tmpTime2 in 0..249) {
                    onDoubleClick()
                } else {
                    onClick()
                }
            }
        }
    )
*/


}

