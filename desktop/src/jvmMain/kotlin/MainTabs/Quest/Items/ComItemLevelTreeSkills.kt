package MainTabs.Quest.Items

import MainTabs.Avatar.Element.PanAddNodeTreeSkills
import MainTabs.Quest.Element.ShowOpisNodeTreeSkills
import MyDialog.MyDialogLayout
import MyShowMessage
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemHandNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemLevelTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

@Composable
fun ComItemLevelTreeSkills(
    dialLay: MyDialogLayout,
    itemTS: ItemTreeSkill,
    item: ItemLevelTreeSkills,
    selection: SingleSelectionType<ItemNodeTreeSkills>,
    listNode: List<ItemNodeTreeSkills>,
    openEdit: Boolean,
    itemTreeSkillStyleLevelState: ItemSkillsTreeLevelState,
    itemNodeTreeSkillStyleState: ItemNodeTreeSkillsState,
    dropMenu: @Composable ColumnScope.(ItemLevelTreeSkills, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    with(itemTreeSkillStyleLevelState) {
        val expandedDropMenu = remember { mutableStateOf(false) }
        val expandedOpis = remember { mutableStateOf(!item.sver) }
        MyCardStyle1(
            false, 0,
            backBrush = if (itemTS.open_edit) {
                if (item.open == 1L) null
                else background_brush_block
            } else {
                if (item.open == 1L) background_brush_no_edit
                else background_brush_no_edit_block
            },
            borderBrush = if (itemTS.open_edit) {
                if (item.open == 1L) null
                else border_brush_block
            } else {
                if (item.open == 1L) border_brush_no_edit
                else border_brush_no_edit_block
            },

            styleSettings = itemTreeSkillStyleLevelState
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA(Modifier.mouseDoubleClick(
                    onClick = {}, onDoubleClick = {
                        item.sver = item.sver.not()
                        expandedOpis.value = !expandedOpis.value
                    }, rightClick = { expandedDropMenu.value = true }
                )) {
                    Text(
                        text = "Уровень ${item.num_level}. ${item.name}",
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 5.dp).weight(1f),
                        style = mainTextStyle
                    )
                    if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = "*",
                        style = noQuestText
                    )
                    if (openEdit && item.quest_key_id == 0L) MyButtDropdownMenuStyle2(
                        Modifier.padding(end = 10.dp).padding(vertical = 0.dp),
                        expandedDropMenu,
                        buttMenu
                    ) {
                        dropMenu(item, expandedDropMenu)
                    }
                    if (item.opis != "") RotationButtStyle1(
                        expandedOpis,
                        Modifier.padding(start = 0.dp, end = 10.dp),
                        color = boxOpisStyleState.colorButt
                    ) {
                        item.sver = item.sver.not()
                    }
                    if (openEdit) MyTextButtStyle1(
                        "+",
                        modifier = Modifier.padding(end = 15.dp),
                        width = 50.dp,
                        height = 40.dp,
                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.avatarParam.skillTab.buttAddNode)
                    ) {
                        PanAddNodeTreeSkills(
                            dialLay,
                            itemTS,
                            item.num_level, typeTree = TypeTreeSkills.LEVELS
                        )
                    }
                }
                if ((item.opis != "")) MyBoxOpisStyle(expandedOpis, item.opis, boxOpisStyleState)
                PlateOrderLayout(Modifier.padding(bottom = 5.dp), alignmentCenter = true) {
                    listNode.forEach { nodeTreeSkills ->
                        if (nodeTreeSkills.complete != TypeStatNodeTree.INVIS) ComItemNodeLevelTreeSkills(
                            nodeTreeSkills,
                            TypeTreeSkills.LEVELS,
                            selection,
                            doubleClick = {
                                ShowOpisNodeTreeSkills(dialLay, typeTree = TypeTreeSkills.LEVELS, item = it)
                            }, itemNodeTreeSkillStyleState
                        ) { item, exp ->
                            MyDropdownMenuItem(exp, "Показать описание") {
                                ShowOpisNodeTreeSkills(dialLay, typeTree = TypeTreeSkills.LEVELS, item = item)
                            }
                            if (item.complete != TypeStatNodeTree.COMPLETE && openEdit && item.quest_key_id == 0L) MyDropdownMenuItem(
                                exp,
                                "Изменить"
                            ) {
                                PanAddNodeTreeSkills(
                                    dialLay,
                                    itemTS,
                                    typeTree = TypeTreeSkills.LEVELS, item = item
                                )
                            }
                            if (item is ItemHandNodeTreeSkills && item.open) MyCompleteDropdownMenuButton(
                                exp,
                                item.complete == TypeStatNodeTree.COMPLETE
                            ) {
                                MainDB.addAvatar.completeHandNode(item, TypeTreeSkills.LEVELS)
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
                RowVA(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = "${item.completeCountNode - item.mustCompleteCountNode}/${item.countNode - item.mustCountNode}(~${
                            ((item.countNode - item.mustCountNode) * item.proc_porog / 100.0).roundToString(
                                1
                            )
                        }-${(item.proc_porog).toInt()}%)",
                        modifier = Modifier.padding(start = 5.dp, top = 0.dp, bottom = 5.dp, end = 5.dp),
                        style = infoText
                    )
                    Text(
                        text = "${item.mustCompleteCountNode}/${item.mustCountNode}",
                        modifier = Modifier.padding(start = 5.dp, top = 0.dp, bottom = 5.dp, end = 5.dp),
                        style = infoText.copy(
                            color = INFO_COLOR_1
                        )
                    )
                    Text(
                        text = "${item.completeCountNode}/${item.countNode}",
                        modifier = Modifier.padding(start = 5.dp, top = 0.dp, bottom = 5.dp, end = 20.dp),
                        style = infoText.copy(
                            color = INFO_COLOR_2
                        )
                    )
                }
            }
        }
    }
}
