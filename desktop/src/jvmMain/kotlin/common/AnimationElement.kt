package common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun RotationButtStyle1(
    expandedOpis: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 17.sp,
    color: Color = MyColorARGB.colorMyBorderStrokeCommon.toColor(),
    onClick: () -> Unit
) {
    val rotateStat: Float by animateFloatAsState(
        targetValue = if (expandedOpis.value) -180F else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    ) {
    }
    MyTextButtSimpleStyle(
        "ᐁ",
        color,
        modifier,
        Modifier
            .graphicsLayer {
                rotationZ = rotateStat
            },
        fontSize
    ) {
        onClick()
        expandedOpis.value = expandedOpis.value.not()
    }
}


@Composable
fun BoxExpand(
    expandedOpis: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    endModif: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier.padding(if (expandedOpis.value) 5.dp else 0.dp).then(endModif).animateContentSize()
    ) {
        if (expandedOpis.value)
            content()
    }

}

fun Modifier.myModWithBound1(radius: Dp = 15.dp) = this.border(
    width = 0.5.dp,
    brush = Brush.horizontalGradient(
        listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
    ),
    shape = RoundedCornerShape(radius)
)

fun Modifier.withMyBound1(radius: Dp = 15.dp) = this.border(
    width = 0.5.dp,
    brush = Brush.horizontalGradient(
        listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
    ),
    shape = RoundedCornerShape(radius)
)