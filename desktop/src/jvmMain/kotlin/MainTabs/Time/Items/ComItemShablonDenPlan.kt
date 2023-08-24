package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemShablonDenPlan
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

@Composable
fun ComItemShablonDenPlan(
    item: ItemShablonDenPlan,
    selection: SingleSelectionType<ItemShablonDenPlan>,
    sort: MutableState<Boolean>,
    onClick: (ItemShablonDenPlan) -> Unit,
    doubleClick: (ItemShablonDenPlan) -> Unit,
    dialLay: MyDialogLayout? = null,
//    dropMenu: @Composable ColumnScope.(ItemShablonDenPlan, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val itemCommonStyle: CommonItemStyleState = CommonItemStyleState(
        MainDB.styleParam.timeParam.denPlanTab.itemDenPlan,
        borderWidth = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.BORDER_WIDTH_next_denplan.getValue().dp,
        shapeCard = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.cornerDenPlan.getValue(),
        paddingInner = Modifier.paddingStyle(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingInnerDenPlan),
        paddingOuter = Modifier.paddingStyle(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOuterDenPlan)
    )
    val expandedDropMenu = mutableStateOf(false)
    val expandedOpis = remember { mutableStateOf(!item.sver) }

    @Composable
    fun dropMenu(itemNA: ItemShablonDenPlan, expanded: MutableState<Boolean>) {
        Text(
            text = item.name,
            modifier = Modifier.padding(bottom = 5.dp),
            style = itemCommonStyle.mainTextStyle.copy(fontSize = 14.sp)
        )
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "Изменить"
        ) {
//            PanAddNextAction(dialL, itemNA)
        }
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "\uD83E\uDC45 В начало \uD83E\uDC45"
        ) {
            MainDB.timeSpis.spisShablonDenPlan.getState().let {
                it.find { it.sort < itemNA.sort }?.let {
                    MainDB.addTime.setSortShabDenPlan(itemNA, it.sort)
                }
            }
        }
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "\uD83E\uDC47 В конец \uD83E\uDC47"
        ) {
            MainDB.timeSpis.spisShablonDenPlan.getState().let {
                it.findLast { it.sort > itemNA.sort }?.let {
                    MainDB.addTime.setSortShabDenPlan(itemNA, it.sort)
                }
            }
        }
        MyDeleteDropdownMenuButton(expanded) {
            MainDB.addTime.delShablonDenPlan(item.id.toLong()) {
                MainDB.complexOpisSpis.spisComplexOpisForShabDenPlan.delAllImageForItem(it)
            }
        }
    }

    with(ItemDenPlanStyleState(MainDB.styleParam.timeParam.denPlanTab.itemDenPlan)) {
        MyCardStyle1(
            selection.isActive(item), 0,
            onClick = {
                selection.selected = item
                onClick(item)
            },
            onDoubleClick = {
                doubleClick(item)
            },
//            modifier = modifier,
            dropMenu = { dropMenu(item, it) },
            styleSettings = itemCommonStyle ?: this
        ) {
            MainDB.complexOpisSpis.spisComplexOpisForShabDenPlan.getState().value?.let { mapOpis ->
                Column {
                    Row(Modifier.padding(top = 0.dp), verticalAlignment = Alignment.CenterVertically) {
                        MySelectStat.statNaborPlan.getIcon("bookmark_01.svg",item.vajn,30.dp,Modifier.padding(horizontal = 5.dp))
                        if (sort.value){
                            MyTextButtWithoutBorder(
                                "\uD83E\uDC45",
                                fontSize = 18.sp,
                                textColor = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.ARROW_SORT_COLOR_next_denplan.getValue()
                                    .toColor()
                            ) {
                                MainDB.timeSpis.spisShablonDenPlan.getState().let {
                                    it.findLast { it.sort < item.sort }?.let {
                                        MainDB.addTime.setSortShabDenPlan(item, it.sort)
                                    }
                                }
                            }
                            MyTextButtWithoutBorder(
                                "\uD83E\uDC47",
                                Modifier.padding(horizontal = 10.dp),
                                fontSize = 18.sp,
                                textColor = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.ARROW_SORT_COLOR_next_denplan.getValue()
                                    .toColor()
                            ) {
                                MainDB.timeSpis.spisShablonDenPlan.getState().let {
                                    it.find { it.sort > item.sort }?.let {
                                        MainDB.addTime.setSortShabDenPlan(item, it.sort)
                                    }
                                }
                            }
                        }
                        with(LocalDensity.current) {
                            PlateOrderLayout(
                                Modifier.weight(1f).padding(3.dp),
                                spaceBetween = 3.dp.toPx().toInt(),
                                alignmentVertRowCenter = true
                            ) {
                                Text(
                                    text = item.name,
                                    style = mainTextStyle
                                )
                                if (item.nameprpl != "") {
                                    val str =
                                        "${item.nameprpl}${if (item.namestap != "") " -> [${item.namestap}]" else ""}"
                                    MyShadowBox(
                                        shadow_priv_plan,
//                                        Modifier.padding(start = 15.dp),//.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            str,
                                            Modifier
                                                .padding(horizontal = 0.dp)
                                                .clip(privPlanShape)
                                                .background(privPlanBackground, privPlanShape)
                                                .border(
                                                    width = privPlanBorderWidth,
                                                    brush = privPlanBorderBrush,
                                                    shape = privPlanShape
                                                )
                                                .padding(2.dp)
                                                .padding(horizontal = 2.dp),
                                            style = privPlanTextStyle.copy(textAlign = TextAlign.Center)
                                        )
                                    }
                                }
                            }
                        }
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(end = 10.dp),
                            expandedDropMenu,
                            buttMenu,
                            dropdown
                        ) { //setDissFun ->
                            dropMenu(item, expandedDropMenu)
                        } else Spacer(Modifier.width(35.dp))
                        mapOpis[item.id.toLong()]?.let {
                            RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 0.dp, end = 10.dp),
                                17.sp,
                                color = boxOpisStyleState.colorButt
                            ) {
                                MainDB.timeSpis.spisShablonDenPlan.sverOpisElem(item)
//                                item.sver = item.sver.not()
                            }
                        }
                    }
                    mapOpis[item.id.toLong()]?.let { listOpis ->
                        if (listOpis.isNotEmpty()) MyBoxOpisStyle(
                            expandedOpis,
                            listOpis,
                            dialLay,
                            ComplexOpisStyleState(
                                MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan,
                                outer_padding = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOpisOuterDenPlan.getValue(
                                    Modifier
                                ),
                                inner_padding = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOpisInnerDenPlan.getValue(
                                    Modifier
                                ),
                                plateView = SimplePlateWithShadowStyleState(
                                    MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan.plateView,
                                    shape = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.cornerOpisDenPlan.getValue()
                                )

                            )
                        )
                    }
                }
            }
        }
    }
}


