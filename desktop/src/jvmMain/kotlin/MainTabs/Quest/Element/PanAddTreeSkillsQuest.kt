package MainTabs.Quest.Element


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
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatQuestElementVisible
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.QuestVM

fun PanAddTreeSkillsQuest(
    dialPan: MyDialogLayout,
    item: ItemTreeSkillsQuest? = null
) {
    val CB_spisTypeTreeSkills = MyComboBox(TypeTreeSkills.values().toList(), nameItem = { it.nameType })

    val CB_spisStartVisible =
        MyComboBox(TypeStatQuestElementVisible.values().toList(), nameItem = { it.nameType }).apply {
            item?.let { itemTree ->
                TypeStatQuestElementVisible.getType(itemTree.visibleStat)?.let {
                    select(it)
                }
            }
        }

    val dialLayInner = MyDialogLayout()

    dialPan.dial = @Composable {
        QuestVM.withDBC { questDB ->
            val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
            val text_opis = remember { mutableStateOf(TextFieldValue(item?.opis ?: "")) }
            BackgroungPanelStyle1 {
                Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                    if (item == null) CB_spisTypeTreeSkills.show()

                    MyOutlinedTextField("Название дерева", text_name)

                    MyOutlinedTextField(
                        "Описание дерева",
                        text_opis,
                        Modifier.heightIn(200.dp, 500.dp),
                        TextAlign.Start
                    )

                    CB_spisStartVisible.show()

                    Row {

                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                        }

                        MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                            if (text_name.value.text != "")
                                item?.let {
                                    questDB.addQuest.updTreeSkills(
                                        id = it.id,
                                        idArea = -1,
                                        name = text_name.value.text,
                                        opis = text_opis.value.text,
                                        icon = -1L,
                                        visibleStat = CB_spisStartVisible.getSelected()?.run {
                                            if (this == TypeStatQuestElementVisible.VISIB) TypeStatTreeSkills.UNBLOCKNOW.codValue else this.codValue
                                        } ?: 0
                                    )
                                    dialPan.close()
                                } ?: run {
                                    questDB.addQuest.addTreeSkills(
                                        idArea = -1,
                                        name = text_name.value.text,
                                        idTypeTree = CB_spisTypeTreeSkills.getSelected()?.id ?: 1,
                                        opis = text_opis.value.text,
                                        icon = -1L,
                                        visibleStat = CB_spisStartVisible.getSelected()?.run {
                                            if (this == TypeStatQuestElementVisible.VISIB) TypeStatTreeSkills.UNBLOCKNOW.codValue else this.codValue
                                        } ?: 0
                                    )
                                    dialPan.close()
                                }
                        }
                    }
                }
            }
            dialLayInner.getLay()
        }
    }
    dialPan.show()
}
