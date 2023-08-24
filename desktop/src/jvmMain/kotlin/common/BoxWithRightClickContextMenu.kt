package common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MouseClickScope
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import extensions.mouseDoubleClick

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun BoxWithRightClickContextMenu(
    onClick: () -> Unit = {}, //
    onDoubleClick: () -> Unit = {}, //MouseClickScope.
    modifier: Modifier = Modifier,
    dropMenu: @Composable ColumnScope.( MutableState<Boolean>) -> Unit = { },
    content: @Composable () -> Unit
) {
    var xBox by remember { mutableStateOf(0.dp) }
    var yBox by remember { mutableStateOf(0.dp) }
    val expandedDropMenuRightButton = remember {mutableStateOf(false) }
    Box(
        modifier = Modifier
            .pointerMoveFilter(
                onMove = {
                    if (!expandedDropMenuRightButton.value) {
                        xBox = it.x.dp - 8.dp
                        yBox = it.y.dp - 1.dp
                    }
                    false
                }
            )
            .then(modifier)
//                .animateContentSize()
            .mouseDoubleClick( onClick = {
//                expandedDropMenuRightButton.value = this.buttons.isSecondaryPressed
                onClick()
            }, onDoubleClick = {onDoubleClick()},
                rightClick = {
                    expandedDropMenuRightButton.value = true
                })
    )
    {
        content()
        if (expandedDropMenuRightButton.value) Box(
            Modifier.fillMaxWidth()
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