package MainTabs.Time.Elements

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.addUpdList
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB
import java.util.*

fun PanAddVxod(
    dialPan: MyDialogLayout,
    item: ItemVxod? = null
) {
    val dialLayInner = MyDialogLayout()
    val stat = MySelectStat(item?.stat ?: 0L)

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название входящего",
            text_name,
            "Описание входящего",
            item?.id?.toLong() ?: -1,
            TableNameForComplexOpis.spisVxod,
            MainDB.styleParam.timeParam.vxodTab.complexOpisForVxod,
            item?.let {
                MainDB.complexOpisSpis.spisComplexOpisForVxod.getState().value?.get(it.id.toLong())
                    ?.toMutableStateList()
            },
        )


    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                complexOpis.show(this, dialLayInner)
                stat.show()
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            complexOpis.listOpis.addUpdList { opis ->
                                item?.let {
                                    MainDB.addTime.updVxod(
                                        id = it.id.toLong(),
                                        name = text_name.value.text,
                                        data = it.data,
                                        opis = opis,
                                        stat = stat.value
                                    )
                                } ?: run {
                                    MainDB.addTime.addVxod(
                                        name = text_name.value.text,
                                        data = Date().time,
                                        opis = opis,
                                        stat = stat.value
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
