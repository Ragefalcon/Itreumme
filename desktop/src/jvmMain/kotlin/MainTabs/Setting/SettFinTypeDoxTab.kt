package MainTabs.Setting

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
import ru.ragefalcon.sharedcode.models.data.ItemSettTypedoxod
import MainTabs.Setting.Items.ComItemTypeDoxSett
import viewmodel.MainDB

class SettFinTypeDoxTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemSettTypedoxod>()

    val visClose = mutableStateOf(false)

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(modifier,horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                MyTextToggleButtStyle1("Закрытые", visClose, modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.finFun.setVisibleOpenSettTypedoxod(!it)
                    selection.selected?.let {
                        if (!it.open) selection.selected = null
                    }
                }
                MyTextStyle1(
                    "Количесто: ${MainDB.finSpis.spisTypedoxodForSett.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp)
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                    MyOneVopros(dialLay, "Введите название типа дохода:", "Добавить", "Тип дохода") {
                        MainDB.addFinFun.addTypedoxod(it)
                    }
//                    PanAddVxod(dialLay)
                }
            }
            MyList(MainDB.finSpis.spisTypedoxodForSett, Modifier.weight(1f).padding(bottom = 10.dp)) { ind, itemSettTypedoxod ->
                ComItemTypeDoxSett(itemSettTypedoxod, selection) { item, expanded ->
                    DropdownMenuItem(onClick = {
                        MyOneVopros(
                            dialLay,
                            "Введите новое название типа дохода:",
                            "Изменить",
                            "Тип дохода",
                            item.typed
                        ) {
                            MainDB.addFinFun.updTypedoxodName(item.id.toLong(), it)
                        }
                        expanded.value = false
                    }) {
                        Text(text = "Изменить", color = Color.White)
                    }
                    DropdownMenuItem(onClick = {
                        MainDB.addFinFun.updTypedoxodOpen(item.id.toLong(), item.open.not())
//                        PanAddVxod(dialLay, item)
                        expanded.value = false
                    }) {
                        Text(text = if (item.open) "Закрыть" else "Открыть", color = Color.White)
                    }
                    if (item.countoper == 0L){
                        MyDeleteDropdownMenuButton(expanded){
                            MainDB.addFinFun.delTypedoxod(item.id.toLong())
                        }
                    }
                }.getComposable()
            }
        }
    }
}
