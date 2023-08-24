package extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

class MyLazyListState {
    val listState: LazyListState = LazyListState(0)

    fun scrollToZero(){
        changeScroll = true
        lastUpdate++
    }

    private var lastUpdate by mutableStateOf(0L)
    private var changeScroll = false

    @Composable
    fun launchEffect(){
        LaunchedEffect(listState.layoutInfo,listState.firstVisibleItemScrollOffset,listState.firstVisibleItemIndex,listState.isScrollInProgress,listState.interactionSource){
//            println(" LaunchedEffect(listState) ${listState.layoutInfo}")

        }
        LaunchedEffect(lastUpdate) {
//            println(" LaunchedEffect(lastUpdate) $changeScroll")
            if (changeScroll) listState.scrollToItem(0, 0)
            changeScroll = false
//            }
        }
    }
}