package MainTabs.Setting.Items

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.MyButtDropdownMenuStyle2
import common.MyCardStyle1
import common.MyTextStyle1
import common.SingleSelectionType
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.ItemSaveSetStyle

class ComItemShablonStyle(
    val item: ItemSaveSetStyle,
    val selection: SingleSelectionType<ItemSaveSetStyle>,
    val doubleClick: (ItemSaveSetStyle) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(ItemSaveSetStyle, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expandedDropMenu = mutableStateOf(false)

    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        },
            onDoubleClick = {
                doubleClick(item)
            },
            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            RowVA {
                MyTextStyle1(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .padding(10.dp)
                        .weight(1f),
                    text = item.name,
                    textAlign = TextAlign.Start
                )
                if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                    Modifier.padding(end = 10.dp).padding(vertical = 0.dp), expandedDropMenu
                ) {
                    dropMenu(item, expandedDropMenu)
                }
            }
        }
    }
}


