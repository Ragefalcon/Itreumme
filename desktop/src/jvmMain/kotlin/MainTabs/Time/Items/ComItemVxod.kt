package MainTabs.Time.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemVxodStyleState
import extensions.format
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import java.util.*

class ComItemVxod(
    val item: ItemVxod,
    val selection: SingleSelection,
    val itemVxodStyleState: ItemVxodStyleState,
    val dialLay: MyDialogLayout?,
    val dropMenu: @Composable ColumnScope.(ItemVxod, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun getComposable() {
        with(itemVxodStyleState) {
            MyCardStyle1(
                selection.isActive(item), 0,
                {
                    selection.selected = item
                },
                {
                    item.sver = item.sver.not()
                    expandedOpis.value = !expandedOpis.value
                },
                styleSettings = itemVxodStyleState,
                dropMenu = { exp -> dropMenu(item, exp) },
            ) {
                viewmodel.MainDB.complexOpisSpis.spisComplexOpisForVxod.getState().value?.let { mapOpis ->
                    Column {
                        Row(
                            androidx.compose.ui.Modifier.padding(start = 15.dp).padding(vertical = 0.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Image(
                                painterResource("bookmark_01.svg"),
                                "statVxod",
                                Modifier.height(33.dp).width(33.dp),
                                colorFilter = ColorFilter.tint(
                                    when (item.stat.toInt()) {
                                        0 -> Color(0xFF2FA61D)
                                        1 -> Color(0xFF7FFAF6)
                                        2 -> Color(0xFFFFF42B)
                                        3 -> Color(0xFFFFA825)
                                        4 -> Color(0xFFFF5858)
                                        else -> Color(0xFFFFF42B)
                                    }, BlendMode.Modulate
                                ),
                                contentScale = ContentScale.FillBounds,
                            )

                            Column(
                                modifier = androidx.compose.ui.Modifier.padding(horizontal = 5.dp).padding(end = 10.dp)
                                    .weight(1f)
                            ) {
                                Row {
                                    Column(androidx.compose.ui.Modifier.padding(0.dp).weight(1f)) {
                                        Text(
                                            modifier = androidx.compose.ui.Modifier.padding(start = 10.dp),
                                            text = item.name,
                                            style = mainTextStyle,

                                            )
                                        Text(
                                            modifier = androidx.compose.ui.Modifier.padding(start = 10.dp),
                                            text = Date(item.data).format("dd.MM.yyyy HH:mm"),
                                            style = dateTextStyle

                                        )
                                    }
                                }
                            }

                            if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                                androidx.compose.ui.Modifier.padding(start = 10.dp).padding(vertical = 0.dp),
                                expandedDropMenu,
                                buttMenu
                            ) {
                                dropMenu(item, expandedDropMenu)
                            }
                            mapOpis[item.id.toLong()]?.let {
                                RotationButtStyle1(
                                    expandedOpis,
                                    modifier = androidx.compose.ui.Modifier.padding(horizontal = 10.dp)
                                        .padding(end = 20.dp),
                                    color = boxOpisStyleState.colorButt
                                ) {
                                    item.sver = item.sver.not()
                                }
                            }
                        }
                        mapOpis[item.id.toLong()]?.let { listOpis ->
                            if (listOpis.isNotEmpty()) MyBoxOpisStyle(
                                expandedOpis,
                                listOpis,
                                dialLay,
                                viewmodel.MainDB.styleParam.timeParam.vxodTab.complexOpisForVxod
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ComItemVxodPlate(
    item: ItemVxod,
) {
    val expandedOpis = remember { mutableStateOf(!item.sver) }

    MyCardStyle1(false, 0, {}, {
        expandedOpis.value = !expandedOpis.value
    }) {
        Column {
            Row(
                Modifier.padding(start = 15.dp).padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    bitmap = useResource("ic_stat_00.png", ::loadImageBitmap),
                    "statVxod",
                    Modifier.height(50.dp).width(50.dp),
                    colorFilter = ColorFilter.tint(
                        when (item.stat.toInt()) {
                            0 -> Color(0xFF2FA61D)
                            1 -> Color(0xFF7FFAF6)
                            2 -> Color(0xFFFFF42B)
                            3 -> Color(0xFFFFA825)
                            4 -> Color(0xFFFF5858)
                            else -> Color(0xFFFFF42B)
                        }, BlendMode.Modulate
                    ),
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.High
                )

                Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                    Row {
                        Column(Modifier.padding(0.dp).weight(1f)) {
                            MyTextStyle1(
                                modifier = Modifier.padding(start = 10.dp),
                                text = item.name,
                            )
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = Date(item.data).format("dd.MM.yyyy HH:mm"),
                                style = TextStyle(color = Color(0xAFFFF7D9)),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
