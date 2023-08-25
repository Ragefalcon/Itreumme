package MainTabs.Setting

import MainTabs.Setting.Element.PanAddValut
import MainTabs.Setting.Items.ComItemValutSett
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.models.data.ItemValut
import viewmodel.MainDB

class SettFinValutTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemValut>()

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                MyTextStyle1(
                    "Количесто: ${MainDB.finSpis.spisValut.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp)
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                    PanAddValut(dialLay)
                }
            }
            MyList(MainDB.finSpis.spisValut, Modifier.weight(1f).padding(bottom = 10.dp)) { _, itemValut ->
                ComItemValutSett(itemValut, selection) { item, expanded ->
                    DropdownMenuItem(onClick = {
                        PanAddValut(dialLay, item)
                        expanded.value = false
                    }) {
                        Text(text = "Изменить", color = Color.White)
                    }
                    if (item.countschet == 0L) {
                        MyDeleteDropdownMenuButton(expanded) {
                            MainDB.addFinFun.delValut(item.id.toLong())
                        }
                    }
                }.getComposable()
            }
        }
    }
}
