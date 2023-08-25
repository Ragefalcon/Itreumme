package MainTabs.Finance.Items


import MainTabs.Finance.Element.PanChangeShabRasxod
import MyDialog.MyDialogLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soywiz.korio.lang.substr
import common.*
import extensions.DropDownMenuStyleState
import extensions.ItemRasxDoxOperStyleState
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemShabRasxod
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB

@Composable
fun ComItemRasxodShab(
    item: ItemShabRasxod,
    selection: SingleSelectionType<ItemShabRasxod>,
    doubleClick: (ItemShabRasxod) -> Unit,
    itemRasxDoxStyleState: StyleVMspis.InterfaceState.ItemRasxDoxOperStyle,
    dialL: MyDialogLayout,
    dropMenu: @Composable ColumnScope.(ItemShabRasxod, MutableState<Boolean>) -> Unit = { itemSh, expanded ->
        MyDropdownMenuItem(
            expanded,
            style = DropDownMenuStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.itemRasxodShablon.dropdown),
            "Изменить"
        ) {
            PanChangeShabRasxod(dialL, itemSh)
        }
        MyDropdownMenuItem(
            expanded,
            style = DropDownMenuStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.itemRasxodShablon.dropdown),
            "\uD83E\uDC45 В начало \uD83E\uDC45"
        ) {
            MainDB.finSpis.spisShabRasxod.getState().value?.let {
                it.find { it.sort > itemSh.sort }?.let {
                    MainDB.addFinFun.setSortShabRasxod(itemSh, it.sort)
                }
            }
        }
        MyDropdownMenuItem(
            expanded,
            style = DropDownMenuStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.itemRasxodShablon.dropdown),
            "\uD83E\uDC47 В конец \uD83E\uDC47"
        ) {
            MainDB.finSpis.spisShabRasxod.getState().value?.let {
                it.findLast { it.sort < itemSh.sort }?.let {
                    MainDB.addFinFun.setSortShabRasxod(itemSh, it.sort)
                }
            }
        }
        MyDeleteDropdownMenuButton(expanded) {
            MainDB.addFinFun.delShabRasxod(item.id.toLong())
        }
    }
) {

    with(ItemRasxDoxOperStyleState(itemRasxDoxStyleState)) {
        val expandedDropMenu = remember { mutableStateOf(false) }
        MyCardStyle1(
            selection.isActive(item), 0, {
                selection.selected = item
            },
            onDoubleClick = { doubleClick(item) },
            dropMenu = { exp -> dropMenu(item, exp) },
            styleSettings = this
        ) {
            RowVA(Modifier.defaultMinSize(25.dp, 25.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                RowVA(Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.weight(1f, false),
                        text = item.name,
                        style = mainTextStyle
                    )
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = item.type,
                        style = textType
                    )
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = item.nameSchet,
                        style = textSchet
                    )
                }
                RowVA {
                    if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                        Modifier.padding(start = 10.dp).padding(vertical = 0.dp), expandedDropMenu, buttMenu, dropdown
                    ) {
                        dropMenu(item, expandedDropMenu)
                    }
                    if (item.summa > 0) Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = AnnotatedString(
                            text = item.summa.roundToStringProb(2).let { it.substr(0, it.length - 2) },
                            spanStyle = SpanStyle(fontSize = textSumm.fontSize)
                        ).plus(
                            AnnotatedString(
                                text = item.summa.roundToStringProb(2).let { it.substr(it.length - 2, 2) },
                                spanStyle = SpanStyle(fontSize = textSumm.fontSize * 0.7f)
                            )
                        ),
                        style = textSumm
                    ) else Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "---",
                        style = textSumm
                    )
                    MyTextButtWithoutBorder(
                        "\uD83E\uDC45",
                        Modifier.padding(horizontal = 10.dp),
                        fontSize = 24.sp,
                        textColor = MainDB.styleParam.finParam.rasxodParam.panAddRasxod.ARROW_SORT_COLOR.getValue()
                            .toColor()
                    ) {
                        MainDB.finSpis.spisShabRasxod.getState().value?.let {
                            it.findLast { it.sort > item.sort }?.let {
                                MainDB.addFinFun.setSortShabRasxod(item, it.sort)
                            }
                        }
                    }
                    MyTextButtWithoutBorder(
                        "\uD83E\uDC47",
                        fontSize = 24.sp,
                        textColor = MainDB.styleParam.finParam.rasxodParam.panAddRasxod.ARROW_SORT_COLOR.getValue()
                            .toColor()
                    ) {
                        MainDB.finSpis.spisShabRasxod.getState().value?.let {
                            it.find { it.sort < item.sort }?.let {
                                MainDB.addFinFun.setSortShabRasxod(item, it.sort)
                            }
                        }
                    }
                }
            }
        }
    }
}


