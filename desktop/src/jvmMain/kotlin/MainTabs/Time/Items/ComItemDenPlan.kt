package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import viewmodel.MainDB
import java.util.*


/**
 * Похоже с учетом того что в этих айтемах происходит взаимодействие со слайдером и меняется содержимое, причем без
 * подтягивания обновления из БД, то при переделывании этих айтемов в функции все идет как то не очень корректно...
 * Возможно можно настроить все это и в формате функции, но т.к. в формате класса все работает нормально, я решил
 * ничего не трогать, т.к. пока не вижу принципиальных различий, разве что нужно помнить во время использования, что
 * это не функция, а класс.
 * */
class ComItemDenPlan(
    val item: ItemDenPlan,
    val selection: SingleSelection,
    val changeGotov: ((ItemDenPlan, Float) -> Unit)? = null,
    val edit: Boolean = true,
    val itemDenPlanStyleState: ItemDenPlanStyleState,
    val dialLay: MyDialogLayout?,
    val dropMenu: @Composable ColumnScope.(ItemDenPlan, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)
    val text_sum_hour = mutableStateOf(Date().fromHourFloat(item.sum_hour.toFloat()).humanizeTime())
    val progressGotov = mutableStateOf((item.gotov / 100f).toFloat())

    @OptIn(ExperimentalComposeUiApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        val expandedOpis = mutableStateOf(!item.sver)
        with(itemDenPlanStyleState) {
            MyCardStyle1(edit && selection.isActive(item), 0, {
                selection.selected = item
            }, {
                MainDB.timeSpis.spisDenPlan.sverOpisElem(item)
//                    .updateElem(item,item.copy(sver = item.sver.not()))
//                MainDB.timeFun.sverDenPlan(item)
                expandedOpis.value = !expandedOpis.value
            }, modifierThen = Modifier, dropMenu = { exp ->
                dropMenu(item, exp)
            },
                styleSettings = itemDenPlanStyleState
            ) {
                MainDB.complexOpisSpis.spisComplexOpisForDenPlan.getState().value?.let { mapOpis ->
                    Column {

                        Row(
                            Modifier.padding(start = 10.dp).padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (item.vajn == -1L) {
                                Image(
                                    painterResource("ic_baseline_nights_stay_24.xml"), //BitmapPainter(
                                    "statDenPlan",
                                    Modifier
                                        .height(40.dp)
                                        .width(40.dp),
                                    colorFilter = ColorFilter.tint(
                                        MyColorARGB.colorSleep.toColor(),
                                        BlendMode.Modulate
                                    ),
                                    contentScale = ContentScale.FillBounds,
                                )
                            } else {
                                Image(
                                    painterResource("bookmark_01.svg"), //BitmapPainter(
//                                bitmap = useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                                    "statDenPlan",
                                    Modifier
//                                    .height(35.dp)
//                                    .width(35.dp),
                                        .height(40.dp)
                                        .width(40.dp),
                                    colorFilter = ColorFilter.tint(
                                        when (item.vajn.toInt()) {
                                            0 -> Color(0xFFFFF42B)
                                            1 -> Color(0xFFFFFFFF)
                                            2 -> Color(0xFF7FFAF6)
                                            3 -> Color(0xFFFF5858)
                                            else -> Color(0xFFFFF42B)
                                        },
                                        BlendMode.Modulate
                                    ),
                                    contentScale = ContentScale.FillBounds,
//                                filterQuality = FilterQuality.High
                                )
                            }
                            Column(
                                modifier = Modifier.padding(bottom = 0.dp, start = 5.dp).padding(end = 10.dp).weight(1f)
                            ) {
                                RowVA {

                                    Column(Modifier.padding(0.dp)) {
                                        Text(
                                            text = item.name,
                                            style = mainTextStyle
                                        )
                                        Text(
                                            modifier = Modifier.padding(0.dp),
                                            text = "${item.time1.subSequence(0, 5)} - ${
                                                item.time2.subSequence(
                                                    0,
                                                    5
                                                )
                                            }",
                                            style = timeTextStyle
                                        )
                                    }
                                    if (item.nameprpl != "") {
                                        val str =
                                            "${item.nameprpl}${if (item.namestap != "") " -> [${item.namestap}]" else ""}"
                                        MyShadowBox(
                                            shadow_priv_plan,
                                            Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                str,
                                                Modifier
                                                    .padding(horizontal = 10.dp)
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
                                    } else {
                                        Spacer(Modifier.weight(1f))
                                    }
                                    Column(modifier = Modifier.width(170.dp)) {

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(start = 3.dp).padding(vertical = 4.dp),
                                                text = text_sum_hour.value,
                                                style = countTextStyle
                                            )
                                            Row(
                                                Modifier.height(25.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                if (edit && selection.isActive(item)) MyButtDropdownMenuStyle2(
                                                    Modifier.padding(top = 0.dp).padding(vertical = 0.dp),
                                                    expandedDropMenu,
                                                    buttMenu,
                                                    itemDenPlanStyleState.dropdown
                                                ) { //setDissFun ->
                                                    dropMenu(item, expandedDropMenu)
                                                }
                                                mapOpis[item.id.toLong()]?.let {
                                                    RotationButtStyle1(
                                                        expandedOpis,
                                                        Modifier.padding(start = 10.dp, end = 10.dp),
                                                        color = boxOpisStyleState.colorButt
                                                    ) {
                                                        MainDB.timeSpis.spisDenPlan.sverOpisElem(item)
//                                                            .updateElem(item,item.copy(sver = item.sver.not()))
//                                                        MainDB.timeFun.sverDenPlan(item)
                                                    }
                                                }
                                            }
                                        }
                                        RowVA(Modifier.height(20.dp)) {
                                            if (edit && selection.isActive(item)) {
                                                Slider(
                                                    value = progressGotov.value,
                                                    modifier = Modifier.height(20.dp).fillMaxWidth(),
                                                    onValueChange = {
                                                        item.setGotov(it.toDouble() * 100){ hour, gotov, exp ->
                                                            MainDB.timeSpis.spisDenPlan.updateElem(item, item.copy(sum_hour = hour, gotov = gotov))
                                                        }
                                                        text_sum_hour.value =
                                                            Date().fromHourFloat(item.sum_hour.toFloat()).humanizeTime()
                                                        progressGotov.value = it
                                                    },
                                                    onValueChangeFinished = {
                                                        changeGotov?.invoke(item, progressGotov.value)
                                                    },
                                                    colors = getMySliderColor(sliderColor2, sliderColor1)
                                                    /*SliderDefaults.colors(
                                                    thumbColor = colorVyp,
                                                    disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                                                        .compositeOver(MaterialTheme.colors.surface),
                                                    activeTrackColor = colorVyp,
                                                    inactiveTrackColor = sliderColor1,
                                                    disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha),
                                                    disabledInactiveTrackColor = MaterialTheme.colors.onSurface.copy(
                                                        alpha = SliderDefaults.DisabledInactiveTrackAlpha
                                                    ),
                                                    activeTickColor = contentColorFor(Color.Blue).copy(alpha = TickAlpha),
                                                    inactiveTickColor = Color.Magenta.copy(alpha = TickAlpha),
                                                    disabledActiveTickColor = contentColorFor(colorVyp).copy(alpha = TickAlpha)
                                                        .copy(alpha = DisabledTickAlpha),
                                                    disabledInactiveTickColor = MaterialTheme.colors.onSurface.copy(
                                                        alpha = SliderDefaults.DisabledInactiveTrackAlpha
                                                    )
                                                        .copy(alpha = DisabledTickAlpha)
                                                )*/
                                                )
                                            } else {
                                                LinearProgressIndicator(
                                                    progress = (item.gotov / 100f).toFloat(),
                                                    modifier = Modifier.padding(bottom = 4.dp),
                                                    sliderColor2,
                                                    sliderColor1
                                                )
                                            }
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
                                MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan
                            )
/*
                            MyBoxOpisStyle(
                                expandedOpis,
                                item.opis,
                                boxOpisStyleState
                            )
*/
                        }
                    }
                }
            }
        }
    }
}