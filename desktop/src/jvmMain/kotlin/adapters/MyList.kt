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
    if (list.isNotEmpty()) {
        BoxWithVScrollBarLazyList(
            modifier,
            state,
            dark = darkScroll,
            maxHeight = maxHeight,
            reverse = reverse
        ) { scrollState ->
            LazyColumn(state = scrollState, reverseLayout = reverse) {
                itemsIndexed(list) { ind, item ->
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
) {

    stateObj.getState().value?.let { typeRList ->

        MyList(typeRList, modifier, state, darkScroll, maxHeight, reverse, comItem)
    } ?: Spacer(modifier)

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

    darkScroll: Boolean = false,
    verticalScroll: Boolean = true,
    alignmentCenter: Boolean = false,

    comItem: @Composable (T) -> Unit
) {
    if (list.isNotEmpty()) {
        if (verticalScroll) BoxWithVScrollBar(modifier, dark = darkScroll) { scrollStateBox ->
            Box(Modifier.verticalScroll(scrollStateBox, enabled = true)) {
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
            ) {
                itemsIndexed(list) { ind, item ->
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
            itemsIndexed(typeRList) { ind, item ->
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

fun <T : Id_class> MyListItems(
    stateObj: ItrCommListObserveObj<T>,
    modifier: Modifier = Modifier,
    scope: LazyListScope,
    comItem: @Composable (T) -> Unit
) {
    MyListItems(stateObj.getState(), modifier, scope, comItem)
}
