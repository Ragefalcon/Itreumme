package MainTabs.Quest.Element

import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.RowVA
import viewmodel.QuestDB

@Composable
fun QuestOpis(dialLay: MyDialogLayout, questDB: QuestDB, modifier: Modifier = Modifier) {
    Column(modifier.border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp)).padding(5.dp)) {
        questDB.spisQuest.spisMainParam.getState().value?.let { list ->
            list.find { it.name == "name" }?.let {
                MyTextToggleEdit("Название квеста", it.stringparam) { newNameQuest ->
                    questDB.addQuest.updMainparam(newNameQuest, "name")
                }
            }
            MyList(questDB.spisQuest.spisMainParam, Modifier.weight(1f, false)) { _, item ->
                if (item.name != "name") {
                    MyTextToggleEdit2(
                        if (item.name == "opis") "Описание" else item.name,
                        item.stringparam,
                        dropMenu = { exp ->
                            MyDropdownMenuItem(exp, "Изменить") {
                                PanAddOpisQuest(dialLay, questDB, item)
                            }
                            MyDeleteDropdownMenuButton(exp) {
                                questDB.addQuest.deleteMainparam(item.id.toLong())
                            }
                        }) { newNameQuest ->
                        questDB.addQuest.updMainparam(newNameQuest, item.id.toLong())
                    }
                }
            }
            RowVA {
                MyTextButtStyle1("+") {
                    PanAddOpisQuest(dialLay, questDB)
                }
            }
        }
    }
}