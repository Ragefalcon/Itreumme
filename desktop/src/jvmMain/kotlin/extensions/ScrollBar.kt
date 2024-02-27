package extensions

import androidx.compose.foundation.*
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun MyVertScrollbar1(
    scrollbarAdapter: ScrollbarAdapter,
    dark: Boolean = false,
    color: Color = if (dark) Color.Black else Color.White,
    modifier: Modifier = Modifier,
    reverse: Boolean = false,
) {
    VerticalScrollbar(
        scrollbarAdapter,
        modifier.width(8.dp),
        style = ScrollbarStyle(
            minimalHeight = 16.dp,
            thickness = 8.dp,
            shape = RoundedCornerShape(4.dp),
            hoverDurationMillis = 0,
            unhoverColor = color.copy(alpha = 0.22f),
            hoverColor = color.copy(alpha = 0.42f)
        ),
        reverseLayout = reverse
    )
}

@Composable
fun MyHorizScrollbar1(
    scrollbarAdapter: ScrollbarAdapter,
    dark: Boolean = false,
    reverse: Boolean = false,
    color: Color = if (dark) Color.Black else Color.White,
    modifier: Modifier = Modifier,
) {
    HorizontalScrollbar(
        scrollbarAdapter,
        modifier.height(8.dp),
        style = ScrollbarStyle(
            minimalHeight = 16.dp,
            thickness = 8.dp,
            shape = RoundedCornerShape(4.dp),
            hoverDurationMillis = 0,
            unhoverColor = color.copy(alpha = 0.22f),
            hoverColor = color.copy(alpha = 0.42f)
        ),
        reverseLayout = reverse
    )
}

@Composable
fun BoxWithVScrollBar(
    modifier: Modifier = Modifier,
    scroll: ScrollState = rememberScrollState(0),
    dark: Boolean = false,
    color: Color = if (dark) Color.Black else Color.White,
    maxHeight: Int = 0,
    content: @Composable BoxScope.(ScrollState) -> Unit
) {
    Row(if (maxHeight == 0) modifier else modifier.heightIn(0.dp, maxHeight.dp)) {
        Box(
            Modifier.weight(1f, false)
        ) {
            content(scroll)
        }
        if (scroll.maxValue != Int.MAX_VALUE && scroll.maxValue > 0) MyVertScrollbar1(
            OldScrollbarAdapter(scroll),
            dark,
            color
        )
    }
}

@Composable
fun BoxWithHScrollBar(
    modifier: Modifier = Modifier,
    scroll: ScrollState = rememberScrollState(0),
    dark: Boolean = false,
    reverse: Boolean = false,
    color: Color = if (dark) Color.Black else Color.White,

    content: @Composable BoxScope.(ScrollState) -> Unit
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        BoxWithConstraints(
            Modifier.weight(1f, false)
        ) {
            content(scroll)
        }
        MyHorizScrollbar1(OldScrollbarAdapter(scroll), dark, reverse, color)
    }
}

@Composable
fun BoxWithHScrollBar(
    modifier: Modifier = Modifier,
    scroll: LazyListState = rememberLazyListState(0),
    dark: Boolean = false,
    reverse: Boolean = false,
    maxWidth: Int = 0,
    color: Color = if (dark) Color.Black else Color.White,
    content: @Composable BoxScope.(LazyListState) -> Unit
) {

    val hashItemHeight = remember { mutableMapOf<Int, Pair<Int, Int>>() }
    val complexAdapter = remember(scroll) { ComplexScrollbarAdapter(scroll, hashItemHeight) }
    val widthScroll = remember { mutableStateOf(10.dp) }

    Column(if (maxWidth == 0) modifier else modifier.widthIn(0.dp, maxWidth.dp)) {
        with(LocalDensity.current) {
            BoxWithConstraints(
                Modifier.weight(1f, false).onGloballyPositioned {
                    widthScroll.value = it.size.width.toDp()
                }
            ) {
                content(scroll)
            }
        }
        Box(Modifier.width(widthScroll.value)) {
            MyHorizScrollbar1(complexAdapter, dark, reverse, color)
        }
    }
}


class ComplexScrollbarAdapter(
    private val listState: LazyListState,
    private val hashItemHeight: MutableMap<Int, Pair<Int, Int>>
) : ScrollbarAdapter {

    var heightAll = 1f
    private val averageItemSize by derivedStateOf {

        if (listState.layoutInfo.totalItemsCount != 0) {
            if (hashItemHeight.isEmpty() || hashItemHeight.size != listState.layoutInfo.totalItemsCount) {
                hashItemHeight.clear()
                val visibleAverageSize =
                    listState.layoutInfo.visibleItemsInfo.sumOf { it.size } / listState.layoutInfo.visibleItemsInfo.size
                for (i in 0 until listState.layoutInfo.totalItemsCount) {
                    hashItemHeight.put(i, visibleAverageSize to visibleAverageSize * i)
                }
                heightAll = visibleAverageSize * listState.layoutInfo.totalItemsCount.toFloat()
            }

            listState.layoutInfo.visibleItemsInfo.forEach { item ->
                hashItemHeight[item.index]?.let { visItem ->
                    if (visItem.first != item.size) {
                        heightAll += item.size - visItem.first

                        hashItemHeight.set(item.index, item.size to visItem.second)
                        hashItemHeight.forEach {
                            if (it.key > item.index)

                                hashItemHeight.set(
                                    it.key,
                                    it.value.first to it.value.second + item.size - visItem.first
                                )
                        }
                    }
                }
            }
            heightAll / listState.layoutInfo.totalItemsCount
        } else 0f
    }

    override val scrollOffset: Float
        get() {
            val firstVisibleItem = listState.layoutInfo.visibleItemsInfo.firstOrNull() ?: return 0.0f
            val rez = (hashItemHeight[firstVisibleItem.index]?.second?.toFloat() ?: 0f) - firstVisibleItem.offset
            return rez
        }

    override suspend fun scrollTo(containerSize: Int, scrollOffset: Float) {
        val scrollOffsetTmp = scrollOffset - this.scrollOffset
        listState.scrollBy(scrollOffsetTmp)
    }

    override fun maxScrollOffset(containerSize: Int): Float {
        val approximateContainerSize = averageItemSize * listState.layoutInfo.totalItemsCount
        return approximateContainerSize - containerSize
    }
}

@Composable
fun BoxWithVScrollBarLazyList(
    modifier: Modifier = Modifier,
    scroll: LazyListState = rememberLazyListState(0),
    dark: Boolean = false,
    color: Color = if (dark) Color.Black else Color.White,
    maxHeight: Int = 0,
    reverse: Boolean = false,
    content: @Composable BoxScope.(LazyListState) -> Unit
) {
    val hashItemHeight = remember { mutableMapOf<Int, Pair<Int, Int>>() }
    val complexAdapter = remember(scroll) { ComplexScrollbarAdapter(scroll, hashItemHeight) }
    val heightScroll = remember { mutableStateOf(10.dp) }

    Row(if (maxHeight == 0) modifier else modifier.heightIn(0.dp, maxHeight.dp)) {
        with(LocalDensity.current) {
            BoxWithConstraints(
                Modifier.weight(1f, false).onGloballyPositioned {
                    heightScroll.value = it.size.height.toDp()
                },
            ) {
                content(scroll)
            }
        }
        Box(Modifier.height(heightScroll.value)) {
            MyVertScrollbar1(
                complexAdapter,
                dark,
                color,
                reverse = reverse
            )
        }
    }
}
