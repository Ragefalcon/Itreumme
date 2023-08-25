package MainTabs

import MainTabs.Finance.Element.PanAddDoxod
import MainTabs.Finance.Element.PanAddPerevod
import MainTabs.Finance.Element.PanAddPerevodPlan
import MainTabs.Finance.Element.PanAddRasx
import MainTabs.Finance.Items.ComItemDoxodShab
import MainTabs.Finance.Items.ComItemRasxodShab
import MainTabs.Finance.Tabs.*
import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB

class MainFinTabs(private val dialLay: MyDialogLayout) {

    enum class FinPeriodTabs(override val nameTab: String) : tabElement {
        Week("Неделя"),
        Month("Месяц"),
        Year("Год"),
        Period("Период");
    }

    private val seekPeriod = EnumDiskretSeekBar(FinPeriodTabs::class, FinPeriodTabs.Month) {
        when (it) {
            FinPeriodTabs.Week -> MainDB.selPer.SetPeriodWeek()
            FinPeriodTabs.Month -> MainDB.selPer.SetPeriodMonth()
            FinPeriodTabs.Year -> MainDB.selPer.SetPeriodYear()
            FinPeriodTabs.Period -> MainDB.selPer.SetPeriodSelectDates()
        }
    }

    private val rasxod = RasxodTab(dialLay)
    private val rasxodGraf = RasxodGrafTab(dialLay, seekPeriod)
    private val doxodGraf = DoxodGrafTab(dialLay, seekPeriod)
    private val schetGraf = SchetGrafTab(dialLay)
    private val schetPlanGraf = SchetPlanGrafTab(dialLay)
    private val doxod = DoxodTab(dialLay)

    private val schet = SchetTab(dialLay)
    private val schetPlan = SchetPlanTab(dialLay)

    private enum class FinanceTabs(override val nameTab: String) : tabElement {
        Rasxod("Расход"),
        Doxod("Доход"),
        Schet("Счета"),
        Plans("Планирование");
    }

    private val financeSeekBar = EnumDiskretSeekBar(FinanceTabs::class)

