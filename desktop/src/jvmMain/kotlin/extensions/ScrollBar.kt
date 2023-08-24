package extensions

import androidx.compose.foundation.*
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
import java.util.*


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
            shape = RoundedCornerShape(4.dp),//RectangleShape,
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
            shape = RoundedCornerShape(4.dp),//RectangleShape,
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
            rememberScrollbarAdapter(scroll),
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
//    maxWidth: Int = 0,
    content: @Composable BoxScope.(ScrollState) -> Unit
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        BoxWithConstraints(
            Modifier.weight(1f, false)
        ) {
            content(scroll)
        }
        MyHorizScrollbar1(rememberScrollbarAdapter(scroll), dark, reverse, color)
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
//    val hashItemHeight = remember { mutableListOf<Pair<Int, Int>>() }
    val hashItemHeight = remember { mutableMapOf<Int, Pair<Int,Int>>() }
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


/**
 * Этот кусок (ComplexState) взят отсюда:
 * https://github.com/JetBrains/compose-jb/issues/181
 * https://github.com/Dominaezzz/FunWithScrollbars/blob/010feec56b8eb7dcfdf7a4c1a0d4258beefa6fe9/src/main/kotlin/Scrollbars.kt#L204
 *
 * он все еще не работает с моими скисками с сильно отличающимися по высоте item-ами.
 * но зато тут можно было подсмотреть механику того как можно было реализовать свой ScrollbarAdapter
 * */
private class ComplexState(
    private val listState: LazyListState
) {
    val cachedRanges = mutableListOf<Pair<IntRange, Int>>()
    private var previousVisibleRange: IntRange? = null
    private var previousVisibleSize: Int? = null

    private val LazyListState.visibleRange: IntRange
        get() {
            val items = layoutInfo.visibleItemsInfo
            return if (items.isEmpty()) {
                IntRange.EMPTY
            } else {
                items.run { (firstOrNull()?.index ?: 0)..(lastOrNull()?.index ?: 0) }
            }
        }

    fun update() {

        println("-------------items: ${listState.layoutInfo.totalItemsCount}-----------")
        listState.layoutInfo.visibleItemsInfo.forEach {
            println("size: ${it.size} -- index: ${it.index}")
        }
        println("-----------------------------")
        val visibleRange = listState.visibleRange
        val visibleSize = listState.layoutInfo.visibleItemsInfo.sumOf { it.size }

        if (previousVisibleRange != visibleRange) {
            cutOutVisibleRange()

            val prevRange = previousVisibleRange
            val prevSize = previousVisibleSize
            if (prevRange != null && prevSize != null) {
                // Add previously visible range
                val newPosition = cachedRanges.binarySearchBy(visibleRange.first) { (range, _) -> range.first }
                check(newPosition < 0) { "Visible range should not be cached. $newPosition, $visibleRange, $cachedRanges" }
                cachedRanges.add(-(newPosition + 1), prevRange to prevSize)
            }
            previousVisibleRange = visibleRange

            cutOutVisibleRange()

            performSimpleMergeAlgorithm()
        }
        previousVisibleSize = visibleSize
    }

    private fun cutOutVisibleRange() {
        val visibleRange = listState.visibleRange

        var index = 0
        while (index < cachedRanges.size) {
            val (range, _) = cachedRanges[index]
            // Is range before the current visible range?
            if (range.last < visibleRange.first) {
                index++
                continue
            }
            break
        }

        while (index < cachedRanges.size) {
            val (range, size) = cachedRanges[index]
            // Is range after the current visible range?
            if (visibleRange.last < range.first) {
                break
            }

            val sizeToTrim = listState.layoutInfo.visibleItemsInfo
                .asSequence()
                .filter { it.index in range }
                .sumOf { it.size }

            cachedRanges.removeAt(index)
            val remainingSize = size - sizeToTrim
            if (remainingSize <= 0) {
                continue
            }

            val newTopRange = range.first until visibleRange.first
            val newBottomRange = (visibleRange.last + 1)..range.last

            if (!newTopRange.isEmpty()) {
                val newSize = (remainingSize * newTopRange.count()) / (newTopRange.count() + newBottomRange.count())
                cachedRanges.add(index++, newTopRange to newSize)
            }
            if (!newBottomRange.isEmpty()) {
                val newSize = (remainingSize * newBottomRange.count()) / (newTopRange.count() + newBottomRange.count())
                cachedRanges.add(index++, newBottomRange to newSize)
            }
        }
    }

    // TODO: This should be extracted out into an interface.
    private fun performSimpleMergeAlgorithm() {
        var index = 0
        while (index < cachedRanges.lastIndex) {
            val (range, size) = cachedRanges[index]
            val (nextRange, nextSize) = cachedRanges[index + 1]
            if (range.last + 1 == nextRange.first) {
                cachedRanges.removeAt(index)
                val newRange = range.first..nextRange.last
                val newSize = size + nextSize
                cachedRanges[index] = newRange to newSize
            } else {
                index++
            }
        }
    }
}


