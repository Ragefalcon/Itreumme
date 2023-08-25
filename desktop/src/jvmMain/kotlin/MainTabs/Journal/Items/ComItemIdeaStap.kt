package MainTabs.Journal.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemIdeaStapStyleState
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisTextCommon
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import viewmodel.MainDB

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemIdeaStap(
    item: ItemIdeaStap,
    selection: SingleSelectionType<ItemIdeaStap>,
    selFun: (ItemIdeaStap) -> Unit = {},
    openFun: (ItemIdeaStap) -> Unit = {},
    editable: Boolean = true,
    itemIdeaStapStyleState: ItemIdeaStapStyleState,
    dialLay: MyDialogLayout? = null,
    dropMenu: @Composable ColumnScope.(ItemIdeaStap, MutableState<Boolean>) -> Unit = { _, _ -> }
) {

    val expandedDropMenu = remember { mutableStateOf(false) }
    val expandedOpis = remember { mutableStateOf(!item.sver) }

    with(itemIdeaStapStyleState) {
        MainDB.complexOpisSpis.spisComplexOpisForIdeaStap.getState().value?.let { mapOpis ->
            val smallOpis =
                mapOpis[item.id.toLong()]?.let { it.sumOf { if (it is ItemComplexOpisTextCommon) it.text.length else 0 } < 700 || it.size > 30 }
                    ?: true
            MyCardStyle1(
                (editable && selection.isActive(item)), onClick = {
                    selection.selected = item
                    if (editable) selFun(item)
                    if (!editable) openFun(item)

                }, onDoubleClick = {
                    if (smallOpis) {
                        expandedOpis.value = expandedOpis.value.not()
                        item.sver = item.sver.not()
                    } else {

                        openFun(item)
                    }
                },
                dropMenu = { exp -> dropMenu(item, exp) },
                styleSettings = itemIdeaStapStyleState
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
                Column {
                    RowVA {
                        Image(
                            painterResource("bookmark_06.svg"),
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
                            if (editable && smallOpis) RotationButtStyle1(
                                expandedOpis,
                                Modifier.padding(start = 0.dp, end = 10.dp),
                                color = boxOpisStyleState.colorButt
                            ) {
                                item.sver = item.sver.not()
                            }
                            if (editable) MyTextButtSimpleStyle(
                                "\uD83D\uDD6E",
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .padding(end = 0.dp),
                                fontSize = 24.sp,
                                color = buttOpenColor
                            ) {
                                selection.selected = item
                                openFun(item)
                            }
                        }
                    }
                    mapOpis[item.id.toLong()]?.let { listOpis ->
                        if (listOpis.isNotEmpty() && editable && smallOpis) MyBoxOpisStyle(
                            expandedOpis,
                            listOpis,
                            dialLay,
                            MainDB.styleParam.journalParam.complexOpisForIdeaStap
                        )
                    }
                }
            }
        }
    }
}



