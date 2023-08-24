package MainTabs.Quest.Items

import MyDialog.MyDialogLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(
            false, 0,
            {
//            selection.selected = item
            },
            onDoubleClick = {
//            item.sver = item.sver.not()
//            expandedOpis.value = !expandedOpis.value
            },
//            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
//                    with(LocalDensity.current) {
//                        MyListRow(
//                            listNode,
//                            Modifier.padding(vertical = 8.dp).heightIn(0.dp, 280.dp),
//                            maxWidth = this@BoxWithConstraints.maxWidth.toPx().toInt()
//                        ) { nodeTreeSkills ->
                    PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                        listNode.forEach { nodeTreeSkills ->
                            ComItemNodeTreeSkillsQuest(
                                nodeTreeSkills,
                                questDB,
                                TypeTreeSkills.TREE,
                                selection,
                                dialLay).getComposable()
                        }
                    }
//                    }
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(
            false, 0,
            {
//            selection.selected = item
            },
            onDoubleClick = {
//            item.sver = item.sver.not()
//            expandedOpis.value = !expandedOpis.value
            },
//            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
//                    MyListRow(
//                        listNode,
//                        Modifier.padding(top = 8.dp).heightIn(0.dp, 150.dp),
//                        maxWidth = this@BoxWithConstraints.maxWidth.value.toInt()
//                    ) { nodeTreeSkills -> //.heightIn(0.dp, 150.dp) .height(150.dp)
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

    @OptIn(ExperimentalFoundationApi::class)
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
