package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComItemLevelCommonTreeSkillsCheckableQuest
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import common.PlateOrderLayout
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import viewmodel.QuestDB

fun PanSelectNodeParentsQuest(
    dialPan: MyDialogLayout,
    questDB: QuestDB,
    spisSelId: MutableState<Array<Long>>,
    level: MutableState<Long>,
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<ItemNodeTreeSkillsQuest>()
    dialPan.dial = @Composable {
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                questDB.spisQuest.spisNodeTreeSkillsForSelection.getState().value?.toList()?.sortedBy { it.first }
                    ?.let { listLevels ->
//                        MyList(
//                            listLevels,
//                            Modifier.weight(1f).padding(horizontal = 5.dp)
//                        ) { levelTreeSkills ->
                        PlateOrderLayout(Modifier.weight(1f).padding(horizontal = 5.dp), alignmentCenter = true) {
                            listLevels.forEach { levelTreeSkills ->
                                ComItemLevelCommonTreeSkillsCheckableQuest(
                                    levelTreeSkills.first,
                                    questDB,
                                    selection,
                                    levelTreeSkills.second
                                ).getComposable()
                            }
                        }
                    }
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1("Выбрать") {
                        val rezSelect = questDB.questFun.getSelectIdNodeTreeSkills()
                        level.value = rezSelect.first + 1
                        spisSelId.value = rezSelect.second
                        dialPan.close()
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}