    @Composable
    private fun buttonBar() {
        with(MainDB.styleParam.finParam) {
            Row(verticalAlignment = Alignment.CenterVertically) {


                MyToggleButtIconStyle1(
                    "ic_baseline_filter_alt_24.xml",
                    value = MainDB.enableFilter,
                    modifier = Modifier.padding(
                        end = 30.dp,
                        start = if (financeSeekBar.active == FinanceTabs.Rasxod || financeSeekBar.active == FinanceTabs.Doxod) 60.dp else 0.dp
                    ),
                    width = 50.dp,
                    height = 50.dp,
                    myStyleToggleButton = ToggleButtonStyleState(buttFilter)
                )
                if (!MainDB.selectTwoDate.value) {
                    MyTextButtStyle1(
                        "❮❮",
                        Modifier.padding(end = 15.dp),
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
                        Modifier.padding(start = 15.dp),
                        width = 50.dp,
                        height = 40.dp,
                        myStyleTextButton = TextButtonStyleState(buttNextPeriod)
                    ) {
                        MainDB.selPer.nextDate()
                    }
                } else {
                    buttDatePickerWithButton(
                        dialLay,
                        MainDB.dateFinBegin,
                        myStyleTextDate = TextButtonStyleState(buttDate),
                        myStyleTextArrow = TextButtonStyleState(buttNextDate)
                    )
                    buttDatePickerWithButton(
                        dialLay,
                        MainDB.dateFinEnd,
                        modifier = Modifier.padding(start = 15.dp),
                        myStyleTextDate = TextButtonStyleState(buttDate),
                        myStyleTextArrow = TextButtonStyleState(buttNextDate)
                    )
                }
                MyTextButtStyle1(
                    "+",
                    Modifier.padding(start = 30.dp),
                    width = 50.dp,
                    height = 50.dp,
                    myStyleTextButton = TextButtonStyleState(
                        when (financeSeekBar.active) {
                            FinanceTabs.Rasxod -> buttAddRasxod
                            FinanceTabs.Doxod -> buttAddDoxod
                            FinanceTabs.Schet -> buttAddPerevod
                            FinanceTabs.Plans -> buttAddPerevodPlan
                        }
                    )
                ) {
                    when (financeSeekBar.active) {
                        FinanceTabs.Rasxod -> PanAddRasx(dialLay)
                        FinanceTabs.Doxod -> PanAddDoxod(dialLay)
                        FinanceTabs.Schet -> PanAddPerevod(dialLay, schetId = MainDB.CB_spisSchet.getSelected()?.id)
                        FinanceTabs.Plans -> PanAddPerevodPlan(
                            dialLay,
                            schetId = MainDB.CB_spisSchetPlan.getSelected()?.id
                        )
                    }
                }

                if (financeSeekBar.active == FinanceTabs.Rasxod || financeSeekBar.active == FinanceTabs.Doxod) MyIconButtStyle(
                    "ic_baseline_cloud_upload_24.xml",
                    Modifier.padding(start = 10.dp),
                    sizeIcon = 40.dp,
                    width = 50.dp,
                    height = 50.dp,
                    myStyleButton = IconButtonStyleState(MainDB.styleParam.finParam.let {
                        if (financeSeekBar.active == FinanceTabs.Rasxod) rasxodParam.panAddRasxod.shablLoadButt else doxodParam.panAddDoxod.shablLoadButt
                    })
                )
                {
                    if (financeSeekBar.active == FinanceTabs.Rasxod) PanSelectOneItem(
                        dialLay, MainDB.finSpis.spisShabRasxod, { itemShab ->
                            PanAddRasx(dialLay, itemShabRasxod = itemShab)
                        },
                        stylePanelItem = MainDB.styleParam.finParam.rasxodParam.panAddRasxod.plateShablon
                    ) { itemSh, selection, dialL, listener ->
                        ComItemRasxodShab(
                            itemSh,
                            selection,
                            listener,
                            MainDB.styleParam.finParam.rasxodParam.panAddRasxod.itemRasxodShablon,
                            dialL
                        )
                    }
                    else PanSelectOneItem(
                        dialLay, MainDB.finSpis.spisShabDoxod, { itemShab ->
                            PanAddDoxod(dialLay, itemShabDoxod = itemShab)
                        },
                        stylePanelItem = MainDB.styleParam.finParam.rasxodParam.panAddRasxod.plateShablon
                    ) { itemSh, selection, dialL, listener ->
                        ComItemDoxodShab(
                            itemSh,
                            selection,
                            listener,
                            MainDB.styleParam.finParam.doxodParam.panAddDoxod.itemDoxodShablon,
                            dialL
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun panelGraf() {
        Row {
            when (financeSeekBar.active) {
                FinanceTabs.Rasxod -> rasxodGraf.show(Modifier.weight(16f))
                FinanceTabs.Doxod -> doxodGraf.show(Modifier.weight(16f))
                FinanceTabs.Schet -> schetGraf.show(Modifier.weight(16f))
                FinanceTabs.Plans -> schetPlanGraf.show(Modifier.weight(16f))
            }
        }
    }

    @Composable
    private fun panelSpis() {
        Row {
            when (financeSeekBar.active) {
                FinanceTabs.Rasxod -> rasxod.show(Modifier.weight(1f))
                FinanceTabs.Doxod -> doxod.show(Modifier.weight(1f))
                FinanceTabs.Schet -> schet.show(Modifier.weight(1f))
                FinanceTabs.Plans -> schetPlan.show(Modifier.weight(1f))
            }
        }
    }

    @Composable
    private fun splittableBox(modifier: Modifier, pan1: @Composable () -> Unit, pan2: @Composable () -> Unit) {
        val panelState = remember { PanelState().apply { expandedSize = 400.dp } }
        val animatedSize =
            if (panelState.splitter.isResizeEnabled) {
                if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
            } else {
                animateDpAsState(
                    if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
                    SpringSpec(stiffness = Spring.StiffnessLow)
                ).value
            }
        Box(modifier) {
            VerticalSplittable(
                Modifier.fillMaxSize(),
                panelState.splitter,
                onResize = {
                    panelState.expandedSize =
                        (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
                },
                color = Color.Transparent,
                splitStyle = {
                    Box(
                        Modifier.padding(vertical = 50.dp).fillMaxHeight().width(2.dp).background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MyColorARGB.colorMyBorderStroke.toColor(),
                                    Color.Transparent
                                )
                            )
                        )
                    )
                }
            ) {
                ResizablePanel(Modifier.width(animatedSize).fillMaxHeight(), panelState) {
                    pan1()
                }
                pan2()
            }
        }
    }

    @Composable
    private fun finDateObser() {
        LaunchedEffect(MainDB.dateFin.value) {
            MainDB.selPer.setDateOpor(MainDB.dateFin.value.time)
        }
        LaunchedEffect(MainDB.dateFinBegin.value) {
            MainDB.selPer.SetPeriodDates(MainDB.dateFinBegin.value.time, MainDB.dateFinEnd.value.time + DAY)
        }
        LaunchedEffect(MainDB.dateFinEnd.value) {
            MainDB.selPer.SetPeriodDates(MainDB.dateFinBegin.value.time, MainDB.dateFinEnd.value.time + DAY)
        }
        LaunchedEffect(MainDB.enableFilter.value) {
            MainDB.finFun.setEnableFilter(MainDB.enableFilter.value)
        }
    }

    @Composable
    fun show() {
        finDateObser()
        Box() {
            Column(
                Modifier.fillMaxSize().padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                financeSeekBar.show(
                    Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    style = MainDB.styleParam.finParam.seekBarStyle
                )
                splittableBox(Modifier.weight(1f), { panelGraf() }, { panelSpis() })
                buttonBar()
                seekPeriod.show(
                    Modifier.fillMaxWidth().padding(top = 5.dp),
                    style = MainDB.styleParam.finParam.seekBarPeriodStyle
                )
            }
        }
    }
}
