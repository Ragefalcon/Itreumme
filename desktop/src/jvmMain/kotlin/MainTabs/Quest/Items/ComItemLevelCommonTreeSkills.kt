package MainTabs.Quest.Items

import MainTabs.Avatar.Element.PanAddNodeTreeSkills
import MainTabs.Quest.Element.ShowOpisNodeTreeSkills
import MyDialog.MyDialogLayout
import MyShowMessage
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import extensions.ItemNodeTreeSkillsState
import extensions.ItemSkillsTreeLevelState
import ru.ragefalcon.sharedcode.models.data.ItemHandNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

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
            },
            onDoubleClick = {
            },
            styleSettings = itemTreeSkillStyleLevelState
        ) {
            BoxWithConstraints(Modifier.wrapContentSize()) {
                Column(Modifier.align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally) {
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
