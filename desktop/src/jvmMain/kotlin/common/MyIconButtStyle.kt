package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.IconButtonStyleState


@Composable
fun MyIconButtStyle(
    nameRes: String,
    modifier: Modifier = Modifier,
    sizeIcon: Dp = 40.dp,
    myStyleButton: IconButtonStyleState,
    width: Dp? = null,
    height: Dp? = null,
    onClick: () -> Unit
) {
    with(myStyleButton) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(blurRadius = getElevation().elevation(true, interactionSource).value.toPx()),
            ) {
                Box(modifier
                    .run { width?.let { this.width(it) } ?: this }
                    .run { height?.let { this.height(it) } ?: this }
                    .clip(shapeCard).border(BorderStroke(borderWidth, border), shapeCard)
                    .background(background, shapeCard)
                    .clickable(interactionSource = interactionSource, indication = null) {
                        onClick()
                    }.then(padingInner), contentAlignment = Alignment.Center
                ) {
                    MyShadowBox(
                        Shadow(
                            Color.Black,
                            Offset(if (isHovered) 4f else 2f, if (isHovered) 4f else 2f),
                            if (isHovered) 4f else 2f
                        ), Modifier
                    ) {
                        Box(
                            Modifier.padding(2.dp)
                                .offset(
                                    2.dp - (if (isHovered) 4f else 2f).dp,
                                    2.dp - (if (isHovered) 4f else 2f).dp
                                )
                        ) {
                            Image(
                                painterResource(nameRes),
                                "buttIconStyle1",
                                Modifier.height(sizeIcon).width(sizeIcon),
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(colorIcon, BlendMode.Modulate),
                            )
                        }
                    }
                }
            }
        }
    }
}

