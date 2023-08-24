package MainTabs.Avatar.Items

import MyDialog.MyDialogLayout
import MyDialog.MyFullScreenPanel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import common.DrawGrafik
import androidx.compose.material.Text
import common.MyShadowBox
import common.MyTextStyleParam
import extensions.GrafikColorStyleState
import extensions.ItemCharacteristicState
import extensions.RowVA
import extensions.withSimplePlate
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemCharacteristic
import viewmodel.MainDB

class ComItemCharacteristics(
    val item: ItemCharacteristic,
    val itemCharacteristicState: ItemCharacteristicState,
    val dialogLayout: MyDialogLayout
) {

    val expandedDropMenu = mutableStateOf(false)

    val text_sum_hour = mutableStateOf("${item.hour.roundToStringProb(1)} ч.")

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        with(itemCharacteristicState) {
            MyShadowBox(plateNonEdit.shadow){
                RowVA(outerPaddingNonEdit.withSimplePlate(plateNonEdit).then(innerPaddingNonEdit)) {
                    Text(
                        item.name,
                        Modifier.weight(1f).padding(10.dp).padding(start = 30.dp),
                        style = mainTextStyle // MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                    )
                    if (item.startStat >= 10L) Text(
                        "${(item.startStat/10).toInt()} + ",
                        Modifier.padding(end = 5.dp).alpha(0.7f),
                        style = startValueText //MyTextStyleParam.style1.copy(fontSize = 16.sp, textAlign = TextAlign.Center)
                    )
                    Text(
                        item.stat.toString(),
                        Modifier.padding(end = 20.dp),
                        style = valueText // MyTextStyleParam.style1.copy(textAlign = TextAlign.Center)
                    )
                    Box(
                        Modifier
                            .clickable {
                                MainDB.avatarFun.setCharacteristicForGrafProgress(item.id)
                                MyFullScreenPanel(dialogLayout) { _, _ ->
                                    MainDB.avatarSpis.spisSumWeekHourOfCharacteristic.getState().value?.let { listOperWeek ->
                                        MainDB.avatarFun.getMinSumWeekHourOfCharacteristic().toFloat().let { min ->
                                            MainDB.avatarFun.getMaxSumWeekHourOfCharacteristic().toFloat().let { max ->
                                                if (listOperWeek.isNotEmpty()) DrawGrafik().drawDiagram(
                                                    Modifier
                                                        .weight(1f, false)
//                                                .fillMaxWidth(1f)
                                                    ,
                                                    listOperWeek,
                                                    min,
                                                    max,
                                                    true,
                                                    GrafikColorStyleState(MainDB.styleParam.avatarParam.characteristicsTab.characteristicsPanelView.grafColor)
                                                ) else Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
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
                    ) {
                        Box(
                            Modifier
                                .padding(end = 40.dp)
                                .width(3.dp)
                                .height(25.dp)
                                .background(COLOR_INDIK_BACK)
                                .border(0.5.dp, COLOR_INDIK_BORDER)
                        ) {
                            Box(
                                Modifier.align(Alignment.BottomCenter).width(3.dp)
                                    .fillMaxHeight(item.progress.toFloat())
                                    .background(COLOR_INDIK_COMPLETE)
                            )
                        }
                    }
                }
            }
        }
    }
}