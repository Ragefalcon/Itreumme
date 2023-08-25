package MainTabs.Finance.Tabs

import MainTabs.Finance.Element.PanAddDoxod
import MainTabs.Finance.Items.ComItemDoxod
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.MyDeleteDropdownMenuButton
import common.MyDropdownMenuItem
import common.SingleSelectionType
import extensions.ComboBoxStyleState
import extensions.ItemRasxDoxOperStyleState
import extensions.RowVA
import extensions.getComposable
import ru.ragefalcon.sharedcode.models.data.ItemDoxod
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

class DoxodTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemDoxod>()

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(
            modifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            MainDB.styleParam.finParam.doxodParam.itemDoxod.getComposable(::ItemRasxDoxOperStyleState) { itemStyle ->
                MyList(MainDB.finSpis.doxodSpisPeriod, Modifier.weight(1f)) { _, itemR ->
                    ComItemDoxod(itemR, selection, itemStyle) { item, expanded ->
                        Text(
                            modifier = Modifier.padding(bottom = 5.dp),
                            text = item.name,
                            style = itemStyle.mainTextStyle
                        )
                        MyDropdownMenuItem(expanded, itemStyle.dropdown, "Повторить") {
                            PanAddDoxod(dialLay, item.copy(id = "-1", data = MainDB.dateFin.value.time))
                        }
                        MyDropdownMenuItem(expanded, itemStyle.dropdown, "Изменить") {
                            item.mayChange()?.let {
                                MyShowMessage(dialLay, it)
                            } ?: PanAddDoxod(dialLay, item)
                        }
                        MyDeleteDropdownMenuButton(expanded) {
                            item.mayChange()?.let {
                                MyShowMessage(dialLay, it)
                            } ?: MainDB.addFinFun.delDoxod(item)
                        }
                    }
                }
            }
            RowVA(Modifier.padding(top = 5.dp).padding(start = 8.dp)) {
                if (MainDB.enableFilter.value) MainDB.CB_spisTypeDox.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.doxodParam.cb_typeDox))
                Text(
                    MainDB.finSpis.doxodSummaPeriod.getState().value ?: "",
                    style = MainDB.styleParam.finParam.doxodParam.textRezSumm.getValue().copy(
                        textAlign = TextAlign.End,
                    ),
                    modifier = Modifier.weight(1f).padding(vertical = 0.dp)
                )
            }
        }
    }
}