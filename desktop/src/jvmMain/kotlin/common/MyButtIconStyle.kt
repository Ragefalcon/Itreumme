package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.ToggleButtonStyleState


@Composable
fun MyButtIconStyle(
    nameRes: String,
    modifier: Modifier = Modifier,
    sizeIcon: Dp = 40.dp,
    myStyleToggleButton: ToggleButtonStyleState,
    width: Dp? = null,
    height: Dp? = null,
    rotation: Boolean = false,
    onClick: () -> Unit = {}
) {
    with(myStyleToggleButton) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        Box(
            modifier
                .shadow(getElevation().elevation(true, interactionSource).value, shapeCard)
                .border(BorderStroke(borderWidth, if (isHovered) borderTrue else border), shapeCard)
                .background(if (isHovered) backgroundTrue else background, shapeCard)
                .run {
                    width?.let {
                        this.width(it)
                    } ?: this
                }
                .run {
                    height?.let {
                        this.height(it)
                    } ?: this
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple()
                ) {
                    onClick()
                }
        ) {
            Box(
                Modifier
                    .then(padingInner),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp)
                ) {
                    Image(
                        painterResource(nameRes),
                        "buttIconStyle1",
                        Modifier
                            .height(sizeIcon)
                            .width(sizeIcon)
                            .rotate(if (rotation) 90f else 0f),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            if (isHovered) colorTrue else colorFalse,
                            BlendMode.Modulate
                        ),
                    )
                }
            }
        }
    }
}
