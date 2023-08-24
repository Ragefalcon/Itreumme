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
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import kotlinx.coroutines.async
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB
import java.util.*

class ComItemStapPlan(
    val item: ItemPlanStap,
    val selection: SingleSelectionType<ItemPlanStap>,
    val selFun: (ItemPlanStap) -> Unit = {},
    val sverFun: (ItemPlanStap) -> Unit = {},
    val changeGotov: ((ItemPlanStap, Float) -> Unit)? = null,
    val editable: Boolean = true,
    val itemPlanStapStyleState: ItemPlanStapStyleState,
    val dialLay: MyDialogLayout? = null,
    val onDoubleClick: ((ItemPlanStap) -> Unit)? = null,
    val sortEnable: Boolean = false,
    val dropMenu: (@Composable ColumnScope.(ItemPlanStap, MutableState<Boolean>) -> Unit)?  = null
) {
    val progressGotov = mutableStateOf((item.gotov / 100f).toFloat())

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        val expandedDropMenu = remember { mutableStateOf(false) }
//        val progressGotov = remember { mutableStateOf((item.gotov / 100f).toFloat()) }
        val expandedOpis = mutableStateOf(!item.sver)
        val remScope = rememberCoroutineScope()
        with(itemPlanStapStyleState) {
            MyCardStyle1(
                selection.isActive(item), item.level, {
                    if (item.stat == TypeStatPlanStap.UNBLOCKNOW) MainDB.timeFun.hideUnblockNowSignalPlanStap(item)
                    selection.selected = item
                    selFun(item)
                },
                {
//                item.sver = item.sver.not()
                    onDoubleClick?.invoke(item) ?: run {
                        MainDB.timeSpis.spisPlanStap.sverOpisElem(item)
//                            .updateElem(item, item.copy(sver = item.sver.not()))
//                        MainDB.timeFun.sverStapPlan(item.copy(sver = item.sver.not()))
                        expandedOpis.value = !expandedOpis.value
                    }
                },
                backBrush = when (item.stat) {
                    TypeStatPlanStap.COMPLETE -> background_brush_gotov
                    TypeStatPlanStap.CLOSE -> background_brush_close
                    TypeStatPlanStap.FREEZE -> background_brush_freeze
                    TypeStatPlanStap.UNBLOCKNOW -> background_brush_unblock
                    else -> null
                },
                borderBrush = when (item.stat) {
                    TypeStatPlanStap.COMPLETE -> border_brush_gotov
                    TypeStatPlanStap.CLOSE -> border_brush_close
                    TypeStatPlanStap.FREEZE -> border_brush_freeze
                    TypeStatPlanStap.UNBLOCKNOW -> border_brush_unblock
                    else -> null
                },
                modifier = if (item.stat == TypeStatPlanStap.FREEZE) Modifier.alpha(0.25f) else Modifier,
                dropMenu = dropMenu?.let{ { exp -> it(item, exp) } },
                styleSettings = itemPlanStapStyleState,
                levelValue = levelValue
            ) {
                Column {
                    MainDB.complexOpisSpis.spisComplexOpisForStapPlan.getState().value?.let { mapOpis ->

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.podstapcount > 0) Image(
                                painterResource(if (item.svernut) "ic_plus.xml" else "ic_minus.xml"),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                                "statDenPlan",
                                Modifier
                                    .height(25.dp)
                                    .width(35.dp)
                                    .padding(start = 10.dp)
                                    .mouseClickable {
                                        sverFun(item)
                                    },
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(
                                    when (item.svernut) {
                                        true -> plus_color
                                        false -> minus_color
                                    },
                                    BlendMode.Modulate
                                ),
                            )

                            Box(
                                modifier = Modifier
                                    .padding(start = 3.dp, bottom = 3.dp, end = 13.dp)
                                    .weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Column(Modifier.padding(end = 5.dp).weight(1f)) {
                                        RowVA {
                                            if (sortEnable) {
                                                MyTextButtWithoutBorder(
                                                    "\uD83E\uDC45",
                                                    Modifier.padding(start = 5.dp),
                                                    fontSize = 18.sp,
//                                    textColor = arrow_color
                                                ) {
                                                    MainDB.timeSpis.spisPlanStap.getState().let {
                                                        it.findLast { it.sort < item.sort && it.parent_id == item.parent_id }
                                                            ?.let { itFind ->
                                                                    MainDB.addTime.setSortPlanStap(item, itFind.sort)
                                                            }
                                                    }
                                                }
                                                MyTextButtWithoutBorder(
                                                    "\uD83E\uDC47",
                                                    Modifier.padding(horizontal = 10.dp),
                                                    fontSize = 18.sp,
//                                    textColor = arrow_color
                                                ) {
                                                    MainDB.timeSpis.spisPlanStap.getState().let {
                                                        it.find { it.sort > item.sort && it.parent_id == item.parent_id }
                                                            ?.let { itFind ->
                                                                    MainDB.addTime.setSortPlanStap(item, itFind.sort)
                                                            }
                                                    }
                                                }
                                            }
                                            Text(
                                                text = item.name,
                                                modifier = Modifier.weight(1f, false)
                                                    .padding(start = if (item.podstapcount > 0 || sortEnable) 3.dp else 10.dp)
                                                    .padding(vertical = if (item.data1 > 1L && item.data2 > 1L) 3.dp else 8.dp),
                                                style = mainTextStyle
                                            )
                                            if (item.podstapcount > 0) Text(
                                                text = "(${item.podstapcount})",
                                                modifier = Modifier.padding(vertical = if (item.data1 > 1L && item.data2 > 1L) 3.dp else 8.dp)
                                                    .padding(start = 3.dp),
                                                style = countStapText
                                            )
                                        }
/*
                                        Text(
                                            modifier = Modifier.padding(start = if (item.podstapcount > 0 || sortEnable) 3.dp else 10.dp),
                                            text = item.sortCTE,
                                            style = dataText.copy(color = Color.Yellow)
                                        )
*/
                                        if (item.data1 > 1L && item.data2 > 1L) RowVA {
                                            Text(
                                                modifier = Modifier.padding(start = if (item.podstapcount > 0 || sortEnable) 3.dp else 10.dp),
                                                text = Date(item.data1).format("dd.MM.yyyy"),
                                                style = dataText
                                            )
                                            Spacer(Modifier.weight(1f))
                                            Text(
                                                modifier = Modifier.padding(start = 0.dp),
                                                text = Date(item.data2).format("dd.MM.yyyy"), // HH:mm
                                                style = dataText //TextStyle(color = Color(0xAFFFF7D9)),
//                                                fontSize = 10.sp
                                            )
                                        }
                                        privSchetPlanInfo(
                                            item.summa,
                                            item.min_aim,
                                            item.max_aim,
                                            item.summaRasxod,
                                            item.schplOpen,
                                            modifier = Modifier.padding(
                                                start = if (item.podstapcount > 0) 3.dp else 10.dp,
                                                end = 5.dp
                                            )
                                        )
                                    }
                                    if (item.marker > 0L) Image(
                                            painterResource("bookmark_01.svg"),
                                            "statDenPlan",
                                            Modifier
                                                .padding(horizontal = 9.dp, vertical = 0.dp)
                                                .height(30.dp)
                                                .width(30.dp),
                                            colorFilter = ColorFilter.tint(
                                                when (item.marker.toInt()) {
                                                    1 -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                                    2 -> MyColorARGB.colorStatTimeSquareTint_01.toColor()
                                                    3 -> MyColorARGB.colorStatTimeSquareTint_02.toColor()
                                                    4 -> MyColorARGB.colorStatTimeSquareTint_03.toColor()
                                                    else -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                                },
                                                BlendMode.Modulate
                                            ),
                                            contentScale = ContentScale.FillBounds,
                                        )
                                    if (item.stat == TypeStatPlanStap.NEXTACTION) Image(
                                            painterResource("ic_round_keyboard_double_arrow_up_24.xml"),
                                            "statDenPlan",
                                            Modifier
                                                .padding(horizontal = 9.dp, vertical = 0.dp)
                                                .height(30.dp)
                                                .width(30.dp),
                                            colorFilter = ColorFilter.tint(
                                                when (item.marker.toInt()) {
                                                    1 -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                                    2 -> MyColorARGB.colorStatTimeSquareTint_01.toColor()
                                                    3 -> MyColorARGB.colorStatTimeSquareTint_02.toColor()
                                                    4 -> MyColorARGB.colorStatTimeSquareTint_03.toColor()
                                                    else -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                                },
                                                BlendMode.Modulate
                                            ),
                                            contentScale = ContentScale.FillBounds,
                                        )
                                    Column(modifier = Modifier.width(170.dp)) {

                                        Row(
                                            modifier = Modifier.fillMaxWidth().height(25.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            if (item.hour > 0) Text(
                                                modifier = Modifier.padding(top = 0.dp),
                                                text = "${item.hour.roundToStringProb(1)} ч.",
                                                style = hourTextStyle
                                            )   else Spacer(Modifier.width(1.dp))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                if (editable && selection.isActive(item)) MyButtDropdownMenuStyle2(
                                                    Modifier.padding(top = 1.dp).padding(vertical = 0.dp),
                                                    expandedDropMenu,
                                                    buttMenu,
                                                    dropdown
                                                ) {
                                                    println("DropButtonMenu stap plan")
                                                    dropMenu?.let{ it(item, expandedDropMenu) }
                                                }
                                                mapOpis[item.id.toLong()]?.let {  //if (item.opis != "")
                                                    RotationButtStyle1(
                                                        expandedOpis,
                                                        Modifier.padding(start = 10.dp, end = 10.dp),
                                                        17.sp,
                                                        color = boxOpisStyleState.colorButt
                                                    ) {
//                                            item.sver = item.sver.not()
                                                        MainDB.timeSpis.spisPlanStap.sverOpisElem(item)
/*
                                                            .updateElem(
                                                            item,
                                                            item.copy(sver = item.sver.not())
                                                        )
                                                        MainDB.timeFun.sverStapPlan(item.copy(sver = item.sver.not()))
*/
                                                    }
                                                }
                                                if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                                                    text = "*",
                                                    style = noQuestText
                                                )
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
                                                        modifier = Modifier.padding(8.dp),//.padding(vertical = 8.dp),
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

                            }
                        }
                        mapOpis[item.id.toLong()]?.let { listOpis ->
                            if (listOpis.isNotEmpty()) MyBoxOpisStyle(
                                expandedOpis,
                                listOpis,
                                dialLay,
                                MainDB.styleParam.timeParam.planTab.complexOpisForPlanStap
                            )
                        }
//                    if ((item.opis != "")) {
//                        MyBoxOpisStyle(expandedOpis,item.opis,boxOpisStyleState)
//                    }
                    }
                }
            }
        }
    }
}


