package MainTabs.Quest.Items

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerFinishTriggerEnum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import viewmodel.QuestVM

@Composable
fun ComInnerFinishTriggerPlate(
    item: InnerFinishTriggerEnum,
    active: Boolean,
    onClick: (InnerFinishTriggerEnum) -> Unit
) {
    val expandedDropMenu = remember { mutableStateOf(false) }

    val expandedOpis = remember { mutableStateOf(false) }
    MyCardStyle1(active, 0, {
        onClick(item)
    }) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            modifier = Modifier.padding(start = 15.dp)
                                .weight(1f),//.padding(0.dp).padding(start = 15.dp),
                            text = item.nameTrig,
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 17.sp)
                        )
                        Column() {
                            QuestVM.getTriggerMarkersForTriggerChilds(TypeStartObjOfTrigger.INNERFINISH.id, item.id, emptyMarker = true)
/*
                            QuestVM.openQuestDB?.spisTrigger?.getState()?.value?.let { listTrig ->
                                listTrig.filter { it.type_id == TypeTrigger.INNERFINISH.id && it.child_id == item.id.toLong() }
                                    .let {
                                        it.forEach { trig ->
                                            Row(
                                                Modifier
                                                    .background(
                                                        MyColorARGB.colorRasxodTheme0.toColor().copy(alpha = 0.3f),
                                                        RoundedCornerShape(5.dp)
                                                    )
                                                    .border(
                                                        width = 0.5.dp,
                                                        brush = Brush.horizontalGradient(
                                                            listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                                        ),
                                                        shape = RoundedCornerShape(5.dp)
                                                    )
                                            ) {
                                                MyTextStyle(
                                                    trig.parent_type,
                                                    Modifier.padding(horizontal = 2.dp, vertical = 3.dp)
                                                        .padding(end = 8.dp),
                                                    param = MyTextStyleParam.style5
                                                )
                                            }
                                        }
                                        if (it.isEmpty()) Box(
                                            Modifier.background(
                                                color = Color.Red.copy(alpha = 0.3f),
                                                shape = RoundedCornerShape(5.dp)
                                            ).defaultMinSize(40.dp, 20.dp)
                                        ) {}
                                    }
                            }
*/
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
                ) {  //, endModif = Modifier::withMyBound1
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


/*
@OptIn(ExperimentalDesktopApi::class)
class ComInnerFinishTriggerPlate1(
    val item: InnerFinishTriggerEnum,
    val selection: SingleSelection,
    val selFun: (InnerFinishTriggerEnum) -> Unit = {},
    val sverFun: (InnerFinishTriggerEnum) -> Unit = {},
    val editable: Boolean = true,
    val dropMenu: (@Composable ColumnScope.(InnerFinishTriggerEnum, MutableState<Boolean>) -> Unit)? = null//{ _, _ -> }
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
            backColor = Color(0xFF464D45), //if (item.statsrok == 10L) Color(0xFF468F45) else
            onDoubleClick = {
                expandedOpis.value = expandedOpis.value.not()
//                item.sver = item.sver.not()
            },
            dropMenu = { exp ->
                dropMenu?.let { drMenu ->
                    drMenu(item, exp)
                }
            }
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            MyTextStyle(
                                modifier = Modifier.padding(start = 15.dp)
                                    .weight(1f),//.padding(0.dp).padding(start = 15.dp),
                                text = item.nameTrig,
                                param = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 17.sp)
                            )
                            QuestVM.openQuestDB?.spisTrigger?.getState()?.value?.let { listTrig ->
                                Column() {
                                    listTrig.filter { it.type_id == TypeTrigger.INNERFINISH.id && it.child_id == item.id.toLong() }
                                        .let {
                                            it.forEach { trig ->
                                                Row(
                                                    Modifier
                                                        .background(
                                                            MyColorARGB.colorRasxodTheme0.toColor().copy(alpha = 0.3f),
                                                            RoundedCornerShape(5.dp)
                                                        )
                                                        .border(
                                                            width = 0.5.dp,
                                                            brush = Brush.horizontalGradient(
                                                                listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                                            ),
                                                            shape = RoundedCornerShape(5.dp)
                                                        )
                                                ) {
                                                    MyTextStyle(
                                                        trig.parent_type,
                                                        Modifier.padding(horizontal = 2.dp, vertical = 3.dp)
                                                            .padding(end = 8.dp),
                                                        param = MyTextStyleParam.style5
                                                    )
                                                }
                                            }
                                            if (it.isEmpty()) Box(
                                                Modifier.background(
                                                    color = Color.Red.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(5.dp)
                                                ).defaultMinSize(40.dp, 20.dp)
                                            ) {}
                                        }
                                }
                            }
                            dropMenu?.let { dropMenuPar ->
                                if (selection.isActive(item) && editable) MyButtDropdownMenuStyle2(
                                    Modifier.padding(top = 0.dp).padding(vertical = 0.dp),
                                    expandedDropMenu
                                ) { setDissFun ->
                                    dropMenuPar(item, expandedDropMenu)
                                }
                            }
                            if (item.opisTrig != "") RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 10.dp, end = 10.dp)
                            ) {
                            }
                        }

                    }

                }
            }
            if ((item.opisTrig != "")) {
                BoxExpand(
                    expandedOpis,
                    Modifier.myModWithBound1(),
                    Modifier.fillMaxWidth()
                ) {  //, endModif = Modifier::withMyBound1
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


*/
