import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import common.PlateOrderLayout
import extensions.BoxWithHScrollBar
import extensions.BoxWithVScrollBar
import extensions.BoxWithVScrollBarLazyList
import extensions.scrollVerticalToHorizontal
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.source.disk.ItrCommListObserveObj
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

@Composable
fun <T : Any> MyList(
    list: List<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    darkScroll: Boolean = false,
    maxHeight: Int = 0,
    reverse: Boolean = false,
    comItem: @Composable (Int, T) -> Unit
) {

    /**
     * Похоже НИКОГДА не стоит использовать state.layoutInfo в качестве параметра для Composable функций
     *
     * если использовать вот это:
    //    LaunchedEffect(state.layoutInfo) {
    //        println("LaunchedEffect(scroll.layoutInfo.viewportEndOffset, scroll.layoutInfo.viewportStartOffset){")
    //        if (risScroll.value != state.layoutInfo.viewportEndOffset - state.layoutInfo.viewportStartOffset >= maxHeight)
    //            risScroll.value = state.layoutInfo.viewportEndOffset - state.layoutInfo.viewportStartOffset >= maxHeight
    //    }
     *
     * То в сочетании с rememberLazyListState который находится во внешней функции, это приводит к зацикливанию перерасчетов
     * положения скролла и соответственно к перерисовке всего этого... println из LaunchedEffect(state.layoutInfo) даже не выводится.
     *
     * Я до конца так и не понял всю цепочку этой проблемы, но думаю лучше ее обходить стороной. Хотя можно проблема на самом деле
     * в реализации VerticalScrollbar.
     * */
//    val risScroll = remember { mutableStateOf(false) }
//    LaunchedEffect(state.layoutInfo) {
//        println("LaunchedEffect(scroll.layoutInfo.viewportEndOffset, scroll.layoutInfo.viewportStartOffset){")
//        if (risScroll.value != state.layoutInfo.viewportEndOffset - state.layoutInfo.viewportStartOffset >= maxHeight)
//            risScroll.value = state.layoutInfo.viewportEndOffset - state.layoutInfo.viewportStartOffset >= maxHeight
//    }
    if (list.isNotEmpty()) {
//        StateVM.timerStop("MyList inn2")
        BoxWithVScrollBarLazyList(modifier, state, dark = darkScroll, maxHeight = maxHeight, reverse = reverse) { scrollState ->
//            StateVM.timerStop("MyList inn3")
            LazyColumn(state = scrollState, reverseLayout = reverse) {
//                StateVM.timerStop("MyList inn4")
                itemsIndexed(list) { ind, item ->
//                    StateVM.timerStop("MyList comItem")
                    comItem(ind, item)
                }
            }
        }
    } else {
        Spacer(modifier)
    }
}

@Composable
fun <T : Any> MyList(
    mutList: SnapshotStateList<T>,
    modifier
    : Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    darkScroll: Boolean = false,
    maxHeight: Int = 0,
    reverse: Boolean = false,
    comItem: @Composable (Int, T) -> Unit
) {
    mutList.toList().let { typeRList ->
        MyList(typeRList, modifier, state, darkScroll, maxHeight, reverse, comItem)
    }
}

@Composable
fun <T : Any> MyList(
    stateObj: ItrCommObserveObj<List<T>>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    darkScroll: Boolean = false,
    /**
     * В случае если размер области правающий и maxHeight отталкивается от него, то необходимо учитывать
     * вертикальные padding-и, иначе если будет указан maxHeight который больше чем размер доступной области минус
     * отступы, то скролл-бар отображаться не будет. Т.е. нужно следить, чтобы maxHeight всегда был меньше или равен
     * достпуной области для отображения объекта.
     * */
    maxHeight: Int = 0,
    reverse: Boolean = false,
    comItem: @Composable (Int, T) -> Unit
    /**
    LazyListState сюда(comItem) не стоит передовать, т.к. при определенных обстоятельствах айтему
    уходят в бесконечный цикл обновления и это можно даже не заметить....
    возможно стоит эту возможность вынести в отдельную не Composable функцию
     */
) {
//    StateVM.timerStart("MyList")
    stateObj.getState().value?.let { typeRList ->
//        StateVM.timerStop("MyList inn1")
        MyList(typeRList, modifier, state, darkScroll, maxHeight, reverse, comItem)
    } ?: Spacer(modifier)
//    StateVM.timerStop("MyList endFun")
}

