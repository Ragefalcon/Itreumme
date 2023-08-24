package MainTabs.Setting

import MainTabs.Setting.Element.PanAddSchet
import MainTabs.Setting.Items.ComItemSchetSett
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.models.data.ItemSettSchet
import viewmodel.MainDB

class SettFinSchetTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemSettSchet>()

    val visClose = mutableStateOf(false)

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(modifier,horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                MyTextToggleButtStyle1("Закрытые", visClose, modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.finFun.setVisibleOpenSettSchet(!it)
                    selection.selected?.let {
                        if (!it.open_) selection.selected = null
                    }
                }
                MyTextStyle1(
                    "Количесто: ${MainDB.finSpis.spisSchetForSett.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp)
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                    PanAddSchet(dialLay)
                }
            }
            MyList(MainDB.finSpis.spisSchetForSett, Modifier.weight(1f).padding(bottom = 10.dp)) { ind, itemSettSchet ->
                ComItemSchetSett(itemSettSchet, selection) { item, expanded ->
                    DropdownMenuItem(onClick = {
                        MyOneVopros(
                            dialLay,
                            "Введите новое название типа счета:",
                            "Изменить",
                            "Название счета",
                            item.name
                        ) {
                            MainDB.addFinFun.updSchetName(item.id.toLong(), it)
                        }
                        expanded.value = false
                    }) {
                        Text(text = "Изменить", color = Color.White)
                    }
                    if (item.summa == 0.0) {
                        DropdownMenuItem(onClick = {
                            MainDB.addFinFun.updSchetOpen(item.id.toLong(), item.open_.not())
//                        PanAddVxod(dialLay, item)
                            expanded.value = false
                        }) {
                            Text(text = if (item.open_) "Закрыть" else "Открыть", color = Color.White)
                        }
                    }
                    if (item.countoper == 0L){
                        MyDeleteDropdownMenuButton(expanded){
                            MainDB.addFinFun.delSchet(item.id.toLong())
                        }
                    }
                }.getComposable()
            }
        }
    }
}
