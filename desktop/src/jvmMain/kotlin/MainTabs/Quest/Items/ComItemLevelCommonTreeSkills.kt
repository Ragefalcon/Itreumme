package MainTabs.Quest.Items

import MainTabs.Avatar.Element.PanAddNodeTreeSkills
import MainTabs.Quest.Element.ShowOpisNodeTreeSkills
import MyDialog.MyDialogLayout
import MyShowMessage
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import extensions.ItemNodeTreeSkillsState
import extensions.ItemSkillsTreeLevelState
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemLevelCommonTreeSkills(
    dialLay: MyDialogLayout,
    itemTS: ItemTreeSkill,
    level: Long,
    selection: SingleSelectionType<ItemNodeTreeSkills>,
    listNode: List<ItemNodeTreeSkills>,
    openEdit: Boolean,
    visibleBinding: Boolean,
    itemTreeSkillStyleLevelState: ItemSkillsTreeLevelState,
    itemNodeTreeSkillStyleState: ItemNodeTreeSkillsState
) {
    with(itemTreeSkillStyleLevelState) {
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
            styleSettings = itemTreeSkillStyleLevelState
        ) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
//                    with(LocalDensity.current) {
//                        MyListRow(
//                            listNode,
//                            Modifier.padding(vertical = 8.dp).heightIn(0.dp, 280.dp),
//                            maxWidth = this@BoxWithConstraints.maxWidth.toPx().toInt()
//                        ) { nodeTreeSkills ->
                    PlateOrderLayout(Modifier.padding(vertical = 5.dp), alignmentCenter = true) {
                        listNode.forEach { nodeTreeSkills ->
                            if (nodeTreeSkills.complete != TypeStatNodeTree.INVIS) ComItemNodeLevelTreeSkills(
                                nodeTreeSkills,
                                TypeTreeSkills.TREE,
                                selection,
                                doubleClick = {
                                    ShowOpisNodeTreeSkills(dialLay, typeTree = TypeTreeSkills.TREE, item = it)
                                }, itemNodeTreeSkillStyleState,
                                visibleBinding = visibleBinding
                            ) { item, exp ->
                                MyDropdownMenuItem(exp, "Показать описание") {
                                    ShowOpisNodeTreeSkills(dialLay, typeTree = TypeTreeSkills.TREE, item = item)
                                }
                                if (item.complete != TypeStatNodeTree.COMPLETE && openEdit && item.quest_key_id == 0L) MyDropdownMenuItem(
                                    exp,
                                    "Изменить"
                                ) {
                                    PanAddNodeTreeSkills(
                                        dialLay,
                                        itemTS,
                                        typeTree = TypeTreeSkills.TREE, item = item
                                    )
                                }
                                if (item is ItemHandNodeTreeSkills && item.open) MyCompleteDropdownMenuButton(
                                    exp,
                                    item.complete == TypeStatNodeTree.COMPLETE
                                ) {
                                    MainDB.addAvatar.completeHandNode(item, TypeTreeSkills.TREE)
                                }
                                if (openEdit) MyDeleteDropdownMenuButton(exp) {
                                    if (item.quest_key_id == 0L) {
                                        MainDB.addAvatar.delNodeTreeSkills(item)
                                    } else {
                                        MyShowMessage(
                                            dialLay,
                                            "Этот элемент из квеста, его можно удалить только вместе с квестом."
                                        )
                                    }
                                }
                            }
                        }
                    }
//                    }
                }
            }
        }
    }
}

class ComItemLevelCommonTreeSkillsCheckable(
    val level: Long,
    val selection: SingleSelectionType<ItemNodeTreeSkills>,
    val listNode: List<ItemNodeTreeSkills>,
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
                    PlateOrderLayout(Modifier.padding(vertical = 5.dp), alignmentCenter = true) {
                        listNode.forEach { nodeTreeSkills ->
                            if (nodeTreeSkills.complete != TypeStatNodeTree.INVIS) ComItemNodeLevelTreeSkillsCheckable(
                                nodeTreeSkills,
                                selection
                            ).getComposable()
                        }
                    }
                }
            }
        }
    }
}
