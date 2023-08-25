package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import common.MyDropdownMenu
import common.MyShadowBox
import common.SingleSelection
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

@Composable
fun ComItemCalendarNapom(
    item: ItemNapom,
    selection: SingleSelection,
    dialLay: () -> MyDialogLayout
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val expandedDropMenu = remember { mutableStateOf(false) }
    with(MainDB.styleParam.timeParam.denPlanTab.calendarPanel.itemCalendarNapom) {
        MyShadowBox(plateItem.shadow.getValue()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .hoverable(interactionSource = interactionSource)
                    .paddingStyle(outer_padding)
                    .run {
                        if (selection.isActive(item)) this.border(
                            plateItem.BORDER_WIDTH.getValue().dp,
                            BORDER_BRUSH_SELECT.getValue(),
                            plateItem.SHAPE.getValue()
                        ) else this
                    }
                    .withSimplePlate(
                        SimplePlateWithShadowStyleState(
                            plateItem,
                            BACKGROUND = if (item.gotov) BACKGROUND_BRUSH_GOTOV.getValue() else plateItem.BACKGROUND.getValue(),
                            BORDER = if (item.gotov) BORDER_BRUSH_GOTOV.getValue() else plateItem.BORDER.getValue()
                        )
                    )
                    .mouseDoubleClick(onClick = {
                        selection.selected = item
                    }, onDoubleClick = {}, rightClick = {
                        selection.selected = item
                        expandedDropMenu.value = true
                    }
                    )
                    .paddingStyle(inner_padding)
            ) {
                mainText.getValue().let { textStyle ->
                    (textStyle.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
                        Text(item.name, Modifier
                            .offset {
                                if (isHovered) IntOffset(
                                    -offset.x.dp.toPx().toInt(),
                                    -offset.y.dp.toPx().toInt()
                                ) else IntOffset.Zero
                            },
                            style = textStyle.copy(
                                shadow = textStyle.shadow?.copy(
                                    offset = if (isHovered) Offset(offset.x * 2, offset.y * 2) else offset,
                                    blurRadius = if (isHovered) textStyle.shadow?.blurRadius?.let { it * 1.5f }
                                        ?: 4f else textStyle.shadow?.blurRadius ?: 2f
                                )
                            )
                        )
                    }
                }
                MyDropdownMenu(
                    expandedDropMenu,
                    ItemNapomStyleState(MainDB.styleParam.timeParam.denPlanTab.itemNapom).dropdown
                ) {
                    DropdownMenuNapom(item, expandedDropMenu, dialLay(), calendar = true)
                }
            }
        }
    }
}