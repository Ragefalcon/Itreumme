package common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.mouseDoubleClick
import androidx.compose.runtime.*
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import extensions.dashedBorder
import java.awt.event.MouseEvent

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MyCardStyle2(
    active: Boolean,
    level: Long = 0,
    onClick: () -> Unit = {}, //MouseClickScope.
    onDoubleClick: () -> Unit = {}, //MouseClickScope.
//    backColor: Color = Color(0xFF464D45),
    radius: Dp = 15.dp,
    modifier: Modifier = Modifier,
    widthBorder: Dp = 0.5.dp,
//    horizontal: Boolean = false,
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    content: @Composable () -> Unit
) {
    with(LocalDensity.current) {
        var xBox by remember { mutableStateOf(0.dp) }
        var yBox by remember { mutableStateOf(0.dp) }
        val expandedDropMenuRightButton = remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .pointerMoveFilter(
                    onMove = {
                        if (!expandedDropMenuRightButton.value) {
                            xBox = it.x.toDp() - 8.dp
                            yBox = it.y.toDp() - 1.dp
                        }
                        false
                    }
                )
                .padding(start = (level * 40).toInt().dp)
                .padding(horizontal = 6.dp, vertical = 6.dp) //.fillMaxWidth()
                .dashedBorder(
                    width = widthBorder,
                    brush = if (active) Brush.horizontalGradient(
                        listOf(
                            Color.Red.copy(0.7f),
                            Color.Red.copy(0.7f)
                        )
                    ) else Brush.horizontalGradient(
                        listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
                    ),
                    shape = RoundedCornerShape(radius),
                    on = 20.dp,
                    off = 10.dp
                )
                .then(modifier)
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
                        } //onRightClick()
//                            when {
//                            it.buttons.isPrimaryPressed -> when (it.awtEvent.clickCount) {
//                                1 -> onSingleLeftClick()
//                                2 -> onDoubleLeftClick()
//                            }
//                            it.buttons.isSecondaryPressed -> onRightClick()
                    }
                }
/*
                .mouseDoubleClick(onClick = {
                    expandedDropMenuRightButton.value = this.buttons.isSecondaryPressed
                    onClick()
                }, onDoubleClick = { onDoubleClick() })
*/
            ,
//            backgroundColor = backColor,
        )
        {
            content()
            if (expandedDropMenuRightButton.value) Box(
                Modifier.matchParentSize()//.fillMaxWidth()
                    .padding(start = if (xBox >= 0.dp) xBox else 0.dp, top = if (yBox >= 0.dp) yBox else 0.dp)
            ) {
                Box(Modifier.height(0.dp).width(0.dp)) {
                    MyDropdownMenuStyle1(expandedDropMenuRightButton) { setDissFun ->
                        dropMenu(expandedDropMenuRightButton)
                    }

                }

            }
        }
    }
}