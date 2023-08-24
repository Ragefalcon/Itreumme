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
import common.MyOutlinedTextFieldDouble
import common.MyTextButtStyle1
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemValut
import viewmodel.MainDB

fun PanAddValut(
    dialPan: MyDialogLayout,
    item: ItemValut? = null
) {
    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
        val text_cod = remember { mutableStateOf(TextFieldValue(item?.cod ?: "")) }
        val kurs = remember { mutableStateOf(TextFieldValue(item?.kurs?.roundToString(2) ?: "0.00")) }
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название валюты", text_name)
                MyOutlinedTextField("Код валюты", text_cod)
                MyOutlinedTextFieldDouble("Курс валюты", kurs)
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    if ((text_name.value.text != "") && (text_cod.value.text != "") && ((kurs.value.text.toDoubleOrNull() ?: 0.0) != 0.0)){
                        if (item != null && item.id != "-1") {
                            MyTextButtStyle1("Изменить", Modifier.padding(start = 5.dp)) {
                                MainDB.addFinFun.updValut(
                                    item.id.toLong(),
                                    text_name.value.text,
                                    text_cod.value.text,
                                    kurs.value.text.toDoubleOrNull() ?: 0.0,
                                )
                                dialPan.close()
                            }
                        } else {
                            MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                                MainDB.addFinFun.addValut(
                                    text_name.value.text,
                                    text_cod.value.text,
                                    kurs.value.text.toDoubleOrNull() ?: 0.0,
                                )
                                dialPan.close()
                            }
                        }
                    }
                }
            }
        }
    }
    dialPan.show()
}

