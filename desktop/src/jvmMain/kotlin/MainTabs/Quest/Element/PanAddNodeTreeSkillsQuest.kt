package MainTabs.Quest.Element

import MainTabs.Avatar.Element.PanCreateIconNode
import MainTabs.Quest.Items.ComItemNodeTreeSkillsSelParentsQuest
import MyDialog.MyDialogLayout
import MyListRow
import adapters.MyComboBox
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import common.tests.IconNode
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemCountNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemHandNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeIconBorder
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatQuestElementVisible
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.QuestDB

fun PanAddNodeTreeSkillsQuest(
    dialPan: MyDialogLayout,
    questDB: QuestDB,
    idTree: Long,
    level: Long = 1L,
    typeTree: TypeTreeSkills,
    item: ItemNodeTreeSkillsQuest? = null
) {
    val cbSpisTypeNodeTreeSkills = MyComboBox(TypeNodeTreeSkills.values().toList(), nameItem = { it.nameType })
    val iconItem: MutableState<ItemIconNodeTree?> = mutableStateOf(item?.icon)
    val iconItemComplete: MutableState<ItemIconNodeTree?> = mutableStateOf(item?.icon_complete)

    val dialLayInner = MyDialogLayout()

    val textName = mutableStateOf(TextFieldValue(item?.name ?: ""))
    val textOpis = mutableStateOf(TextFieldValue(item?.opis ?: ""))
    val levelM: MutableState<Long> = mutableStateOf(item?.level ?: level)
    val mustNodeForLevel: MutableState<Boolean> = mutableStateOf(item?.must_node ?: false)
    val parentsId: MutableState<Array<Long>> = mutableStateOf(arrayOf<Long>())

    val CB_spisStartVisible = MyComboBox(TypeStatQuestElementVisible.values().toList(), nameItem = { it.nameType }).apply {
        item?.let {  itemTree ->
            TypeStatQuestElementVisible.getType(item.visible_stat)?.let {
                select(it)
            }

        }
    }

    val cbSpisLevelTreeSkills = MyComboBox(
        questDB.spisQuest.spisLevelTreeSkills.getState().value ?: listOf(),
        nameItem = { "Уровень ${it.num_level}. ${it.name}" }) {
        levelM.value = it.num_level
    }.apply {
        if (typeTree == TypeTreeSkills.LEVELS) {
            questDB.spisQuest.spisLevelTreeSkills.getState().value?.let { listLevel ->
                listLevel.find { it.num_level == (item?.level ?: level) }?.let {
                    this.select(it)
                }
            }
        }
    }

    item?.let {
        when (it) {
            is ItemCountNodeTreeSkillsQuest -> {
                cbSpisTypeNodeTreeSkills.select(TypeNodeTreeSkills.COUNTER_END)
            }
            is ItemPlanNodeTreeSkillsQuest -> {
                cbSpisTypeNodeTreeSkills.select(TypeNodeTreeSkills.PLAN)
            }
//            else -> {}
            is ItemHandNodeTreeSkillsQuest -> {
                cbSpisTypeNodeTreeSkills.select(TypeNodeTreeSkills.HAND)
            }
        }

    }

    when (typeTree) {
        TypeTreeSkills.KIT -> {}
        TypeTreeSkills.LEVELS -> {}
        TypeTreeSkills.TREE -> {
            item?.id?.let { idNode ->
                parentsId.value = questDB.questFun.setSelectNodeTreeSkillsForSelectionForNode(idNode).second
            }
        }
    }

    @Composable
    fun ColumnScope.mainOpis() {
        MyOutlinedTextField("Название дерева", textName)
        MyOutlinedTextField(
            "Описание дерева",
            textOpis,
            Modifier.heightIn(200.dp, 500.dp),
            TextAlign.Start
        )
        CB_spisStartVisible.show()
    }

    @Composable
    fun ColumnScope.buttPanel(changeNode: (ItemNodeTreeSkillsQuest) -> Unit, addNode: () -> Unit, condition: Boolean) {
        RowVA {
            MyTextButtStyle1("Отмена") {
                questDB.questFun.resetSelectionNodeTreeSkills()
                dialPan.close()
            }
            if (textName.value.text != "" && condition) MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить",
                Modifier.padding(start = 5.dp)) {
                        println("Добавить test0")
                if (textName.value.text != "")
                        println("Добавить test")
                    item?.let {
                        changeNode(it)
                        questDB.questFun.resetSelectionNodeTreeSkills()
                        questDB.questFun.setMarkerParentAndChildForNodeTreeSkills(it)
                        dialPan.close()
                    } ?: run {
                        println("Добавить")
                        addNode()
                        questDB.questFun.resetSelectionNodeTreeSkills()
                        dialPan.close()
                    }
            }
        }
    }

    fun checkIdTreeAndTypeNode(funBD: (Long, Long) -> Unit) {
//        StateVM.selectionTreeSkillsQuest.selected?.id?.let { id_tree ->
            cbSpisTypeNodeTreeSkills.getSelected()?.id?.let { id_type_node ->
                funBD(idTree, id_type_node)
            }
//        }
    }

    @Composable
    fun ColumnScope.handType() {
        mainOpis()
        buttPanel(
            changeNode = { changeItem ->
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    questDB.addQuest.updHandNodeTreeSkills(
                        item = changeItem,
                        id_tree = id_tree,
                        name = textName.value.text,
                        opis = textOpis.value.text,
                        level = levelM.value,
                        icon = iconItem.value?.id ?: -1L,
                        icon_complete = iconItemComplete.value?.id ?: -1L,
                        visible_stat = CB_spisStartVisible.getSelected()?.codValue ?: 0,
                        typeTree = typeTree,
                        parents = parentsId.value,
                        mustNodeForLevel = mustNodeForLevel.value
                    )
                }
            },
            addNode = {
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    questDB.addQuest.addHandNodeTreeSkills(
                        id_tree = id_tree,
                        name = textName.value.text,
                        opis = textOpis.value.text,
                        id_type_node = id_type_node,
                        level = levelM.value,
                        icon = iconItem.value?.id ?: -1L,
                        visible_stat = CB_spisStartVisible.getSelected()?.codValue ?: 0,
                        icon_complete = iconItemComplete.value?.id ?: -1L,
                        typeTree = typeTree,
                        parents = parentsId.value,
                        mustNodeForLevel = mustNodeForLevel.value
                    )
                }
            },
            true
        )
    }

    @Composable
    fun ColumnScope.planType() {
        val parentPlan = remember {
            BoxSelectParentPlanQuest().apply {
                item?.let { itemNP ->
                    if (itemNP is ItemPlanNodeTreeSkillsQuest) {
                        selectionPlanParent.selected = questDB.spisQuest.spisPlan.getState().value?.find { itemPlan ->
                            itemPlan.id.toLong() == itemNP.privplan
                        }
                        selectionPlanStapParent.selected =
                            questDB.spisQuest.spisAllStapPlan.getState().value?.find { itemPlanStap ->
                                itemPlanStap.id.toLong() == itemNP.stap_prpl
                            }
                    }
                }
            }
        }
        parentPlan.getComposable()
        mainOpis()
        buttPanel(
            changeNode = { changeItem ->
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    parentPlan.selectionPlanParent.selected?.id?.toLong()?.let { id_plan ->
                        questDB.addQuest.updPlanNodeTreeSkills(
                            item = changeItem,
                            id_tree = id_tree,
                            name = textName.value.text,
                            opis = textOpis.value.text,
                            level = levelM.value,
                            icon = iconItem.value?.id ?: -1L,
                            icon_complete = iconItemComplete.value?.id ?: -1L,
                            visible_stat = CB_spisStartVisible.getSelected()?.codValue ?: 0,
                            privplan = id_plan,
                            stap_prpl = parentPlan.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                            porog_hour = 0.0,
                            typeTree = typeTree,
                            parents = parentsId.value,
                            mustNodeForLevel = mustNodeForLevel.value
                        )
                    }
                }

            },
            addNode = {
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    parentPlan.selectionPlanParent.selected?.id?.toLong()?.let { id_plan ->
                        questDB.addQuest.addPlanNodeTreeSkills(
                            id_tree = id_tree,
                            name = textName.value.text,
                            opis = textOpis.value.text,
                            id_type_node = id_type_node,
                            level = levelM.value,
                            icon = iconItem.value?.id ?: -1L,
                            icon_complete = iconItemComplete.value?.id ?: -1L,
                            visible_stat = CB_spisStartVisible.getSelected()?.codValue ?: 0,
                            privplan = id_plan,
                            stap_prpl = parentPlan.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                            porog_hour = 0.0,
                            typeTree = typeTree,
                            parents = parentsId.value,
                            mustNodeForLevel = mustNodeForLevel.value
                        )
                    }
                }
            },
            parentPlan.selectionPlanParent.selected != null
        )
    }

    @Composable
    fun ColumnScope.counterType() {
        val text_max_count = mutableStateOf(TextFieldValue("3"))
        mainOpis()
        MyOutlinedTextFieldInt("Максимальное значение", text_max_count)
        buttPanel(changeNode = {

        }, addNode = {

        }, true)
    }


    dialPan.dial = @Composable {

//        val iconImage: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }

        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA {
                    IconNode(iconItem.value, "icon_skill_color_lamp.png", questDB.dirQuest,
                        modifier = Modifier.clickable {
                            PanCreateIconNode(dialLayInner, icon = iconItem, questDB)
                        }
                    )
                    Text(
                        text = "❯❯❯",
                        modifier = Modifier.align(Alignment.CenterVertically), //.padding(start = 10.dp)
                        style = MyTextStyleParam.style1.copy(fontSize = 25.sp)
                    )
                    IconNode(iconItemComplete.value ?: iconItem.value, "icon_skill_color_lamp.png", questDB.dirQuest,
                        complete = iconItemComplete.value?.let { TypeIconBorder.getType(it.type_ramk) != TypeIconBorder.NONE } ?: true,
                        modifier = Modifier.clickable {
                            PanCreateIconNode(dialLayInner, icon = iconItemComplete, questDB)
                        }
                    )
                }
                if (item == null) cbSpisTypeNodeTreeSkills.show()

                when (typeTree) {
                    TypeTreeSkills.KIT -> {
                    }
                    TypeTreeSkills.LEVELS -> {
                        cbSpisLevelTreeSkills.show(Modifier.padding(top = 10.dp))
                        MyCheckbox(mustNodeForLevel, "Обязательно для выполнения")
                    }
                    TypeTreeSkills.TREE -> {
                        MyListRow(
                            questDB.spisQuest.spisNodeTreeSkillsSelection,
                            Modifier.padding(top = 8.dp)//.heightIn(0.dp, 150.dp)
                        ) { ind, nodeTreeSkills -> //.heightIn(0.dp, 150.dp) .height(150.dp)
                            ComItemNodeTreeSkillsSelParentsQuest(nodeTreeSkills,questDB).getComposable()
                        }
                        MyTextButtStyle1("Выбрать родителей") {
                                PanSelectNodeParentsQuest(dialLayInner, questDB, parentsId, levelM)
                        }
                    }
                }

                when (cbSpisTypeNodeTreeSkills.getSelected()) {
                    TypeNodeTreeSkills.HAND -> {
                        handType()
                    }
                    TypeNodeTreeSkills.PLAN -> {
                        planType()
                    }
                    TypeNodeTreeSkills.COUNTER_END -> {
                        counterType()
                    }
                    TypeNodeTreeSkills.COUNTER_ENDLESS -> {
                    }
                    TypeNodeTreeSkills.HOUR_END -> {
                    }
                    TypeNodeTreeSkills.HOUR_ENDLESS -> {
                    }
                    null -> {
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()


}
