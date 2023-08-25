package MainTabs.Setting.Items

import androidx.compose.foundation.ExperimentalFoundationApi
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
import common.MyTextStyleParam
import common.SingleSelectionType
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemSettTyperasxod

class ComItemTypeRasxSett(
    val item: ItemSettTyperasxod,
    val selection: SingleSelectionType<ItemSettTyperasxod>,
    val dropMenu: @Composable ColumnScope.(ItemSettTyperasxod, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(
            selection.isActive(item), 0, {
                selection.selected = item
            },
            dropMenu = { exp -> dropMenu(item, exp) }, backColor = if (!item.open)
                Color.Red.toMyColorARGB().plusWhite().plusWhite().toColor().copy(alpha = 0.7f)
            else
                Color(0xFF464D45)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selection.isActive(item)) MyButtDropdownMenuStyle1(
                    Modifier.padding(start = 10.dp).padding(vertical = 5.dp), expandedDropMenu
                ) {
                    dropMenu(item, expandedDropMenu)
                }
                Column(Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp).weight(1f)) {
                    Text(
                        modifier = Modifier.padding(0.dp).padding(bottom = 0.dp),
                        text = item.typer,
                        style = MyTextStyleParam.style2
                    )
                    Text(
                        modifier = Modifier,
                        text = item.planschet,
                        style = TextStyle(color = Color(0xAFFFF7D9)),
                        fontSize = 12.sp
                    )
                }
                Text(
                    modifier = Modifier.padding(end = 15.dp).padding(vertical = 5.dp),
                    text = item.countoper.toString(),
                    style = MyTextStyleParam.style2.copy(
                        color = Color.Green.toMyColorARGB().plusWhite().toColor()
                    )
                )
            }
        }
    }
}