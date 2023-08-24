package MainTabs.Avatar.Items


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemCharacteristicState
import extensions.RowVA
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemCharacteristic
import viewmodel.MainDB

class ComItemCharacteristicsEdit(
    val item: ItemCharacteristic,
    val selection: SingleSelectionType<ItemCharacteristic>,
    val itemCharacteristicState: ItemCharacteristicState,
    val dropMenu: @Composable ColumnScope.(ItemCharacteristic, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    val expandedOpis = mutableStateOf(!item.sver)

    val text_sum_hour = mutableStateOf("${item.hour.roundToStringProb(1)} ч.")

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        with(itemCharacteristicState) {
            MyCardStyle1(
                selection.isActive(item), 0, {
                    selection.selected = item
                },
                onDoubleClick = {
                    item.sver = item.sver.not()
                    expandedOpis.value = !expandedOpis.value
                },
                dropMenu = { exp -> dropMenu(item, exp) },
                styleSettings = itemCharacteristicState
            ) {
                Column {
                    RowVA {
                        MyTextButtWithoutBorder(
                            "\uD83E\uDC45",
                            Modifier.padding(start = 20.dp),
                            fontSize = 24.sp,
                            textColor = ARROW_COLOR
                        ) {
                            MainDB.avatarSpis.spisCharacteristics.getState().value?.let {
                                println(it.size)
                                it.findLast { it.sort < item.sort }?.let {
                                    println(it)
                                    MainDB.addAvatar.setSortCharacteristic(item, it.sort)
                                }
                            }
                        }
                        MyTextButtWithoutBorder(
                            "\uD83E\uDC47", Modifier.padding(horizontal = 10.dp),
                            fontSize = 24.sp,
                            textColor = ARROW_COLOR
                        ) {
                            MainDB.avatarSpis.spisCharacteristics.getState().value?.let {
                                it.find { it.sort > item.sort }?.let {
                                    println(it)
                                    MainDB.addAvatar.setSortCharacteristic(item, it.sort)
                                }
                            }
                        }
                        Text(
                            item.name,
                            Modifier.weight(1f).padding(10.dp),
                            style = mainTextStyle // MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                        )
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(start = 5.dp, end = 10.dp).padding(vertical = 0.dp), expandedDropMenu, buttMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        if (item.opis != "") RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 5.dp, end = 10.dp),
                            color = boxOpisStyleState.colorButt
                        ) {
                            item.sver = item.sver.not()
                        }
                        if (item.startStat >= 10L) Text(
                            "${(item.startStat/10).toInt()} + ",
                            Modifier.padding(end = 5.dp).alpha(0.7f),
                            style = startValueText // MyTextStyleParam.style1.copy(fontSize = 16.sp, textAlign = TextAlign.Center)
                        )
                        Text(
                            item.stat.toString(),
                            Modifier.padding(end = 20.dp),
                            style = valueText //MyTextStyleParam.style1.copy(textAlign = TextAlign.Center)
                        )
                        Box(
                            Modifier.padding(end = 20.dp).width(3.dp).height(25.dp)
                                .background(COLOR_INDIK_BACK).border(
                                0.5.dp,
                                COLOR_INDIK_BORDER
                            )
                        ) {
                            Box(
                                Modifier.align(Alignment.BottomCenter).width(3.dp)
                                    .fillMaxHeight(item.progress.toFloat()).background(
                                        COLOR_INDIK_COMPLETE
                                    )
                            )
                        }
                    }
                    if ((item.opis != "")) MyBoxOpisStyle(expandedOpis, item.opis, boxOpisStyleState)
                }
            }
        }
    }
}
