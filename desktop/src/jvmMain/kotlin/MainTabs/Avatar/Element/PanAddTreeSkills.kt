package MainTabs.Avatar.Element

import MyDialog.MyDialogLayout
import adapters.MyComboBox
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
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

fun PanAddTreeSkills(
    dialPan: MyDialogLayout,
    item: ItemTreeSkill? = null
) {
    val CB_spisTypeTreeSkills = MyComboBox(TypeTreeSkills.values().toList(), nameItem = { it.nameType })
//        .apply {
//            item?.govorun_id?.let { cod ->
//                questDB.spisGovorun.getState().value?.find { it.id.toLong() == cod }?.let {
//                    select(it)
//                }
//            }
//        }
    val dialLayInner = MyDialogLayout()
//        val checkAvtor =  mutableStateOf((item?.govorun_id ?: -2L) != -1L )
    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
        val text_opis = remember { mutableStateOf(TextFieldValue(item?.opis ?: "")) }
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//                    RowVA {
//                        Checkbox(checkAvtor.value, onCheckedChange = {
//                            checkAvtor.value = it //checkAvtor.value.not()
//                        })
                if (item == null) CB_spisTypeTreeSkills.show()
//                    }
                MyOutlinedTextField("Название дерева", text_name)
                MyOutlinedTextField(
                    "Описание дерева",
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
                                MainDB.addAvatar.updTreeSkills(
                                    id = it.id.toLong(),
                                    id_area = -1,
                                    name = text_name.value.text,
                                    opis = text_opis.value.text,
                                    icon = -1L
                                )
                                dialPan.close()
                            } ?: run {
                                MainDB.addAvatar.addTreeSkills(
                                    id_area = -1,
                                    name = text_name.value.text,
                                    id_type_tree = CB_spisTypeTreeSkills.getSelected()?.id?.toLong() ?: -1,
                                    opis = text_opis.value.text,
                                    open_edit = TypeStatTreeSkills.OPEN_EDIT.codValue,
                                    icon = -1L
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
