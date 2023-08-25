package MainTabs.Time.Items

import MainTabs.Time.Elements.PanAddNextAction
import MyDialog.MyDialogLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemNextAction
import ru.ragefalcon.sharedcode.models.data.ItemNextActionCommon
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

@Composable
fun ComItemNextActionCommon(
    item: ItemNextActionCommon,
    selection: SingleSelectionType<ItemNextAction>,
    sort: MutableState<Boolean>,
    onClick: (ItemNextActionCommon) -> Unit,
    doubleClick: (ItemNextActionCommon, () -> Unit) -> Unit,
    dialL: MyDialogLayout
) {
    val itemCommonStyle: CommonItemStyleState = CommonItemStyleState(
        MainDB.styleParam.timeParam.denPlanTab.itemDenPlan,
        borderWidth = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.BORDER_WIDTH_next_denplan.getValue().dp,
        shapeCard = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.cornerDenPlan.getValue(),
        paddingInner = Modifier.paddingStyle(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingInnerDenPlan),
        paddingOuter = Modifier.paddingStyle(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOuterDenPlan)
    )

    val expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun dropMenu(itemNA: ItemNextActionCommon, expanded: MutableState<Boolean>) {
        Text(
            text = item.name,
            modifier = Modifier.padding(bottom = 5.dp),
            style = itemCommonStyle.mainTextStyle.copy(fontSize = 14.sp)
        )
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "Запланировать и удалить"
        ) {
            doubleClick(item) {
                MainDB.addTime.delNextAction(item.common_id) {
                    MainDB.complexOpisSpis.spisComplexOpisForNextActionCommon.delAllImageForItem(it)
                }
            }
        }
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "Изменить"
        ) {
            PanAddNextAction(dialL, itemNA)
        }
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "\uD83E\uDC45 В начало \uD83E\uDC45"
        ) {
            MainDB.timeSpis.spisNextAction.getState().let {
                it.find { it.sort < itemNA.sort }?.let {
                    MainDB.addTime.setSortNextAction(itemNA, it.sort)
                }
            }
        }
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "\uD83E\uDC47 В конец \uD83E\uDC47"
        ) {
            MainDB.timeSpis.spisNextAction.getState().let {
                it.findLast { it.sort > itemNA.sort }?.let {
                    MainDB.addTime.setSortNextAction(itemNA, it.sort)
                }
            }
        }
        MyDeleteDropdownMenuButton(expanded) {
            MainDB.addTime.delNextAction(item.common_id) {
                MainDB.complexOpisSpis.spisComplexOpisForNextActionCommon.delAllImageForItem(it)
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
                doubleClick(item) {}
            },

            dropMenu = { dropMenu(item, it) },
            styleSettings = itemCommonStyle
        ) {
            Column {
                MainDB.complexOpisSpis.spisComplexOpisForNextActionCommon.getState().value?.get(item.common_id)
                    .also { listOpis ->
                        RowVA(
                            modifier = androidx.compose.ui.Modifier.defaultMinSize(minHeight = 30.dp)
                                .padding(horizontal = 0.dp)
                                .padding(end = 10.dp)
                        ) {
                            MySelectStat.statNaborPlan.getIcon(
                                "bookmark_01.svg",
                                item.vajn,
                                30.dp,
                                Modifier.padding(horizontal = 5.dp)
                            )
                            if (sort.value) {
                                MyTextButtWithoutBorder(
                                    "\uD83E\uDC45",
                                    fontSize = 18.sp,
                                    textColor = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.ARROW_SORT_COLOR_next_denplan.getValue()
                                        .toColor()
                                ) {
                                    MainDB.timeSpis.spisNextAction.getState().let {
                                        it.findLast { it.sort < item.sort }?.let {
                                            MainDB.addTime.setSortNextAction(item, it.sort)
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
                                    MainDB.timeSpis.spisNextAction.getState().let {
                                        it.find { it.sort > item.sort }?.let {
                                            MainDB.addTime.setSortNextAction(item, it.sort)
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
                                    if (item.namePlan != "") {
                                        val str =
                                            "${item.namePlan}${if (item.nameStap != "") " -> [${item.nameStap}]" else ""}"
                                        MyShadowBox(
                                            shadow_priv_plan,

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
                                Modifier.padding(top = 0.dp).padding(end = 5.dp),
                                expandedDropMenu,
                                buttMenu,
                                dropdown
                            ) {
                                dropMenu(item, expandedDropMenu)
                            } else Spacer(Modifier.width(30.dp))
                            listOpis?.also {
                                RotationButtStyle1(
                                    expandedOpis,
                                    Modifier.padding(start = 0.dp, end = 0.dp),
                                    17.sp,
                                    color = boxOpisStyleState.colorButt
                                ) {
                                    MainDB.timeSpis.spisNextAction.sverOpisElem(item)
                                }
                            }
                        }
                        listOpis?.also { listOp ->
                            if (listOp.isNotEmpty()) MyBoxOpisStyle(
                                expandedOpis,
                                listOp,
                                dialL,
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


