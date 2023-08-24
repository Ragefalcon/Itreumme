package MainTabs.Avatar.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.MyButtDropdownMenuStyle2
import common.MyCardStyle2
import common.SingleSelectionType
import common.tests.IconNode
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree

class ComItemIconNode(
    val item: ItemIconNodeTree,
    val selection: SingleSelectionType<ItemIconNodeTree>,
    val dirQuest: String? = null,
    val doubleClick: (ItemIconNodeTree) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(ItemIconNodeTree, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)

    val shape = RoundedCornerShape(15.dp) //CircleShape

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle2(selection.isActive(item), 0, {
            selection.selected = item
        }, onDoubleClick = {
            doubleClick(item)
        },
            widthBorder = 1.5.dp,
//            backColor = MyColorARGB.colorMyMainTheme.toColor(),
            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA {
                    IconNode(item, "icon_skill_color_lamp.png", dirQuest)
                    if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                        Modifier.padding(end = 10.dp),
                        expandedDropMenu
                    ) {// setDissFun ->
                        dropMenu(item, expandedDropMenu)
                    }
                }
            }
        }
    }
}