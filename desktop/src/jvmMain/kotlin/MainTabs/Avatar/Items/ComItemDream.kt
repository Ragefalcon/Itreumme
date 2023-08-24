package MainTabs.Avatar.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemDreamStyleState
import extensions.ItemGoalStyleState
import extensions.RowVA
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.MainDB

class ComItemDream(
    val item: ItemDream,
    val selection: SingleSelectionType<ItemDream>,
    val selFun: (ItemDream) -> Unit = {},
    val changeGotov: ((ItemDream, Float) -> Unit)? = null,
    val editable: Boolean = true,
    val selectable: Boolean = true,
    val itemDreamStyleState: ItemDreamStyleState,
    val dropMenu: @Composable ColumnScope.(ItemDream, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    val expandedOpis = mutableStateOf(!item.sver)

    val text_sum_hour = mutableStateOf("${item.hour.roundToStringProb(1)} ч.")

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        with(itemDreamStyleState) {
            MyCardStyle1(
                selection.isActive(item) && selectable, 0, {
                    selection.selected = item
                    selFun(item)
//            expandedDropMenu.value = this.buttons.isSecondaryPressed
                },
                onDoubleClick = {
                    if (selectable) {
                        item.sver = item.sver.not()
                        expandedOpis.value = !expandedOpis.value
                    }
                },
//            backColor = if (item.stat == 10L) Color(0xFF468F45) else Color(0xFF464D45),
                backBrush = if (item.stat == TypeStatPlan.COMPLETE.codValue) background_brush_gotov else null,
                borderBrush = if (item.stat == TypeStatPlan.COMPLETE.codValue) border_brush_gotov else null,
                dropMenu = { exp -> dropMenu(item, exp) },
                styleSettings = itemDreamStyleState
            ) {
                Column {

                    RowVA(modifier = Modifier.padding(5.dp).padding(end = 10.dp)) {
                        if (selectable && editable) {
                            MyTextButtWithoutBorder(
                                "\uD83E\uDC45",
                                Modifier.padding(start = 20.dp),
                                fontSize = 24.sp,
                                textColor = arrow_color
                            ) {
                                MainDB.avatarSpis.spisDreams.getState().value?.let {
                                    println(it.size)
                                    it.findLast { it.lvl < item.lvl }?.let {
                                        println(it)
                                        MainDB.addAvatar.setLvlDream(item, it.lvl)
                                    }
                                }
                            }
                            MyTextButtWithoutBorder(
                                "\uD83E\uDC47",
                                Modifier.padding(horizontal = 10.dp),
                                fontSize = 24.sp,
                                textColor = arrow_color
                            ) {
                                MainDB.avatarSpis.spisDreams.getState().value?.let {
                                    it.find { it.lvl > item.lvl }?.let {
                                        println(it)
                                        MainDB.addAvatar.setLvlDream(item, it.lvl)
                                    }
                                }
                            }

                        }
                        Text(
                            modifier = Modifier.weight(1f).padding(10.dp).padding(vertical = 5.dp),
                            text = item.name,
                            style = mainTextStyle
                        )

                        if (selection.isActive(item) && editable) MyButtDropdownMenuStyle2(
                            Modifier.padding(start = 10.dp, end = 10.dp).padding(vertical = 5.dp), expandedDropMenu, buttMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        if (item.opis != "" && selectable) RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 10.dp, end = 10.dp),
                            color = boxOpisStyleState.colorButt
                        ) {
                            item.sver = item.sver.not()
                        }
/*
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = text_sum_hour.value,
                        style = TextStyle(color = Color(0xFFFFF7F9)),
                        fontSize = 20.sp
                    )
*/
                    }
                    if ((item.opis != "")) MyBoxOpisStyle(expandedOpis, item.opis, boxOpisStyleState)
/*
                    if ((item.opis != "")) { //(isActive()) &&
                        BoxExpand(
                            expandedOpis,
                            Modifier.myModWithBound1(),
                            Modifier.fillMaxWidth()
                        ) {  //, endModif = Modifier::withMyBound1
                            Text(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .padding(start = 10.dp),
                                text = item.opis,
                                style = TextStyle(color = Color(0xFFFFF7D9)),
                                fontSize = 15.sp
                            )
                        }
                    }
*/
                }
            }
        }
    }
}


