package common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
fun BackBoxEllipsGradient(
    width1: Dp = 10.dp,
    height1: Dp = 10.dp,
    modifier: Modifier = Modifier,
    colors: List<Color>? = null,
    brush: Brush? = null
) {

    BoxWithConstraints(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        val width = this.maxWidth
        val height = this.maxHeight
        val aspectRatio = width / height
        val scaleX = maxOf(aspectRatio, 1f)
        val scaleY = maxOf(1f / aspectRatio, 1f)
        Box(
            with(LocalDensity.current) {
                Modifier
                    .scale(scaleX, scaleY)
                    .matchParentSize()
                    .background(
                        brush = brush ?: Brush.radialGradient(
                            colors = colors ?: listOf(Color(0x8Fffffff), Color(0x8F000000)),
                            radius = minOf(width, height).toPx()
                        ),
                    )
            }
        )
    }
}