package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.TextButtonStyleState
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import viewmodel.StateVM

val myButtColorBorder = MyColorARGB.colorMyBorderStrokeCommon.toColor()
val myButtWidthBorder = 0.5.dp
val myButtBorder = BorderStroke(myButtWidthBorder, myButtColorBorder)

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyTextButtSimpleStyle(
    text: String,
    color: Color = MyColorARGB.colorMyBorderStroke.toColor(),
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    fontSize: TextUnit = 17.sp,
    textAlign: TextAlign = TextAlign.Center,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Box(modifier
        .mouseClickable {
            onClick()
        }
        .hoverable(interactionSource = interactionSource)
    ) {
        Text(
            text,
            modifier = modifierText
                .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp),
            style = MyTextStyleParam.style1.copy(
                color = color,
                textAlign = textAlign,
                shadow = Shadow(
                    offset = if (isHovered) Offset(4f, 4f) else Offset(2f, 2f),
                    blurRadius = if (isHovered) 4f else 2f
                ), fontSize = fontSize
            )
        )
    }

}


@Composable
fun MyTextButtStyle1(
    text: String,
    modifier: Modifier = Modifier,
    radius: Dp = 10.dp,
    modifierText: Modifier = Modifier,
    fontSize: TextUnit? = null,
    backgroundColor: Color = Color(0xFF464D45),
    myStyleTextButton: TextButtonStyleState? = null,
    width: Dp? = null,
    height: Dp? = null,

    onClick: () -> Unit = {}
) {
    with(myStyleTextButton ?: StateVM.commonButtonStyleState.value) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(blurRadius = getElevation().elevation(true, interactionSource).value.toPx()),
                modifier
            ) {
                Box(
                    Modifier
                        .clip(shapeCard)
                        .border(BorderStroke(borderWidth, border), shapeCard)
                        .background(background, shapeCard)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onClick()
                        }
                        .run {
                            width?.let {
                                this.width(it)
                            } ?: this.padding(horizontal = 24.dp)
                        }
                        .run {
                            height?.let {
                                this.height(it)
                            } ?: this.padding(vertical = 8.dp)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text,
                        modifier = modifierText
                            .offset(
                                if (isHovered) offsetTextHover.x.dp else 0.dp,
                                if (isHovered) offsetTextHover.y.dp else 0.dp
                            )
                            .padding(0.dp),
                        style = textStyle.copy(
                            fontSize = fontSize ?: textStyle.fontSize,
                            textAlign = TextAlign.Center,
                            shadow = if (isHovered) textStyleShadowHover.shadow else textStyle.shadow
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MyTextButtStyle2(
    text: String,
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    myStyleTextButton: TextButtonStyleState? = null,

    onClick: () -> Unit = {}
) {
    with(myStyleTextButton ?: StateVM.commonItemStyleState.value.buttMenu) {

        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(
                    blurRadius = getElevation().elevation(
                        true,
                        interactionSource
                    ).value.toPx()
                ), modifier, contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .matchParentSize()
                        .clip(shapeCard)
                        .background(background, shapeCard)
                        .border(BorderStroke(borderWidth, border), shapeCard)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onClick()
                        }
                )
                Text(
                    text,
                    modifier = modifierText
                        .offset(
                            if (isHovered) offsetTextHover.x.dp else 0.dp,
                            if (isHovered) offsetTextHover.y.dp else 0.dp
                        )
                        .padding(0.dp),
                    style = textStyle.copy(
                        fontSize = fontSize,
                        textAlign = TextAlign.Center,
                        shadow = if (isHovered) textStyleShadowHover.shadow else textStyle.shadow
                    )
                )
            }
        }
    }
}

@Composable
fun MyTextButtStyle3(
    modifier: Modifier = Modifier,
    radius: Dp = 10.dp,
    backgroundColor: Color? = Color(0xFF464D45),
    content: @Composable (RowScope) -> Unit,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.padding(2.dp),
        border = myButtBorder,
        shape = RoundedCornerShape(corner = CornerSize(radius)),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor ?: Color(0xFF464D45)),
        onClick = {
            onClick()
        }
    ) {
        content(this)
    }
}

@Composable
fun MyTextStyle1(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFFF7D9),
    fontSize: TextUnit = 20.sp,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text,
        modifier = modifier.padding(0.dp),
        style = TextStyle(
            color = color,
            fontSize = fontSize,
            fontFamily = FontFamily.SansSerif,
            textAlign = textAlign,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(4f, 4f),
                blurRadius = 4f
            ),
        ),
    )
}

@Composable
fun MyTextStyle2(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFFF7D9),
    fontSize: TextUnit = 20.sp,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text,
        modifier = modifier.padding(0.dp),
        style = TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = textAlign,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.7f),
                offset = Offset(2f, 2f),
                blurRadius = 4f
            ),
        ),
    )
}

