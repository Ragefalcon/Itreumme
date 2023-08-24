package MainTabs.Avatar.Element


import MyDialog.MyDialogLayout
import MyDialog.MyEmptyPanel
import MyDialog.buttDatePicker
import MyShowMessage
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import common.*
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemCharacteristic
import viewmodel.MainDB
import java.util.*

fun PanAddCharacteristic(
    dialPan: MyDialogLayout,
    item: ItemCharacteristic? = null
) {
    val dialLayInner = MyDialogLayout()
    val expandedStartValue = mutableStateOf((item?.startStat ?: 0L) != 0L)

    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.let { it.name } ?: "")) }
        val text_opis = remember { mutableStateOf(TextFieldValue(item?.let { it.opis } ?: "")) }
        val startValue = remember {
            mutableStateOf(TextFieldValue(item?.startStat?.toString() ?: "0"))
        }
        LaunchedEffect(expandedStartValue.value){
            if (expandedStartValue.value) MyShowMessage(dialLayInner,
                "Т.к. один балл характеристики соответствует 10 часам, то введенное значение будет поделено на 10.\n" +
                        "Помните!!! Что если вы не вели точного учета потраченного времени, оценка потраченного времени " +
                        "на вскидку может иметь очень большую погрешность, что в свою очередь может создать ложное впечатление" +
                        " об уровне навыка, так что будьте осторожны указывая это значение.")
        }
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название характеристики", text_name)
                MyOutlinedTextField(
                    "Описание характеристики",
                    text_opis,
                    Modifier.heightIn(200.dp, 500.dp),
                    TextAlign.Start
                )
                MyCheckbox(expandedStartValue, "Указать количество часов уделенных характеристике до начала учета в Itreumme", Modifier.padding(end = 15.dp))
                if (expandedStartValue.value) {
                    MyOutlinedTextFieldInt("", startValue)
                }

                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            item?.let {
                                MainDB.addAvatar.updCharacteristic(
                                    id = it.id,
                                    name = text_name.value.text,
                                    opis = text_opis.value.text,
                                    startStat = if (expandedStartValue.value) startValue.value.text.toLong() else 0L,
                                )
                                dialPan.close()
                            } ?: run {
                                val idNew = MainDB.addAvatar.addCharacteristic(
                                    name = text_name.value.text,
                                    opis = text_opis.value.text,
                                    startStat = startValue.value.text.toLong(),
                                )
                                dialPan.close()
                                if (idNew > 0L) MyEmptyPanel(dialPan, false) { dialInner, closeFun ->
                                    MyTextButtStyle1("Привязать проекты и этапы к характеристике \"${text_name.value.text}\"") {
                                        closeFun()
                                        MainDB.avatarFun.setSelectedIdForPrivsCharacteristic(idNew)
                                        PanPrivsGoal(idNew,dialPan,true)
                                    }
                                    MyTextButtStyle1("Пока не привязывать") {
                                        closeFun()
                                    }
                                }
                            }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}
