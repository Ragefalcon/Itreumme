package tests

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GoodSampleDraggable() {
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(300f) }
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color(0xFF34AB52),
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight()
                .offset {
                    IntOffset(0, offsetY.value.roundToInt())
                }
                .draggable(
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            offsetY.snapTo(offsetY.value + delta)
                        }
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        coroutineScope.launch {
                            offsetY.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 3000,
                                    delayMillis = 0
                                )
                            )
                        }
                    }
                )
        ) {
        }
    }

}

