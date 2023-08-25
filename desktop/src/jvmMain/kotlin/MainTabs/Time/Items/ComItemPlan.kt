package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.MainDB
import java.util.*

class ComItemPlan(
    val item: ItemPlan,
    val selection: SingleSelectionType<ItemPlan>,
    val selFun: (ItemPlan) -> Unit = {},
    val onDoubleClick: () -> Unit = {},
    val changeGotov: ((ItemPlan, Float) -> Unit)? = null,
    val editable: Boolean = true,
    val itemPlanStyleState: ItemPlanStyleState,
    val dialLay: MyDialogLayout? = null,
    val sortEnable: Boolean = false,
    val dropMenu: (@Composable ColumnScope.(ItemPlan, MutableState<Boolean>, (() -> Unit) -> Unit) -> Unit)? = null
) {


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        val expandedDropMenu = remember { mutableStateOf(false) }
        val expandedOpis = mutableStateOf(!item.sver)
        val progressGotov = remember { mutableStateOf((item.gotov / 100f).toFloat()) }
        val remScope = rememberCoroutineScope()
        with(itemPlanStyleState) {
            MyCardStyle1(
                selection.isActive(item),
                0,
                {
                    if (item.stat == TypeStatPlan.UNBLOCKNOW) MainDB.timeFun.hideUnblockNowSignalPlan(item)
                    selection.selected = item
                    selFun(item)
                },
                {
                    onDoubleClick()
                },
                backBrush = when (item.stat) {
                    TypeStatPlan.COMPLETE -> background_brush_gotov
                    TypeStatPlan.CLOSE -> if (item.direction) background_brush_direction_close else background_brush_close
                    TypeStatPlan.FREEZE -> if (item.direction) background_brush_direction_freeze else background_brush_freeze
                    TypeStatPlan.UNBLOCKNOW -> background_brush_unblock
                    else -> if (item.direction) background_brush_direction else null
                },
                borderBrush = when (item.stat) {
                    TypeStatPlan.COMPLETE -> border_brush_gotov
                    TypeStatPlan.CLOSE -> if (item.direction) border_brush_direction_close else border_brush_close
                    TypeStatPlan.FREEZE -> if (item.direction) border_brush_direction_freeze else border_brush_freeze
                    TypeStatPlan.UNBLOCKNOW -> border_brush_unblock
                    else -> if (item.direction) border_brush_direction else null
                },
                modifierThen = if (item.namequest == "") Modifier else Modifier.border(
                    width = BORDER_WIDTH_QUEST,
                    brush = border_quest,
                    shape = shapeCard
                ),
                modifier = if (item.stat == TypeStatPlan.FREEZE) Modifier.alpha(0.25f) else Modifier,
                dropMenu = dropMenu?.let { { exp -> it(item, exp, {}) } },
                styleSettings = itemPlanStyleState
            ) {
                Column {
                    if (item.namequest != "") {
                        MyShadowBox(quest_plate.shadow) {
                            RowVA(
                                Modifier.fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                                    .withSimplePlate(quest_plate)
                                    .padding(3.dp)
                                    .padding(horizontal = 2.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    item.namequest,
                                    Modifier.weight(1f, false),
                                    style = questNameText
                                )
                            }
                        }
                    }

                    MainDB.complexOpisSpis.spisComplexOpisForPlan.getState().value?.let { mapOpis ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box {
                                Image(
                                    painterResource("bookmark_01.svg"),
                                    "statDenPlan",
                                    Modifier
                                        .padding(horizontal = 9.dp, vertical = 6.dp)
                                        .height(44.dp)
                                        .width(44.dp)

                                        .mouseClickable {
                                            if (editable) {
                                                selFun(item)
                                                selection.selected = item
                                                onDoubleClick()
                                            }
                                        },
                                    colorFilter = ColorFilter.tint(
                                        when (item.vajn.toInt()) {
                                            0 -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                            1 -> MyColorARGB.colorStatTimeSquareTint_01.toColor()
                                            2 -> MyColorARGB.colorStatTimeSquareTint_02.toColor()
                                            3 -> MyColorARGB.colorStatTimeSquareTint_03.toColor()
                                            else -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                        },
                                        BlendMode.Modulate
                                    ),
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                            Column(
                                Modifier
                                    .padding(
                                        top = if (item.namequest == "") 5.dp else 0.dp,
                                        start = 3.dp,
                                        bottom = 5.dp,
                                        end = 8.dp
                                    )
                                    .weight(1f)
                            ) {
                                RowVA {
                                    if (sortEnable) {
                                        MyTextButtWithoutBorder(
                                            "\uD83E\uDC45",
                                            Modifier.padding(start = 0.dp),
                                            fontSize = 24.sp,
                                        ) {
                                            MainDB.timeSpis.spisPlan.getState().let {
                                                it.filter { if (item.stat == TypeStatPlan.FREEZE) it.stat == TypeStatPlan.FREEZE else it.stat != TypeStatPlan.FREEZE }
                                                    .findLast { it.sort > item.sort }?.let {
                                                        MainDB.timeSpis.spisPlan.updateElem(
                                                            item,
                                                            item.copy(sort = it.sort)
                                                        )
                                                        MainDB.timeSpis.spisPlan.updateElem(
                                                            it,
                                                            it.copy(sort = item.sort)
                                                        ) { itt -> itt.sort }
                                                        remScope.run {
                                                            MainDB.addTime.setSortPlan(item, it.sort)
                                                        }
                                                    }
                                            }
                                        }
                                        MyTextButtWithoutBorder(
                                            "\uD83E\uDC47",
                                            Modifier.padding(horizontal = 10.dp),
                                            fontSize = 24.sp,

                                            ) {
                                            MainDB.timeSpis.spisPlan.getState().let {
                                                it.filter { if (item.stat == TypeStatPlan.FREEZE) it.stat == TypeStatPlan.FREEZE else it.stat != TypeStatPlan.FREEZE }
                                                    .find { it.sort < item.sort }?.let {
                                                        MainDB.timeSpis.spisPlan.updateElem(
                                                            item,
                                                            item.copy(sort = it.sort)
                                                        )
                                                        MainDB.timeSpis.spisPlan.updateElem(
                                                            it,
                                                            it.copy(sort = item.sort)
                                                        ) { itt -> itt.sort }
                                                        remScope.run {
                                                            MainDB.addTime.setSortPlan(item, it.sort)
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.padding(0.dp),
                                        style = mainTextStyle
                                    )
                                    if (item.countstap > 0) Text(
                                        text = "(${item.open_countstap}/${item.countstap})",
                                        modifier = Modifier.padding(start = 3.dp),
                                        style = countStapText
                                    )
                                }
                                if (item.data1 > 1L && item.data2 > 1L) RowVA {
                                    Text(
                                        text = Date(item.data1).format("dd.MM.yyyy"),
                                        style = dataText
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        text = Date(item.data2).format("dd.MM.yyyy"),
                                        style = dataText
                                    )
                                }
                                privSchetPlanInfo(
                                    item.summa,
                                    item.min_aim,
                                    item.max_aim,
                                    item.summaRasxod,
                                    item.schplOpen
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(top = if (item.namequest == "") 3.dp else 0.dp, bottom = 3.dp, end = 13.dp)
                                    .width(170.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().height(25.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (item.hour > 0) Text(
                                        modifier = Modifier.padding(start = 0.dp),
                                        text = "${item.hour.roundToStringProb(1)} ч.",
                                        style = hourTextStyle
                                    ) else Spacer(Modifier.width(1.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        if (editable && selection.isActive(item)) MyButtDropdownMenuStyle2(
                                            Modifier.padding(top = 0.dp).padding(vertical = 0.dp),
                                            expandedDropMenu,
                                            buttMenu,
                                            dropdown
                                        ) {
                                            dropMenu?.let { it(item, expandedDropMenu, {}) }
                                        }

                                        mapOpis[item.id.toLong()]?.let {
                                            if (it.isNotEmpty()) RotationButtStyle1(
                                                expandedOpis,
                                                Modifier.padding(start = 10.dp, end = 10.dp),
                                                color = boxOpisStyleState.colorButt
                                            ) {
                                                MainDB.timeSpis.spisPlan.sverOpisElem(item)
                                            }
                                        }
                                    }
                                }
                                @Composable
                                fun sliderProg() {
                                    if (item.gotov >= 0) RowVA(Modifier.padding(top = 2.dp).height(20.dp)) {
                                        if (selection.isActive(item) && editable) {
                                            Slider(
                                                value = progressGotov.value,
                                                modifier = Modifier.height(20.dp).fillMaxWidth(),
                                                onValueChange = {
                                                    progressGotov.value = it
                                                },
                                                onValueChangeFinished = {
                                                    changeGotov?.invoke(item, progressGotov.value)
                                                },
                                                colors = getMySliderColor(sliderThumb, sliderInactive)
                                            )
                                        } else {
                                            LinearProgressIndicator(
                                                progress = (item.gotov / 100f).toFloat(),
                                                modifier = Modifier.padding(8.dp),
                                                sliderThumb,
                                                sliderInactive
                                            )
                                        }
                                    }
                                }
                                Box(contentAlignment = Alignment.CenterStart) {
                                    if (item.data1 > 1L && item.data2 > 1L) Box(
                                        Modifier.padding(top = 2.dp).height(20.dp).fillMaxWidth()
                                            .withSimplePlate(plateSrok),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Box(
                                            Modifier.height(20.dp).fillMaxWidth(
                                                Date().time.let { now ->
                                                    if (now < item.data1) 0f
                                                    else if (now > item.data2 && item.data2 != item.data1) 1f
                                                    else (now - item.data1).toFloat() / (TimeUnits.DAY.milliseconds + item.data2 - item.data1).toFloat()
                                                }.toFloat()
                                            )
                                                .withSimplePlate(plateSrokIn)
                                        )
                                    }
                                    sliderProg()
                                }
                            }
                        }
                        mapOpis[item.id.toLong()]?.let { listOpis ->
                            if (listOpis.isNotEmpty()) MyBoxOpisStyle(
                                expandedOpis,
                                listOpis,
                                dialLay,
                                MainDB.styleParam.timeParam.planTab.complexOpisForPlan
                            )
                        }
                    }
                }
            }
        }
    }
}


