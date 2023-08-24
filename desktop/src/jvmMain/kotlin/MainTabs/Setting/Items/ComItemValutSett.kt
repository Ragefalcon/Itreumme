package MainTabs.Setting.Items

import androidx.compose.material.Text
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemValut
import java.awt.event.MouseEvent

class ComItemValutSett(
    val item: ItemValut,
    val selection: SingleSelectionType<ItemValut>,
    val dropMenu: @Composable ColumnScope.(ItemValut, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    var lastEvent = mutableStateOf<MouseEvent?>(null)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
//            expandedDropMenu.value = this.buttons.isSecondaryPressed
        },
            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selection.isActive(item)) MyButtDropdownMenuStyle1(
                    Modifier.padding(start = 10.dp).padding(vertical = 5.dp), expandedDropMenu
                ) { setDissFun ->
                    dropMenu(item, expandedDropMenu)
                }

                Column(modifier = Modifier.padding(5.dp).padding(start = 15.dp, end = 10.dp, ).weight(1f)) {
                    Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) { //horizontalArrangement = Arrangement.SpaceBetween,

//                        Column(Modifier.padding(0.dp).weight(1f)) {
                        Row(Modifier.padding(bottom = 2.dp).weight(1f, fill = false),verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.padding(bottom = 2.dp).weight(1f, fill = false),
                                text = item.name,
                                style = MyTextStyleParam.style2
                            )
                            Text(
                                modifier = Modifier,
                                text = " (${item.countschet})",
                                style = MyTextStyleParam.style2.copy(
                                    color = Color.Black.toMyColorARGB().plusWhite().toColor().copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                            )
                        }
//                        Spacer(Modifier.weight(0.001f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.padding(start = 10.dp), //.align(Alignment.CenterVertically)
                                text = "(",
                                style = MyTextStyleParam.style2.copy(
                                    color = Color(0xDFFFF7D9),
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                modifier = Modifier,
                                text = "${item.kurs.roundToStringProb(2)}",
                                style = MyTextStyleParam.style2.copy(
                                    color = Color.Green.toMyColorARGB().plusWhite().toColor(),
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                modifier = Modifier,
                                text = " RUB)",
                                style = MyTextStyleParam.style2.copy(
                                    color = Color(0xDFFFF7D9),
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                modifier = Modifier.padding(start = 10.dp), //.align(Alignment.CenterVertically)
                                text = item.cod,
                                style = MyTextStyleParam.style2,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}


