package MainTabs.Avatar.Items

import MainTabs.Time.Elements.ShkalTime
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyComplexOpisView
import common.MyTextStyleParam
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import viewmodel.MainDB


@Composable
fun ComItemDenPlanOfChronicle(
    item: ItemDenPlan,
    first: Boolean = false,
    dialLay: MyDialogLayout,
) {
    Column(Modifier.fillMaxWidth(0.8f).padding(vertical = 0.dp)) {
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
                                MyColorARGB.colorMyBorderStroke.toColor(),
                                Color.Transparent
                            )
                        )
                    )
            )
            if (item.vajn == -1L) {
                Image(
                    painterResource("ic_baseline_nights_stay_24.xml"),
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
                    bitmap = useResource("ic_stat_00.png", ::loadImageBitmap),
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
            RowVA(
                Modifier.padding(start = 40.dp, top = 5.dp).padding(vertical = 2.dp),
            ) {
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = "${item.time1.subSequence(0, 5)} - ${
                        item.time2.subSequence(
                            0,
                            5
                        )
                    }",
                    style = MyTextStyleParam.style2.copy(
                        color = Color.Cyan.toMyColorARGB().plusDark().plusDark().toColor()
                            .copy(alpha = 0.6f), fontSize = 13.sp
                    )
                )
                Text(
                    text = item.name,
                    style = MyTextStyleParam.style2.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyColorARGB.colorDoxodItem0.toColor()
                    ),
                    modifier = Modifier.padding(start = 15.dp).weight(1f)
                )
                with(LocalDensity.current) {
                    ShkalTime(
                        item,
                        Modifier.padding(start = 10.dp, end = 40.dp).height(11.dp).width(88.dp),
                        this,
                    )
                }
            }
        }
        MainDB.complexOpisSpis.spisComplexOpisForDenPlanInBestDays.getState().value?.let { mapOpis ->
            mapOpis[item.id.toLong()]?.let { listOpis ->
                if (listOpis.isNotEmpty()) with(ComplexOpisStyleState(MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan)) {
                    SelectionContainer {
                        MyComplexOpisView(
                            modifier = Modifier
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