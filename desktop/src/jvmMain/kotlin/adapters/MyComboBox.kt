package adapters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import common.*
import extensions.ComboBoxStyleState
import extensions.RowVA
import extensions.toColor
import extensions.withSimplePlate
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import viewmodel.MainDB
import java.util.*

class MyComboBox<T : Any> private constructor(
    val comItem: @Composable ((T, Boolean) -> Unit)? = null,
    val nameItem: (T) -> String,
    val emptyListMessage: String = "Список пуст",
    val openButtAtLeft: Boolean = true,
    val plateOrder: Boolean = false,
    val width: Dp = 250.dp,
    val comItemExp: @Composable ((T, Boolean, MutableState<Boolean>, () -> Unit) -> Unit)? = null,
    val listener: ((T) -> Unit)? = null

) {

    var getListItem: MutableState<List<T>?> = mutableStateOf(null)
    var selectedIndex = mutableStateOf(0)
    fun getSelectIndex(): Int = getListItem.value?.let { list ->
        if (list.isNotEmpty()) {
            if (selectedIndex.value < list.size) selectedIndex.value else {
                selectedIndex.value = list.size - 1
                listener?.let {
                    if (list.isNotEmpty()) it(list[selectedIndex.value])
                }
                selectedIndex.value
            }
        } else -1
    } ?: run { -1 }


    constructor(
        list: List<T>,
        comItem: @Composable ((T, Boolean) -> Unit)? = null,
        nameItem: (T) -> String,
        emptyListMessage: String = "Список пуст",
        openButtAtLeft: Boolean = true,
        listenerInStart: Boolean = true,
        plateOrder: Boolean = false,
        width: Dp = 250.dp,
        comItemExp: @Composable ((T, Boolean, MutableState<Boolean>, () -> Unit) -> Unit)? = null,
        listener: ((T) -> Unit)? = null
    ) : this(
        comItem,
        nameItem,
        emptyListMessage,
        openButtAtLeft,
        plateOrder,
        width,
        comItemExp,
        listener
    ) {
        getListItem.value = list

        if (listenerInStart) getListItem.value?.let { list ->
            listener?.let {
                if (list.isNotEmpty()) it(list[0])
            }
        }
    }

    constructor(
        list: MutableState<List<T>?>,
        comItem: @Composable ((T, Boolean) -> Unit)? = null,
        nameItem: (T) -> String,
        emptyListMessage: String = "Список пуст",
        openButtAtLeft: Boolean = true,
        listenerInStart: Boolean = true,
        plateOrder: Boolean = false,
        width: Dp = 250.dp,
        comItemExp: @Composable ((T, Boolean, MutableState<Boolean>, () -> Unit) -> Unit)? = null,
        listener: ((T) -> Unit)? = null
    ) : this(
        comItem,
        nameItem,
        emptyListMessage,
        openButtAtLeft,
        plateOrder,
        width,
        comItemExp,
        listener
    ) {
        getListItem = list

        if (listenerInStart) getListItem.value?.let { list ->
            listener?.let {
                if (list.isNotEmpty()) it(list[0])
            }
        }
    }

    constructor(
        stateObj: ItrCommObserveObj<List<T>>,
        comItem: @Composable ((T, Boolean) -> Unit)? = null,
        nameItem: (T) -> String,
        emptyListMessage: String = "Список пуст",
        openButtAtLeft: Boolean = true,
        listenerInStart: Boolean = true,
        plateOrder: Boolean = false,
        width: Dp = 250.dp,
        comItemExp: @Composable ((T, Boolean, MutableState<Boolean>, () -> Unit) -> Unit)? = null,
        listener: ((T) -> Unit)? = null
    ) : this(
        comItem,
        nameItem,
        emptyListMessage,
        openButtAtLeft,
        plateOrder,
        width,
        comItemExp,
        listener
    ) {
        getListItem = stateObj.getState()
        if (listenerInStart) getListItem.value?.let { list ->
            listener?.let {
                if (list.isNotEmpty()) it(list[0])
            }
        }
    }


    fun select(item: T) {
        getListItem.value?.let { list ->
            list.indexOf(item).let { ind ->
                if (ind >= 0) {
                    selectedIndex.value = ind
                    listener?.invoke(item)
                }
            }

        }
    }

    fun getSelected(): T? {
        getListItem.value?.let { list ->
            return if (selectedIndex.value < list.size) list[selectedIndex.value] else null
        }
        return null
    }

    @Composable
    fun show(
        modifier: Modifier = Modifier,
        style: ComboBoxStyleState = ComboBoxStyleState(MainDB.styleParam.commonParam.commonComboBoxStyle),
    ) {
        val expanded = remember { mutableStateOf(false) }
        val timeExpanded = remember { mutableStateOf(0L) }
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        getListItem.value?.let { list ->
            with(style) {
                MyShadowBox(panel.shadow) {
                    if (list.isNotEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LaunchedEffect( expanded.value){
                                timeExpanded.value = Date().time
                            }
                            RowVA(
                                modifier
                                    .withSimplePlate(panel)
                                    .clickable(interactionSource = interactionSource, indication = null) {
                                        if (Date().time - timeExpanded.value > 100) expanded.value = true else expanded.value = false
                                    }
                                    .padding(5.dp)
                            ) {

                                (textStyle.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
                                    @Composable
                                    fun textName() {
                                        if (comItem == null) Text(
                                            nameItem(list[getSelectIndex()]),
                                            modifier = Modifier.padding(1.dp).padding(horizontal = 5.dp),
                                            style = textStyle,
                                        )
                                    }

                                    @Composable
                                    fun buttOpen() {
                                        Text(
                                            "ᐁ",
                                            modifier = Modifier
                                                .offset(
                                                    offset.x.dp - (if (isHovered) offset.x * 2 else offset.x).dp,
                                                    offset.y.dp - (if (isHovered) offset.y * 2 else offset.y).dp
                                                )
                                                .padding(1.dp).padding(horizontal = 5.dp),
                                            style = textStyle.copy(color = color_open_butt.toColor(),
                                                shadow = Shadow(
                                                    offset = if (isHovered) Offset(
                                                        offset.x * 2,
                                                        offset.y * 2
                                                    ) else offset,
                                                    blurRadius = if (isHovered) textStyle.shadow?.blurRadius?.let { it * 2 }
                                                        ?: 4f else textStyle.shadow?.blurRadius ?: 2f
                                                )
                                            ),
                                        )
                                    }
                                    if (openButtAtLeft) {
                                        buttOpen()
                                        textName()
                                    } else {
                                        textName()
                                        buttOpen()
                                    }
                                    getSelected()?.let {
                                        comItem?.invoke(it, false)
                                    }
                                }
                            }
                            MyDropdownMenu(expanded, dropdown, width = width) {
                                fun selectInd(index: Int) {
                                    selectedIndex.value = index
                                    getListItem.value?.let { list ->
                                        listener?.let {
                                            if (index < list.size) it(list[index])
                                        }
                                    }
                                }

                                @Composable
                                fun getItemsForSelect() {
                                    list.forEachIndexed { index, item ->
                                        if (comItemExp != null) MyDropdownMenuCommonItem(
                                            expanded, dropdown,
                                            { isHovered ->
                                                comItemExp.invoke(item, isHovered, expanded) { selectInd(index) }
                                            },
                                            autoClick = false
                                        ) {
                                        } else if (comItem != null) MyDropdownMenuCommonItem(expanded, dropdown,
                                            { isHovered ->
                                                comItem.invoke(item, isHovered)
                                            }) { selectInd(index) }
                                        else MyDropdownMenuItem(expanded, dropdown, nameItem(item)) { selectInd(index) }
                                    }
                                }
                                if (plateOrder) PlateOrderLayout(alignmentCenter = true) {
                                    getItemsForSelect()
                                } else getItemsForSelect()
                            }
                        }
                    } else {
                        RowVA(
                            modifier
                                .withSimplePlate(panel)
                                .padding(10.dp)
                        ) {
                            Text(
                                emptyListMessage,
                                modifier = modifier,
                                style = textStyle
                            )
                        }
                    }
                }
            }
        }
    }
}
