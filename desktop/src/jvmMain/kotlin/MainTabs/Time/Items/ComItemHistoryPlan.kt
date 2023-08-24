package MainTabs.Time.Items


import MainTabs.Time.Elements.ShkalTime
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*


@OptIn(ExperimentalComposeUiApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ComItemHistoryPlan(
    item: ItemDenPlan,
    stap: Boolean = false,
    first: Boolean = false,
    dialLay: MyDialogLayout,
) {
    with(MainDB.styleParam.timeParam.planTab.panHistory.itemHistory){
        Column(Modifier.fillMaxWidth(0.8f)) {
            Box(Modifier.fillMaxWidth()) {
                if (!first) Box(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.8f)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    colorRazdelit.getValue().toColor(),
                                    Color.Transparent
                                )
                            )
                        )
                )
/*
            if (item.vajn == -1L) {
                Image(
                    painterResource("ic_baseline_nights_stay_24.xml"), //BitmapPainter(
                    "statDenPlan",
                    Modifier
                        .height(30.dp)
                        .width(30.dp),
                    colorFilter = ColorFilter.tint(
                        MyColorARGB.colorSleep.toColor(),
                        BlendMode.Modulate
                    ),
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                Image(
                    bitmap = useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                    "statDenPlan",
                    Modifier
                        .height(30.dp)
                        .width(30.dp),
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
                    filterQuality = FilterQuality.High
                )
            }
*/
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = Date(item.data).format("MM"),
                    style = textMonth.getValue()
                )
                RowVA(
                    Modifier.padding(start = 45.dp, top = 5.dp).padding(vertical = 2.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = Date(item.data).format("dd.MM.yyyy (EEE)"),
                        style = textDate.getValue()
                    )
                    PlateOrderLayout(Modifier.weight(1f)) {
                        if (item.namestap == "") Text( // && !stap
                            text = item.name,
                            style = textPlanName.getValue(),
                            modifier = Modifier.padding(start = 15.dp).weight(1f)
                        )   else {
                            MyShadowBox(plateStapName.shadow.getValue()){
                                Text(
                                    "[${item.namestap}]",
                                    Modifier
//                                        .padding(2.dp)
                                        .padding(horizontal = 5.dp)
                                        .withSimplePlate(SimplePlateWithShadowStyleState(plateStapName))
/*
                                        .border(
                                            width = 0.5.dp,
                                            brush = Brush.horizontalGradient(
                                                listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                            ),
                                            shape = RoundedCornerShape(5.dp)
                                        )
*/
                                        .padding(4.dp)
                                        .padding(horizontal = 2.dp),
                                    style = textStapName.getValue()
                                )
                            }
                    }
                    }
                    Text(
                        modifier = Modifier.padding(start = 3.dp).padding(vertical = 4.dp),
                        text = Date().fromHourFloat(item.sum_hour.toFloat()).humanizeTime(),
                        style = textHour.getValue()
                    )
                    with(LocalDensity.current) {
                        ShkalTime(
                            item,
                            Modifier.padding(start = 10.dp, end = 40.dp).height(11.dp).width(88.dp),
                            this,
                            colorShkal
/*
                            when (item.vajn.toInt()) {
                                -1 -> MyColorARGB.colorSleep.toColor()
                                0 -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                                1 -> MyColorARGB.colorStatTimeSquareTint_01.toColor()
                                2 -> MyColorARGB.colorStatTimeSquareTint_02.toColor()
                                3 -> MyColorARGB.colorStatTimeSquareTint_03.toColor()
                                else -> MyColorARGB.colorStatTimeSquareTint_00.toColor()
                            }
*/
                        )
                    }
                }
            }
            MainDB.complexOpisSpis.run { if (stap) spisComplexOpisForHistoryStapPlan else spisComplexOpisForHistoryPlan }
                .getState().value?.let { mapOpis ->
                    mapOpis[item.id.toLong()]?.let { listOpis ->
                        if (listOpis.isNotEmpty()) with(ComplexOpisStyleState(MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan)) {
                            SelectionContainer {
                                MyComplexOpisView(
                                    modifier = Modifier //outer_padding
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                        .padding(horizontal = 40.dp)
                                        .then(inner_padding),
                                    listOpis, dialLay, this
                                )
                            }
                        }
                    }
                }
        }
    }
}
