package MainTabs.Quest.Items

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import viewmodel.QuestVM

class ComInnerStartTrigger(
    val item: InnerStartTriggerEnum,
    val selection: SingleSelection,
    val selFun: (InnerStartTriggerEnum) -> Unit = {},
    val sverFun: (InnerStartTriggerEnum) -> Unit = {},
    val editable: Boolean = true,
    val dropMenu: @Composable ColumnScope.(InnerStartTriggerEnum, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    val expandedOpis = mutableStateOf(false)

    @Composable
    fun getComposable() {

        MyCardStyle1(selection.isActive(item), 0,
            onClick = {
                selection.selected = item
                selFun(item)
            },
            backColor = Color(0xFF464D45),
            onDoubleClick = {
                expandedOpis.value = expandedOpis.value.not()

            },
            dropMenu = { exp ->
                dropMenu(item, exp)
            }
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.padding(start = 15.dp)
                                    .weight(1f),
                                text = item.nameTrig,
                                style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 17.sp)
                            )
                            Column() {
                                QuestVM.getComItemTriggers(
                                    TypeParentOfTrig.INNERSTART.code,
                                    item.id,
                                    emptyMarker = true
                                )
                            }
                            if (selection.isActive(item) && editable) MyButtDropdownMenuStyle2(
                                Modifier.padding(top = 0.dp).padding(vertical = 0.dp),
                                expandedDropMenu
                            ) {
                                dropMenu(item, expandedDropMenu)
                            }
                            if (item.opisTrig != "") RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 10.dp, end = 10.dp)
                            ) {
                            }

                        }

                    }
                }
                if ((item.opisTrig != "")) {
                    BoxExpand(
                        expandedOpis,
                        Modifier.myModWithBound1(),
                        Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .padding(start = 10.dp),
                            text = item.opisTrig,
                            style = TextStyle(color = Color(0xFFFFF7D9)),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
