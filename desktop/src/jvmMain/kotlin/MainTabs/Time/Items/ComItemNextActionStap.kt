package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemNextAction
import ru.ragefalcon.sharedcode.models.data.ItemNextActionStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB

@Composable
fun ComItemNextActionStap(
    item: ItemNextActionStap,
    selection: SingleSelectionType<ItemNextAction>,
    sort: MutableState<Boolean>,
    onClick: (ItemNextActionStap) -> Unit,
    doubleClick: (ItemNextActionStap,()->Unit) -> Unit,
    dialL: MyDialogLayout
) {

    val itemCommonStyle = CommonItemStyleState(
        MainDB.styleParam.timeParam.planTab.itemPlanStap,
        borderWidth = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.BORDER_WIDTH_next_stap.getValue().dp,
        shapeCard = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.cornerStap.getValue(),
        paddingInner = Modifier.paddingStyle(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingInnerStap),
        paddingOuter = Modifier.paddingStyle(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOuterStap)
    )

    val expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun dropMenu(itemNA: ItemNextActionStap, expanded: MutableState<Boolean>) {
        Text(
            text = item.name,
            modifier = Modifier.padding(bottom = 5.dp),
            style = itemCommonStyle.mainTextStyle.copy(fontSize = 14.sp)
        )
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "Запланировать и убрать из сл. д."
        ) {
            doubleClick(item){
                MainDB.timeSpis.spisAllPlanStap.getState().value?.find { it.id == itemNA.stap_prpl.toString() }?.let {
                    MainDB.addTime.updStatPlanStap(
                        it,
                        TypeStatPlanStap.VISIB
                    )
                }
            }
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
        MyDropdownMenuItem(
            expanded,
            style = itemCommonStyle.dropdown,
            "Убрать из след. д."
        ) {
            MainDB.timeSpis.spisAllPlanStap.getState().value?.find { it.id == itemNA.stap_prpl.toString() }?.let {
                MainDB.addTime.updStatPlanStap(
                    it,
                    TypeStatPlanStap.VISIB
                )
            }
        }
    }

    with(ItemPlanStapStyleState(MainDB.styleParam.timeParam.planTab.itemPlanStap)) {
        MyCardStyle1(
            selection.isActive(item), 0,
            onClick = {
                selection.selected = item
                onClick(item)
            },
            onDoubleClick = {
                doubleClick(item){}
            },
//            modifier = modifier,
            dropMenu = { dropMenu(item, it) },
            styleSettings = itemCommonStyle ?: this
        ) {
            Column {
                MainDB.complexOpisSpis.spisComplexOpisForNextActionStap.getState().value?.get(item.stap_prpl)
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
                            if (sort.value){
                                MyTextButtWithoutBorder(
                                    "\uD83E\uDC45",
                                    fontSize = 18.sp,
                                    textColor = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.ARROW_SORT_COLOR_next_stap.getValue()
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
                                    textColor = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.ARROW_SORT_COLOR_next_stap.getValue()
                                        .toColor()
                                ) {
                                    MainDB.timeSpis.spisNextAction.getState().let {
                                        it.find { it.sort > item.sort }?.let {
                                            MainDB.addTime.setSortNextAction(item, it.sort)
                                        }
                                    }
                                }
                            }
                            Text(
                                text = AnnotatedString(
                                    text = "[${item.namePlan}] ",
                                    spanStyle = viewmodel.MainDB.styleParam.timeParam.boxSelectParentPlanParam.textParentPlan.getValue()
                                        .toSpanStyle() //SpanStyle(fontSize = textSumm.fontSize)
                                ).plus(
                                    AnnotatedString(
                                        text = item.nameStap,
                                    )
                                ),
                                Modifier.padding( end = 5.dp).weight(1f),
                                style = mainTextStyle
                            )
                            if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                                Modifier.padding(end = 5.dp).padding(vertical = 0.dp),
                                expandedDropMenu,
                                buttMenu,
                                dropdown
                            ) { //setDissFun ->
                                dropMenu(item, expandedDropMenu)
                            }  else Spacer(Modifier.width(30.dp))
                            listOpis?.also {  //if (item.opis != "")
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
                                    outer_padding = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOpisOuterStap.getValue(
                                        Modifier
                                    ),
                                    inner_padding = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.paddingOpisInnerStap.getValue(
                                        Modifier
                                    ),
                                    plateView = SimplePlateWithShadowStyleState(
                                        MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan.plateView,
                                        shape = MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.cornerOpisStap.getValue()
                                    )

                                )
                            )
                        }
                    }
            }
        }
    }
}


