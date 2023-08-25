package MainTabs.Time.Items

import MainTabs.Time.Elements.ShkalSrok
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import common.MeasureUnconstrainedViewHeight
import common.MyCardStyle1
import common.PlateOrderLayout
import common.SingleSelectionType
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemSrokPlanAndStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB

@Composable
fun ComItemTimeline(
    item: ItemSrokPlanAndStap,
    selection: SingleSelectionType<ItemSrokPlanAndStap>,
    dateCurr: Long,
    year: Boolean
) {

    with(MainDB.styleParam.timeParam.timelineCommonParam) {
        val planStyle = remember { ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan) }
        val stapStyle = remember { ItemPlanStapStyleState(MainDB.styleParam.timeParam.planTab.itemPlanStap) }
        val commonStyle = CommonItemStyleState(
            MainDB.styleParam.timeParam.planTab.run { if (item.stap_id == 0L) itemPlan else itemPlanStap },
            paddingInner = Modifier.paddingStyle(inner_padding),
            paddingOuter = Modifier.paddingStyle(outer_padding),
            shapeCard = (if (item.stap_id == 0L) cornerPlan else cornerStap).getValue()
        )

        @Composable
        fun cardItem() {
            MyCardStyle1(
                selection.isActive(item), 0,
                onClick = {
                    selection.selected = item
                },
                onDoubleClick = {
                },
                backBrush = if (item.stap_id == 0L) when (TypeStatPlan.getType(item.stat)) {
                    TypeStatPlan.COMPLETE -> planStyle.background_brush_gotov
                    TypeStatPlan.CLOSE -> planStyle.background_brush_close
                    TypeStatPlan.FREEZE -> planStyle.background_brush_freeze
                    TypeStatPlan.UNBLOCKNOW -> planStyle.background_brush_unblock
                    else -> null
                } else when (TypeStatPlanStap.getType(item.stat)) {
                    TypeStatPlanStap.COMPLETE -> stapStyle.background_brush_gotov
                    TypeStatPlanStap.CLOSE -> stapStyle.background_brush_close
                    TypeStatPlanStap.FREEZE -> stapStyle.background_brush_freeze
                    TypeStatPlanStap.UNBLOCKNOW -> stapStyle.background_brush_unblock
                    else -> null
                },
                borderBrush = if (item.stap_id == 0L) when (TypeStatPlan.getType(item.stat)) {
                    TypeStatPlan.COMPLETE -> planStyle.border_brush_gotov
                    TypeStatPlan.CLOSE -> planStyle.border_brush_close
                    TypeStatPlan.FREEZE -> planStyle.border_brush_freeze
                    TypeStatPlan.UNBLOCKNOW -> planStyle.border_brush_unblock
                    else -> null
                } else when (TypeStatPlanStap.getType(item.stat)) {
                    TypeStatPlanStap.COMPLETE -> stapStyle.border_brush_gotov
                    TypeStatPlanStap.CLOSE -> stapStyle.border_brush_close
                    TypeStatPlanStap.FREEZE -> stapStyle.border_brush_freeze
                    TypeStatPlanStap.UNBLOCKNOW -> stapStyle.border_brush_unblock
                    else -> null
                },
                modifierThen = if (!item.quest || (item.namePlan.isEmpty() && item.stap_id > 0L)) Modifier else Modifier.border(
                    width = planStyle.BORDER_WIDTH_QUEST,
                    brush = planStyle.border_quest,
                    shape = commonStyle.shapeCard
                ),
                modifier = Modifier
                    .run {
                        if ((item.stap_id == 0L && TypeStatPlan.getType(item.stat) == TypeStatPlan.FREEZE) || (item.stap_id > 0L && TypeStatPlanStap.getType(
                                item.stat
                            ) == TypeStatPlanStap.FREEZE)
                        ) this.alpha(0.25f) else this
                    }
                    .width(250.dp).padding(start = 5.dp),
                styleSettings = commonStyle,
            ) {
                PlateOrderLayout(Modifier.fillMaxWidth()) {
                    if (item.stap_id > 0 && item.namePlan.isNotEmpty()) Text(
                        text = "[${item.namePlan}]",
                        modifier = Modifier.padding(end = 5.dp),
                        style = planStyle.mainTextStyle.copy(
                            fontSize = commonStyle.mainTextStyle.fontSize
                        )
                    )
                    Text(
                        text = item.name,

                        style = commonStyle.mainTextStyle
                    )
                    Text(
                        text = "(${item.listDate.size})",

                        style = commonStyle.mainTextStyle.copy(color = Color.Green)
                    )
                }
            }
        }
        MeasureUnconstrainedViewHeight({ cardItem() }) { heightItem ->
            Box(Modifier.fillMaxWidth()) {
                RowVA {
                    Spacer(Modifier.width(260.dp))
                    ShkalSrok(
                        TimelineDiagramColorsState(timelineColors),
                        item, Modifier
                            .height(heightItem)
                            .fillMaxWidth()
                            .clip(RectangleShape), dateOpor = dateCurr,
                        year = year
                    )
                }

                cardItem()
                Column(Modifier.fillMaxWidth().padding(vertical = 0.dp)) {
                    Box(
                        Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        timelineColors.COLOR_RAMK.getValue().toColor()
                                    )
                                )
                            )
                    )
                    Spacer(
                        Modifier.height(heightItem - 2.dp)
                            .fillMaxWidth()
                    )
                    Box(
                        Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        timelineColors.COLOR_RAMK.getValue().toColor()
                                    )
                                )
                            )
                    )
                }
                Box(
                    Modifier.padding(start = 260.dp).matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                0.0f to timelineColors.COLOR_VIGNETTE.getValue().toColor(),
                                0.2f to Color.Transparent,
                                0.8f to Color.Transparent,
                                1.0f to timelineColors.COLOR_VIGNETTE.getValue().toColor()
                            )
                        )
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier.fillMaxHeight().width(1.dp)
                                .background(timelineColors.COLOR_RAMK.getValue().toColor())
                        )
                        Spacer(Modifier.weight(1f))
                        Box(
                            Modifier.fillMaxHeight().width(1.dp)
                                .background(timelineColors.COLOR_RAMK.getValue().toColor())
                        )
                    }
                }
            }
        }
    }
}