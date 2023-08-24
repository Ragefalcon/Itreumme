package MainTabs.Quest.Element

import androidx.compose.material.Text
import MainTabs.Avatar.Element.PanAddLevelTreeSkills
import MainTabs.Avatar.Element.PanAddNodeTreeSkills
import MainTabs.Quest.Items.ComItemLevelCommonTreeSkills
import MainTabs.Quest.Items.ComItemLevelTreeSkills
import MainTabs.Quest.Items.ComItemNodeLevelTreeSkills
import MyDialog.MyDialogLayout
import MyList
import MyListPlate
import MyShowMessage
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemHandNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemTreeSkillsOpened(
    dialLay: MyDialogLayout,
    itemTS: ItemTreeSkill,
    openTree: (ItemTreeSkill) -> Unit = {},
    itemTreeSkillStyleState: ItemSkillsTreeState,
) {
    with(itemTreeSkillStyleState) {

        val visibleBinding = remember { mutableStateOf(false) }

        val selectionNodeTreeSkills = remember { SingleSelectionType<ItemNodeTreeSkills>() }
        MainDB.avatarSpis.spisTreeSkills.getState().value?.find { it.id == itemTS.id }?.let { itemFTS ->
            MyCardStyle1(
                false, 0,
                backBrush =// with (MainDB.avatarSpis.spisTreeSkills.getState().value?.find { it.id == itemTS.id }) {
                    if (itemFTS.completeCountNode == itemFTS.countNode) background_brush_complete else when (itemTS.stat) {
                    TypeStatTreeSkills.OPEN_EDIT -> null
                    TypeStatTreeSkills.VISIB -> background_brush_no_edit
                    TypeStatTreeSkills.COMPLETE -> null
                    TypeStatTreeSkills.UNBLOCKNOW -> background_brush_unblock
                    TypeStatTreeSkills.BLOCK -> background_brush_block
                    TypeStatTreeSkills.INVIS -> null
                    else -> null
                } ,
                borderBrush = if (itemFTS.completeCountNode == itemFTS.countNode) border_brush_complete else when (itemTS.stat) {
                    TypeStatTreeSkills.OPEN_EDIT -> null
                    TypeStatTreeSkills.VISIB -> border_brush_no_edit
                    TypeStatTreeSkills.COMPLETE -> null
                    TypeStatTreeSkills.UNBLOCKNOW -> border_brush_unblock
                    TypeStatTreeSkills.BLOCK -> border_brush_block
                    TypeStatTreeSkills.INVIS -> null
                    else -> null
                },
                modifierThen = if (itemTS.namequest == "") Modifier else Modifier.border(
                    width = BORDER_WIDTH_QUEST,
                    brush = border_quest,
                    shape = shapeCard
                ),
                styleSettings = itemTreeSkillStyleState
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (itemTS.namequest != "") {
                        MyShadowBox(quest_plate.shadow){
                            RowVA(
                                Modifier.fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                                    .withSimplePlate(quest_plate)
                                    .padding(3.dp)
                                    .padding(horizontal = 2.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    itemTS.namequest,
                                    Modifier.weight(1f, false),
                                    style = questNameText
                                )
                            }
                        }
                    }

                    RowVA(Modifier.padding(top = topPadding).mouseDoubleClick({}, onDoubleClick = { openTree(itemTS) })) {
                        MyShadowBox(icon_plate.shadow){
                            Image(
                                bitmap = useResource("free-icon-tree-shape-42090.png", ::loadImageBitmap),
                                "defaultAvatar",
                                Modifier
                                    .height(70.dp)
                                    .width(70.dp)
                                    .withSimplePlate(icon_plate)
                                    .clip(icon_plate.shape)
                                    .wrapContentSize(),
                                contentScale = ContentScale.Crop,// Fit,
                                colorFilter = ColorFilter.tint(ICON_TREE_COLOR)
                            )
                        }
/*
                    IconNode(
                        File(System.getProperty("user.dir"), "Quests").path,
                        "${itemTS.icon}.jpg",
                        "free-icon-tree-shape-42090.png",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
*/
                        Text(
                            text = itemTS.name,
                            modifier = Modifier.padding(start = 15.dp).weight(1f),
                            style = mainTextStyle
                        )

                        if (TypeTreeSkills.getType(itemTS.id_type_tree) == TypeTreeSkills.TREE) MyToggleButtIconStyle1(
                            "ic_baseline_visibility_off_24.xml",
                            "ic_baseline_visibility_24.xml",
                            sizeIcon = 35.dp,
                            value = visibleBinding,
                            modifier = Modifier.padding(start = 5.dp, end = 10.dp),
                            width = 40.dp,
                            height = 40.dp,
                            myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.avatarParam.skillTab.buttVisibleBinding)
//                    , enabledColor = MyColorARGB.colorStatTint_03.toColor()
                        )
                        Text(
                            text = "(${itemFTS.completeCountNode}/${itemFTS.countNode})",
                            modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 5.dp, end = 5.dp),
                            style = countStapText
                        )
                        MyTextButtSimpleStyle(
                            text = "\uD83D\uDD6E",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(end = 20.dp),
                            fontSize = 20.sp,
                            color = OPEN_COLOR
                        ) {
                            openTree(itemTS)
                        }
                    }
                    MainDB.avatarSpis.spisNodeTreeSkills.getState().value?.let { spisNodeTreeSkills ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            when (TypeTreeSkills.getType(itemTS.id_type_tree)) {
                                TypeTreeSkills.KIT -> {
                                    spisNodeTreeSkills[1L]?.let { listNode ->
//                                        MyList(
//                                            listNode,
//                                            Modifier.weight(1f).padding(horizontal = 60.dp)
//                                        ) { ind, itemNodeTreeSkills ->
                                        MainDB.styleParam.avatarParam.skillTab.itemSkillNode.getComposable(::ItemNodeTreeSkillsState) { itemNodeStyle ->
                                            MyListPlate(
                                                listNode,
                                                Modifier.weight(1f).padding(top = 10.dp).padding(horizontal = 10.dp),
                                                alignmentCenter = true
                                            ) { itemNodeTreeSkills ->
                                                if (itemNodeTreeSkills.complete != TypeStatNodeTree.INVIS) ComItemNodeLevelTreeSkills(
                                                    itemNodeTreeSkills,
                                                    TypeTreeSkills.KIT,
                                                    selectionNodeTreeSkills,
                                                    doubleClick = {
                                                        ShowOpisNodeTreeSkills(
                                                            dialLay,
                                                            typeTree = TypeTreeSkills.TREE,
                                                            item = it
                                                        )
                                                    }, itemNodeStyle
                                                ) { item, expanded ->
                                                    MyDropdownMenuItem(expanded, "Показать описание") {
                                                        ShowOpisNodeTreeSkills(
                                                            dialLay,
                                                            typeTree = TypeTreeSkills.KIT,
                                                            item = item
                                                        )
                                                    }
                                                    if (item.complete != TypeStatNodeTree.COMPLETE && itemTS.open_edit && item.quest_key_id == 0L) MyDropdownMenuItem(
                                                        expanded,
                                                        "Изменить"
                                                    ) {
                                                        PanAddNodeTreeSkills(
                                                            dialLay,
                                                            itemTS,
                                                            typeTree = TypeTreeSkills.KIT,
                                                            item = item
                                                        )
                                                    }
                                                    if (item is ItemHandNodeTreeSkills && item.open) MyCompleteDropdownMenuButton(
                                                        expanded,
                                                        item.complete == TypeStatNodeTree.COMPLETE
                                                    ) {
                                                        MainDB.addAvatar.completeHandNode(item, TypeTreeSkills.KIT)
                                                    }
                                                    if (itemTS.open_edit) MyDeleteDropdownMenuButton(expanded) {
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
                                    if (itemTS.open_edit) MyTextButtStyle1(
                                        "+",
                                        modifier = Modifier.padding(end = 15.dp),
                                        width = 50.dp,
                                        height = 50.dp,
                                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.avatarParam.skillTab.buttAddNode)
                                    ) {
                                        PanAddNodeTreeSkills(
                                            dialLay,
                                            itemTS,
                                            typeTree = TypeTreeSkills.KIT
                                        )
                                    }
                                }
                                TypeTreeSkills.LEVELS -> {
                                    MainDB.styleParam.avatarParam.skillTab.itemSkillLevel.getComposable(::ItemSkillsTreeLevelState) { itemStyle ->
                                        MainDB.styleParam.avatarParam.skillTab.itemSkillNode.getComposable(::ItemNodeTreeSkillsState) { itemNodeStyle ->
                                            MyList(
                                                MainDB.avatarSpis.spisLevelTreeSkills,
                                                Modifier.weight(1f).padding(horizontal = 5.dp).padding(top = 5.dp)
                                            ) { ind, itemLevelTreeSkills ->
                                                ComItemLevelTreeSkills(
                                                    dialLay, itemTS, itemLevelTreeSkills, selectionNodeTreeSkills,
                                                    spisNodeTreeSkills.get(itemLevelTreeSkills.num_level)
                                                        ?.sortedByDescending { it.must_node }
                                                        ?: listOf(),
                                                    itemTS.open_edit,
                                                    itemTreeSkillStyleLevelState = itemStyle,
                                                    itemNodeTreeSkillStyleState = itemNodeStyle
                                                ) { item, expanded ->
                                                    if (itemTS.open_edit && itemLevelTreeSkills.quest_key_id == 0L) {
                                                        MyDropdownMenuItem(expanded, "Изменить") {
                                                            PanAddLevelTreeSkills(dialLay, itemTS, item)
                                                        }
                                                        MyDropdownMenuItem(expanded, "Вставить уровень") {
                                                            PanAddLevelTreeSkills(
                                                                dialLay,
                                                                itemTS,
                                                                level = item.num_level
                                                            )
                                                        }
                                                        if (item.countNode == 0L) MyDeleteDropdownMenuButton(expanded) {
                                                            MainDB.addAvatar.delLevelTreeSkills(item)
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                    if (itemTS.open_edit) MyTextButtStyle1(
                                        "+",
                                        modifier = Modifier.padding(end = 15.dp),
                                        width = 50.dp,
                                        height = 50.dp,
                                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.avatarParam.skillTab.buttAddLevel)
                                    ) {
                                        PanAddLevelTreeSkills(dialLay, itemTS)
                                    }
                                }
                                TypeTreeSkills.TREE -> {
                                    spisNodeTreeSkills.toList()?.sortedBy { it.first }
                                        ?.let { listLevels ->
                                            MainDB.styleParam.avatarParam.skillTab.itemSkillLevel.getComposable(::ItemSkillsTreeLevelState) { itemStyle ->
                                                MainDB.styleParam.avatarParam.skillTab.itemSkillNode.getComposable(::ItemNodeTreeSkillsState) { itemNodeStyle ->
                                                    MyList(
                                                        listLevels,
                                                        Modifier.weight(1f).padding(5.dp)
                                                    ) { ind, levelTreeSkills ->
                                                        ComItemLevelCommonTreeSkills(
                                                            dialLay,
                                                            itemTS,
                                                            levelTreeSkills.first,
                                                            selectionNodeTreeSkills,
                                                            levelTreeSkills.second,
                                                            itemTS.open_edit,
                                                            visibleBinding.value,
                                                            itemTreeSkillStyleLevelState = itemStyle,
                                                            itemNodeTreeSkillStyleState = itemNodeStyle
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    if (itemTS.open_edit) MyTextButtStyle1(
                                        "+",
                                        modifier = Modifier.padding(end = 15.dp),
                                        width = 50.dp,
                                        height = 50.dp,
                                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.avatarParam.skillTab.buttAddNode)
                                    ) {
                                        PanAddNodeTreeSkills(
                                            dialLay,
                                            itemTS,
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
            }
        }
    }
}

