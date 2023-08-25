package MainTabs.Time.Items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyCardStyle1
import common.PlateOrderLayout
import extensions.CommonItemStyleState
import extensions.ItemPlanStapStyleState
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB

@Composable
fun ComItemPlanStapPlate(
    item: ItemPlanStap,
    modifier: Modifier = Modifier,
    itemPlanStapStyleState: ItemPlanStapStyleState,
    parentName: String = "",
    itemCommonStyle: CommonItemStyleState? = null,
    onClick: (ItemPlanStap) -> Unit = {}
) {
    with(itemPlanStapStyleState) {
        MyCardStyle1(
            false, 0, {
                onClick(item)
            },
            backBrush = when (item.stat) {
                TypeStatPlanStap.COMPLETE -> background_brush_gotov
                TypeStatPlanStap.UNBLOCKNOW -> background_brush_unblock
                else -> null
            },
            borderBrush = when (item.stat) {
                TypeStatPlanStap.COMPLETE -> border_brush_gotov
                TypeStatPlanStap.UNBLOCKNOW -> border_brush_unblock
                else -> null
            },
            modifier = modifier,
            styleSettings = itemCommonStyle ?: itemPlanStapStyleState
        ) {
            RowVA(
                modifier = Modifier.defaultMinSize(minHeight = 30.dp).padding(horizontal = 0.dp).padding(end = 10.dp)
            ) {
                PlateOrderLayout(Modifier.padding(start = 12.dp).weight(1f), alignmentVertRowCenter = true) {
                    if (parentName != "")
                        Text(
                            text = AnnotatedString(
                                text = "[${parentName}] ",
                                spanStyle = MainDB.styleParam.timeParam.boxSelectParentPlanParam.textParentPlan.getValue()
                                    .toSpanStyle()
                            ).plus(
                                AnnotatedString(
                                    text = item.name,
                                )
                            ),
                            style = mainTextStyle
                        )
                    else Text(
                        text = item.name,
                        style = mainTextStyle
                    )
                }
                if (TypeStatPlanStap.getBlockList().contains(item.stat)) Image(
                    painterResource(if (item.stat == TypeStatPlanStap.BLOCK) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"),
                    "statDenPlan",
                    Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .padding(start = 0.dp),
                    alpha = 0.7F,
                    contentScale = ContentScale.FillBounds,
                )
                if (item.marker > 0L) Box {
                    Image(
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
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "${item.hour.roundToStringProb(1)} ч.",
                    style = hourTextStyle.copy(fontSize = 16.sp)
                )
                if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                    modifier = Modifier.padding(top = 0.dp),
                    text = "*",
                    style = noQuestText
                )
            }
        }
    }
}