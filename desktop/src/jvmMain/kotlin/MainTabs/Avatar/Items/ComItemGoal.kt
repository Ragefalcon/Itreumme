package MainTabs.Avatar.Items

import MainTabs.Time.Items.privSchetPlanInfo
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemGoalStyleState
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemGoal
import viewmodel.MainDB

class ComItemGoal(
    val item: ItemGoal,
    val selection: SingleSelectionType<ItemGoal>,
    val selFun: (ItemGoal) -> Unit = {},
    val changeGotov: ((ItemGoal, Float) -> Unit)? = null,
    val editable: Boolean = true,
    val selectable: Boolean = true,
    val itemGoalStyleState: ItemGoalStyleState,
    val dropMenu: @Composable ColumnScope.(ItemGoal, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    val expandedOpis = mutableStateOf(!item.sver)

    val text_sum_hour = mutableStateOf("${item.hour.roundToStringProb(1)} ч.")

    @Composable
    fun getComposable() {
        with(itemGoalStyleState) {
            MyCardStyle1(
                selection.isActive(item) && selectable, 0, {
                    selection.selected = item
                    selFun(item)
                },
                onDoubleClick = {
                    item.sver = item.sver.not()
                    expandedOpis.value = !expandedOpis.value
                },
                backBrush = if (item.gotov == 100.0) background_brush_gotov else null,
                borderBrush = if (item.gotov == 100.0) border_brush_gotov else null,
                dropMenu = { exp -> dropMenu(item, exp) },
                styleSettings = itemGoalStyleState
            ) {
                Column {
                    Row(
                        modifier = Modifier.padding(5.dp).padding(start = 10.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectable && editable) {
                            MyTextButtWithoutBorder(
                                "\uD83E\uDC45",
                                Modifier.padding(start = 20.dp),
                                fontSize = 24.sp,
                                textColor = arrow_color
                            ) {
                                MainDB.avatarSpis.spisGoals.getState().value?.let {
                                    it.findLast { it.lvl < item.lvl }?.let {
                                        MainDB.addAvatar.setLvlGoal(item, it.lvl)
                                    }
                                }
                            }
                            MyTextButtWithoutBorder(
                                "\uD83E\uDC47",
                                Modifier.padding(horizontal = 10.dp),
                                fontSize = 24.sp,
                                textColor = arrow_color
                            ) {
                                MainDB.avatarSpis.spisGoals.getState().value?.let {
                                    it.find { it.lvl > item.lvl }?.let {
                                        MainDB.addAvatar.setLvlGoal(item, it.lvl)
                                    }
                                }
                            }

                        }
                        Column(Modifier.padding(10.dp).padding(vertical = 5.dp).weight(1f)) {
                            Text(modifier = Modifier, text = item.name, style = mainTextStyle)
                            privSchetPlanInfo(
                                item.summa,
                                item.min_aim,
                                item.max_aim,
                                item.summaRasxod,
                                item.schplOpen,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }

                        if (selection.isActive(item) && editable) MyButtDropdownMenuStyle2(
                            Modifier.padding(start = 10.dp).padding(vertical = 5.dp), expandedDropMenu, buttMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        if (item.opis != "") RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 20.dp, end = 20.dp),
                            color = boxOpisStyleState.colorButt
                        ) {
                            item.sver = item.sver.not()
                        }
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = text_sum_hour.value,
                            style = hourTextStyle
                        )
                    }
                    if ((item.opis != "")) MyBoxOpisStyle(expandedOpis, item.opis, boxOpisStyleState)
                }
            }
        }
    }
}


