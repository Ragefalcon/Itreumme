package MainTabs.Setting

import MainTabs.Setting.Items.ComItemTypeRasxSett
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
import ru.ragefalcon.sharedcode.models.data.ItemSettTyperasxod
import viewmodel.MainDB

class SettFinTypeRasxTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemSettTyperasxod>()

    val visClose = mutableStateOf(false)

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                MyTextToggleButtStyle1("Закрытые", visClose, modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.finFun.setVisibleOpenSettTyperasxod(!it)
                    selection.selected?.let {
                        if (!it.open) selection.selected = null
                    }
                }
                MyTextStyle1(
                    "Количесто: ${MainDB.finSpis.spisTyperasxodForSett.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp)
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                    MyOneVopros(dialLay, "Введите название типа расхода:", "Добавить", "Тип расхода") {
                        MainDB.addFinFun.addTyperasxod(it)
                    }
                }
            }
            MyList(
                MainDB.finSpis.spisTyperasxodForSett,
                Modifier.weight(1f).padding(bottom = 10.dp)
            ) { ind, itemSettTyperasxod ->
                ComItemTypeRasxSett(itemSettTyperasxod, selection) { item, expanded ->
                    DropdownMenuItem(onClick = {
                        MyOneVopros(
                            dialLay,
                            "Введите новое название типа расхода:",
                            "Изменить",
                            "Тип расхода",
                            item.typer
                        ) {
                            MainDB.addFinFun.updTyperasxodName(item.id.toLong(), it)
                        }
                        expanded.value = false
                    }) {
                        Text(text = "Изменить", color = Color.White)
                    }
                    DropdownMenuItem(onClick = {
                        MainDB.addFinFun.updTyperasxodOpen(item.id.toLong(), item.open.not())
                        expanded.value = false
                    }) {
                        Text(text = if (item.open) "Закрыть" else "Открыть", color = Color.White)
                    }
                    if (item.countoper == 0L) {
                        MyDeleteDropdownMenuButton(expanded) {
                            MainDB.addFinFun.delTyperasxod(item.id.toLong())
                        }
                    }
                }.getComposable()
            }
        }
    }
}
