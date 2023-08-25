package MainTabs.Journal.Element

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyComplexOpisWithNameBox
import common.MyTextButtStyle1
import extensions.addUpdList
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB

fun PanAddBloknot(
    dialPan: MyDialogLayout,
    item: ItemBloknot? = null
) {
    val dialLayInner = MyDialogLayout()

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название блокнота",
            text_name,
            "Описание блокнота",
            item?.id?.toLong() ?: -1,
            TableNameForComplexOpis.spisBloknot,
            MainDB.styleParam.journalParam.complexOpisForBloknot,
            item?.let {
                MainDB.complexOpisSpis.spisComplexOpisForBloknot.getState().value?.get(it.id.toLong())
                    ?.toMutableStateList()
            },
        )

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                complexOpis.show(this, dialLayInner)
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            complexOpis.listOpis.addUpdList { opis ->
                                item?.let {
                                    MainDB.addJournal.updBloknot(
                                        id = it.id.toLong(),
                                        name = text_name.value.text,
                                        opis = opis
                                    )
                                } ?: run {
                                    MainDB.addJournal.addBloknot(
                                        name = text_name.value.text,
                                        opis = opis
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