@Composable
fun <T : Any> MyListPlate(
    stateObj: ItrCommObserveObj<List<T>>,
    modifier: Modifier = Modifier,
    darkScroll: Boolean = false,
    verticalScroll: Boolean = true,
    alignmentCenter: Boolean = false,
    comItem: @Composable (T) -> Unit
) {
    stateObj.getState().value?.let { typeList ->
        MyListPlate(typeList, modifier, darkScroll, verticalScroll, alignmentCenter, comItem)
    }
}

@Composable
fun <T : Any> MyListPlate(
    list: List<T>,
    modifier: Modifier = Modifier,
//    state: LazyListState = rememberLazyListState(),
    darkScroll: Boolean = false,
    verticalScroll: Boolean = true,
    alignmentCenter: Boolean = false,
//    maxHeight: Int = 0,
    comItem: @Composable (T) -> Unit
) {
    if (list.isNotEmpty()) {
        if (verticalScroll) BoxWithVScrollBar(modifier, dark = darkScroll) { scrollStateBox ->
            Box(Modifier.verticalScroll(scrollStateBox, enabled = true)) { //fillMaxWidth().
                PlateOrderLayout(alignmentCenter = alignmentCenter) {
                    list.forEach { item ->
                        comItem(item)
                    }
                }
            }
        }
        else PlateOrderLayout(modifier, alignmentCenter = alignmentCenter) {
            list.forEach { item ->
                comItem(item)
            }
        }
    } else {
        Box(modifier)
    }
}


@Composable
fun <T : Any> MyListRow(
    list: List<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    darkScroll: Boolean = false,
    maxWidth: Int = 0,
    comItem: @Composable (Int, T) -> Unit
) {
    if (list.isNotEmpty()) {
        BoxWithHScrollBar(
            modifier = modifier, scroll = state, dark = darkScroll, reverse = false, maxWidth = maxWidth
        ) { scrollSt ->
            LazyRow(
                state = scrollSt,
                modifier = Modifier.scrollVerticalToHorizontal(scrollSt),
                verticalAlignment = Alignment.CenterVertically
            ) {//scrollState) {
                itemsIndexed(list) { ind, item -> //.toList() ?: listOf()
                    comItem(ind, item)
                }
            }
        }
    } else {
        Spacer(modifier)
    }
}

@Composable
fun <T : Any> MyListRow(
    stateObj: ItrCommObserveObj<List<T>>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    darkScroll: Boolean = false,
    maxWidth: Int = 0,
    comItem: @Composable (Int, T) -> Unit
) {
    stateObj.getState().value?.let { typeRList ->
        MyListRow(typeRList, modifier, state, darkScroll, maxWidth, comItem)
    }
}


fun <T> MyListItems(
    typeRList: List<T>,
    modifier: Modifier = Modifier,
    scope: LazyListScope,
    comItem: @Composable (T) -> Unit
) {
    scope.apply {
        if (typeRList.isNotEmpty()) {
            itemsIndexed(typeRList) { ind, item -> //.toList() ?: listOf()
                comItem(item)
            }
        }
    }
}

fun <T> MyListItems(
    stateObj: ItrCommObserveObj<List<T>>,
    modifier: Modifier = Modifier,
    scope: LazyListScope,
    comItem: @Composable (T) -> Unit
) {
    stateObj.getState().value?.let { typeRList ->
        MyListItems(typeRList, modifier, scope, comItem)
    }
}

fun <T: Id_class> MyListItems(
    stateObj: ItrCommListObserveObj<T>,
    modifier: Modifier = Modifier,
    scope: LazyListScope,
    comItem: @Composable (T) -> Unit
) {
    MyListItems(stateObj.getState(), modifier, scope, comItem)
}
