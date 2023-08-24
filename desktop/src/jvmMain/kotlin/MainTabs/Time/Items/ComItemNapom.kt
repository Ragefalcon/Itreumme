package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import viewmodel.MainDB

@Composable
fun  ComItemNapom(
    item: ItemNapom,
    selection: SingleSelection,
    vypFun: (Boolean) -> Unit,
    itemNapomStyleState: ItemNapomStyleState,
    dialLay: MyDialogLayout?,
    dropMenu: @Composable ColumnScope.(ItemNapom, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expandedDropMenu = remember { mutableStateOf(false) }
    val expandedOpis = mutableStateOf(!item.sver)

        with(itemNapomStyleState) {
            MyCardStyle1(
                selection.isActive(item), 0, {
                    selection.selected = item
                }, {
                    MainDB.timeSpis.spisNapom.sverOpisElem(item)
                    expandedOpis.value = !expandedOpis.value
//                    item.sver = item.sver.not()
                },
                backBrush = if (item.gotov) backgroundGotov else null, // Color(0xFF468F45)   else Color(0xFF464D45)
                borderBrush = if (item.gotov) borderGotov else null, // Color(0xFF468F45)   else Color(0xFF464D45)
                styleSettings = itemNapomStyleState, dropMenu = { exp -> dropMenu(item, exp) }
            ) {
                MainDB.complexOpisSpis.spisComplexOpisForNapom.getState().value?.let { mapOpis ->
                    Column {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp).padding(start = 15.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(Modifier.padding(vertical = 2.dp).weight(1f)) {
                                Text(
                                    modifier = Modifier.padding(0.dp),
                                    text = item.name,
                                    style = mainTextStyle
                                )
                                /*
                            Text(
                                modifier = Modifier,
                                text = "${item.time.subSequence(0, 5)}",
                                style = timeTextStyle
                            )
*/
                            }
                            if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                                Modifier, expandedDropMenu,
                                buttMenu,
                                dropdown
                            ) {
                                dropMenu(item, expandedDropMenu)
                            }
                            mapOpis[item.id.toLong()]?.let {
                                RotationButtStyle1( //if (item.opis != "")
                                    expandedOpis,
                                    Modifier.padding(horizontal = 10.dp),
                                    color = boxOpisStyleState.colorButt
                                ) {
                                    MainDB.timeSpis.spisNapom.sverOpisElem(item)
//                                    item.sver = item.sver.not()
                                }
                            }
                            if (selection.isActive(item)) {
//                                MyTextButtStyle2(if (item.gotov) "❮❮❮ ✔ ❯❯❯" else "❮❮❮    ❯❯❯", fontSize = 17.sp,modifier = Modifier.height(30.dp).width(100.dp).padding(start = 10.dp)) {
                                MyTextButtSimpleStyle(
                                    if (item.gotov) "❮❮❮ ✔ ❯❯❯" else "❮❮❮    ❯❯❯",
                                    fontSize = 17.sp,
                                    modifierText = Modifier.height(25.dp).width(90.dp).padding(start = 10.dp)
                                        .align(Alignment.CenterVertically),
                                    color = buttGotovColor
                                ) {
                                    vypFun(item.gotov.not())
                                }
                            } else {
                                Text(
                                    text = if (item.gotov) "❮❮❮ ✔ ❯❯❯" else "❮❮❮    ❯❯❯",// sum_hour.roundToStringProb(1),
                                    modifier = Modifier.height(25.dp).width(90.dp).padding(start = 10.dp)
                                        .align(Alignment.CenterVertically),
                                    style = MyTextStyleParam.style1.copy(
                                        color = buttGotovColor,
                                        fontSize = 17.sp,
                                        shadow = Shadow(
                                            offset = Offset(2f, 2f),
                                            blurRadius = 2f
                                        )
                                    )
                                )
                            }
                        }
                        mapOpis[item.id.toLong()]?.let { listOpis ->
                            if (listOpis.isNotEmpty()) MyBoxOpisStyle(expandedOpis, listOpis, dialLay, MainDB.styleParam.timeParam.denPlanTab.complexOpisForNapom)
                        }
                    }
                }
            }
        }
}
