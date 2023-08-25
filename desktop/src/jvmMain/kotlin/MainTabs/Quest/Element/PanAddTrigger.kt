package MainTabs.Quest.Element

import MainTabs.Quest.Items.*
import MyDialog.MyDialogLayout
import MyList
import MyListPlate
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import common.tests.CommonOpenItemPanel
import common.tests.IconNode
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemDialog
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemLevelTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerFinishTriggerEnum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.*
import viewmodel.QuestVM
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
fun PanAddTrigger(
    dialPan: MyDialogLayout,
    itemPar: ParentOfTrigger,
    isklParam: List<Long> = listOf()
) {
    QuestVM.openQuestDB?.let { questDB ->
        val seekTypeStartObjOfTrigger = DiskretSeekBarManyRows(
            TypeStartObjOfTrigger.values().filter { it != TypeStartObjOfTrigger.STARTLEVELTREE }.toList()
                .map { it.nameType to it.id.toString() },
            3,
            TypeStartObjOfTrigger.STARTDIALOG.id.toString()
        ) {}

        @Composable
        fun ColumnScope.StartPlan() {
            val selParents = remember { BoxSelectParentPlanQuest() }
            selParents.apply {
                idIsklPlan =
                    if (itemPar.typeParent == TypeParentOfTrig.PLAN) itemPar.parent_id.toString() else ""
                onlyPlans = true
            }.getComposable(Modifier.fillMaxWidth(0.65f))
            Row {
                MyTextButtStyle1("Отмена") {
                    dialPan.close()
                }
                selParents.selectionPlanParent.selected?.let { startPlan ->
                    MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        questDB.addQuest.addTriggerStartPlan(itemPar, startPlan.startObjOfTrig())
                        dialPan.close()
                    }
                }
            }
        }

        @Composable
        fun ColumnScope.StartDialog() {
            val selDial = remember { SingleSelectionType<ItemDialog>() }
            MySelectOneItem(
                selDial,
                questDB.spisQuest.spisDialog,
                "Выбрать диалог",
                comItemSelect = { item, onClick ->
                    ComItemDialogPlate(item, false, onClick)
                }) { item, onClick ->
                ComItemDialogPlate(item, selDial.isActive(item), onClick)
            }
            Row {
                MyTextButtStyle1("Отмена") {
                    dialPan.close()
                }
                selDial.selected?.let { itemDialog ->
                    MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        questDB.addQuest.addTriggerStartPlan(itemPar, itemDialog.startObjOfTrig())
                        dialPan.close()
                    }
                }
            }
        }

        @Composable
        fun ColumnScope.StartTree() {
            val selectionTreeSkillsQuest = remember { SingleSelectionType<ItemTreeSkillsQuest>() }
            questDB.spisQuest.spisTreeSkills.getState()?.value?.let { spisTree ->
                MyList(
                    spisTree.filter {
                        if (itemPar.typeParent == TypeParentOfTrig.NODETREESKILLS) !isklParam.contains(
                            it.id
                        ) else true
                    } ?: listOf<ItemTreeSkillsQuest>(),
                    Modifier.weight(1f).padding(horizontal = 60.dp)
                ) { ind, itemTreeSkill ->
                    ComItemTreeSkillsQuest(itemTreeSkill, selectionTreeSkillsQuest).getComposable()
                }
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    selectionTreeSkillsQuest.selected?.let { itemTree ->
                        MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                            questDB.addQuest.addTriggerStartPlan(itemPar, itemTree.startObjOfTrig())
                            dialPan.close()
                        }
                    }
                }
            }
        }

        @Composable
        fun ColumnScope.StartNodeTree() {
            val selectionNodeTreeSkills =
                remember { SingleSelectionType<ItemNodeTreeSkillsQuest>() }
            val commonSkillPanel = remember {
                CommonOpenItemPanel<ItemTreeSkillsQuest>(
                    whenOpen = {
                        TypeTreeSkills.getType(it.id_type_tree)?.let { typeTree ->
                            questDB.questFun.setSelectTreeSkillsForSelectForTrigger(it.id, typeTree)
                        }
                    },
                    mainSpis = { modifierList, selection, openSpis_Index_Item, lazyListState, dialLay ->
                        questDB.spisQuest.spisTreeSkills.getState()?.value?.let { spisTree ->
                            MyList(spisTree.filter {
                                if (itemPar.typeParent == TypeParentOfTrig.NODETREESKILLS) !isklParam.contains(
                                    it.id
                                ) else true
                            } ?: listOf<ItemTreeSkillsQuest>(),
                                Modifier.weight(1f).padding(horizontal = 60.dp).then(modifierList),
                                lazyListState) { ind, itemTreeSkill ->
                                ComItemTreeSkillsQuest(itemTreeSkill, selection,
                                    openTree = { itemA ->
                                        openSpis_Index_Item(ind, itemA)
                                    }
                                ).getComposable()
                            }
                        }
                    },
                    openedItem = { itemTS, startOpenAnimation, selection, dialLay ->
                        MyCardStyle1(
                            false, 0,
                            backColor = MyColorARGB.colorMyMainTheme.toColor()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                RowVA {
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
                                                selectionNodeTreeSkills.selected = null
                                                startOpenAnimation.value = false
                                            })
                                }
                                Box(
                                    Modifier.myModWithBound1().padding(5.dp).fillMaxWidth()
                                        .animateContentSize()
                                ) {
                                    Column {
                                        when (TypeTreeSkills.getType(itemTS.id_type_tree)) {
                                            TypeTreeSkills.KIT -> {
                                                questDB.spisQuest.spisNodeTreeSkillsForSelectionForTrigger.getState().value?.get(
                                                    1L
                                                )?.let { listNode ->
                                                    MyListPlate(
                                                        listNode,
                                                        Modifier.weight(1f)
                                                            .padding(horizontal = 10.dp),
                                                        alignmentCenter = true
                                                    ) { itemNodeTreeSkills ->
                                                        ComItemNodeTreeSkillsSelParentsQuest(
                                                            itemNodeTreeSkills,
                                                            questDB,
                                                            selectionNodeTreeSkills
                                                        ).getComposable()
                                                    }
                                                }
                                            }

                                            TypeTreeSkills.LEVELS -> {
                                                MyList(
                                                    questDB.spisQuest.spisLevelTreeSkillsForSelect,
                                                    Modifier.weight(1f).padding(horizontal = 5.dp)
                                                ) { ind, itemLevelTreeSkills ->
                                                    ComItemLevelTreeSkillsForSelectNodeQuest(
                                                        questDB,
                                                        itemLevelTreeSkills,
                                                        selectionNodeTreeSkills,
                                                        questDB.spisQuest.spisNodeTreeSkillsForSelectionForTrigger.getState().value?.get(
                                                            itemLevelTreeSkills.num_level
                                                        )
                                                            ?.sortedByDescending { it.must_node }
                                                            ?: listOf()
                                                    ).getComposable()
                                                }
                                            }

                                            TypeTreeSkills.TREE -> {
                                                questDB.spisQuest.spisNodeTreeSkillsForSelectionForTrigger.getState().value?.toList()
                                                    ?.sortedBy { it.first }
                                                    ?.let { listLevels ->
                                                        MyList(
                                                            listLevels,
                                                            Modifier.weight(1f)
                                                                .padding(horizontal = 5.dp)
                                                        ) { ind, levelTreeSkills ->
                                                            ComItemLevelCommonTreeSkillsForSelectQuest(
                                                                levelTreeSkills.first,
                                                                questDB,
                                                                selectionNodeTreeSkills,
                                                                levelTreeSkills.second,
                                                            ).getComposable()
                                                        }
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
                )
            }
            commonSkillPanel.show(Modifier.weight(1f))
            Row {
                MyTextButtStyle1("Отмена") {
                    dialPan.close()
                }
                selectionNodeTreeSkills.selected?.let { itemTree ->
                    MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        questDB.addQuest.addTriggerStartPlan(itemPar, itemTree.startObjOfTrig())
                        dialPan.close()
                    }
                }
            }
        }

        @Composable
        fun ColumnScope.StartStap() {
            val selParents = remember { BoxSelectParentPlanQuest() }
            selParents.apply {
                arrayIskl =
                    if (itemPar.typeParent == TypeParentOfTrig.PLANSTAP) listOf(itemPar.parent_id) else listOf()
            }.getComposable(Modifier.fillMaxWidth(0.65f))
            Row {
                MyTextButtStyle1("Отмена") {
                    dialPan.close()
                }
                selParents.selectionPlanStapParent.selected?.let { startStapPlan ->
                    MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        questDB.addQuest.addTriggerStartPlan(
                            itemPar,
                            startStapPlan.startObjOfTrig()
                        )
                        dialPan.close()
                    }
                }
            }
        }

        @Composable
        fun ColumnScope.StartInnerFinish() {
            val selDial = remember { SingleSelectionType<InnerFinishTriggerEnum>() }
            MySelectOneItem(
                selDial,
                InnerFinishTriggerEnum.values().toList(),
                "Выбрать финишный триггер",
                comItemSelect = { item, onClick ->
                    ComInnerFinishTriggerPlate(item, false, onClick)
                }) { item, onClick ->
                ComInnerFinishTriggerPlate(item, selDial.isActive(item), onClick)
            }
            Row {
                MyTextButtStyle1("Отмена") {
                    dialPan.close()
                }
                selDial.selected?.let { itemDialog ->
                    MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        questDB.addQuest.addTriggerStartPlan(itemPar, itemDialog.startObjTrig())
                        dialPan.close()
                    }
                }
            }
        }

        @Composable
        fun ColumnScope.StartLevelTree() {
            val selectionLevelTreeSkills =
                remember { SingleSelectionType<ItemLevelTreeSkillsQuest>() }
            val commonSkillPanel = remember {
                CommonOpenItemPanel<ItemTreeSkillsQuest>(
                    whenOpen = {
                        TypeTreeSkills.getType(it.id_type_tree)?.let { typeTree ->
                            questDB.questFun.setSelectTreeSkillsForSelectForTrigger(it.id, typeTree)
                        }
                    },
                    mainSpis = { modifierList, selection, openSpis_Index_Item, lazyListState, dialLay ->
                        questDB.spisQuest.spisTreeSkills.getState()?.value?.let { spisTree ->
                            MyList(spisTree.filter {
                                TypeTreeSkills.getType(it.id_type_tree) == TypeTreeSkills.LEVELS && (if (itemPar.typeParent == TypeParentOfTrig.NODETREESKILLS) !isklParam.contains(
                                    it.id
                                ) else true)
                            },
                                Modifier.weight(1f).padding(horizontal = 60.dp).then(modifierList),
                                lazyListState) { ind, itemTreeSkill ->
                                ComItemTreeSkillsQuest(itemTreeSkill, selection,
                                    openTree = { itemA ->
                                        openSpis_Index_Item(ind, itemA)
                                    }
                                ).getComposable()
                            }
                        }
                    },
                    openedItem = { itemTS, startOpenAnimation, selection, dialLay ->
                        MyCardStyle1(
                            false, 0,
                            backColor = MyColorARGB.colorMyMainTheme.toColor()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                RowVA {
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
                                                selectionLevelTreeSkills.selected = null
                                                startOpenAnimation.value = false
                                            })
                                }
                                Box(
                                    Modifier.myModWithBound1().padding(5.dp).fillMaxWidth()
                                        .animateContentSize()
                                ) {
                                    Column {
                                        if (TypeTreeSkills.getType(itemTS.id_type_tree) == TypeTreeSkills.LEVELS) {
                                            MyList(
                                                questDB.spisQuest.spisLevelTreeSkillsForSelect,
                                                Modifier.weight(1f).padding(horizontal = 5.dp)
                                            ) { ind, itemLevelTreeSkills ->
                                                ComItemLevelTreeSkillsForSelectQuest(
                                                    questDB,
                                                    itemLevelTreeSkills,
                                                    selectionLevelTreeSkills,
                                                    questDB.spisQuest.spisNodeTreeSkillsForSelectionForTrigger.getState().value?.get(
                                                        itemLevelTreeSkills.num_level
                                                    )
                                                        ?.sortedByDescending { it.must_node }
                                                        ?: listOf()
                                                ).getComposable()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
            commonSkillPanel.show(Modifier.weight(1f))
            Row {
                MyTextButtStyle1("Отмена") {
                    dialPan.close()
                }
                selectionLevelTreeSkills.selected?.let { itemLevel ->
                    MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                        questDB.addQuest.addTriggerStartPlan(itemPar, itemLevel.startObjOfTrig())
                        dialPan.close()
                    }
                }
            }
        }

        val dialLayInner = MyDialogLayout()
        dialPan.dial = @Composable {
            BackgroungPanelStyle1 {
                Column(
                    Modifier.padding(15.dp)
                        .heightIn(0.dp, dialPan.layHeight.value * 0.7F)
                        .widthIn(0.dp, dialPan.layWidth.value * 0.7F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    seekTypeStartObjOfTrigger.show(Modifier.padding(bottom = 10.dp))
                    seekTypeStartObjOfTrigger.active?.cod?.toLong()?.let { cod ->
                        when (TypeStartObjOfTrigger.getType(cod)) {

                            TypeStartObjOfTrigger.STARTPLAN -> {
                                StartPlan()
                            }

                            TypeStartObjOfTrigger.STARTDIALOG -> {
                                StartDialog()
                            }

                            TypeStartObjOfTrigger.SUMTRIGGER -> {
                            }

                            TypeStartObjOfTrigger.STARTSTAP -> {
                                StartStap()
                            }

                            TypeStartObjOfTrigger.INNERFINISH -> {
                                StartInnerFinish()
                            }

                            TypeStartObjOfTrigger.STARTTREE -> {
                                StartTree()
                            }

                            TypeStartObjOfTrigger.STARTNODETREE -> {
                                StartNodeTree()
                            }

                            TypeStartObjOfTrigger.STARTLEVELTREE -> {
                                StartLevelTree()
                            }

                            null -> {}
                        }
                    }
                }
            }
            dialLayInner.getLay()
        }
        dialPan.show()
    }
}

