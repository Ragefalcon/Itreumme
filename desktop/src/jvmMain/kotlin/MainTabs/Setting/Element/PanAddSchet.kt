package MainTabs.Setting.Element

import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyOutlinedTextField
import common.MyTextButtStyle1
import ru.ragefalcon.sharedcode.models.data.ItemSchet
import viewmodel.MainDB

fun PanAddSchet(
    dialPan: MyDialogLayout,
    item: ItemSchet? = null
) {
    val CB_spisValut = MyComboBox(MainDB.finSpis.spisValut, nameItem = { "${it.name}, ${it.cod}" })

    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название счета", text_name)
                CB_spisValut.show(Modifier.padding(3.dp))
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    if (text_name.value.text != "") MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        CB_spisValut.getSelected()?.id?.toLong()?.let { valid ->
                            MainDB.addFinFun.addSchet(
                                text_name.value.text,
                                valid,
                            )
                            dialPan.close()
                        }
                    }
                }
            }
        }
    }
    dialPan.show()
}

