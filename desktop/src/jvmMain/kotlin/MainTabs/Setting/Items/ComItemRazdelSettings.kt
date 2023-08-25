package MainTabs.Setting.Items


import MainTabs.Setting.Element.PanSelectShablonStyle
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.source.disk.getState
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MyTypeCorner
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet
import viewmodel.MainDB
import java.util.*

class ComItemRazdelSettings(
    val item: CommonInterfaceSetting.RazdelSetting,
    val dialLay: MyDialogLayout,
    val selection: SingleSelectionType<CommonInterfaceSetting.InterfaceSettingsType>,
    val selectionRazdel: SingleSelectionType<CommonInterfaceSetting.RazdelSetting>,
    val selFun: (CommonInterfaceSetting.InterfaceSettingsType) -> Unit

) {
    var expandedDropMenu = mutableStateOf(false)

    @Composable
    fun getComposable() {
        Box(
            Modifier.padding(2.dp).padding(start = 5.dp).background(item.color.toColor())
                .border(1.dp, if (selectionRazdel.isActive(item)) Color.Red.copy(0.7f) else Color.White.copy(0.7f))
                .padding(bottom = 2.dp)
                .animateContentSize()
        ) {

            Column(modifier = Modifier) {
                item.svernut.getState().value?.let { sver ->
                    RowVA(Modifier.run {
                        if (item.type_razdel != TypeSaveStyleSet.NOTSAVE)
                            this.clickable {
                                selection.selected = null
                                selectionRazdel.selected = item
                            }
                        else this
                    }) {
                        Image(
                            painterResource(if (sver) "ic_plus.xml" else "ic_minus.xml"),
                            "statDenPlan",
                            Modifier
                                .height(25.dp)
                                .width(35.dp)
                                .padding(start = 10.dp)
                                .clickable {
                                    item.svernut.switch()
                                },
                            contentScale = ContentScale.Fit,
                        )
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f),
                            text = item.nameRazdel,
                            style = MyTextStyleParam.style2.copy(fontSize = 14.sp)
                        )
                        if (item.type_razdel != TypeSaveStyleSet.NOTSAVE) {
                            Image(
                                painterResource("ic_baseline_save_24.xml"),
                                "saveRazdel",
                                Modifier
                                    .padding(horizontal = 10.dp)
                                    .height(25.dp)
                                    .width(25.dp)
                                    .clickable {
                                        MyOneVopros(
                                            dialPan = dialLay,
                                            "Введите название для нового шаблона стиля.",
                                            "Добавить",
                                            otvetDefault = "${item.nameRazdel}_${Date().time}"
                                        ) {
                                            if (it != "" && item.type_razdel != TypeSaveStyleSet.NOTSAVE) {
                                                if (item.type_razdel != TypeSaveStyleSet.FULL)
                                                    MainDB.addEditStyle.addSaveSetStyleCommon(
                                                        it,
                                                        item.type_razdel,
                                                        item.getListInterfaceSettings()
                                                    )
                                                else
                                                    MainDB.addEditStyle.addSaveSetStyleFull(
                                                        it
                                                    )
                                            }
                                        }
                                    },
                                contentScale = ContentScale.Fit,
                            )
                            Image(
                                painterResource("ic_baseline_cloud_upload_24.xml"),
                                "loadRazdel",
                                Modifier
                                    .padding(horizontal = 10.dp)
                                    .height(25.dp)
                                    .width(25.dp)
                                    .clickable {
                                        PanSelectShablonStyle(dialLay, item.type_razdel) {
                                            MainDB.addEditStyle.loadSaveSetStyleCommon(
                                                set_id = it.id,
                                                item.code_name_razdel
                                            )
                                        }
                                    },
                                contentScale = ContentScale.Fit,
                            )

                        }
                    }
                    if (!sver) {
                        item.forEach {
                            Box(Modifier
                                .padding(horizontal = 5.dp)
                                .padding(bottom = 2.dp)
                                .background(it.color.toColor(), RoundedCornerShape(5.dp))
                                .border(
                                    1.dp,
                                    if (selection.isActive(it)) Color.Red.copy(0.4f) else Color.White.copy(0.4f),
                                    RoundedCornerShape(5.dp)
                                )
                                .run {
                                    if (it is CommonInterfaceSetting.InterfaceSettingsBoolean || it is CommonInterfaceSetting.InterfaceSettingsTypeCorner) this else this.clickable {
                                        selectionRazdel.selected = null
                                        selection.selected = it
                                        selFun(it)
                                    }
                                }
                                .padding(2.dp)
                                .padding(horizontal = 8.dp)
                            ) {
                                when (it) {
                                    is CommonInterfaceSetting.InterfaceSettingsBoolean -> ComItemInterfaceSettingsBoolean(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsDoublePozitive -> ComItemInterfaceSettingsDoublePoz(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsDouble -> ComItemInterfaceSettingsDouble(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsLong -> ComItemInterfaceSettingsLong(it)
                                    is CommonInterfaceSetting.InterfaceSettingsFontWeight -> ComItemInterfaceSettingsFontWeight(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsAngle -> ComItemInterfaceSettingsAndle(it)
                                    is CommonInterfaceSetting.InterfaceSettingsTypeCorner -> ComItemInterfaceSettingsTypeCorner(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsMyColor -> ComItemInterfaceSettingsMyColor(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsMyColorGradient -> ComItemInterfaceSettingsMyColorGradient(
                                        it
                                    )

                                    is CommonInterfaceSetting.InterfaceSettingsString -> ComItemInterfaceSettingsString(
                                        it
                                    )

                                    else -> {}
                                }
                            }
                        }
                        item.list2.forEach {
                            ComItemRazdelSettings(it, dialLay, selection, selectionRazdel, selFun).getComposable()
                        }
                    }
                }
            }
        }
    }
}

val fontSs = 15.sp

@Composable
fun ComItemInterfaceSettingsBoolean(item: CommonInterfaceSetting.InterfaceSettingsBoolean) {
    RowVA {
        getSettName(item, Modifier.weight(1f))
        MyCheckbox(item.itrObj)
    }
}

@Composable
fun ComItemInterfaceSettingsTypeCorner(item: CommonInterfaceSetting.InterfaceSettingsTypeCorner) {
    val seekBar = remember {
        EnumDiskretSeekBar(MyTypeCorner::class, item.getValue()) {
            item.itrObj.value = it
        }
    }
    Column(Modifier.fillMaxWidth()) {
        getSettName(item, Modifier)
        seekBar.show(Modifier.padding(2.dp), style = MainDB.styleParam.timeParam.seekBarStyle, thin = true)
    }
}

@Composable
fun ComItemInterfaceSettingsDoublePoz(item: CommonInterfaceSetting.InterfaceSettingsDoublePozitive) {
    RowVA {
        getSettName(item, Modifier.weight(1f))
        Text(
            modifier = Modifier,
            text = item.itrObj.value.roundToString(1),
            style = MyTextStyleParam.style3.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = fontSs
            )
        )

    }
}

@Composable
fun ComItemInterfaceSettingsDouble(item: CommonInterfaceSetting.InterfaceSettingsDouble) {
    RowVA {
        getSettName(item, Modifier.weight(1f))
        Text(
            modifier = Modifier,
            text = item.itrObj.value.roundToString(1),
            style = MyTextStyleParam.style3.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = fontSs
            )
        )

    }
}

@Composable
fun ComItemInterfaceSettingsAndle(item: CommonInterfaceSetting.InterfaceSettingsAngle) {
    RowVA {
        getSettName(item, Modifier.weight(1f))
        Text(
            modifier = Modifier,
            text = item.itrObj.value.toString(),
            style = MyTextStyleParam.style3.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = fontSs
            )
        )

    }
}

@Composable
fun ComItemInterfaceSettingsFontWeight(item: CommonInterfaceSetting.InterfaceSettingsFontWeight) {
    RowVA {
        getSettName(item, Modifier.weight(1f))
        Text(
            modifier = Modifier,
            text = item.itrObj.value.toString(),
            style = MyTextStyleParam.style3.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = fontSs
            )
        )

    }
}

