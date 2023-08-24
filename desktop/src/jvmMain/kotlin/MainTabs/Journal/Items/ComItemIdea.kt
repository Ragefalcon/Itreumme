package MainTabs.Journal.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemIdeaStyleState
import extensions.ItemVxodStyleState
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import viewmodel.MainDB

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemIdea(
    item: ItemIdea,
    selection: SingleSelectionType<ItemIdea>,
    selFun: (ItemIdea) -> Unit = {},
    editable: Boolean = true,
    itemIdeaStyleState: ItemIdeaStyleState,
    dialLay: MyDialogLayout? = null,
    dropMenu: @Composable ColumnScope.(ItemIdea, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val colorItem = when (item.stat) {
        0L -> MyColorARGB.colorStatTint_01
        1L -> MyColorARGB.colorStatTint_02
        2L -> MyColorARGB.colorStatTint_03
        3L -> MyColorARGB.colorStatTint_04
        4L -> MyColorARGB.colorStatTint_05
        else -> MyColorARGB.colorStatTint_00
    }.toColor()

    val expandedDropMenu = remember { mutableStateOf(false) }
    val expandedOpis = remember { mutableStateOf(!item.sver) }

    with(itemIdeaStyleState) {
        MyCardStyle1(selection.isActive(item), item.level, {
            selection.selected = item
            selFun(item)
//            expandedDropMenu.value = this.buttons.isSecondaryPressed
        }, onDoubleClick = {
            expandedOpis.value = expandedOpis.value.not()
            item.sver = item.sver.not()
        },
            styleSettings = itemIdeaStyleState,
            dropMenu = { exp -> dropMenu(item, exp) }
        )
        {
            MainDB.complexOpisSpis.spisComplexOpisForIdea.getState().value?.let { mapOpis ->
                Column {

                    Row(verticalAlignment = Alignment.CenterVertically) {

/*
                        if(item.podstapcount > 0)Image(
                            painterResource(if (item.sverChild) "ic_plus.xml" else "ic_minus.xml"),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
                            "statDenPlan",
                            Modifier
                                .height(40.dp)
                                .width(50.dp)
                                .padding(start = 10.dp)
                                .mouseClickable {
                                    item.sverChild = item.sverChild.not()
//                                    sverFun(item)
                                },
                            contentScale = ContentScale.Fit,
//                        filterQuality = FilterQuality.High
                        )
*/
                        Image(
                            painterResource("bookmark_01.svg"), //BitmapPainter(
                            "statIdea",
                            Modifier
                                .height(35.dp)
                                .width(35.dp),
                            colorFilter = ColorFilter.tint(
                                colorItem,
                                BlendMode.Modulate
                            ),
                            contentScale = ContentScale.FillBounds,
                        )
                        Text(
                            modifier = Modifier.padding(0.dp).padding(start = 5.dp).weight(1f),
                            text = item.name,
                            style = mainTextStyle
                        )
                        if (selection.isActive(item) && editable) MyButtDropdownMenuStyle2(
                            Modifier.padding(start = 10.dp, end = 10.dp).padding(vertical = 5.dp),
                            expandedDropMenu,
                            buttMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        mapOpis[item.id.toLong()]?.let {
                            RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 0.dp, end = 10.dp),
                                color = boxOpisStyleState.colorButt
                            ) {
                                item.sver = item.sver.not()
                            }
                        }
                    }
                    mapOpis[item.id.toLong()]?.let { listOpis ->
                        if (listOpis.isNotEmpty()) MyBoxOpisStyle(expandedOpis, listOpis, dialLay, MainDB.styleParam.journalParam.complexOpisForIdea)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemIdeaPlate(
    item: ItemIdea,
    sverFun: (ItemIdea) -> Unit = {},
    itemIdeaStyleState: ItemIdeaStyleState,
    onClick: () -> Unit
) {

    val expandedOpis = remember { mutableStateOf(!item.sver) }

    with(itemIdeaStyleState) {
        MyCardStyle1(
            false, 0,
            {
                onClick()
            },
            styleSettings = itemIdeaStyleState,
        )
        {
            val colorItem = when (item.stat) {
                0L -> MyColorARGB.colorStatTint_01
                1L -> MyColorARGB.colorStatTint_02
                2L -> MyColorARGB.colorStatTint_03
                3L -> MyColorARGB.colorStatTint_04
                4L -> MyColorARGB.colorStatTint_05
                else -> MyColorARGB.colorStatTint_00
            }.toColor()
            RowVA {

                if (item.podstapcount > 0) Image(
                    painterResource(if (item.sverChild) "ic_plus.xml" else "ic_minus.xml"),//useResource("ic_stat_00.png", ::loadImageBitmap), //BitmapPainter(
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
                Image(
                    painterResource("bookmark_01.svg"), //BitmapPainter(
                    "statIdea",
                    Modifier
                        .height(35.dp)
                        .width(35.dp),
                    colorFilter = ColorFilter.tint(
                        colorItem,
                        BlendMode.Modulate
                    ),
                    contentScale = ContentScale.FillBounds,
                )
                Text(
                    modifier = Modifier.padding(0.dp).padding(start = 15.dp, end = 10.dp).weight(1f),
                    text = item.name,
                    style = mainTextStyle
                )
            }
        }
    }
}

