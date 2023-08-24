package common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.SimplePlateStyleState

@Composable
fun BackgroungPanelStyle1(
    modifier: Modifier = Modifier,
    radius: Dp = 18.dp,
    vignette: Boolean = true,
    style: SimplePlateStyleState? = null,
    modifierContent: Modifier = Modifier,
    shapePanel: Shape? = null,
    content: @Composable BoxWithConstraintsScope.() -> Unit = {}) {
    Box(
        modifier
            .wrapContentSize()
            .run {
                style?.let {
                    with(it){
                     this@run
                         .background(BACKGROUND,shapePanel ?: shape)
                         .border(BORDER_WIDTH,BORDER,shapePanel ?: shape)
                         .clip(shapePanel ?: shape)

                    }
                }   ?: this
                    .background(color = Color(0xFF576350), shape = shapePanel ?: RoundedCornerShape(radius))
                    .clip(shapePanel ?: RoundedCornerShape(radius))
/*
                    .graphicsLayer {
//            shadowElevation = 8.dp.toPx()
                        shape = RoundedCornerShape(radius - 2.dp)
                        clip = true
                    }
*/
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xBFFFF7D9), Color(0xbFFFF7D9))),
                        shape = shapePanel ?: RoundedCornerShape(radius)
                    )
                 }
    ) {
            if (vignette) BackBoxEllipsGradient(
                modifier = Modifier.matchParentSize(),
//                colors = listOf(Color.Transparent, Color(0xFF000000), Color(0xFF000000), Color(0xFF000000))
                        colors = listOf(Color.Transparent, Color(0x3F000000), Color(0x8F000000), Color(0xFF000000))
            )
            BoxWithConstraints(modifierContent.wrapContentSize()
            ) {
                content()
            }
    }
}

@Preview
@Composable
fun PreviewBackgroungPanelStyle1() {
    BackgroungPanelStyle1 {// mod ->
        Column(Modifier.padding(100.dp)) {
            Text("asdfasdf")
            Text("14122134")
            Text("saf125fg")
        }
    }
}