@Composable
fun ComItemInterfaceSettingsLong(item: CommonInterfaceSetting.InterfaceSettingsLong) {

}

@Composable
fun ComItemInterfaceSettingsMyColor(item: CommonInterfaceSetting.InterfaceSettingsMyColor) {
    RowVA {
        getSettName(item, Modifier.weight(1f))
        Box(
            Modifier.height(20.dp).width(60.dp).background(item.itrObj.value.toColor())
                .border(1.dp, Color.Black)
        )

    }
}

@Composable
fun ComItemInterfaceSettingsMyColorGradient(item: CommonInterfaceSetting.InterfaceSettingsMyColorGradient) {
    RowVA {
        getSettName(item, Modifier)
        PlateOrderLayout(alignmentEnd = true) {
            item.itrObj.value.forEach {
                Box(
                    Modifier.padding(horizontal = 3.dp).height(20.dp).width(20.dp).border(1.dp, Color.Black)
                ) {
                    Box(Modifier.height(10.dp).width(10.dp).background(Color.White).align(Alignment.TopStart))
                    Box(Modifier.height(10.dp).width(10.dp).background(Color.White).align(Alignment.BottomEnd))
                    Box(Modifier.height(10.dp).width(10.dp).background(Color.Black).align(Alignment.TopEnd))
                    Box(Modifier.height(10.dp).width(10.dp).background(Color.Black).align(Alignment.BottomStart))
                    Box(Modifier.height(20.dp).width(20.dp).background(it.toColor()))
                }
            }
        }
    }
}

@Composable
fun ComItemInterfaceSettingsString(item: CommonInterfaceSetting.InterfaceSettingsString) {

}

@Composable
fun getSettName(item: CommonInterfaceSetting.InterfaceSettingsType, modifier: Modifier) {
    Text(
        modifier = modifier.padding(vertical = 2.dp),
        text = if (item.nameSett != "") item.nameSett else item.code,
        style = MyTextStyleParam.style3.copy(
            fontWeight = FontWeight.Normal,
            fontSize = fontSs
        )
    )
}