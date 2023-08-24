package MainTabs.Finance.Tabs

import MyDialog.MyDialogLayout
import MyList
import adapters.MyComboBox
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import MainTabs.Finance.Items.ComItemSectorDiagram
import MainTabs.MainFinTabs
import MyDialog.MyFullScreenPanel
import MyDialog.buttDatePicker
import MyDialog.buttDatePickerWithButton
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.mouse.mouseScrollFilter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.sun.tools.javac.Main
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PeriodSelecter
import viewmodel.MainDB

class RasxodGrafTab(
    val dialLay: MyDialogLayout, val diskretSeekBar: EnumDiskretSeekBar<MainFinTabs.FinPeriodTabs>
) {

    val sektorDiagram = DrawSektorDiagram()

    enum class TypeGrafRasxTabs(override val nameTab: String) : tabElement {
        common("Общая"),
        type("По типу");
    }

    private val seekTypeGraf = EnumDiskretSeekBar(TypeGrafRasxTabs::class, TypeGrafRasxTabs.common)

    val rectDiagram = DrawRectDiagram()

    val CB_spisTypeRasx = MyComboBox(MainDB.finSpis.spisTypeRasx, nameItem = { it.second }) {
            MainDB.finFun.selectRasxodTypeByMonthOnYear(it.first.toLong())
        }

    @Composable
    private fun mainContent(
        panel: Boolean = false,
        dialLayInner: MyDialogLayout? = null,
        modifier: Modifier = Modifier
    ) = Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(MainDB.styleParam.finParam) {
                when (seekTypeGraf.active) {
                    TypeGrafRasxTabs.common -> {
                        if (panel) {
                            MainDB.finSpis.spisRasxodByType.getState().value?.let { listRTM ->
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
                                            MainDB.finSpis.spisRasxodByType,
                                            Modifier.padding(start = 30.dp),
                                            maxHeight = this.maxHeight.value.toInt()
                                        ) { ind, itemSD ->
                                            if (itemSD.procent >= 0.02 || itemSD == listRTM.lastOrNull()) ComItemSectorDiagram(
                                                itemSD,
                                                MainDB.styleParam.finParam.rasxodParam.textTire.getValue(),
                                                MainDB.styleParam.finParam.rasxodParam.textRasxDiag.getValue()
                                            )
                                        }
                                    }
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!MainDB.selectTwoDate.value) {
                                    MyTextButtStyle1(
                                        "❮❮",
                                        Modifier.padding(end = 10.dp),
                                        width = 50.dp,
                                        height = 40.dp,
                                        myStyleTextButton = TextButtonStyleState(buttNextPeriod)
                                    ) {
                                        MainDB.selPer.prevDate()
                                    }
                                    buttDatePickerWithButton(
                                        dialLay,
                                        MainDB.dateFin,
                                        myStyleTextDate = TextButtonStyleState(buttDate),
                                        myStyleTextArrow = TextButtonStyleState(buttNextDate)
                                    )
                                    MyTextButtStyle1(
                                        "❯❯",
                                        Modifier.padding(start = 10.dp),
                                        width = 50.dp,
                                        height = 40.dp,
                                        myStyleTextButton = TextButtonStyleState(buttNextPeriod)
                                    ) {
                                        MainDB.selPer.nextDate()
                                    }
                                } else {
                                    dialLayInner?.let {
                                        buttDatePickerWithButton(
                                            dialLay,
                                            MainDB.dateFinBegin,
                                            myStyleTextDate = TextButtonStyleState(buttDate),
                                            myStyleTextArrow = TextButtonStyleState(buttNextDate)
                                        )
                                        buttDatePickerWithButton(
                                            dialLay,
                                            MainDB.dateFinEnd,
                                            Modifier.padding(start = 15.dp),
                                            myStyleTextDate = TextButtonStyleState(buttDate),
                                            myStyleTextArrow = TextButtonStyleState(buttNextDate)
                                        )
                                    }
                                }
                            }
                            diskretSeekBar.show(Modifier.fillMaxWidth().padding(top = 5.dp), style = MainDB.styleParam.finParam.seekBarPeriodStyle)
                            Spacer(Modifier.height(15.dp))
                        } else {
                            MainDB.finSpis.spisRasxodByType.getState().value?.let { listRTM ->
                                sektorDiagram.drawDiagram(
                                    Modifier.height(250.dp).padding(10.dp).fillMaxWidth(),
                                    listRTM
                                )// listOf(ItemSectorDiag("1","sfasdf", 250.0, 0.30, 30f,4f, MyColorARGB.colorDoxodItem0.toFloatCol())))
                                MyList(MainDB.finSpis.spisRasxodByType, Modifier.weight(1f)) { ind, itemSD ->
                                    if (itemSD.procent >= 0.02 || itemSD == listRTM.lastOrNull()) ComItemSectorDiagram(
                                        itemSD,
                                        MainDB.styleParam.finParam.rasxodParam.textTire.getValue(),
                                        MainDB.styleParam.finParam.rasxodParam.textRasxDiag.getValue()
                                    )
                                }

                            }
                        }
                    }
                    TypeGrafRasxTabs.type -> {
                        MainDB.finSpis.spisRasxodTypeByMonthWithDate.getState().value?.let {
                            rectDiagram.drawDiagram(
                                Modifier.weight(1f).fillMaxWidth(1f),
                                it,
                                styleState = RectDiagramColorStyleState(MainDB.styleParam.finParam.rasxodParam.rectDiagColor)
                            )
                        }
                        CB_spisTypeRasx.show(Modifier.padding(5.dp),style = ComboBoxStyleState(MainDB.styleParam.finParam.rasxodParam.cb_typeRasx))
                    }
                }
            seekTypeGraf.show(style = MainDB.styleParam.finParam.rasxodParam.seekBarTypeGrafStyle)
        }
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
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
                                    colorFilter = ColorFilter.tint(MainDB.styleParam.finParam.colorButtFullScreenGraf.getValue().toColor()),
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