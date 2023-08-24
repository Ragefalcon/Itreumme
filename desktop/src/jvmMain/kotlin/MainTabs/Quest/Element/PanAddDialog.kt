package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComItemGovorunPlate
import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
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
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemDialog
import viewmodel.QuestVM

fun PanAddDialog(
    dialPan: MyDialogLayout,
    item: ItemDialog? = null
) {
    QuestVM.openQuestDB?.let { questDB ->

        val CB_spisAvtor = MyComboBox(
            questDB.spisQuest.spisGovorun,
            comItem = { itemCB, hover -> ComItemGovorunPlate(itemCB,questDB.dirQuest) },
            nameItem = { it.name }).apply {
            item?.govorun_id?.let { cod ->
                questDB.spisQuest.spisGovorun.getState().value?.find { it.id.toLong() == cod }?.let {
                    select(it)
                }
            }
        }
        val dialLayInner = MyDialogLayout()
        val checkAvtor =  mutableStateOf((item?.govorun_id ?: -2L) != -1L )
        dialPan.dial = @Composable {
            val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
            val text_main = remember { mutableStateOf(TextFieldValue(item?.maintext ?: "")) }
            BackgroungPanelStyle1 { //modif ->

                Column(Modifier.padding(15.dp).widthIn(0.dp,this.maxWidth*0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
                    RowVA {
                        Checkbox(checkAvtor.value, onCheckedChange = {
                            checkAvtor.value = it //checkAvtor.value.not()
                        })
                        CB_spisAvtor.show()
                    }
                    MyOutlinedTextField("Название диалога", text_name)
                    MyOutlinedTextField(
                        "Основной текст",
                        text_main,
                        Modifier.heightIn(200.dp, this@BackgroungPanelStyle1.maxHeight*0.5f),
                        TextAlign.Start
                    )
                    Row {
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                        }
                        MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                            if (text_name.value.text != "")
                                item?.let {
                                    questDB.addQuest.updDialog(
                                        id = it.id.toLong(),
                                        name = text_name.value.text,
                                        maintext = text_main.value.text,
                                        govorun_id = if (checkAvtor.value) CB_spisAvtor.getSelected()?.id?.toLong() ?: -1 else -1,
                                    )
                                    dialPan.close()
                                } ?: run {
                                    questDB.addQuest.addDialog(
                                        name = text_name.value.text,
                                        maintext = text_main.value.text,
                                        govorun_id = if (checkAvtor.value) CB_spisAvtor.getSelected()?.id?.toLong() ?: -1 else -1,
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
}
