package MainTabs.Avatar.Element

import MainTabs.Quest.Items.ComItemLevelCommonTreeSkillsCheckable
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
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.ItemNodeTreeSkills
import viewmodel.MainDB

fun PanSelectNodeParents(
    dialPan: MyDialogLayout,
    id_tree: Long,
    spisSelId: MutableState<Array<Long>>,
    level: MutableState<Long>,
    item: ItemNodeTreeSkills? = null
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<ItemNodeTreeSkills>()
    dialPan.dial = @Composable {
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MainDB.avatarSpis.spisNodeTreeSkillsForSelection.getState().value?.toList()?.sortedBy { it.first }
                    ?.let { listLevels ->
                        MyList(
                            listLevels,
                            Modifier.weight(1f).padding(horizontal = 5.dp)
                        ) { ind, levelTreeSkills ->
                            ComItemLevelCommonTreeSkillsCheckable(
                                levelTreeSkills.first,
                                selection,
                                levelTreeSkills.second
                            ).getComposable()
                        }
                    }
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1("Выбрать") {
                        val rezSelect = MainDB.avatarFun.getSelectIdNodeTreeSkills()
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
