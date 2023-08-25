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

import androidx.compose.ui.input.pointer.pointerMoveFilter
import java.awt.Cursor

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.cursorCommon(type: Int): Modifier = composed {
    var isHover by remember { mutableStateOf(false) }

    pointerMoveFilter(
        onEnter = { isHover = true; true },
        onExit = { isHover = false; true }
    ).pointerHoverIcon(
        PointerIcon(
            if (isHover) {
                Cursor(type)
            } else {
                Cursor.getDefaultCursor()
            }
        )
    )
}

fun Modifier.cursorForHorizontalResize(): Modifier = composed {
    cursorCommon(Cursor.E_RESIZE_CURSOR)
}

fun Modifier.cursorForVerticalResize(): Modifier = composed {
    cursorCommon(Cursor.S_RESIZE_CURSOR)
}

fun Modifier.cursorForTopLeftResize(): Modifier = composed {

    cursorCommon(Cursor.NW_RESIZE_CURSOR)
}

fun Modifier.cursorForTopRightResize(): Modifier = composed {
    cursorCommon(Cursor.NE_RESIZE_CURSOR)
}

fun Modifier.cursorForMove(): Modifier = composed {
    cursorCommon(Cursor.MOVE_CURSOR)
}
