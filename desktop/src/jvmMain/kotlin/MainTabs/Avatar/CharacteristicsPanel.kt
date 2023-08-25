package MainTabs.Avatar

import MainTabs.Avatar.Element.PanAddCharacteristic
import MainTabs.Avatar.Element.PanPrivsGoal
import MainTabs.Avatar.Items.ComItemCharacteristics
import MainTabs.Avatar.Items.ComItemCharacteristicsEdit
import MyDialog.MyDialogLayout
import MyDialog.MyFullScreenPanel
import MyList
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemCharacteristic
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

@Composable
fun myBackground2(modifier: Modifier) {
    BoxWithConstraints(
        modifier
            .background(
                Color.Black.copy(0.3f),

                ),
        contentAlignment = Alignment.Center
    ) {

        Box(Modifier.padding(top = 0.dp).height(170.dp).fillMaxWidth().align(Alignment.TopCenter)) {
            Box(
                with(LocalDensity.current) {
                    Modifier
                        .matchParentSize()
                        .alpha(0.4f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MyColorARGB.colorMyBorderStroke.toColor(),

                                    Color.Transparent
                                ),
                                radius = this@BoxWithConstraints.maxWidth.toPx() / 1.17f,
                                center = Offset(
                                    this@BoxWithConstraints.maxWidth.toPx() / 2f,
                                    -this@BoxWithConstraints.maxWidth.toPx() / 1.5f
                                )
                            ),
                        )
                }
            )
        }


        Box(Modifier.padding(bottom = 0.dp).height(170.dp).fillMaxWidth().align(Alignment.BottomCenter)) {
            Box(
                with(LocalDensity.current) {
                    Modifier
                        .matchParentSize()
                        .alpha(0.4f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MyColorARGB.colorMyBorderStroke.toColor(),

                                    Color.Transparent
                                ),
                                radius = this@BoxWithConstraints.maxWidth.toPx() / 1.17f,
                                center = Offset(
                                    this@BoxWithConstraints.maxWidth.toPx() / 2f,
                                    170.dp.toPx() + this@BoxWithConstraints.maxWidth.toPx() / 1.5f
                                )
                            ),
                        )
                }
            )
        }
    }
}

class CharacteristicsPanel {

    private val selection = SingleSelectionType<ItemCharacteristic>()
    val editCharacteristics = mutableStateOf(false)

    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        with(MainDB.styleParam.avatarParam.characteristicsTab) {
            Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                MyShadowBox(plate.shadow.getValue(), Modifier.weight(1f).padding(vertical = 10.dp)) {
                    BoxWithConstraints(
                        Modifier
                            .withSimplePlate(SimplePlateWithShadowStyleState(plate))
                            .border(BORDER2_WIDTH.getValue().dp, BORDER2_BRUSH.getValue(), plate.SHAPE.getValue())
                            .clip(plate.SHAPE.getValue()), contentAlignment = Alignment.Center
                    ) {
                        if (VIGNETTE1.getValue()) myBackground2(Modifier.fillMaxSize())
                        if (VIGNETTE2.getValue()) BackBoxEllipsGradient(
                            modifier = Modifier.matchParentSize(),
                            colors = listOf(
                                Color.Transparent,
                                Color(0x3F000000),
                                Color(0x8F000000),
                                Color(0xFF000000)
                            )
                        )
                        MainDB.avatarSpis.spisCharacteristics.getState().value?.let { listCharacteristics ->
                            itemCharacteristic.getComposable(::ItemCharacteristicState) { itemStyle ->
                                with(LocalDensity.current) {
                                    MyList(
                                        MainDB.avatarSpis.spisCharacteristics,
                                        Modifier
                                            .align(Alignment.Center)

                                            .padding(horizontal = 50.dp, vertical = 20.dp),
                                        maxHeight = (this@BoxWithConstraints.maxHeight * 0.9f - 44.dp).toPx().toInt()
                                    ) { ind, itemChar ->
                                        if (editCharacteristics.value) ComItemCharacteristicsEdit(
                                            itemChar,
                                            selection,
                                            itemCharacteristicState = itemStyle
                                        ) { item, exp ->
                                            DropdownMenuItem(onClick = {
                                                MainDB.avatarFun.setSelectedIdForPrivsCharacteristic(item.id)
                                                PanPrivsGoal(item.id, dialLay, true)
                                                exp.value = false
                                            }) {
                                                Text(text = "Привязать проекты", color = Color.White)
                                            }
                                            MyDropdownMenuItem(exp, "График роста") {
                                                MainDB.avatarFun.setCharacteristicForGrafProgress(item.id)
                                                MyFullScreenPanel(dialLay) { _, _ ->
                                                    MainDB.avatarSpis.spisSumWeekHourOfCharacteristic.getState().value?.let { listOperWeek ->
                                                        MainDB.avatarFun.getMinSumWeekHourOfCharacteristic().toFloat()
                                                            .let { min ->
                                                                MainDB.avatarFun.getMaxSumWeekHourOfCharacteristic()
                                                                    .toFloat()
                                                                    .let { max ->
                                                                        if (listOperWeek.isNotEmpty()) DrawGrafik().drawDiagram(
                                                                            Modifier
                                                                                .weight(1f, false),
                                                                            listOperWeek,
                                                                            min,
                                                                            max,
                                                                            true,
                                                                            GrafikColorStyleState(MainDB.styleParam.avatarParam.characteristicsTab.characteristicsPanelView.grafColor)
                                                                        ) else Box(
                                                                            Modifier.weight(1f),
                                                                            contentAlignment = Alignment.Center
                                                                        ) {
                                                                            Text(
                                                                                "Здесь появится график роста харктеристики, как только будут учтены первые часы по ней.",
                                                                                style = MyTextStyleParam.style1
                                                                            )
                                                                        }
                                                                    }
                                                            }
                                                    }
                                                }
                                            }
                                            DropdownMenuItem(onClick = {
                                                PanAddCharacteristic(dialLay, item)
                                                exp.value = false
                                            }) {
                                                Text(text = "Изменить", color = Color.White)
                                            }
                                            MyDeleteDropdownMenuButton(exp) {
                                                MainDB.addAvatar.delCharacteristic(item.id)
                                            }

                                        }.getComposable() else ComItemCharacteristics(
                                            itemChar,
                                            itemCharacteristicState = itemStyle,
                                            dialogLayout = dialLay
                                        ).getComposable()
                                    }
                                }
                            }
                            if (listCharacteristics.isEmpty()) Text(
                                "Здесь пока не добавлено ни одной характеристики",
                                Modifier.align(Alignment.Center).padding(horizontal = 10.dp),
                                style = textCount.getValue().copy(textAlign = TextAlign.Center)
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyToggleButtIconStyle1(
                        "ic_round_settings_24.xml", value = editCharacteristics,
                        modifier = Modifier.padding(start = 15.dp),
                        width = 50.dp,
                        height = 50.dp,
                        myStyleToggleButton = ToggleButtonStyleState(buttEdit)
                    ) {
                    }
                    Text(
                        "Характеристик: ${MainDB.avatarSpis.spisCharacteristics.getState().value?.size ?: 0}",
                        Modifier.weight(1f).padding(horizontal = 10.dp),
                        style = textCount.getValue().copy(textAlign = TextAlign.Center)
                    )
                    MyTextButtStyle1(
                        "+", modifier = Modifier.padding(end = 15.dp),
                        width = 50.dp,
                        height = 50.dp,
                        myStyleTextButton = TextButtonStyleState(buttAdd)
                    ) {
                        PanAddCharacteristic(dialLay)
                    }
                }
            }
        }
    }
}
