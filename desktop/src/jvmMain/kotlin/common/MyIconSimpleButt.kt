package common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import extensions.toMyColorARGB

@Composable
fun MyIconSimpleButt(iconResInner: String, color: Color, funRez: () -> Unit) {
    if (iconResInner != "") with(LocalDensity.current) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        MyShadowBox(
            Shadow(
                if (isHovered) Color.Black.copy(0.7f) else Color.Transparent,
                if (isHovered) Offset(3.dp.toPx(), 3.dp.toPx()) else Offset.Zero,
                2.5.dp.toPx()
            ), Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                funRez()
            }
        ) {
            Image(
                painterResource(iconResInner),
                "statDenPlan",
                Modifier
                    .padding(horizontal = 2.dp, vertical = 0.dp)
                    .run {
                        if (isHovered)
                            this.offset(
                                (-2).dp,
                                (-2).dp
                            )
                        else this
                    }
                    .height(30.dp)
                    .width(30.dp)
                    .run {
                        if (color.toMyColorARGB().A == 0) this.alpha(0.3f) else this
                    },
                colorFilter = ColorFilter.tint(
                    color,
                    BlendMode.Modulate
                ),
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}
