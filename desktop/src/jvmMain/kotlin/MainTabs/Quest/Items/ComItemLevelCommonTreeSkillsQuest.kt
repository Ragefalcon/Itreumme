package MainTabs.Quest.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.MyCardStyle1
import common.PlateOrderLayout
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.QuestDB

class ComItemLevelCommonTreeSkillsQuest(
    val dialLay: MyDialogLayout,
    val questDB: QuestDB,
    val itemTS: ItemTreeSkillsQuest,
    val level: Long,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
    val listNode: List<ItemNodeTreeSkillsQuest>
) {
    @Composable
    fun getComposable() {
        MyCardStyle1(
            false, 0,
            {
            },
            onDoubleClick = {
            },
        ) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                    PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                        listNode.forEach { nodeTreeSkills ->
                            ComItemNodeTreeSkillsQuest(
                                nodeTreeSkills,
                                questDB,
                                TypeTreeSkills.TREE,
                                selection,
                                dialLay
                            ).getComposable()
                        }
                    }
                }
            }
        }
    }
}

class ComItemLevelCommonTreeSkillsCheckableQuest(
    val level: Long,
    val questDB: QuestDB,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
    val listNode: List<ItemNodeTreeSkillsQuest>,
) {
    @Composable
    fun getComposable() {
        MyCardStyle1(
            false, 0,
            {
            },
            onDoubleClick = {
            },
        ) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                    PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                        listNode.forEach { nodeTreeSkills ->
                            ComItemNodeTreeSkillsCheckableQuest(nodeTreeSkills, questDB, selection).getComposable()
                        }
                    }
                }
            }
        }
    }
}

class ComItemLevelCommonTreeSkillsForSelectQuest(
    val level: Long,
    val questDB: QuestDB,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
    val listNode: List<ItemNodeTreeSkillsQuest>,
) {

    @Composable
    fun getComposable() {
        MyCardStyle1(false, 0) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                    PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                        listNode.forEach { nodeTreeSkills ->
                            ComItemNodeTreeSkillsSelParentsQuest(nodeTreeSkills, questDB, selection).getComposable()
                        }
                    }
                }
            }
        }
    }
}
