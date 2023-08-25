package MainTabs.Journal.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemBloknotStyleState
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import viewmodel.MainDB

@Composable
fun ComItemBloknot(
    item: ItemBloknot,
    selection: SingleSelectionType<ItemBloknot>,
    edit: Boolean = true,
    openBloknot: (ItemBloknot) -> Unit,
    itemBloknotStyleState: ItemBloknotStyleState,
    dialLay: MyDialogLayout? = null,
    dropMenu: @Composable ColumnScope.(ItemBloknot, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expanded = remember { mutableStateOf(false) }
    val expandedOpis = remember { mutableStateOf(!item.sver) }

    with(itemBloknotStyleState) {
        MyCardStyle1(edit && selection.isActive(item), onClick = {
            selection.selected = item
            if (!edit) openBloknot(item)
        }, onDoubleClick = {
            selection.selected = item
            if (edit) openBloknot(item)
        }, dropMenu = if (edit) { exp ->
            dropMenu(item, exp)
        } else null,
            styleSettings = itemBloknotStyleState
        )
        {
            MainDB.complexOpisSpis.spisComplexOpisForBloknot.getState().value?.let { mapOpis ->
                Box(Modifier.padding(if (edit) 20.dp else 10.dp)) {

                    Column(
                        Modifier.padding(5.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 45.dp),
                            text = item.name,
                            style = mainTextStyle
                        )
                        Text(
                            modifier = Modifier.padding(0.dp),
                            text = "Количество записей: ${item.countidea}",
                            style = countTextStyle
                        )
                        mapOpis[item.id.toLong()]?.let { listOpis ->
                            if (edit && listOpis.isNotEmpty()) MyBoxOpisStyle(
                                expandedOpis,
                                listOpis,
                                dialLay,
                                MainDB.styleParam.journalParam.complexOpisForBloknot
                            )
                        }
                    }
                    Row(Modifier.height(45.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (edit && selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(start = 30.dp).padding(vertical = 5.dp),
                            expanded,
                            buttMenu
                        ) {
                            dropMenu(item, expanded)
                        }
                        Spacer(Modifier.weight(1f))
                        mapOpis[item.id.toLong()]?.let {
                            if (edit) RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 10.dp, end = 20.dp),
                                color = buttOpenColor
                            ) {
                                item.sver = item.sver.not()
                            }
                        }
                        MyTextButtSimpleStyle(
                            "\uD83D\uDD6E",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(end = 20.dp),
                            fontSize = 24.sp,
                            color = buttOpenColor
                        ) {
                            selection.selected = item
                            openBloknot(item)
                        }
                    }
                }
            }
        }
    }
}

class ItemBloknotPlate(
    val item: ItemBloknot,
    val onClick: () -> Unit
) {
    var expanded = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun getComposable() {
        MyCardStyle1(false, onClick = {
            onClick()
        })
        {
            Box(Modifier.padding(5.dp)) {

                Column(
                    Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MyTextStyle1(
                        modifier = Modifier.padding(horizontal = 45.dp),
                        text = item.name,
                    )
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = "Количество записей: ${item.countidea}",
                        style = TextStyle(color = Color(0xAFFFF7D9)),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

