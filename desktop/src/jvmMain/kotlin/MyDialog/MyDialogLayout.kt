package MyDialog

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import common.BackCanvasWithHole
import extensions.MyRectF

class MyDialogLayout() {
    var dial: @Composable (() -> Unit)? = null

    private var keyDial = mutableStateOf(false)
    private var keyVisDial = mutableStateOf(false)
//    private var currentState by remember { mutableStateOf(false) }
//    private val transition = updateTransition(currentState)
    private var holePriv: MutableState<MyRectF?> = mutableStateOf(null)

    var layHeight = mutableStateOf(0.dp)
        private set
    var layWidth = mutableStateOf(0.dp)
        private set

    fun show(hole: MyRectF? = null) {
        holePriv.value = hole
        keyDial.value = true
        keyVisDial.value = true
    }

    fun close() {
        keyVisDial.value = false
        holePriv.value = null
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun getLay() {
        val alpha: Float by animateFloatAsState(
            targetValue = if (keyVisDial.value) 1f else 0.0f,
            // Configure the animation duration and easing.
            animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
        ) { if (!keyVisDial.value) keyDial.value = false }

        BoxWithConstraints(Modifier.fillMaxSize().alpha(alpha), Alignment.Center) {
            layHeight.value = maxHeight
            layWidth.value = maxWidth
            if (keyDial.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (holePriv.value != null) Color.Transparent else Color(0x88000000)
                ) {
                    holePriv.value?.let {
                        BackCanvasWithHole(it)
                    }
/*
                    Dialog(
                        onCloseRequest = { keyDial.value = false },
                        state = DialogState(
                            WindowPosition(Alignment.Center),
                            width = Dp.Unspecified,
                            height = Dp.Unspecified
                        ),
                        onPreviewKeyEvent = {
                            if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                                keyDial.value = false
                                true
                            } else {
                                false
                            }
                        },
                        resizable = false, undecorated = true
                    ) {
                        dial?.invoke()
                    }
*/
                    dial?.invoke()
                }
            }
        }
    }
}