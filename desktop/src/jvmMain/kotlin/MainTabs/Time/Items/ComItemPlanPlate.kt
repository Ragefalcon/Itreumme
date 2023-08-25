package MainTabs.Time.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyCardStyle1
import common.MyShadowBox
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan

@Composable
fun ComItemPlanPlate(
    item: ItemPlan,
    modifier: Modifier = Modifier,
    itemPlanStyleState: ItemPlanStyleState,
    itemCommonStyle: CommonItemStyleState? = null,
    onClick: (ItemPlan) -> Unit = {}
) {
    with(itemPlanStyleState) {
        MyCardStyle1(
            false,
            0,
            {
                onClick(item)
            },
            backBrush = when (item.stat) {
                TypeStatPlan.COMPLETE -> background_brush_gotov
                TypeStatPlan.UNBLOCKNOW -> background_brush_unblock
                else -> null
            },
            borderBrush = when (item.stat) {
                TypeStatPlan.COMPLETE -> border_brush_gotov
                TypeStatPlan.UNBLOCKNOW -> border_brush_unblock
                else -> null
            },
            modifier = modifier,
            modifierThen = if (item.namequest == "") Modifier else Modifier.border(
                width = BORDER_WIDTH_QUEST,
                brush = border_quest,
                shape = itemCommonStyle?.shapeCard ?: shapeCard
            ),
            styleSettings = itemCommonStyle ?: itemPlanStyleState
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        Image(
                            painterResource("bookmark_01.svg"),
                            "statDenPlan",
                            Modifier
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                                .padding(start = 10.dp)
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
                        )
                        if (item.stat == TypeStatPlan.BLOCK || item.stat == TypeStatPlan.INVIS) Image(
                            painterResource(if (item.stat == TypeStatPlan.BLOCK) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"),
                            "statDenPlan",
                            Modifier
                                .padding(start = 10.dp)
                                .height(30.dp)
                                .width(30.dp),
                            alpha = 0.7F,
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(start = 5.dp).weight(1f),
                        style = mainTextStyle
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        text = "${item.hour.roundToStringProb(1)} ч.",
                        style = hourTextStyle.copy(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}