package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.ToggleButtonStyleState
import extensions.ToggleTextButtonStyleState
import extensions.toColor
import extensions.toMyColorARGB
import viewmodel.MainDB

@Composable
fun MyTextToggleButtStyle1(
    text: String,
    value: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
    myStyleToggleButton: ToggleTextButtonStyleState? = null,
    onClick: (Boolean) -> Unit = {}
) {
    with(myStyleToggleButton ?: ToggleTextButtonStyleState(MainDB.styleParam.timeParam.planTab.buttTextToggleTMP)) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        Box(
            modifier
                .shadow(getElevation().elevation(true, interactionSource).value, shapeCard)
                .border(BorderStroke(borderWidth, if (value.value) borderTrue else border), shapeCard)
                .background(if (value.value) backgroundTrue else background, shapeCard)
                .run {
                    width?.let {
                        this.width(it)
                    } ?: this.padding(horizontal = 24.dp)
                }
                .run {
                    height?.let {
                        this.height(it)
                    } ?: this.padding(vertical = 8.dp)
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    value.value = value.value.not()
                    onClick(value.value)
                }
        ) {
            Text(
                text,
                style = textStyle
            )
        }
    }
}

@Composable
fun MyToggleButtIconStyle1(
    nameRes: String,
    nameResOn: String? = null,
    twoIcon: Boolean = false,
    value: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    sizeIcon: Dp = 40.dp,
    myStyleToggleButton: ToggleButtonStyleState,
    width: Dp? = null,
    height: Dp? = null,
    rotateIcon: Boolean = false,

    onClick: (Boolean) -> Unit = {}
) {
    with(myStyleToggleButton) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(blurRadius = getElevation().elevation(true, interactionSource).value.toPx()),
            ) {
                Box(
                    modifier
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
                        .clip(shapeCard)
                        .border(BorderStroke(borderWidth, if (value.value) borderTrue else border), shapeCard)
                        .background(if (value.value) backgroundTrue else background, shapeCard)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            value.value = value.value.not()
                            onClick(value.value)
                        }
                        .then(padingInner),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp)
                    ) {
                        if (value.value) Image(
                            painterResource(
                                nameResOn ?: nameRes
                            ),
                            "buttIconStyle1",
                            Modifier
                                .height(sizeIcon)
                                .width(sizeIcon)
                                .run {
                                    if (rotateIcon) this.rotate(90f) else this
                                },
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                colorTrue,
                                BlendMode.Modulate
                            ),
                        )
                        if (twoIcon || !value.value) Image(
                            painterResource(nameRes),
                            "buttIconStyle1",
                            Modifier
                                .height(sizeIcon)
                                .width(sizeIcon)
                                .run {
                                    if (rotateIcon) this.rotate(90f) else this
                                },
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                colorFalse,
                                BlendMode.Modulate
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyIconTextIndikatorStyle1(
    nameRes: String = "ic_round_check_circle_outline_24.xml",
    nameResOn: String? = "ic_round_check_circle_24.xml",
    text: String = "",
    twoIcon: Boolean = false,
    value: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    colorOn: Color = Color.Green,
    colorOff: Color? = Color.Red.toMyColorARGB().plusDark(0.9f).toColor(),
    colorText: Color = Color.Black,
    sizeIcon: Dp = 40.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            if (twoIcon || value.value) Image(
                painterResource(
                    nameResOn ?: nameRes
                ),
                "buttIconStyle1",
                Modifier
                    .height(sizeIcon)
                    .width(sizeIcon),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    if (value.value) colorOn else colorOff ?: Color.Transparent,
                    BlendMode.Modulate
                ),
            )
            if (twoIcon || !value.value) Image(
                painterResource(nameRes),
                "buttIconStyle1",
                Modifier
                    .height(sizeIcon)
                    .width(sizeIcon),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    if (twoIcon) Color.White else colorOff ?: Color.Red.toMyColorARGB().plusDark(0.9f).toColor(),
                    BlendMode.Modulate
                ),
            )
        }
        Text(
            text,
            style = TextStyle(
                color = Color(0xFFFFF7D9),
                fontSize = (sizeIcon.value / 1.5f).sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Start,
                shadow = Shadow(
                    color = colorText,
                    offset = Offset(2f, 2f),
                    blurRadius = 2f
                ),
            ),
        )
    }
}
