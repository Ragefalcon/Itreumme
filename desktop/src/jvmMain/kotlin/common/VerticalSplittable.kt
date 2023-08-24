package common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun VerticalSplittable(
    modifier: Modifier,
    splitterState: SplitterState,
    onResize: (delta: Dp) -> Unit,
    color: Color = Color.Green,
    splitStyle: @Composable BoxScope.() -> Unit = {
        Box(
            Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(color)
        )
    },
    children: @Composable () -> Unit
) = Layout({
    children()
    VerticalSplitter(splitterState, onResize, color, splitStyle)
}, modifier, measurePolicy = { measurables, constraints ->
    require(measurables.size == 3)

    val firstPlaceable = measurables[0].measure(constraints.copy(minWidth = 0))
    val secondWidth = constraints.maxWidth - firstPlaceable.width
    val secondPlaceable = measurables[1].measure(
        Constraints(
            minWidth = secondWidth,
            maxWidth = secondWidth,
            minHeight = constraints.maxHeight,
            maxHeight = constraints.maxHeight
        )
    )
    val splitterPlaceable = measurables[2].measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) {
        firstPlaceable.place(0, 0)
        secondPlaceable.place(firstPlaceable.width, 0)
        splitterPlaceable.place(firstPlaceable.width, 0)
    }
})

class SplitterState {
    var isResizing by mutableStateOf(false)
    var isResizeEnabled by mutableStateOf(true)
}

@Composable
fun
        VerticalSplitter(
    splitterState: SplitterState,
    onResize: (delta: Dp) -> Unit,
    color: Color = Color.Green,
    splitStyle: @Composable BoxScope.() -> Unit = {
        Box(
            Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(color)
        )
    }
) = Box {
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .width(8.dp)
            .fillMaxHeight()
            .cursorForHorizontalResize()
            .run {
                if (splitterState.isResizeEnabled) {
                    this.draggable(
                        state = rememberDraggableState {
                            with(density) {
                                onResize(it.toDp())
                            }
                        },
                        orientation = Orientation.Horizontal,
                        startDragImmediately = false,
                        onDragStarted = { splitterState.isResizing = true },
                        onDragStopped = { splitterState.isResizing = false },
//                        reverseDirection = true
                    )
//                        .cursorForHorizontalResize()
                    /**
                     * Почему-то если располагать изменение курсора(cursorForHorizontalResize()) здесь,
                     * то когда курсор покидает область этого объекта перетаскивание заканчивается, как будто пользователь
                     * отпускает кнопку мыши.
                     * */
                } else {
                    this
                }
            }
    )
    Box(
        modifier = Modifier
            .width(8.dp).fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        splitStyle()
    }
//    Box(
//        Modifier
//            .width(1.dp)
//            .fillMaxHeight()
//            .background(color)
//    )
}

@Composable
fun MyVerticalSplitter(
    color: Color = Color.Green,
    offsetX: Animatable<Float, AnimationVector1D>
//    offsetX: MutableState<Dp>,
//    onResize: (delta: Dp) -> Unit
) =
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val coroutineScope = rememberCoroutineScope()
        val offsetY = remember { Animatable(0f) }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Surface(
                color = color,
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .offset {
                        IntOffset(offsetY.value.roundToInt(), 0)
                    }
                    .draggable(
                        state = rememberDraggableState {
//                    with(density) {
                            coroutineScope.launch {
                                offsetY.snapTo(offsetY.value + it)
                            }
//                        onResize(it.toDp())
//                    }
                        },
                        orientation = Orientation.Horizontal,
//                startDragImmediately = true,
//                        onDragStarted = { splitterState.isResizing = true },
//                        onDragStopped = { splitterState.isResizing = false },
                    )
//                    .cursorForHorizontalResize()
//                    .background(color),
            ) {}
        }
//    Box(
//        Modifier
//            .width(1.dp)
//            .fillMaxHeight()
//            .offset {
//                IntOffset(offsetX.value.roundToInt(), 0)
//            }
//            .background(color)
//    )
    }


/*****************************/

class PanelState {
    val collapsedSize = 24.dp
    var expandedSize by mutableStateOf(300.dp)
    val expandedSizeMin = 90.dp
    var isExpanded by mutableStateOf(true)
    val splitter = SplitterState()
}

fun Modifier.withoutWidthConstraints() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
    layout(constraints.maxWidth, placeable.height) {
        placeable.place(0, 0)
    }
}


@Composable
fun ResizablePanel(
    modifier: Modifier,
    state: PanelState,
    expandable: Boolean = false,
    content: @Composable () -> Unit,
) {
    val alpha by animateFloatAsState(if (state.isExpanded) 1f else 0f, SpringSpec(stiffness = Spring.StiffnessLow))

    Box(modifier) {
        Box(Modifier.fillMaxSize().graphicsLayer(alpha = alpha)) {
            content()
        }

        if (expandable) {
            Icon(
                if (state.isExpanded) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
                contentDescription = if (state.isExpanded) "Collapse" else "Expand",
                tint = LocalContentColor.current,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(24.dp)
                    .clickable {
                        state.isExpanded = !state.isExpanded
                    }
                    .padding(4.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}
