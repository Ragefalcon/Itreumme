package MainTabs.Quest.Items

import androidx.compose.material.Text
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemDialog
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import viewmodel.QuestVM

@Composable
fun ComItemDialogPlate(
    item: ItemDialog,
    active: Boolean,
    onClick: (ItemDialog) -> Unit
) {
    MyCardStyle1(active, 0, {
        onClick(item)
    }) {
        Row(
            Modifier.padding(start = 15.dp).padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (item.govorun_name != "") "${item.govorun_name}: ${item.name}" else item.name,
                modifier = Modifier.padding(start = 10.dp).weight(1f),
                style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
            )
        }
    }
}

class ComItemDialog(
    val item: ItemDialog,
    val selection: SingleSelectionType<ItemDialog>,
    val dialPan: MyDialogLayout,
    val scrollToThis: (Long) -> Unit = { },
    val dropMenu: @Composable ColumnScope.(ItemDialog, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)


    @Composable
    fun getComposable() {
        val expandedOpis = remember { mutableStateOf(!item.sver) }
//        LaunchedEffect(expandedOpis.value){
//            scrollToThis(100)
//        }
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        }, {
            item.sver = item.sver.not()
            expandedOpis.value = !expandedOpis.value
            scrollToThis(100)
        }, dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Column {
                Row(
                    Modifier.padding(start = 15.dp).padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RowVA(Modifier.weight(1f)) {
                        if (item.govorun_name != "") Text(
                            text = "${item.govorun_name}: ",
                            modifier = Modifier.padding(start = 10.dp),
                            style = MyTextStyleParam.style1.copy(
                                fontSize = 17.sp,
                                textAlign = TextAlign.Start,
                                color = MyColorARGB.colorEffektShkal_Nedel.toColor()
                            )
                        )
                        Text(
                            text = item.name,
                            modifier = Modifier.padding(start = 10.dp).weight(1f),
                            style = MyTextStyleParam.style1.copy(fontSize = 17.sp, textAlign = TextAlign.Start)
                        )
                        Column(Modifier.padding(end = 10.dp)) {
                            QuestVM.getTriggerMarkersForTriggerChilds(TypeStartObjOfTrigger.STARTDIALOG.id, item.id.toLong(), emptyMarker = true)
/*
                            QuestVM.openQuestDB?.spisTrigger?.getState()?.value?.let { listTrig ->
                                listTrig.filter { it.type_id == TypeTrigger.STARTDIALOG.id && it.child_id == item.id.toLong() }
                                    .let {
                                        it.forEach { trig ->
                                            Row(
                                                Modifier
                                                    .background(
                                                        if (trig.parent_type != TypeParentTrig.STARTQUESTDIALOG.code)
                                                            MyColorARGB.colorRasxodTheme0.toColor().copy(alpha = 0.3f)
                                                        else MyColorARGB.colorStatTint_01.toColor().copy(alpha = 0.3f),
                                                        RoundedCornerShape(5.dp)
                                                    )
                                                    .border(
                                                        width = 0.5.dp,
                                                        brush = if (trig.parent_type != TypeParentTrig.STARTQUESTDIALOG.code)
                                                            Brush.horizontalGradient(
                                                                listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                                            )
                                                        else Brush.horizontalGradient(
                                                            listOf(
                                                                MyColorARGB.colorStatTint_01.toColor()
                                                                    .copy(alpha = 0.6f),
                                                                MyColorARGB.colorStatTint_01.toColor()
                                                                    .copy(alpha = 0.6f)
                                                            )
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
                                                color = Color.Red.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(5.dp)
                                            ).defaultMinSize(70.dp, 20.dp)
                                        ) {}
                                    }
                            }
*/
                        }

                    }
//                    Spacer(Modifier.weight(1f))
                    if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                        Modifier.padding(top = 3.dp).padding(vertical = 0.dp),
                        expandedDropMenu
                    ) {
                        dropMenu(item, expandedDropMenu)
                    }
                    if (item.maintext != "") RotationButtStyle1(
                        expandedOpis,
                        Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        item.sver = item.sver.not()
                        scrollToThis(100)
                    }
                }
                if ((item.maintext != "")) { //(selection.selected == item) &&&&(expandedOpis.value)
                    BoxExpand(
                        expandedOpis,
                        Modifier.myModWithBound1(),
                        Modifier.fillMaxWidth()
                    ) {  //, endModif = Modifier::withMyBound1
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .padding(start = 10.dp)
                                    .fillMaxWidth(),
                                text = item.maintext,
                                style = MyTextStyleParam.style4.copy(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Normal
                                ) //TextStyle(color = Color(0xFFFFF7D9)),
//                                fontSize = 15.sp
                            )
                            QuestVM.openQuestDB?.let { questDB ->
                                questDB.spisQuest.spisOtvetDialog.getState().value?.let { listOtvet ->
                                    listOtvet.filter { it.dialog_id == item.id.toLong() }
                                        .let {
                                            it.forEach { otvet ->
                                                ComItemOtvetDialog(otvet, dialPan)
                                            }
                                        }
                                }
                                Row {
                                    MyTextButtStyle1("Добавить вариант ответа", fontSize = 17.sp) {
                                        MyOneVopros(
                                            dialPan,
                                            "Введите текст нового ответа.",
                                            "Добавить",
                                            "Ответ",
                                            ""
                                        ) { textOtv ->
                                            if (textOtv != "") {
                                                questDB.addQuest.addOtvetDialog(item.id.toLong(), textOtv)
                                            }
                                        }
                                    }
                                    MyTextButtStyle1("++ вариант ответа", fontSize = 17.sp) {
                                        questDB.addQuest.addOtvetDialog(item.id.toLong(), "Новый ответ")
                                        scrollToThis(200)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


