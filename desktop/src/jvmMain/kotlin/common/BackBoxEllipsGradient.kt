package common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
//fun BackBoxEllipsGradient(width: MutableState<Dp>, height: MutableState<Dp>, colors: List<Color>? = null, brush: Brush? = null) {
fun BackBoxEllipsGradient(width1: Dp = 10.dp, height1: Dp = 10.dp, modifier: Modifier = Modifier, colors: List<Color>? = null, brush: Brush? = null) {

    BoxWithConstraints(
        modifier
//            .width(width)
//            .height(height)
            ,
        contentAlignment = Alignment.Center
    ) {
        val width = this.maxWidth
        val height = this.maxHeight
//        println("width/max: ${width}/${this.maxWidth}")
//        println("height/max: ${height}/${this.maxHeight}")
        val aspectRatio = width / height
        val scaleX = maxOf(aspectRatio, 1f)
        val scaleY = maxOf(1f / aspectRatio, 1f)
//        val aspectRatio = remember { mutableStateOf(width / height)}
//        val scaleX = remember { mutableStateOf(maxOf(aspectRatio.value, 1f))}
//        val scaleY = remember { mutableStateOf(maxOf(1f / aspectRatio.value, 1f))}
//        LaunchedEffect(width,height) {
//            println("width: ${width}")
//            println("height: ${height}")
//            aspectRatio.value = width / height
//            scaleX.value = maxOf(aspectRatio.value, 1f)
//            scaleY.value = maxOf(1f / aspectRatio.value, 1f)
//        }
//        Box(
//            Modifier
//                .width(maxWidth)
//                .height(maxHeight)
//                .border( 2.dp, Color.Red, shape = RoundedCornerShape(25.dp))
//        )
        Box(
            with(LocalDensity.current) {
                Modifier
                    .scale(scaleX, scaleY)
                    .matchParentSize()
//                .width(minOf(width,height))
//                .height(minOf(width,height))
                    .background(
                        brush = brush ?: Brush.radialGradient(
                            colors = colors ?: listOf(Color(0x8Fffffff), Color(0x8F000000)),
                            radius = minOf(width, height).toPx() // value * 2//*0.8f// *0.69F //maxOf(scaleX,scaleY)*
                        ),
                    )
            }
        )
//        Column {
//            Text(this@BoxWithConstraints.maxWidth.toString(),color = Color.Green)
//            Text(this@BoxWithConstraints.maxHeight.toString(),color = Color.Green)
//            Text(scaleY.toString(),color = Color.Green)
//            Text(offsetY.toString(),color = Color.Green)
//        }
    }
}