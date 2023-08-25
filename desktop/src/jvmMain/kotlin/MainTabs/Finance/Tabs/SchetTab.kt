package MainTabs.Finance.Tabs

import MainTabs.Finance.Element.PanAddDoxod
import MainTabs.Finance.Element.PanAddPerevod
import MainTabs.Finance.Element.PanAddRasx
import MainTabs.Finance.Items.ComItemCommonFinOper
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.FilterSchetOper
import viewmodel.MainDB

class SchetTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemCommonFinOper>()
    val cb_filter = MyComboBox(FilterSchetOper.values().asList(), nameItem = { it.title })

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(
            modifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainDB.styleParam.finParam.rasxodParam.itemRasxod.getComposable(::ItemRasxDoxOperStyleState) { itemRStyle ->
                MainDB.styleParam.finParam.doxodParam.itemDoxod.getComposable(::ItemRasxDoxOperStyleState) { itemDStyle ->
                    MainDB.styleParam.finParam.schetParam.itemPerevod.getComposable(::ItemRasxDoxOperStyleState) { itemPStyle ->
                        MainDB.finSpis.schetSpisPeriod.getState().value?.let {
                            (if (MainDB.enableFilter.value) it.filter { it.schet == cb_filter.getSelected()?.title } else it).let { spisOper ->
                                MyList(spisOper, Modifier.weight(1f)) { ind, itemSchet ->
                                    when (itemSchet.schet) {
                                        "Расход" -> itemRStyle
                                        "Доход" -> itemDStyle
                                        "Перевод" -> itemPStyle
                                        else -> itemPStyle
                                    }.let { itemStyle ->
                                        ComItemCommonFinOper(itemSchet, selection, itemStyle) { item, expanded ->
                                            Text(
                                                modifier = Modifier.padding(bottom = 5.dp),
                                                text = item.name,
                                                style = itemStyle.mainTextStyle
                                            )
                                            MyDropdownMenuItem(expanded, itemStyle.dropdown, "Повторить") {
                                                when (itemSchet.schet) {
                                                    "Расход" -> {
                                                        MainDB.finFun.getItemRasxodById(itemSchet.id.toLong())?.let {
                                                            PanAddRasx(
                                                                dialLay,
                                                                it.copy(id = "-1", data = MainDB.dateFin.value.time)
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
                                                        PanAddPerevod(
                                                            dialLay,
                                                            item.copy(id = "-1", data = MainDB.dateFin.value.time),
                                                            schetId = MainDB.CB_spisSchet.getSelected()?.id
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
                                                        } ?: PanAddPerevod(
                                                            dialLay,
                                                            item,
                                                            schetId = MainDB.CB_spisSchet.getSelected()?.id
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
                                                        } ?: MainDB.addFinFun.delPerevod(item)
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
                MainDB.finSpis.sumsOperForPeriod.getState().value?.let {
                    SimplePlateWithShadowStyleState(MainDB.styleParam.finParam.schetParam.panelStat).let { plate ->
                        MyShadowBox(plate.shadow, Modifier.weight(1f)) {
                            MainDB.styleParam.finParam.schetParam.let { styleStat ->

                                RowVA(
                                    Modifier.withSimplePlate(plate)
                                        .paddingStyle(MainDB.styleParam.finParam.schetParam.panelStatInnerPadding)
                                ) {
                                    Text(
                                        it.rasxodsum.roundToStringProb(2),
                                        style = styleStat.textStat.getValue().copy(
                                            color = styleStat.COLOR_STAT_RASX.getValue().toColor()
                                        )
                                    )
                                    Text(
                                        it.perevodsum.roundToStringProb(2),
                                        modifier = Modifier.weight(1f),
                                        style = styleStat.textStat.getValue().copy(
                                            textAlign = TextAlign.Center,
                                            color = styleStat.COLOR_STAT_PER.getValue().toColor()
                                        )
                                    )
                                    Text(
                                        it.doxodsum.roundToStringProb(2),
                                        style = styleStat.textStat.getValue().copy(
                                            color = styleStat.COLOR_STAT_DOX.getValue().toColor()
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        MainDB.finSpis.schetSumma.getState().value ?: "",
                        style = MainDB.styleParam.finParam.schetParam.textRezSumm.getValue().copy(
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.padding(start = 50.dp)
                    )
                }
            }
            RowVA(Modifier.padding(top = 5.dp).padding(start = 8.dp)) {
                MainDB.CB_spisSchet.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchet))
                Spacer(Modifier.weight(1f))
                if (MainDB.enableFilter.value) cb_filter.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_typeFilter))
                MyTextButtStyle1(
                    "+",
                    Modifier.padding(horizontal = 5.dp),
                    width = 70.dp,
                    height = 35.dp,
                    myStyleTextButton = TextButtonStyleState(MainDB.styleParam.finParam.schetParam.buttAddInnerRasxod)
                ) {
                    PanAddRasx(dialLay, schetId = MainDB.CB_spisSchet.getSelected()?.id)
                }
                MyTextButtStyle1(
                    "+",
                    width = 70.dp,
                    height = 35.dp,
                    myStyleTextButton = TextButtonStyleState(MainDB.styleParam.finParam.schetParam.buttAddInnerDoxod)
                ) {
                    PanAddDoxod(dialLay, schetId = MainDB.CB_spisSchet.getSelected()?.id)
                }
            }
        }
    }
}