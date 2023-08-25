package MyDialog

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.BackCanvasWithHole
import extensions.MyRectF

class MyDialogLayout() {
    var dial: @Composable (() -> Unit)? = null

    private var keyDial = mutableStateOf(false)
    private var keyVisDial = mutableStateOf(false)
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

    @Composable
    fun getLay() {
        val alpha: Float by animateFloatAsState(
            targetValue = if (keyVisDial.value) 1f else 0.0f,
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
                    dial?.invoke()
                }
            }
        }
    }
}