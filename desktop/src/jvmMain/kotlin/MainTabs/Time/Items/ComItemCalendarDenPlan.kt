package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

@Composable
fun ComItemCalendarDenPlan(
    item: ItemDenPlan,
    selection: SingleSelection,
    style: ItemCalendarDenPlanStyleState,
    dialLay: () -> MyDialogLayout
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
//    var time by remember { mutableStateOf(0L) }
//    var time = 0L
    val expandedDropMenu = remember { mutableStateOf(false) }
        MyShadowBox(style.plateItem.shadow) {
            MyDropdownMenu(expandedDropMenu,style.denPlanStyle.dropdown) {  //setDissFun ->
                DropdownMenuDenPlan(item, expandedDropMenu, dialLay(), style = style.denPlanStyle, calendar = true)
//                time = Date().time - time
//                println("timeDropMenu = $time")
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .hoverable(interactionSource = interactionSource)
                    .then(style.outer_padding)
                    .mouseDoubleClick(onClick = {
                                                selection.selected = item
                    }, onDoubleClick = {}, rightClick = {
//                        time = Date().time
//                        selection.selected = item
                        expandedDropMenu.value = true
                    }
                    )
            ){
                Box(
                    Modifier
                        .matchParentSize()
                        .clip(style.plateItem.shape)
                        .graphicsLayer {
                            renderEffect = ImageFilter.makeColorFilter(
                                ColorFilter.tint(
                                    (MySelectStat.statNaborPlan.toMap()[item.vajn] ?: MyColorARGB.colorStatTimeSquareTint_00.toColor())
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
                RowVA(Modifier
//                    .run {
//                        if (selection.isActive(item)) this.border(
//                            style.plateItem.BORDER_WIDTH,
//                            style.border_brush_select,
//                            style.plateItem.shape
//                        ) else this
//                    }
//                    .withSimplePlate(style.plateItem)
                    .then(style.inner_padding)) {
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
                                .fillMaxHeight(item.gotov.toFloat()/100)
                                .background(style.color_indik_complete)
                        )
                    }
                    Column(
                        Modifier
                            .weight(1f)
//                            .paddingStyle(inner_padding)
                    ) {
                        style.mainText.let { textStyle ->
                            (textStyle.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
                                Text(item.name, Modifier
                                    .offset {
                                        if (isHovered) IntOffset(
                                            -offset.x.dp.toPx().toInt(),
                                            -offset.y.dp.toPx().toInt()
                                        ) else IntOffset.Zero
                                    },
                                    style = if (isHovered) style.mainTextHover else textStyle
/*
                                    textStyle.copy(
                                        shadow = textStyle.shadow?.copy(
                                            offset = if (isHovered) Offset(offset.x * 2, offset.y * 2) else offset,
                                            blurRadius = if (isHovered) textStyle.shadow?.blurRadius?.let { it * 1.5f }
                                                ?: 4f else textStyle.shadow?.blurRadius ?: 2f
                                        )
                                    )
*/
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
                        Modifier//.padding(horizontal = 5.dp)
                    )
                }
            }
        }
}

@Preview
@Composable
fun tmp(){
    Box(Modifier.width(100.dp).height(50.dp).background(Color.Red)){

    }
}