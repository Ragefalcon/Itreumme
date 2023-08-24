package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.DropDownMenuStyleState
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import viewmodel.MainDB

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyDropdownMenuItem(
    expanded: MutableState<Boolean>,
    style: DropDownMenuStyleState,
    name: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    with(style) {
        Box(Modifier
            .run { if (isHovered) this.background(BACKGROUND_HOVERED) else this }
            .mouseClickable {
                onClick()
                expanded.value = false
            }
            .hoverable(interactionSource = interactionSource)
        ) {
            (textStyle.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
                Text(
                    name,
                    modifier = Modifier.fillMaxWidth().padding(vertical = verticalPadding)
                        .offset(
                            offset.x.dp - (if (isHovered) offset.x * 2 else offset.x).dp,
                            offset.y.dp - (if (isHovered) offset.y * 2 else offset.y).dp
                        ),
                    style = textStyle.copy(
                        textAlign = TextAlign.Center,
                        shadow = Shadow(
                            offset = if (isHovered) Offset(offset.x * 2, offset.y * 2) else offset,
                            blurRadius = if (isHovered) textStyle.shadow?.blurRadius?.let { it * 2 }
                                ?: 4f else textStyle.shadow?.blurRadius ?: 2f
                        )
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyDropdownMenuCommonItem(
    expanded: MutableState<Boolean>,
    style: DropDownMenuStyleState,
    content: @Composable (Boolean)->Unit,
    autoClick: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    with(style) {
        Box(Modifier
            .run { if (isHovered) this.background(BACKGROUND_HOVERED) else this }
            .run {
                if (autoClick) this.mouseClickable {
                    onClick()
                    expanded.value = false
                }   else this
            }
            .hoverable(interactionSource = interactionSource)
        ) {
            content(isHovered)
        }
    }
}

@Composable
fun MyDropdownMenu(
    expanded: MutableState<Boolean>,
    style: DropDownMenuStyleState = DropDownMenuStyleState(MainDB.styleParam.commonParam.commonDropdownMenuStyle),
    list: List<String>,
    content: @Composable ColumnScope.() -> Unit = {},
    width: Dp? = null,
    funSelect: (Int) -> Unit
) {
    MyDropdownMenu(expanded, style,width) {
        list.forEachIndexed { index, itemStr ->
            MyDropdownMenuItem(expanded, style, itemStr) {
                funSelect(index)
            }
        }
        content()
    }
}

@Composable
fun MyDropdownMenu(
    expanded: MutableState<Boolean>,
    style: DropDownMenuStyleState,
    width: Dp? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    with(style) {
        MaterialTheme(
            colors = lightColors().copy(surface = Color(0x000000FF)),
/*
                                Colors (
                            primary = Color(0xFF0000FF),//99851F),
                            primaryVariant = Color(0xFF0000FF),
                            secondary = Color.Transparent,//Color(0xFF9eba85),
                            secondaryVariant = Color(0xFF00FF00),
                            background = Color.Transparent,//Color(0xFF464D45),
                            surface = Color(0x2F0000FF), //Color.Transparent,//
                            error = Color(0xFFFF0000),
                            onPrimary = Color(0xFFFF0000),
                            onSecondary = Color(0xFFFF0000),
                            onBackground = Color(0xFF0000FF),
                            onSurface = Color(0xFF00FF00),
                            onError = Color(0xFFFFFF00),
                            isLight = true
                        ),
*/
            shapes = Shapes(shape_for_shadow, shape_for_shadow, shape_for_shadow)
        ) {
//                        with(LocalElevationOverlay.current?.apply (Color.White, elevation = 0.dp)){
//            CompositionLocalProvider(LocalAbsoluteElevation provides 0.dp) {
//                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val state = rememberScrollState(0)
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .background(BACKGROUND, shape)
                    .heightIn(0.dp, 400.dp)
                    .border(
                        BORDER_WIDTH,
                        BORDER,
                        shape
                    )
                    .wrapContentHeight()
            ) {
                Row(inner_padding.heightIn(0.dp, 350.dp)) {
                    Column(
                        Modifier
                            .run {
                                width?.let { this.width(it) } ?: this
                            }
                            .verticalScroll(state),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        content()
                    }
                    if (state.maxValue != Int.MAX_VALUE && state.maxValue > 0) VerticalScrollbar( // && state.maxValue.dp >= 350.dp
                        rememberScrollbarAdapter(state),
                        Modifier.width(8.dp).height(350.dp)
                            .clickable(false) {},
                        style = ScrollbarStyle(
                            minimalHeight = 16.dp,
                            thickness = 8.dp,
                            shape = RoundedCornerShape(4.dp),
                            hoverDurationMillis = 0,
                            unhoverColor = Color.White.copy(alpha = 0.22f),
                            hoverColor = Color.White.copy(alpha = 0.42f)
                        )
                    )
                }
            }
        }
    }
}