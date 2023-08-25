package MainTabs.Time.Elements

import MyDialog.MyDialogLayout
import MyDialog.buttDatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.addUpdList
import extensions.format
import extensions.timeFromHHmmss
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB
import java.util.*

fun PanAddNapom(
    dialPan: MyDialogLayout,
    item: ItemNapom? = null,
    calendar: Boolean = false
) {
    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(item?.let { Date(it.data) } ?: MainDB.denPlanDate.value)
    val timeZvonok = mutableStateOf(item?.let { Date().timeFromHHmmss(it.time) } ?: Date())

    val timePiker1 = MyTimePicker(timeZvonok)

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название напоминания",
            text_name,
            "Описание напоминания",
            item?.id?.toLong() ?: -1,
            TableNameForComplexOpis.spisNapom,
            MainDB.styleParam.timeParam.denPlanTab.complexOpisForNapom,
            item?.let {
                MainDB.complexOpisSpis.run { if (calendar) spisComplexOpisForCalendarNapom else spisComplexOpisForNapom }
                    .getState().value?.get(it.id.toLong())?.toMutableStateList()
            },
        )

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(
                Modifier.padding(15.dp).fillMaxWidth(0.8F).heightIn(0.dp, this@BackgroungPanelStyle1.maxHeight * 0.8F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                complexOpis.show(this, dialLayInner)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    buttDatePicker(dialLayInner, dateInner)
                }
                Row {
                    timePiker1.show()
                }
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            complexOpis.listOpis.addUpdList { opis ->
                                item?.let {
                                    MainDB.addTime.updNapom(
                                        id = it.id.toLong(),
                                        name = text_name.value.text,
                                        data = dateInner.value.time,
                                        opis = opis,
                                        time = timeZvonok.value.format("HH:mm"),
                                    )
                                } ?: run {
                                    MainDB.addTime.addNapom(
                                        name = text_name.value.text,
                                        gotov = false,
                                        data = dateInner.value.time,
                                        opis = opis,
                                        time = timeZvonok.value.format("HH:mm"),
                                        idplan = -1L,
                                        idstap = -1L
                                    )
                                }
                            }
                        dialPan.close()
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}
