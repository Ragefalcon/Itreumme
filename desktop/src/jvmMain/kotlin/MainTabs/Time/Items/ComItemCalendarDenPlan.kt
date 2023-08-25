package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import common.MyDropdownMenu
import common.MySelectStat
import common.MyShadowBox
import common.SingleSelection
import extensions.*
import org.jetbrains.skia.ImageFilter
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan

@Composable
fun ComItemCalendarDenPlan(
    item: ItemDenPlan,
    selection: SingleSelection,
    style: ItemCalendarDenPlanStyleState,
    dialLay: () -> MyDialogLayout
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val expandedDropMenu = remember { mutableStateOf(false) }
    MyShadowBox(style.plateItem.shadow) {
        MyDropdownMenu(expandedDropMenu, style.denPlanStyle.dropdown) {
            DropdownMenuDenPlan(item, expandedDropMenu, dialLay(), style = style.denPlanStyle, calendar = true)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .hoverable(interactionSource = interactionSource)
                .then(style.outer_padding)
                .mouseDoubleClick(onClick = {
                    selection.selected = item
                }, onDoubleClick = {}, rightClick = {
                    expandedDropMenu.value = true
                }
                )
        ) {
            Box(
                Modifier
                    .matchParentSize()
                    .clip(style.plateItem.shape)
                    .graphicsLayer {
                        renderEffect = ImageFilter.makeColorFilter(
                            ColorFilter.tint(
                                (MySelectStat.statNaborPlan.toMap()[item.vajn]
                                    ?: MyColorARGB.colorStatTimeSquareTint_00.toColor())
                                    .copy(0.2f),
                                BlendMode.Color
                            ).asSkiaColorFilter(), null, null
                        ).asComposeRenderEffect()
                    }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .run {
                            if (selection.isActive(item)) this.border(
                                style.plateItem.BORDER_WIDTH,
                                style.border_brush_select,
                                style.plateItem.shape
                            ) else this
                        }
                        .withSimplePlate(style.plateItem)
                ) {
                }
            }
            RowVA(
                Modifier
                    .then(style.inner_padding)
            ) {
                Box(
                    Modifier
                        .padding(end = 5.dp)
                        .width(3.dp)
                        .height(20.dp)
                        .background(style.color_indik_back)
                        .border(0.5.dp, style.color_indik_border)
                ) {
                    Box(
                        Modifier.align(Alignment.BottomCenter).width(3.dp)
                            .fillMaxHeight(item.gotov.toFloat() / 100)
                            .background(style.color_indik_complete)
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                ) {
                    style.mainText.let { textStyle ->
                        (textStyle.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
                            Text(
                                item.name, Modifier
                                    .offset {
                                        if (isHovered) IntOffset(
                                            -offset.x.dp.toPx().toInt(),
                                            -offset.y.dp.toPx().toInt()
                                        ) else IntOffset.Zero
                                    },
                                style = if (isHovered) style.mainTextHover else textStyle
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = "${item.time1.subSequence(0, 5)} - ${
                            item.time2.subSequence(
                                0,
                                5
                            )
                        }",
                        style = style.texthour
                    )
                }
                MySelectStat.statNaborPlan.getIcon(
                    "bookmark_01.svg",
                    item.vajn,
                    20.dp,
                    Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun tmp() {
    Box(Modifier.width(100.dp).height(50.dp).background(Color.Red)) {
    }
}