package common

import androidx.compose.material.Text
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
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
        // Configure the animation duration and easing.
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    ) {
//            if (!expandedVis.value) expanded.value = false
    }
    MyTextButtSimpleStyle(
        "ᐁ",
        color,
        modifier,
        Modifier
//            .rotate(rotateStat)
            .graphicsLayer {
                           rotationZ = rotateStat
            }
        ,
        fontSize
    ){
        onClick()
        expandedOpis.value = expandedOpis.value.not()
    }
/*
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Box (modifier
        .mouseClickable {
            onClick()
            expandedOpis.value = expandedOpis.value.not()
        }
        .hoverable(interactionSource = interactionSource)
    ) {
        Text(
            "ᐁ",
            modifier = Modifier
                .rotate(rotateStat)
                .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp)
            ,
            style = MyTextStyleParam.style1.copy(
                shadow = Shadow(
                    offset = if (isHovered) Offset(4f, 4f) else Offset(2f, 2f),
                    blurRadius = if (isHovered) 4f else 2f
                ), fontSize = fontSize
            )
        )
    }

*/
}


@Composable
fun BoxExpand(
    expandedOpis: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    endModif: Modifier = Modifier,
//    .()->Modifier = {
//                                      this
//    },
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier.padding(if (expandedOpis.value) 5.dp else 0.dp).then(endModif).animateContentSize() //.fillMaxWidth()
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