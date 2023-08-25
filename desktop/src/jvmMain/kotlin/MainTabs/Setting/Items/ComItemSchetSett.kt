package MainTabs.Setting.Items

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyButtDropdownMenuStyle1
import common.MyCardStyle1
import common.MyTextStyleParam
import common.SingleSelectionType
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemSettSchet
import java.awt.event.MouseEvent

class ComItemSchetSett(
    val item: ItemSettSchet,
    val selection: SingleSelectionType<ItemSettSchet>,
    val dropMenu: @Composable ColumnScope.(ItemSettSchet, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    var lastEvent = mutableStateOf<MouseEvent?>(null)

    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        }, backColor = if (!item.open_)
            Color.Red.toMyColorARGB().plusWhite().plusWhite().toColor().copy(alpha = 0.7f)
        else
            Color(0xFF464D45),
            dropMenu = { exp -> dropMenu(item, exp) }

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selection.isActive(item)) MyButtDropdownMenuStyle1(
                    Modifier.padding(start = 10.dp).padding(vertical = 5.dp), expandedDropMenu
                ) { setDissFun ->
                    dropMenu(item, expandedDropMenu)
                }

                Column(modifier = Modifier.padding(5.dp).padding(start = 15.dp, end = 10.dp).weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.padding(bottom = 2.dp).weight(1f),
                            text = item.name,
                            style = MyTextStyleParam.style2
                        )
                        Text(
                            modifier = Modifier,
                            text = item.summa.roundToStringProb(2),
                            style = MyTextStyleParam.style2.copy(
                                color = Color.Green.toMyColorARGB().plusWhite().toColor(),
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = item.cod,
                            style = MyTextStyleParam.style2
                        )
                    }
                }
            }
        }
    }
}


