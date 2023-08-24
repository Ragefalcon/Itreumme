package common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.IconButtonStyleState
import extensions.IconButtonWithoutBorderStyleState
import extensions.mouseDoubleClick
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyTextButtWithoutBorder(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 17.sp,
    textColor: Color = MyColorARGB.colorMyBorderStroke.toColor(),
    onDoubleClick: (() -> Unit)? = null,
    onRightClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Box(modifier
        .run {
            if (onDoubleClick != null || onRightClick != null)
                this.mouseDoubleClick(onClick, onDoubleClick ?: {}, onRightClick ?: {})
            else this.mouseClickable {
                onClick()
            }
        }
        .hoverable(interactionSource = interactionSource)) {
        Text(
            text,
            modifier = Modifier
                .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp),
            style = MyTextStyleParam.style1.copy(
                shadow = Shadow(
                    offset = if (isHovered) Offset(4f, 4f) else Offset(2f, 2f), //Offset(activeElev,activeElev),
                    blurRadius = if (isHovered) 4f else 2f //activeElev
                ), fontSize = fontSize,
                color = textColor
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyIconButtWithoutBorder(
    nameRes: String,
    modifier: Modifier = Modifier,
    sizeIcon: Dp = 40.dp,
    myStyleButton: IconButtonWithoutBorderStyleState,
    onDoubleClick: (() -> Unit)? = null,
    onRightClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    with(myStyleButton) {
        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(blurRadius = getElevation().elevation(true, interactionSource).value.toPx(), offset = Offset(
                    shadow.offset.x*(if (isHovered) 2f else 1f),
                    shadow.offset.y*(if (isHovered) 2f else 1f)
                )),
            ) {
                Box(modifier
                    .run {
                        if (onDoubleClick != null || onRightClick != null)
                            this.mouseDoubleClick(onClick, onDoubleClick ?: {}, onRightClick ?: {})
                        else this.mouseClickable {
                            onClick()
                        }
                    }
                    .hoverable(interactionSource = interactionSource)) {
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

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyTextToggleButtWithoutBorder(
    text: String,
    boolVal: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 17.sp,
    textStyle: TextStyle? = null,
    textColor: Color = MyColorARGB.colorMyBorderStroke.toColor(),
    textColorTrue: Color = MyColorARGB.colorDoxodTheme.toColor(),
    onClick: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Box(modifier
        .mouseClickable {
            boolVal.value = boolVal.value.not()
            onClick(boolVal.value)
        }
        .hoverable(interactionSource = interactionSource)) {
        Text(
            text,
            modifier = Modifier
                .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp),
            style = (textStyle ?: MyTextStyleParam.style1).copy(
                shadow = Shadow(
                    offset = if (isHovered) Offset(4f, 4f) else Offset(2f, 2f), //Offset(activeElev,activeElev),
                    blurRadius = if (isHovered) 4f else 2f //activeElev
                ), fontSize = fontSize,
                color = if (boolVal.value) textColorTrue else textColor
            )
        )
    }
}
