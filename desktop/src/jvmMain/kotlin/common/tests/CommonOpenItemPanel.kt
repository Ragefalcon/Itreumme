package common.tests

import MyDialog.MyDialogLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import common.SingleSelectionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *
 * Sample mainSpis = { modifier, openSpis, stateList ->
 * MyList(MainDB.spisTreeSkills, Modifier.weight(1f).padding(horizontal = 60.dp).then(modifier), stateList) { _, ind, itemTreeSkill ->
 *     ComItemTreeSkills(itemTreeSkill, StateVM.selectionTreeSkills, //
 *         openTree = { itemA ->
 *             openOpis(index,itemA)
 *         }
 *     ) { item, expanded -> ... }
 * }
 *
 */
class CommonOpenItemPanel<T : Any>(
    val whenOpen: (T) -> Unit,
    val openedItem: @Composable (itemMainOpen: T, startOpenAnimation: MutableState<Boolean>, selection: SingleSelectionType<T>, dialLay: MyDialogLayout?) -> Unit,
    val mainSpis: @Composable ColumnScope.(modifierList: Modifier, selection: SingleSelectionType<T>, openSpis_Index_Item: (Int, T) -> Unit, lazyListState: LazyListState, dialLay: MyDialogLayout?) -> Unit
) {

    val rectListTree = mutableStateOf(Rect(Offset(0f, 0f), Size(0f, 0f)))
    val rectParent = mutableStateOf(Rect(Offset(0f, 0f), Size(0f, 0f)))
    var layoutParent: LayoutCoordinates? = null
    val offsetItem = mutableStateOf(0)
    val sizeItem = mutableStateOf(0)

    val openTS: MutableState<Boolean> = mutableStateOf(false)
    val selectionMainSpis = SingleSelectionType<T>()

    val startOpenAnimation = mutableStateOf(false)
    val state: LazyListState = LazyListState()

    @Composable
    fun show(
        modifier: Modifier = Modifier,
        dialLay: MyDialogLayout? = null
    ) {

        LaunchedEffect(openTS.value) {
            if (openTS.value) {
                selectionMainSpis.selected?.let { mainItem ->
                    whenOpen(mainItem)
                }
            }
        }

        Column(modifier.onGloballyPositioned {

            rectParent.value = it.boundsInParent()

        }, horizontalAlignment = Alignment.CenterHorizontally) {
            if (openTS.value) {
                openMainSpis(dialLay)
            } else {
                mainSpis(Modifier.onGloballyPositioned { itemLay ->
                    rectListTree.value = itemLay.boundsInParent()
                }, selectionMainSpis, { index, item ->
                    selectionMainSpis.selected = item
                    openTS.value = true
                    val visibleItem = state.layoutInfo.visibleItemsInfo.find { it.index == index }
                    offsetItem.value = visibleItem?.offset ?: 0
                    sizeItem.value = visibleItem?.size ?: 0
                }, state, dialLay)
            }
        }
    }

    @Composable
    private fun openMainSpis(dialLay: MyDialogLayout?) {
        with(LocalDensity.current) {
            val firstStart = remember { mutableStateOf(true) }
            rememberCoroutineScope().launch {
                delay(100)
                if (firstStart.value) {
                    startOpenAnimation.value = true
                    firstStart.value = false
                }
            }
            val durationAnim = 200
            val topOffsetTree: Float by animateFloatAsState(
                targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectListTree.value.top + offsetItem.value),
                animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
            ) {
                if (!startOpenAnimation.value && openTS.value) {
                    openTS.value = false
                }
            }
            val startOffsetTree: Float by animateFloatAsState(
                targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectListTree.value.left),
                animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
            ) {
                if (!startOpenAnimation.value && openTS.value) {
                    openTS.value = false
                }
            }
            val endOffsetTree: Float by animateFloatAsState(
                targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectParent.value.width - rectListTree.value.right + 8.dp.toPx()),
                animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
            ) {
                if (!startOpenAnimation.value && openTS.value) {
                    openTS.value = false
                }
            }
            val bottomOffsetTree: Float by animateFloatAsState(
                targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectParent.value.height - rectListTree.value.top - offsetItem.value - sizeItem.value),
                animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
            ) {
                if (!startOpenAnimation.value && openTS.value) {
                    openTS.value = false
                }
            }

            with(LocalDensity.current) {
                Box(
                    Modifier.padding(
                        top = topOffsetTree.toDp(),
                        bottom = bottomOffsetTree.toDp(),
                        start = startOffsetTree.toDp(),
                        end = endOffsetTree.toDp()
                    )
                ) {
                    selectionMainSpis.selected?.let { itemMainOpen ->
                        openedItem(itemMainOpen, startOpenAnimation, selectionMainSpis, dialLay)
                    }
                }
            }
        }
    }

    fun zeroIfMinus(value: Float): Float = if (value < 0f) 0f else value
}