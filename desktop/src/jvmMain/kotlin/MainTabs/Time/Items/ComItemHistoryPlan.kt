package MainTabs.Time.Items


import MainTabs.Time.Elements.ShkalTime
import MyDialog.MyDialogLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*


@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ComItemHistoryPlan(
    item: ItemDenPlan,
    stap: Boolean = false,
    first: Boolean = false,
    dialLay: MyDialogLayout,
) {
    with(MainDB.styleParam.timeParam.planTab.panHistory.itemHistory) {
        Column(Modifier.fillMaxWidth(0.8f)) {
            Box(Modifier.fillMaxWidth()) {
                if (!first) Box(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.8f)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    colorRazdelit.getValue().toColor(),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = Date(item.data).format("MM"),
                    style = textMonth.getValue()
                )
                RowVA(
                    Modifier.padding(start = 45.dp, top = 5.dp).padding(vertical = 2.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = Date(item.data).format("dd.MM.yyyy (EEE)"),
                        style = textDate.getValue()
                    )
                    PlateOrderLayout(Modifier.weight(1f)) {
                        if (item.namestap == "") Text(
                            text = item.name,
                            style = textPlanName.getValue(),
                            modifier = Modifier.padding(start = 15.dp).weight(1f)
                        ) else {
                            MyShadowBox(plateStapName.shadow.getValue()) {
                                Text(
                                    "[${item.namestap}]",
                                    Modifier

                                        .padding(horizontal = 5.dp)
                                        .withSimplePlate(SimplePlateWithShadowStyleState(plateStapName))
                                        .padding(4.dp)
                                        .padding(horizontal = 2.dp),
                                    style = textStapName.getValue()
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.padding(start = 3.dp).padding(vertical = 4.dp),
                        text = Date().fromHourFloat(item.sum_hour.toFloat()).humanizeTime(),
                        style = textHour.getValue()
                    )
                    with(LocalDensity.current) {
                        ShkalTime(
                            item,
                            Modifier.padding(start = 10.dp, end = 40.dp).height(11.dp).width(88.dp),
                            this,
                            colorShkal
                        )
                    }
                }
            }
            MainDB.complexOpisSpis.run { if (stap) spisComplexOpisForHistoryStapPlan else spisComplexOpisForHistoryPlan }
                .getState().value?.let { mapOpis ->
                    mapOpis[item.id.toLong()]?.let { listOpis ->
                        if (listOpis.isNotEmpty()) with(ComplexOpisStyleState(MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan)) {
                            SelectionContainer {
                                MyComplexOpisView(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                        .padding(horizontal = 40.dp)
                                        .then(inner_padding),
                                    listOpis, dialLay, this
                                )
                            }
                        }
                    }
                }
        }
    }
}
