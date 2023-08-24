package extensions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import common.MyDropdownMenuStyle1

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun BoxWithMyRightClick (
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}, //MouseClickScope.
    onDoubleClick: () -> Unit = {}, //MouseClickScope.
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
//                .then(modifier)

//            .mouseClickable {
//                expandedDropMenuRightButton.value = this.buttons.isSecondaryPressed
//            }
//            .combinedClickable(object : MutableInteractionSource {
//                // TODO: consider replay for new indication instances during events?
//                override val interactions = MutableSharedFlow<Interaction>(
//                    extraBufferCapacity = 16,
//                    onBufferOverflow = BufferOverflow.DROP_LATEST,
//                )
//
//                override suspend fun emit(interaction: Interaction) {
//                    interactions.emit(interaction)
//                }
//
//                override fun tryEmit(interaction: Interaction): Boolean {
//                    return interactions.tryEmit(interaction)
//                }
//            }, null, onDoubleClick = { onDoubleClick() }) {
//                onClick()
//            }
//                .animateContentSize()
                .mouseDoubleClick(onClick = {
//                    expandedDropMenuRightButton.value = this.buttons.isSecondaryPressed
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
                    MyDropdownMenuStyle1(expandedDropMenuRightButton) { setDissFun ->
                        dropMenu(expandedDropMenuRightButton)
                    }

                }

            }
        }
    }
}