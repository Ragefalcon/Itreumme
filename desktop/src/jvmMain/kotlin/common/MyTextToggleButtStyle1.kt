package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import extensions.*
import viewmodel.MainDB

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
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
    with (myStyleToggleButton ?: ToggleTextButtonStyleState(MainDB.styleParam.timeParam.planTab.buttTextToggleTMP)) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        Box(
            modifier
                .shadow(getElevation().elevation(true, interactionSource).value, shapeCard)
                .border(BorderStroke(borderWidth,if (value.value) borderTrue else border), shapeCard)
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
                    indication = null //rememberRipple()
                ) {
                    value.value = value.value.not()
                    onClick(value.value)
                  }
        ) {
/*
    Button(
        modifier = Modifier.padding(3.dp),
        border = myButtBorder,//BorderStroke(1.dp,Color(0xFFFFF7D9)),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (!value.value) Color(0xFF464D45) else Color(0xFF468F45)),
//        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        onClick = {
            value.value = value.value.not()
            onClick(value.value)
        }
    ) {
*/
            Text(
                text,
                style = textStyle
/*
                TextStyle(
                    color = Color(0xFFFFF7D9),
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    //                letterSpacing = 4.sp,
                    textAlign = TextAlign.Center,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 4f
                    ),
                ),
*/

                )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
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
//    enabledColor: Color = Color.Red.toMyColorARGB().plusDark(0.9f).toColor(),
    onClick: (Boolean) -> Unit = {}
) {
    with(myStyleToggleButton) {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

/*
        Surface(
            modifier = modifier,
            shape = shapeCardShadow,
            color = Color.Transparent,
//            border = border,
            elevation = getElevation().elevation(true, interactionSource).value,
            onClick = {
                value.value = value.value.not()
                onClick(value.value)
            },
            interactionSource = interactionSource,
            indication = rememberRipple()
        ) {
*/
        with(LocalDensity.current) {
            MyShadowBox(
                shadow.copy(blurRadius = getElevation().elevation(true, interactionSource).value.toPx()),
//                modifier
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
//                        .shadow(getElevation().elevation(true, interactionSource).value, shapeCard)
                        .clip(shapeCard)
                        .border(BorderStroke(borderWidth,if (value.value) borderTrue else border), shapeCard)
                        .background(if (value.value) backgroundTrue else background, shapeCard)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null //rememberRipple()
                        ) {
                            value.value = value.value.not()
                            onClick(value.value)
                        }
//                    .border(BorderStroke(borderWidth, border), shapeCard)
//                    .background(background, shapeCard)
                        .then(padingInner),
//                    .padding(horizontal = 4.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .offset(2.dp - (if (isHovered) 4f else 2f).dp, 2.dp - (if (isHovered) 4f else 2f).dp)
//            .shadow((if (isHovered) 4f else 2f).dp, shape = CircleShape)
                    ) {
                        if (value.value) Image(
                            painterResource(
                                nameResOn ?: nameRes
                            ),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                            "buttIconStyle1",
                            Modifier
                                .height(sizeIcon)
                                .width(sizeIcon)
                                .run {
                                     if (rotateIcon) this.rotate(90f) else this
                                }
                            ,
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                colorTrue,
                                BlendMode.Modulate
                            ),

                            )
                        if (twoIcon || !value.value) Image(
                            painterResource(nameRes),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                            "buttIconStyle1",
                            Modifier
                                .height(sizeIcon)
                                .width(sizeIcon)
                                .run {
                                    if (rotateIcon) this.rotate(90f) else this
                                }
                            ,
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
//        }

    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
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
                ),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
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
                painterResource(nameRes),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
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
                //                letterSpacing = 4.sp,
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
