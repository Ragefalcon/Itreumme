package MainTabs.Finance.Tabs


import MainTabs.Finance.Element.PanAddDoxod
import MainTabs.Finance.Element.PanAddPerevod
import MainTabs.Finance.Element.PanAddPerevodPlan
import MainTabs.Finance.Element.PanAddRasx
import MainTabs.Finance.Items.ComItemCommonFinOper
import MainTabs.Finance.Items.ComItemCommonFinOperPopravka
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import adapters.MyComboBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.FilterSchetOper
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.FilterSchetPlanOper
import viewmodel.MainDB

class SchetPlanTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemCommonFinOper>()
    val cb_filter = MyComboBox(FilterSchetPlanOper.values().asList(), nameItem = { it.title })

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(
            modifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainDB.styleParam.finParam.rasxodParam.itemRasxod.getComposable(::ItemRasxDoxOperStyleState) { itemRStyle ->
                MainDB.styleParam.finParam.doxodParam.itemDoxod.getComposable(::ItemRasxDoxOperStyleState) { itemDStyle ->
                    MainDB.styleParam.finParam.schetParam.itemPerevod.getComposable(::ItemRasxDoxOperStyleState) { itemPStyle ->
                        MainDB.styleParam.finParam.schetParam.itemPopravka.getComposable(::ItemRasxDoxOperStyleState) { itemPKStyle ->
                            MainDB.finSpis.schetPlanSpisPeriod.getState().value?.let {
                                (if (MainDB.enableFilter.value) it.filter { it.schet == cb_filter.getSelected()?.title } else it).let { spisOper ->
                                    MyList(spisOper, Modifier.weight(1f)) { ind, itemSchet ->
                                        when (itemSchet.schet) {
                                            "Расход" -> itemRStyle
                                            "Доход" -> itemDStyle
                                            "Перевод" -> itemPStyle
                                            "Поправка на курс" -> itemPKStyle
                                            else -> itemPStyle
                                        }.let { itemStyle ->
                                            if (itemSchet.schet == "Поправка на курс") ComItemCommonFinOperPopravka(
                                                itemSchet,
                                                itemPKStyle
                                            )
                                            else ComItemCommonFinOper(
                                                itemSchet,
                                                selection,
                                                itemStyle
                                            ) { item, expanded ->
                                                MyDropdownMenuItem(expanded, itemStyle.dropdown, "Повторить") {
                                                    when (itemSchet.schet) {
                                                        "Расход" -> {
                                                            MainDB.finFun.getItemRasxodById(itemSchet.id.toLong())
                                                                ?.let {
                                                                    PanAddRasx(
                                                                        dialLay,
                                                                        it.copy(
                                                                            id = "-1",
                                                                            data = MainDB.dateFin.value.time
                                                                        )
                                                                    )
                                                                }
                                                        }
                                                        "Доход" -> {
                                                            MainDB.finFun.getItemDoxodById(itemSchet.id.toLong())?.let {
                                                                PanAddDoxod(
                                                                    dialLay,
                                                                    it.copy(id = "-1", data = MainDB.dateFin.value.time)
                                                                )
                                                            }
                                                        }
                                                        "Перевод" -> {
                                                            PanAddPerevodPlan(
                                                                dialLay,
                                                                item.copy(id = "-1", data = MainDB.dateFin.value.time),
                                                                schetId = MainDB.CB_spisSchetPlan.getSelected()?.id
                                                            )
                                                        }
                                                    }
                                                }
                                                MyDropdownMenuItem(expanded, itemStyle.dropdown, "Изменить") {
                                                    when (itemSchet.schet) {
                                                        "Расход" -> {
                                                            MainDB.finFun.getItemRasxodById(itemSchet.id.toLong())
                                                                ?.let { itemR ->
                                                                    itemR.mayChange()?.let {
                                                                        MyShowMessage(dialLay, it)
                                                                    } ?: PanAddRasx(dialLay, itemR)
                                                                }
                                                        }
                                                        "Доход" -> {
                                                            MainDB.finFun.getItemDoxodById(itemSchet.id.toLong())
                                                                ?.let { itemD ->
                                                                    itemD.mayChange()?.let {
                                                                        MyShowMessage(dialLay, it)
                                                                    } ?: PanAddDoxod(dialLay, itemD)
                                                                }
                                                        }
                                                        "Перевод" -> {
                                                            item.mayChange()?.let {
                                                                MyShowMessage(dialLay, it)
                                                            } ?: PanAddPerevodPlan(
                                                                dialLay,
                                                                item,
                                                                schetId = MainDB.CB_spisSchetPlan.getSelected()?.id
                                                            )
                                                        }
                                                    }
                                                }
                                                MyDeleteDropdownMenuButton(expanded) {
                                                    when (itemSchet.schet) {
                                                        "Расход" -> {
                                                            MainDB.finFun.getItemRasxodById(itemSchet.id.toLong())
                                                                ?.let { itemR ->
                                                                    itemR.mayChange()?.let {
                                                                        MyShowMessage(dialLay, it)
                                                                    } ?: MainDB.addFinFun.delRasxod(itemR)
                                                                }
                                                        }
                                                        "Доход" -> {
                                                            MainDB.finFun.getItemDoxodById(itemSchet.id.toLong())
                                                                ?.let { itemD ->
                                                                    itemD.mayChange()?.let {
                                                                        MyShowMessage(dialLay, it)
                                                                    } ?: MainDB.addFinFun.delDoxod(itemD)
                                                                }
                                                        }
                                                        "Перевод" -> {
                                                            item.mayChange()?.let {
                                                                MyShowMessage(dialLay, it)
                                                            } ?: MainDB.addFinFun.delPerevodPlan(item)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            RowVA(Modifier.padding(top = 5.dp).padding(start = 10.dp)) {
                MainDB.finSpis.sumsOperPlForPeriod.getState().value?.let {
                    SimplePlateWithShadowStyleState(MainDB.styleParam.finParam.schetParam.panelStat).let { plate ->
                        MyShadowBox(plate.shadow, Modifier.weight(1f)) {
                            MainDB.styleParam.finParam.schetParam.let { styleStat ->
                                Column(
                                    Modifier.withSimplePlate(plate)
                                        .paddingStyle(MainDB.styleParam.finParam.schetParam.panelStatInnerPadding)
                                ) {
                                    RowVA{
                                        Text(
                                            it.rasxodsum.roundToStringProb(2),
                                            style = styleStat.textStat.getValue().copy(
                                                color = styleStat.COLOR_STAT_RASX.getValue().toColor()
                                            )
                                        )
                                        Spacer(Modifier.weight(1f))
                                        Text(
                                            it.perevodsum.roundToStringProb(2),
//                                            modifier = Modifier.weight(1f),
                                            style = styleStat.textStat.getValue().copy(
                                                textAlign = TextAlign.Center,
                                                color = styleStat.COLOR_STAT_PER.getValue().toColor()
                                            )
                                        )
                                        if (it.popravkasum == 0.0){
                                            Spacer(Modifier.weight(1f))
                                            Text(
                                                it.doxodsum.roundToStringProb(2),
                                                style = styleStat.textStat.getValue().copy(
                                                    color = styleStat.COLOR_STAT_DOX.getValue().toColor()
                                                )
                                            )
                                        }
                                    }
                                    if (it.popravkasum != 0.0)RowVA {
                                        Text(
                                            it.doxodsum.roundToStringProb(2),
                                            style = styleStat.textStat.getValue().copy(
                                                color = styleStat.COLOR_STAT_DOX.getValue().toColor()
                                            )
                                        )
                                        Spacer(Modifier.weight(1f))
                                        Text(
                                            it.popravkasum.roundToStringProb(2),
//                                            modifier = Modifier.weight(1f),
                                            style = styleStat.textStat.getValue().copy(
                                                textAlign = TextAlign.Center,
                                                color = styleStat.COLOR_STAT_POPRAV.getValue().toColor()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        MainDB.finSpis.schetPlanSumma.getState().value ?: "",
                        style = MainDB.styleParam.finParam.schetParam.textRezSumm.getValue().copy(
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.padding(start = 50.dp)
                    )
                }
            }

            RowVA(Modifier.padding(top = 5.dp, start = 8.dp)) {
                MainDB.CB_spisSchetPlan.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchetPlan))
                if (MainDB.enableFilter.value) cb_filter.show(
                    Modifier.padding(top = 0.dp, start = 10.dp),
                    style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_typeFilter)
                )
                Spacer(Modifier.weight(1f))
                MyTextButtStyle1(
                    "+",
                    Modifier.padding(horizontal = 5.dp),
                    width = 70.dp,
                    height = 35.dp,
                    myStyleTextButton = TextButtonStyleState(MainDB.styleParam.finParam.schetParam.buttAddInnerRasxod)
                ) {
                    PanAddRasx(dialLay, schetPlanId = MainDB.CB_spisSchetPlan.getSelected()?.id)
                }
            }
        }
    }
}