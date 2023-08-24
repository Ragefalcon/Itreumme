package MainTabs.Quest.Element

import androidx.compose.material.Text
import MainTabs.Quest.Items.ComItemLevelCommonTreeSkillsQuest
import MainTabs.Quest.Items.ComItemLevelTreeSkillsQuest
import MainTabs.Quest.Items.ComItemNodeTreeSkillsQuest
import MyDialog.MyDialogLayout
import MyList
import MyListPlate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.mouseClickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import common.tests.IconNode
import extensions.RowVA
import extensions.mouseDoubleClick
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.QuestDB
import java.io.File

class ComItemTreeSkillsQuestOpened(
    val dialLay: MyDialogLayout,
    val questDB: QuestDB,
    val itemTS: ItemTreeSkillsQuest,
    val selection: SingleSelectionType<ItemTreeSkillsQuest>,
//    val doubleClick: (ItemTreeSkillsQuest)->Unit = {},
    val openTree: (ItemTreeSkillsQuest) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(ItemTreeSkillsQuest, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(true)

    val selectionNodeTreeSkills = SingleSelectionType<ItemNodeTreeSkillsQuest>()

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
//        rememberCoroutineScope().launch {
//            delay(10)
//            println("expandedOpis.value = true")
//            expandedOpis.value = true
//        }
        MyCardStyle1(false, 0, {
//            selection.selected = itemTS
        }, {
//            item.sver = item.sver.not()
//            expandedOpis.value = !expandedOpis.value
        },
            backColor = MyColorARGB.colorMyMainTheme.toColor(),
            dropMenu = { exp -> dropMenu(itemTS, exp) }
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA(Modifier.mouseDoubleClick({}, onDoubleClick = { openTree(itemTS) })) {
                IconNode(
                        File(System.getProperty("user.dir"), "Quests").path,
                        "${itemTS.icon}.jpg",
                        "free-icon-tree-shape-42090.png",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "(${itemTS.id_type_tree})${itemTS.name}",
                        modifier = Modifier.padding(5.dp).weight(1f),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                    )
                    MyTextStyle1(
                        "\uD83D\uDD6E",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(end = 20.dp)
                            .mouseClickable {
                                openTree(itemTS)
                            })
                }
                questDB.spisQuest.spisNodeTreeSkills.getState().value?.let {  spisNodeTreeSkills ->
                    BoxExpand(
                        expandedOpis,
                        Modifier.myModWithBound1(),
                        Modifier.fillMaxWidth()
                    ) {
                        Column {
                            when (TypeTreeSkills.getType(itemTS.id_type_tree)) {
                                TypeTreeSkills.KIT -> {
                                    spisNodeTreeSkills.get(1L)?.let { listNode ->
                                        MyListPlate(
                                            listNode,
                                            Modifier.weight(1f).padding(horizontal = 10.dp),
                                            alignmentCenter = true
                                        ) { itemNodeTreeSkills ->
                                            ComItemNodeTreeSkillsQuest(
                                                itemNodeTreeSkills,
                                                questDB,
                                                TypeTreeSkills.KIT,
                                                selectionNodeTreeSkills,
                                                dialLay
                                            ).getComposable()
                                        }
                                    }
                                    MyTextButtStyle1(
                                        "+",
                                        modifier = Modifier.padding(end = 15.dp)
                                    ) {
                                        PanAddNodeTreeSkillsQuest(
                                            dialLay,
                                            questDB,
                                            itemTS.id,
                                            typeTree = TypeTreeSkills.KIT
                                        )
                                    }
                                }
                                TypeTreeSkills.LEVELS -> {
                                    MyList(
                                        questDB.spisQuest.spisLevelTreeSkills,
                                        Modifier.weight(1f).padding(horizontal = 5.dp)
                                    ) { ind, itemLevelTreeSkills ->
                                        ComItemLevelTreeSkillsQuest(
                                            dialLay, questDB,
                                            itemLevelTreeSkills, selectionNodeTreeSkills,
                                            spisNodeTreeSkills.get(itemLevelTreeSkills.num_level)
                                                ?.sortedByDescending { it.must_node }
                                                ?: listOf()
                                        ) { item, expanded ->
                                            MyDropdownMenuItem(expanded, "Изменить") {
                                                PanAddLevelTreeSkillsQuest(dialLay, questDB, itemTS.id, item)
                                            }
                                            MyDropdownMenuItem(expanded, "Вставить уровень") {
                                                PanAddLevelTreeSkillsQuest(
                                                    dialLay,
                                                    questDB,
                                                    itemTS.id,
                                                    level = item.num_level
                                                )
                                            }
                                            if (item.countNode == 0L) MyDeleteDropdownMenuButton(expanded) {
                                                questDB.addQuest.delLevelTreeSkills(item.id)
                                            }

                                        }.getComposable()
                                    }
                                    MyTextButtStyle1(
                                        "+",
                                        modifier = Modifier.padding(end = 15.dp)
                                    ) {
                                        PanAddLevelTreeSkillsQuest(dialLay, questDB, itemTS.id)
                                    }
                                }
                                TypeTreeSkills.TREE -> {
                                    spisNodeTreeSkills.toList()?.sortedBy { it.first }
                                        ?.let { listLevels ->
                                            MyList(
                                                listLevels,
                                                Modifier.weight(1f).padding(horizontal = 5.dp)
                                            ) { ind, levelTreeSkills ->
                                                ComItemLevelCommonTreeSkillsQuest(
                                                    dialLay,
                                                    questDB,
                                                    itemTS,
                                                    levelTreeSkills.first,
                                                    selectionNodeTreeSkills,
                                                    levelTreeSkills.second
                                                ).getComposable()
                                            }
                                        }
                                    MyTextButtStyle1(
                                        "+",
                                        modifier = Modifier.padding(end = 15.dp)
                                    ) {
                                        PanAddNodeTreeSkillsQuest(
                                            dialLay,
                                            questDB,
                                            itemTS.id,
                                            typeTree = TypeTreeSkills.TREE
                                        )
                                    }
                                }
                                null -> {

                                }
                            }
                        }
                    }
                }
//                }
            }
        }
    }
}
