package extensions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import common.MyDropdownMenuStyle1

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxWithMyRightClick(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    content: @Composable () -> Unit
) {
    with(LocalDensity.current) {
        var xBox by remember { mutableStateOf(0.dp) }
        var yBox by remember { mutableStateOf(0.dp) }
        val expandedDropMenuRightButton = remember { mutableStateOf(false) }
        Box(
            modifier = modifier
                .pointerMoveFilter(
                    onMove = {
                        if (!expandedDropMenuRightButton.value) {
                            xBox = it.x.toDp() - 8.dp
                            yBox = it.y.toDp() - 1.dp
                        }
                        false
                    }
                )
                .mouseDoubleClick(onClick = {
                    onClick()
                }, onDoubleClick = { onDoubleClick() },
                    rightClick = {
                        expandedDropMenuRightButton.value = true
                    }),
        )
        {
            content()
            if (expandedDropMenuRightButton.value) Box(
                Modifier.fillMaxWidth()
                    .padding(start = if (xBox >= 0.dp) xBox else 0.dp, top = if (yBox >= 0.dp) yBox else 0.dp)
            ) {
                Box(Modifier.height(0.dp).width(0.dp)) {
                    MyDropdownMenuStyle1(expandedDropMenuRightButton) { _ ->
                        dropMenu(expandedDropMenuRightButton)
                    }

                }

            }
        }
    }
}