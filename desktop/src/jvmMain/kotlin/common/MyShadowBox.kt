package common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.ImageFilter

@Composable
fun MyShadowBox(shadow: Shadow, modifier: Modifier = Modifier, contentAlignment: Alignment = Alignment.Center, content: @Composable BoxScope.() -> Unit) {
    with(LocalDensity.current) {
        Box(
            modifier
                .graphicsLayer(
                    renderEffect = ImageFilter.makeDropShadow(
                        shadow.offset.x.dp.toPx(), //2.dp.toPx(), //
                        shadow.offset.y.dp.toPx(), //2.dp.toPx(), //
                        shadow.blurRadius.dp.toPx(),
                        shadow.blurRadius.dp.toPx(),
                        shadow.color.toArgb()
                    )
                        .asComposeRenderEffect() // .makeBlur(elevation.toPx(), elevation.toPx(), FilterTileMode.REPEAT).asComposeRenderEffect()
                ),
//            propagateMinConstraints = true,
            contentAlignment = contentAlignment

        ) {
            content()
        }
    }
}