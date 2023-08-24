package common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
//import androidx.compose.ui.input.pointer.pointerIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter
import java.awt.Cursor

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorCommon(type: Int): Modifier =  composed {
    var isHover by remember { mutableStateOf(false) }

    pointerMoveFilter(
        onEnter = { isHover = true; true },
        onExit = { isHover = false; true }
    ).pointerHoverIcon(//) . pointerIcon(
        PointerIcon(
            if (isHover) {
                Cursor(type)
            } else {
                Cursor.getDefaultCursor()
            }
        )
    )
}
//@OptIn(ExperimentalComposeUiApi::class)
//fun Modifier.cursorForHorizontalResize() = Modifier.cursorCommon(Cursor.E_RESIZE_CURSOR)

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorForHorizontalResize(): Modifier = composed {
    cursorCommon(Cursor.E_RESIZE_CURSOR)
}
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorForVerticalResize(): Modifier = composed {
    cursorCommon(Cursor.S_RESIZE_CURSOR)
}
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorForTopLeftResize(): Modifier = composed {
    cursorCommon(Cursor.NW_RESIZE_CURSOR)
}
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorForTopRightResize(): Modifier = composed {
    cursorCommon(Cursor.NE_RESIZE_CURSOR)
}
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorForMove(): Modifier = composed {
    cursorCommon(Cursor.MOVE_CURSOR)
}
