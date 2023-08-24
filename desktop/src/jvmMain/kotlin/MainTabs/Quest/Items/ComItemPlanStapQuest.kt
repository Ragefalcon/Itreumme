package MainTabs.Quest.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanStapQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import viewmodel.QuestVM

class ComItemPlanStapQuest(
    val item: ItemPlanStapQuest,
    val selection: SingleSelectionType<ItemPlanStapQuest>,
    val selFun: (ItemPlanStapQuest) -> Unit = {},
    val sverFun: (ItemPlanStapQuest) -> Unit = {},
    val editable: Boolean = true,
    val dropMenu: @Composable ColumnScope.(ItemPlanStapQuest, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)

    val expandedOpis = mutableStateOf(!item.sver)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {

        MyCardStyle1(selection.isActive(item), item.level,
            onClick = {
                selection.selected = item
                selFun(item)
            },
            backColor = if (item.statsrok == 10L) Color(0xFF468F45) else Color(0xFF464D45),
            onDoubleClick = {
                expandedOpis.value = expandedOpis.value.not()
                item.sver = item.sver.not()
            },
            dropMenu = { exp ->
                dropMenu(item, exp)
            }
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    if (item.podstapcount > 0) Image(
                        painterResource(if (item.svernut) "ic_plus.xml" else "ic_minus.xml"),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                        "statDenPlan",
                        Modifier
                            .height(40.dp)
                            .width(50.dp)
                            .padding(start = 10.dp)
                            .mouseClickable {
                                sverFun(item)
                            },
                        contentScale = ContentScale.Fit,
//                        filterQuality = FilterQuality.High
                    )
                    if (item.opis != "") RotationButtStyle1(
                        expandedOpis,
                        Modifier.padding(start = 20.dp, end = 10.dp)
                    ) {
                        item.sver = item.sver.not()
                    }
                    Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Column(Modifier.padding(start = 15.dp).weight(1f)) {
                                MyTextStyle1(
                                    modifier = Modifier,//.padding(0.dp).padding(start = 15.dp),
                                    text = item.name,
                                    textAlign = TextAlign.Start
                                )
                                PlateOrderLayout() {
                                    QuestVM.getComItemTriggers(TypeParentOfTrig.PLANSTAP.code, item.id.toLong(), editable = editable)
                                    QuestVM.getTriggerMarkersForTriggerChilds(
                                        TypeStartObjOfTrigger.STARTSTAP.id,
                                        item.id.toLong(),
                                        emptyMarker = (item.commstat == -2L || item.commstat == -3L)
                                    )
                                }
                            }
                            if (selection.isActive(item) && editable) MyButtDropdownMenuStyle1(
                                Modifier.padding(start = 10.dp), expandedDropMenu
                            ) { setDissFun ->
                                dropMenu(item, expandedDropMenu)
                            }
                            if (item.commstat == -2L || item.commstat == -3L) Image(
                                painterResource(if (item.commstat == -2L) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"), //BitmapPainter(
                                "statDenPlan",
                                Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                                    .padding(vertical = 0.dp)
                                    .padding(start = 0.dp),
//                        colorFilter = ColorFilter.tint(
//                            when (item.vajn.toInt()) {
//                                0 -> Color(0xFFFFF42B)
//                                1 -> Color(0xFFFFFFFF)
//                                2 -> Color(0xFF7FFAF6)
//                                3 -> Color(0xFFFF5858)
//                                else -> Color(0xFFFFF42B)
//                            },
//                            BlendMode.Modulate
//                        ),
                                alpha = 0.7F,
                                contentScale = ContentScale.FillBounds,
                            )
                            Column {
                                Text(
                                    text = "${item.hour.roundToStringProb(1)} ч.",
                                    modifier = Modifier.padding(start = 10.dp).align(Alignment.End),
                                    style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                                )
                                Text(
                                    text = "${item.srok} дней",
                                    modifier = Modifier.padding(start = 10.dp).align(Alignment.End),
                                    style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                                )
                            }
                        }

                    }
                }
                if ((item.opis != "")) { //(isActive()) &&  && (expandedOpis.value)
                    BoxExpand(
                        expandedOpis,
                        Modifier.myModWithBound1(),
                        Modifier.fillMaxWidth()
                    ) {  //, endModif = Modifier::withMyBound1
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .padding(start = 10.dp),
                            text = item.opis,
                            style = TextStyle(color = Color(0xFFFFF7D9)),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemPlanStapQuestPlate(
    item: ItemPlanStapQuest,
    modifier: Modifier = Modifier,
    onClick: (ItemPlanStapQuest) -> Unit = {}
) {
    Card(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp).fillMaxWidth().mouseClickable {
            onClick(item)
        }
            .border(
                width = 0.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
                ),
                shape = RoundedCornerShape(15.dp)
            ),
        elevation = 8.dp,
        backgroundColor = Color(0xFF464D45),
        shape = RoundedCornerShape(corner = CornerSize(15.dp))
    ) {
        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Column(Modifier.padding(0.dp).weight(1f)) {
                            MyTextStyle1(
                                modifier = Modifier.padding(start = 15.dp),
                                text = item.name,
                            )
                        }
                        Column {
                            Text(
                                text = "${item.hour.roundToStringProb(1)} ч.",
                                modifier = Modifier.padding(start = 10.dp).align(Alignment.End),
                                style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                            )
                            Text(
                                text = "${item.srok} дней",
                                modifier = Modifier.padding(start = 10.dp).align(Alignment.End),
                                style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}


