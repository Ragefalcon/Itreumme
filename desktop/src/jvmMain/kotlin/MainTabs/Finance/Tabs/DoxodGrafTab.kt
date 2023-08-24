package MainTabs.Finance.Tabs

import MainTabs.Finance.Items.ComItemSectorDiagram
import MainTabs.MainFinTabs
import MyDialog.MyDialogLayout
import MyDialog.MyFullScreenPanel
import MyDialog.buttDatePicker
import MyList
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import common.*
import extensions.GrafikColorStyleState
import extensions.RowVA
import extensions.TwoRectDiagramColorStyleState
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PeriodSelecter
import viewmodel.MainDB

class DoxodGrafTab(
    val dialLay: MyDialogLayout,
    /**
     * Если не передать здесь ту же DiskretSeekBar для выбора периода, то они зациклят друг друга, т.к. там код выбора
     * вызывается при перерисовке... сейчас это не выглядит правильно, хотя в целом думаю должно работать стабильно
     * во всех остальных случаях. Здесь же поскольку наблюдаются одни и те же списки в любом случае логично передавать
     * ту же DiskretSeekBar, иначе при закрытии панели могут отличаться значения на основной DiskretSeekBar для выбора
     * периода и списками в финансах.
     * */
    val diskretSeekBar: EnumDiskretSeekBar<MainFinTabs.FinPeriodTabs>
) {

    val sektorDiagram = DrawSektorDiagram()
    val rectDiagram = DrawTwoRectDiagram()
    val grafikDiagram = DrawGrafik()

    enum class TypeGrafDoxTabs(override val nameTab: String) : tabElement {
        common("Общая"),
        doxrasx("дох./расх."),
        grafik("График");
    }

    private val seekTypeGraf = EnumDiskretSeekBar(TypeGrafDoxTabs::class, TypeGrafDoxTabs.common)

    @Composable
    private fun mainContent(
        panel: Boolean = false,
        dialLayInner: MyDialogLayout? = null,
        modifier: Modifier = Modifier
    ) = Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (seekTypeGraf.active) {
            TypeGrafDoxTabs.common -> {
                if (panel) {
                    MainDB.finSpis.spisDoxodByType.getState().value?.let { listRTM ->
                        RowVA(Modifier.weight(1f), Arrangement.Center) {
                            BoxWithConstraints(Modifier.weight(1f), Alignment.CenterEnd) {
                                sektorDiagram.drawDiagram(
                                    Modifier
                                        .height(minOf(this.maxHeight, this.maxWidth) * 0.8f)
                                        .width(
                                            minOf(
                                                this.maxHeight,
                                                this.maxWidth
                                            ) * 0.8f
                                        ),//.height(250.dp),//.fillMaxWidth(),
                                    listRTM
                                )
                            }
                            BoxWithConstraints(Modifier.weight(1f), Alignment.CenterStart) {
                                MyList(
                                    MainDB.finSpis.spisDoxodByType,
                                    Modifier.padding(start = 30.dp),
                                    maxHeight = this.maxHeight.value.toInt()
                                ) { ind, itemSD ->
                                    if (itemSD.procent >= 0.02 || itemSD == listRTM.lastOrNull()) ComItemSectorDiagram(
                                        itemSD,
                                        MainDB.styleParam.finParam.doxodParam.textTire.getValue(),
                                        MainDB.styleParam.finParam.doxodParam.textDoxDiag.getValue()
                                    )
                                }
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!MainDB.selectTwoDate.value) {
                            MyTextButtStyle1("❮❮") {
                                MainDB.selPer.prevDate()
                            }
                            MyTextButtStyle1("<") {
                                MainDB.selPer.prevDate(PeriodSelecter.FinPeriod.Day)
                            }
                            dialLayInner?.let {
                                buttDatePicker(it, MainDB.dateFin)//{ MainDB.dateFinObs = it }
                            }
                            MyTextButtStyle1(">") {
                                MainDB.selPer.nextDate(PeriodSelecter.FinPeriod.Day)
                            }
                            MyTextButtStyle1("❯❯") {
                                MainDB.selPer.nextDate()
                            }
                        } else {
                            dialLayInner?.let {
                                buttDatePicker(it, MainDB.dateFinBegin)//{ MainDB.dateFinObs = it }
                                buttDatePicker(it, MainDB.dateFinEnd)//{ MainDB.dateFinObs = it }
                            }
                        }
                    }
                    diskretSeekBar.show()
                    Spacer(Modifier.height(15.dp))
                } else {
                    MainDB.finSpis.spisDoxodByType.getState().value?.let { listRTM ->
                        sektorDiagram.drawDiagram(
                            Modifier.height(250.dp).padding(10.dp).fillMaxWidth(),
                            listRTM
                        )
                        MyList(
                            MainDB.finSpis.spisDoxodByType,
                            Modifier.weight(1f)
                        ) { ind, itemSD ->
                            if (itemSD.procent >= 0.02 || itemSD == listRTM.lastOrNull()) ComItemSectorDiagram(
                                itemSD,
                                MainDB.styleParam.finParam.doxodParam.textTire.getValue(),
                                MainDB.styleParam.finParam.doxodParam.textDoxDiag.getValue()
                            )
                        }
                    }
                }
            }
            TypeGrafDoxTabs.doxrasx -> {
                MainDB.finSpis.spisDoxodRasxodByMonthOnYear.getState().value?.let {
                    rectDiagram.drawDiagram(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(1f),
                        it,
                        true,
                        TwoRectDiagramColorStyleState(MainDB.styleParam.finParam.doxodParam.twoRectDiagColor)
                    )
                }
            }
            TypeGrafDoxTabs.grafik -> {
                MainDB.finSpis.spisSumOperWeek.getState().value?.let { listOperWeek ->
                    if (listOperWeek.isNotEmpty()) grafikDiagram.drawDiagram(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(1f),
                        listOperWeek,
                        MainDB.finFun.getMinSumOperWeek().toFloat(),
                        MainDB.finFun.getMaxSumOperWeek().toFloat(),
                        true,
                        GrafikColorStyleState(MainDB.styleParam.finParam.doxodParam.GrafColor)
                    )
                }
            }
        }
        seekTypeGraf.show(style = MainDB.styleParam.finParam.doxodParam.seekBarTypeGrafStyle)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun show(modifier: Modifier = Modifier) {

        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        Box(modifier.padding(vertical = 5.dp)) {
            mainContent()
            Image(
                painterResource("ic_baseline_unfold_more_24.xml"), //BitmapPainter(
                "buttUnfold",
                Modifier
                    .align(Alignment.TopEnd)
                    .height(if (isHovered) 60.dp else 40.dp)
                    .width(if (isHovered) 60.dp else 40.dp)
                    .hoverable(interactionSource = interactionSource)
                    .border(2.dp, Color.White.copy(0.4f), RoundedCornerShape(8.dp))
                    .mouseClickable {
                        MyFullScreenPanel(dialLay, showHideButton = false) { dialLayInner, closeFun ->
                            Box(modifier.weight(1f)) {
                                mainContent(true, dialLayInner)
                                Image(
                                    painterResource("ic_baseline_unfold_less_24.xml"), //BitmapPainter(
                                    "buttUnfold",
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .height(if (isHovered) 60.dp else 40.dp)
                                        .width(if (isHovered) 60.dp else 40.dp)
                                        .hoverable(interactionSource = interactionSource)
                                        .border(2.dp, Color.White.copy(0.4f), RoundedCornerShape(8.dp))
                                        .mouseClickable {
                                            closeFun()
                                        },
                                    alpha = 0.4F,
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                        }
                    },
                alpha = 0.4F,
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}