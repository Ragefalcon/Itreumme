package MainTabs.Avatar.Items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyButtDropdownMenuStyle1
import common.MyCardStyle1
import common.MyTextStyle1
import common.SingleSelectionType
import extensions.format
import ru.ragefalcon.sharedcode.models.data.ItemLoadQuest
import java.util.*

class ComItemLoadQuest(
    val item: ItemLoadQuest,
    val selection: SingleSelectionType<ItemLoadQuest>,
    val doubleClick: (ItemLoadQuest) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(ItemLoadQuest, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)

    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        }, {
            doubleClick(item)
            expandedOpis.value = !expandedOpis.value
        }, dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Column {
                Row(
                    Modifier.padding(start = 15.dp).padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                        Row {
                            Column(Modifier.padding(0.dp).weight(1f)) {
                                MyTextStyle1(
                                    modifier = Modifier.padding(start = 10.dp),
                                    text = item.name,
                                )
                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    text = Date(item.dateopen).format("dd.MM.yyyy HH:mm"),
                                    style = TextStyle(color = Color(0xAFFFF7D9)),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    if (selection.isActive(item)) MyButtDropdownMenuStyle1(
                        Modifier.padding(horizontal = 15.dp).padding(vertical = 5.dp), expandedDropMenu
                    ) { _ ->
                        dropMenu(item, expandedDropMenu)
                    }
                }
            }
        }
    }
}
