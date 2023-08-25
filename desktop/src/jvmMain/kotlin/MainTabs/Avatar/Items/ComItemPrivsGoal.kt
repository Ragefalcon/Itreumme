package MainTabs.Avatar.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.RowVA
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemPrivsGoal

class ComItemPrivsGoal(
    val item: ItemPrivsGoal,
    val selection: SingleSelectionType<ItemPrivsGoal>,
    val selFun: (ItemPrivsGoal) -> Unit = {},
    val editable: Boolean = true,
    val dropMenu: @Composable ColumnScope.(ItemPrivsGoal, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = mutableStateOf(false)
    val progressGotov = mutableStateOf((item.gotov / 100f).toFloat())
    val expandedOpis = mutableStateOf(!item.sver)
    val text_sum_hour = mutableStateOf("${item.hour.roundToStringProb(1)} ч.")

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
            selFun(item)
        },
            dropMenu = { exp -> dropMenu(item, exp) },
            onDoubleClick = {
                expandedOpis.value = !expandedOpis.value
                item.sver = item.sver.not()
            }
        ) {
            Column {

                RowVA(Modifier.padding(3.dp).padding(end = 5.dp)) {
                    Image(
                        bitmap = useResource("ic_stat_00.png", ::loadImageBitmap),
                        "statPrivsPlan",
                        Modifier
                            .height(50.dp)
                            .width(60.dp)
                            .padding(start = 10.dp)
                            .mouseClickable {
                                if (editable) {
                                    selFun(item)
                                    selection.selected = item
                                }
                            },
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
                        filterQuality = FilterQuality.High
                    )

                    if (item.name != item.namePlan) Text(
                        item.namePlan,
                        Modifier
                            .padding(horizontal = 5.dp)
                            .border(
                                width = 0.5.dp,
                                brush = Brush.horizontalGradient(listOf(Color.Yellow, Color.Yellow)),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(2.dp)
                            .padding(horizontal = 2.dp),
                        style = MyTextStyleParam.style5.copy(color = Color.Yellow)
                    )
                    Text(
                        modifier = Modifier.padding(0.dp).weight(1f),
                        text = item.name,
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                    )
                    Column(
                        modifier = Modifier
                            .padding(top = 3.dp, bottom = 3.dp, end = 13.dp)
                            .width(170.dp)
                    ) {
                        RowVA(
                            modifier = Modifier.fillMaxWidth().height(30.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 0.dp),
                                text = text_sum_hour.value,
                                style = TextStyle(color = Color(0xFFFFF7F9)),
                                fontSize = 15.sp
                            )
                            if (editable && selection.isActive(item)) MyButtDropdownMenuStyle2(
                                Modifier.padding(vertical = 0.dp),
                                expandedDropMenu
                            ) {
                                dropMenu(item, expandedDropMenu)
                            }
                            if (item.opis != "") RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 10.dp, end = 10.dp)
                            ) {
                                item.sver = item.sver.not()
                            }
                        }
                        LinearProgressIndicator(
                            progress = (item.gotov / 100f).toFloat(),
                            modifier = Modifier.padding(vertical = 5.dp),
                            Color.Green,
                            Color(0x6FFF8888)
                        )

                    }
                }
                if ((item.opis != "")) {
                    BoxExpand(
                        expandedOpis,
                        Modifier.myModWithBound1(),
                        Modifier.fillMaxWidth()
                    ) {
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
