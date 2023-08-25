package MainTabs.Quest.Element

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyOutlinedTextField
import common.MyTextButtStyle1
import ru.ragefalcon.sharedcode.models.data.ItemMainParam
import viewmodel.QuestDB

fun PanAddOpisQuest(
    dialPan: MyDialogLayout,
    questDB: QuestDB,
    item: ItemMainParam? = null
) {
    val dialLayInner = MyDialogLayout()

    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.let { it.name } ?: "")) }
        val text_opis = remember { mutableStateOf(TextFieldValue(item?.let { it.stringparam } ?: "")) }
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название раздела описания", text_name)

                MyOutlinedTextField(
                    "Содержание",
                    text_opis,
                    Modifier.heightIn(200.dp, 500.dp),
                    TextAlign.Start
                )
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            item?.let {
                                questDB.addQuest.updMainparam(
                                    id = it.id.toLong(),
                                    name = text_name.value.text,
                                    str = text_opis.value.text,
                                    int = 2
                                )
                                dialPan.close()
                            } ?: run {
                                questDB.addQuest.addMainparam(
                                    name = text_name.value.text,
                                    opis = text_opis.value.text,
                                    prioritet = 2
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