/**
 * Вроде этот вариант у меня работает, не уверен, что он будет отрабатывать все ситуации, нужно понаблюдать за ним...
 * */
class ComplexScrollbarAdapter(
    private val listState: LazyListState,
    private val hashItemHeight: MutableMap<Int, Pair<Int,Int>>
) : ScrollbarAdapter {
    //    private val complexState = ComplexState(listState)
    var heightAll = 1f
//    var countAvar = 0
//    var countScroll = 0

    private val averageItemSize by derivedStateOf {
//            println("averageItemSize: Float get($this) {")
//            println("averageItemSize: Float scroll($listState) {")
//            println("averageItemSize: Float scroll(${hashItemHeight}) {")
            if (listState.layoutInfo.totalItemsCount != 0) {
                if (hashItemHeight.isEmpty() || hashItemHeight.size != listState.layoutInfo.totalItemsCount) {
//                    println("hashItemHeight.isEmpty()")
                    hashItemHeight.clear()
                    val visibleAverageSize =
                        listState.layoutInfo.visibleItemsInfo.sumOf { it.size } / listState.layoutInfo.visibleItemsInfo.size
                    for (i in 0 until listState.layoutInfo.totalItemsCount) {
                        hashItemHeight.put(i, visibleAverageSize to visibleAverageSize * i)
                    }
                    heightAll = visibleAverageSize * listState.layoutInfo.totalItemsCount.toFloat()
                }
//                val timeD1 = Date().time
                listState.layoutInfo.visibleItemsInfo.forEach { item ->
//                    println("item")
//                    println(item.index)
//                    println(item.key)
                    hashItemHeight[item.index]?.let { visItem ->
                        if (visItem.first != item.size) {
//                            println()
//                            println(it.second)
//                            println(item.size)
                                heightAll += item.size - visItem.first
//                            hashItemHeight.remove(item.index)
                            hashItemHeight.set(item.index, item.size to visItem.second)
                            hashItemHeight.forEach {
                                if (it.key >  item.index)
//                                hashItemHeight.remove(it.key)
                                hashItemHeight.set(it.key, it.value.first to it.value.second + item.size - visItem.first)
                            }
                        }
                    }
                }
//                println("timeD = ${Date().time - timeD1}")
//            complexState.update()
//            val totalSizeOfCachedItems = complexState.cachedRanges.sumOf { (_, size) -> size }
//            val numberOfCachedItems = complexState.cachedRanges.sumOf { (range, _) -> range.count() }
//
//            val visibleItems = listState.layoutInfo.visibleItemsInfo
//            val totalSizeOfKnownItems = visibleItems.sumOf { it.size } + totalSizeOfCachedItems
//            val numberOfKnownItems = visibleItems.size + numberOfCachedItems
//            return totalSizeOfKnownItems.toFloat() / numberOfKnownItems
//                println(hashItemHeight)
/*
                println(
                    "averageItemSize: Float scroll(${
                        hashItemHeight.sumOf { it.second }.toFloat() / listState.layoutInfo.totalItemsCount
                    }) {"
                )
*/
//                var sum = 0
//                hashItemHeight.forEach {  sum += it.value }
//                return visibleAverageSize.toFloat() //hashItemHeight.map { it.key to it.value }.sumOf { it.second }.toFloat()
//                countAvar++
//                println("countAvar = ${countAvar}")
                 heightAll / listState.layoutInfo.totalItemsCount
            } else  0f
        }

    override val scrollOffset: Float
        get() {
            val firstVisibleItem = listState.layoutInfo.visibleItemsInfo.firstOrNull() ?: return 0.0f

//            val knownBits = complexState.cachedRanges.takeWhile { (range, _) -> range.last < firstVisibleItem.index }
//            val numberOfUnknownBits = firstVisibleItem.index - knownBits.sumOf { (range, _) -> range.count() }
//
//            return knownBits.sumOf { (_, size) -> size } +
//                    (numberOfUnknownBits * averageItemSize) +
//                    (listState.layoutInfo.viewportStartOffset - firstVisibleItem.offset)

            val rez =  (hashItemHeight[ firstVisibleItem.index ]?.second?.toFloat() ?: 0f ) - firstVisibleItem.offset

//            val rez =  hashItemHeight.map { it.key to it.value }.filter { it.first < firstVisibleItem.index }.sumOf { it.second }
//                .toFloat() - firstVisibleItem.offset

//            var rez: Float = -firstVisibleItem.offset.toFloat()
//            hashItemHeight.forEach {
//                if (it.key < firstVisibleItem.index) rez += it.value
//            }
//            println("hashItemHeight.filter = ${hashItemHeight.filter {it.first < firstVisibleItem.index }.sumOf { it.second }.toFloat()}")
//            println("firstVisibleItem.offset = ${firstVisibleItem.offset}")
//            println("rez = ${rez}")
//            println("containerSize = ${listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset}")
//            println("firstVisibleItem = ${firstVisibleItem.offset}")
//            println("scrollOffset: Float get($rez) ")
//            countScroll++
//            println("countScroll = ${countScroll}")
            return rez
        }

    override suspend fun scrollTo(containerSize: Int, scrollOffset: Float) {
//        println("scrollTo")
//        println("scrollOffset = ${scrollOffset - this.scrollOffset}")
        val scrollOffsetTmp = scrollOffset - this.scrollOffset
//        if (scrollOffset + scrollOffsetTmp >=0 && scrollOffset + scrollOffsetTmp <= maxScrollOffset(containerSize)) {
        listState.scrollBy(scrollOffsetTmp)
//        }
    }

    override fun maxScrollOffset(containerSize: Int): Float {
//        println("maxScrollOffset")
        val approximateContainerSize = averageItemSize * listState.layoutInfo.totalItemsCount
//        println("approximateContainerSize = ${approximateContainerSize}")
//        println("containerSizeMax = ${containerSize}")
        // val viewportHeight = listState.layoutInfo.run { viewportEndOffset - viewportStartOffset }
//        println("maxScrollOffset: ${approximateContainerSize - containerSize}")
        return approximateContainerSize - containerSize
    }
}

@Composable
fun BoxWithVScrollBarLazyList(
    modifier: Modifier = Modifier,
    scroll: LazyListState = rememberLazyListState(0),
    dark: Boolean = false,
    color: Color = if (dark) Color.Black else Color.White,
    /**
     * В случае если размер области правающий и maxHeight отталкивается от него, то необходимо учитывать
     * вертикальные padding-и, иначе если будет указан maxHeight который больше чем размер доступной области минус
     * отступы, то скролл-бар отображаться не будет. Т.е. нужно следить, чтобы maxHeight всегда был меньше или равен
     * достпуной области для отображения объекта.
     * */
    maxHeight: Int = 0,
    reverse: Boolean = false,
    content: @Composable BoxScope.(LazyListState) -> Unit
) {
    val hashItemHeight = remember { mutableMapOf<Int, Pair<Int,Int>>() }
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
//                rememberScrollbarAdapter(scroll),
                dark,
                color,
                reverse = reverse
            )
        }
    }
}
