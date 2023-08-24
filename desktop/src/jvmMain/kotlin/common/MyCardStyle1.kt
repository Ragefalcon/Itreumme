package common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.CommonItemStyleState
import org.jetbrains.skia.ImageFilter
import viewmodel.StateVM
import java.awt.event.MouseEvent

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyCardStyle1(
    active: Boolean,
    level: Long = 0,
    onClick: () -> Unit = {}, //MouseClickScope.
    onDoubleClick: () -> Unit = {}, //MouseClickScope.
    backColor: Color? = null,//Color(0xFF464D45),
    backBrush: Brush? = null,//Color(0xFF464D45),
    borderBrush: Brush? = null,//Color(0xFF464D45),
    modifierThen: Modifier = Modifier,
    widthBorder: Dp? = null,
    fillWidth: Boolean = true,
    styleSettings: CommonItemStyleState? = null,
//    horizontal: Boolean = false,
    dropMenu: @Composable (ColumnScope.(MutableState<Boolean>) -> Unit)? = null,
    multiBorder: Modifier? = null,
    modifier: Modifier = Modifier,
    levelValue: Double = 20.0,
    content: @Composable () -> Unit
) {

    with(LocalDensity.current) {
        with(styleSettings?.let { if (it.active) it else StateVM.commonItemStyleState.value }
            ?: StateVM.commonItemStyleState.value) {
            var xBox by remember { mutableStateOf(0.dp) }
            var yBox by remember { mutableStateOf(0.dp) }
            val expandedDropMenuRightButton = remember { mutableStateOf(false) }
            Box(modifier
                .graphicsLayer(
                    renderEffect = ImageFilter.makeDropShadow(
                        shadow.offset.x.dp.toPx(), //2.dp.toPx(), //
                        shadow.offset.y.dp.toPx(), //2.dp.toPx(), //
                        shadow.blurRadius.dp.toPx(),
                        shadow.blurRadius.dp.toPx(),
                        shadow.color.toArgb()
                    ).asComposeRenderEffect() // .makeBlur(elevation.toPx(), elevation.toPx(), FilterTileMode.REPEAT).asComposeRenderEffect()
                )
                ){
//                if (elevation != 0.dp) Box(Modifier
//                    .matchParentSize()
//                    .then(paddingOuter)
////                    .background(Color.Black, shapeCardShadow)
//                    .graphicsLayer(
//                        renderEffect = ImageFilter.makeDropShadow(
//                            elevation.toPx(),
//                            elevation.toPx(),
//                            elevation.toPx(),
//                            elevation.toPx(),Color.Black.toArgb()).asComposeRenderEffect() // .makeBlur(elevation.toPx(), elevation.toPx(), FilterTileMode.REPEAT).asComposeRenderEffect()
//                    )
//                ){
//                    Box(Modifier
//                        .matchParentSize()
////                        .offset(elevation/2,elevation/2 )
////                        .offset( 2.dp,2.dp )
//                        .background(Color.Black.copy(0.22f), shapeCardShadow)
//                    )
//                }
                Box(
                    modifier = Modifier
                        .padding(start = (level * levelValue).toInt().dp)
                        .then(paddingOuter)
                        .pointerMoveFilter(
                            onMove = {
                                if (!expandedDropMenuRightButton.value) {
                                    xBox = it.x.toDp() - 8.dp
                                    yBox = it.y.toDp() - 1.dp
                                }
                                false
                            }
                        )
//                    .run {
//                        if (fillWidth) this.fillMaxWidth() else this
//                    }
                        .onPointerEvent(PointerEventType.Press) {
                            when (it.awtEventOrNull?.button) {
                                MouseEvent.BUTTON1 -> when (it.awtEventOrNull?.clickCount) {
                                    1 -> {
                                        onClick()
                                    }
                                    2 -> {
                                        onDoubleClick()
                                    }
                                }
                                MouseEvent.BUTTON2 -> {
                                }
                                MouseEvent.BUTTON3 -> {
                                    onClick()
                                    if (dropMenu != null) expandedDropMenuRightButton.value = true
                                }
                            }
                        }
//                    .shadow(
//                        elevation,
//                        shapeCardShadow,
//                        true
//                    )
                        .clip(shapeCard)
                        .background(backBrush ?: background, shapeCard)
                        .run {
                            if (active)
                                this.border(
                                    BorderStroke(borderWidthActive, borderActive),
                                    shapeCard
                                )
                            else this
                        }
                        .then(modifierThen)
                        .run {
                            if (multiBorder != null) this.then(multiBorder)
                            else this.border(
                                width = widthBorder ?: borderWidth,
                                brush = borderBrush ?: border,
                                shape = shapeCard
                            )
                        }
                        .then(paddingInner.animateContentSize())

                    ,
                )
                {
                    content()
                    if (expandedDropMenuRightButton.value) Box(
                        Modifier.matchParentSize()// fillMaxWidth()
                            .padding(start = if (xBox >= 0.dp) xBox else 0.dp, top = if (yBox >= 0.dp) yBox else 0.dp)
                    ) {
                        Box(Modifier.height(0.dp).width(0.dp)) {
                            MyDropdownMenu(expandedDropMenuRightButton, style = dropdown) { //setDissFun ->
                                dropMenu?.let {
                                    dropMenu(expandedDropMenuRightButton)
                                }
                            }

                        }

                    }
                }
            }
        }
    }
}