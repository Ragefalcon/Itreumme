package common.color

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.MyCardStyle2
import common.MyDeleteDropdownMenuButton
import common.MyDropdownMenuItem
import common.SingleSelectionType
import extensions.toColor
import ru.ragefalcon.sharedcode.models.data.ItemColorLibrary
import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree
import viewmodel.MainDB

class ComItemColorLibrary(
    val item: ItemColorLibrary,
    val selection: SingleSelectionType<ItemColorLibrary>? = null,
    val doubleClick: (ItemColorLibrary) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(ItemColorLibrary, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle2(false //selection.isActive(item)
            , 0, onClick =  {
//                        selection.selected = item
            }, onDoubleClick = {
                doubleClick(item)
            },
            widthBorder = 0.dp,
//            backColor = MyColorARGB.colorMyMainTheme.toColor(),
            dropMenu = { exp ->
                dropMenu(item,exp)
            }
        ) {
            Box(
                Modifier.padding(10.dp).height(50.dp).width(30.dp).background(item.color.toColor())
                    .border(1.dp, Color.Black)
            )
        }
    }
}