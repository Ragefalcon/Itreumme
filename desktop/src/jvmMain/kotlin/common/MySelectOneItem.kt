package common

import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

@Composable
fun <T : Any> MySelectOneItem(
    selection: SingleSelectionType<T>,
    spisok: ItrCommObserveObj<List<T>>,
    textButt: String = "Выбрать что-то",
    modifier: Modifier = Modifier,
    startWithOpenPlan: Boolean = false,
    emptyValue: Boolean = true,
    comItemSelect: @Composable (T, (T) -> Unit) -> Unit,
    comItem: @Composable (T, (T) -> Unit) -> Unit
) = MySelectOneItemCommon(
    selection,
    textButt,
    modifier,
    startWithOpenPlan,
    emptyValue,
    comItemSelect,
    comItem
) { expandedSel ->
    MyList(
        stateObj = spisok,
        modifier = Modifier.weight(1f).padding(bottom = 10.dp).padding(horizontal = 20.dp),
        darkScroll = true
    ) { ind, itemShablonDenPlan ->
        comItem(itemShablonDenPlan) {
            selection.selected = it
            expandedSel.value = false
        }
    }
}

@Composable
fun <T : Any> MySelectOneItem(
    selection: SingleSelectionType<T>,
    spisok: List<T>,
    textButt: String = "Выбрать что-то",
    modifier: Modifier = Modifier,
    startWithOpenPlan: Boolean = false,
    emptyValue: Boolean = true,
    comItemSelect: @Composable (T, (T) -> Unit) -> Unit,
    comItem: @Composable (T, (T) -> Unit) -> Unit
) = MySelectOneItemCommon(
    selection,
    textButt,
    modifier,
    startWithOpenPlan,
    emptyValue,
    comItemSelect,
    comItem
) { expandedSel ->
    MyList(
        list = spisok,
        modifier = Modifier.weight(1f).padding(bottom = 10.dp).padding(horizontal = 20.dp),
        darkScroll = true
    ) { _, itemShablonDenPlan ->
        comItem(itemShablonDenPlan) {
            selection.selected = it
            expandedSel.value = false
        }
    }
}

@Composable
private fun <T : Any> MySelectOneItemCommon(
    selection: SingleSelectionType<T>,
    textButt: String = "Выбрать что-то",
    modifier: Modifier = Modifier,
    startWithOpenPlan: Boolean = false,
    emptyValue: Boolean = true,
    comItemSelect: @Composable (T, (T) -> Unit) -> Unit,
    comItem: @Composable (T, (T) -> Unit) -> Unit,
    myList: @Composable ColumnScope.(MutableState<Boolean>) -> Unit
) {
    val loadPovtor = mutableStateOf(true)
    val loadTime = mutableStateOf(true)
    val expandedSel = remember { mutableStateOf(startWithOpenPlan) }

    Column(
        modifier = modifier.padding(bottom = 5.dp).animateContentSize().border(
            width = 1.dp,
            brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
            shape = RoundedCornerShape(10.dp)
        ).background(
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            color = Color(0xFFE4E0C7),
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!expandedSel.value) {
            selection.selected?.let {
                comItemSelect(it) { expandedSel.value = true }
            }
            if (selection.selected == null) MyTextButtStyle1(textButt) {
                expandedSel.value = true
            }
        } else {
            myList(expandedSel)
            if (emptyValue) MyTextButtStyle1("Отменить выбор") {
                selection.selected = null
                expandedSel.value = false
            }
        }
    }
}


