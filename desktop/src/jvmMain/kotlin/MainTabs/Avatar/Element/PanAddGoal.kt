package MainTabs.Avatar.Element

import MyDialog.MyDialogLayout
import MyDialog.buttDatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.BackgroungPanelStyle1
import common.BoxExpand
import common.MyOutlinedTextField
import common.MyTextButtStyle1
import extensions.add
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.models.data.ItemGoal
import viewmodel.MainDB
import java.util.*

fun PanAddGoal(
    dialPan: MyDialogLayout,
    item: ItemGoal? = null
) {
    val dialLayInner = MyDialogLayout()

    val expandedSroki = mutableStateOf(false)
    val dateEnd = mutableStateOf(Date().add(14, TimeUnits.DAY))

    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.let { it.name } ?: "")) }
        val text_opis = remember { mutableStateOf(TextFieldValue(item?.let { it.opis } ?: "")) }
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название цели", text_name)
                MyOutlinedTextField(
                    "Описание цели",
                    text_opis,
                    Modifier.heightIn(200.dp, 500.dp),
                    TextAlign.Start
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(30.dp))
                    Checkbox(
                        expandedSroki.value,
                        modifier = Modifier.padding(start = 20.dp),
                        onCheckedChange = {
                            expandedSroki.value = it
                        })
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = " Успеть до:",
                        style = TextStyle(color = Color(0xFFFFF7D9)),
                        fontSize = 15.sp
                    )
                }
                BoxExpand(expandedSroki) {
                    buttDatePicker(dialLayInner, dateEnd)
                }

                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            item?.let {
                                MainDB.addAvatar.updGoal(
                                    id = it.id.toLong(),
                                    name = text_name.value.text,
                                    data1 = it.data1,
                                    data2 = if (expandedSroki.value) dateEnd.value.time else 0,
                                    opis = text_opis.value.text,
                                    foto = 0
                                )
                                dialPan.close()
                            } ?: run {
                                MainDB.addAvatar.addGoal(
                                    name = text_name.value.text,
                                    data1 = Date().time,
                                    data2 = if (expandedSroki.value) dateEnd.value.time else 0,
                                    opis = text_opis.value.text,
                                    gotov = 0.0,
                                    foto = 0
                                )
                                dialPan.close()
                            }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}
