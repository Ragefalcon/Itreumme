package MainTabs.Setting.Items

import androidx.compose.material.Text
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemSettTypedoxod

class ComItemTypeDoxSett(
    val item: ItemSettTypedoxod,
    val selection: SingleSelectionType<ItemSettTypedoxod>,
    val dropMenu: @Composable ColumnScope.(ItemSettTypedoxod, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(
            selection.isActive(item), 0, {
                selection.selected = item
//                expandedDropMenu.value = this.buttons.isSecondaryPressed
            },
            dropMenu = { exp -> dropMenu(item, exp) }
            , backColor = if (!item.open)
                Color.Red.toMyColorARGB().plusWhite().plusWhite().toColor().copy(alpha = 0.7f)// Color(0xFF468F45)
            else
                Color(0xFF464D45)
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (selection.isActive(item)) MyButtDropdownMenuStyle1(
                        Modifier.padding(start = 10.dp).padding(vertical = 5.dp), expandedDropMenu
                    ) {
                        dropMenu(item, expandedDropMenu)
                    }
                    Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.padding(start = 15.dp).weight(1f)) {
                                Text(
                                    modifier = Modifier.padding(0.dp).padding(bottom = 0.dp),
                                    text = item.typed,
                                    style = MyTextStyleParam.style2
                                )
/*
                                Text(
                                    modifier = Modifier,
                                    text = item.planschet,
                                    style = TextStyle(color = Color(0xAFFFF7D9)),
                                    fontSize = 12.sp
                                )
*/
                            }
                            Text(
                                modifier = Modifier.padding(0.dp).padding(vertical = 5.dp),
                                text = item.countoper.toString(),
                                style = MyTextStyleParam.style2.copy(
                                    color = Color.Green.toMyColorARGB().plusWhite().toColor()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}