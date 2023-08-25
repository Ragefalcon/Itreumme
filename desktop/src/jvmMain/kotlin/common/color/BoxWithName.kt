package common.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import common.MeasureUnconstrainedViewWidth
import common.MyShadowBox
import extensions.SimplePlateWithShadowStyleState

@Composable
fun BoxWithName(
    label: String,
    stylePlate: SimplePlateWithShadowStyleState,
    startPadding: Dp,
    textStyleOn: TextStyle,
    textStyleOff: TextStyle? = null,
    modifierOut: Modifier = Modifier,
    modifierInner: Modifier = Modifier,
    focusable: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSourceHover: MutableInteractionSource = remember { MutableInteractionSource() }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val focus by interactionSource.collectIsFocusedAsState()
    val hover by interactionSourceHover.collectIsHoveredAsState()
    with(LocalDensity.current) {
        (if (focus || hover) textStyleOn else textStyleOff ?: textStyleOn).let { textStyle ->
            MeasureUnconstrainedViewWidth(
                {
                    Text(
                        label,
                        style = textStyle
                    )
                }
            ) { widthText ->
                MyShadowBox(stylePlate.shadow) {
                    Box(modifierOut.hoverable(interactionSourceHover)
                        .run {
                            if (focusable) this.focusable(true, interactionSource) else this
                        }
                    ) {
                        Column(
                            Modifier
                                .padding(top = (textStyle.fontSize / 2).toDp())
                                .background(
                                    stylePlate.BACKGROUND,
                                    stylePlate.shape
                                )
                                .fillMaxWidth()
                                .then(modifierInner)
                        ) {
                            content()
                        }
                        Box(
                            Modifier
                                .matchParentSize()
                                .padding(top = (textStyle.fontSize / 2).toDp())
                                .fillMaxSize()
                                .drawWithCache {
                                    onDrawWithContent {
                                        clipRect(
                                            (startPadding - 5.dp).toPx(),
                                            0f,
                                            (startPadding + 5.dp + widthText).toPx(),
                                            textStyle.fontSize.toPx(),
                                            ClipOp.Difference
                                        ) {
                                            this@onDrawWithContent.drawContent()
                                        }
                                    }
                                }
                                .border(stylePlate.BORDER_WIDTH, stylePlate.BORDER, stylePlate.shape)
                        )
                        Text(
                            label,
                            Modifier.padding(start = startPadding),
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}
