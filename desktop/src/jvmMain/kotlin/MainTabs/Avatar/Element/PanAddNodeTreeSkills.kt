package MainTabs.Avatar.Element


import MainTabs.Quest.Items.ComItemNodeLevelTreeSkillsSelParents
import MainTabs.Time.Elements.BoxSelectParentPlan
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
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeIconBorder
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

fun PanAddNodeTreeSkills(
    dialPan: MyDialogLayout,
    itemTree: ItemTreeSkill,
    level: Long = 1L,
    typeTree: TypeTreeSkills,
    item: ItemNodeTreeSkills? = null
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

    val cbSpisLevelTreeSkills = MyComboBox(
        MainDB.avatarSpis.spisLevelTreeSkills.getState().value ?: listOf(),
        nameItem = { "Уровень ${it.num_level}. ${it.name}" }) {
        levelM.value = it.num_level
    }.apply {
        if (typeTree == TypeTreeSkills.LEVELS) {
            MainDB.avatarSpis.spisLevelTreeSkills.getState().value?.let { listLevel ->
                listLevel.find { it.num_level == (item?.level ?: level) }?.let {
                    this.select(it)
                }
            }
        }
    }

    item?.let {
        when (it) {
            is ItemCountNodeTreeSkills -> {
                cbSpisTypeNodeTreeSkills.select(TypeNodeTreeSkills.COUNTER_END)
            }

            is ItemPlanNodeTreeSkills -> {
                cbSpisTypeNodeTreeSkills.select(TypeNodeTreeSkills.PLAN)
            }

            is ItemHandNodeTreeSkills -> {
                cbSpisTypeNodeTreeSkills.select(TypeNodeTreeSkills.HAND)
            }
        }

    }

    when (typeTree) {
        TypeTreeSkills.KIT -> {}
        TypeTreeSkills.LEVELS -> {}
        TypeTreeSkills.TREE -> {
            item?.id?.let { idNode ->
                parentsId.value = MainDB.avatarFun.setSelectNodeTreeSkillsForSelectionForNode(idNode).second
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
    }

    @Composable
    fun ColumnScope.buttPanel(changeNode: (ItemNodeTreeSkills) -> Unit, addNode: () -> Unit, condition: Boolean) {
        RowVA {
            MyTextButtStyle1("Отмена") {
                MainDB.avatarFun.resetSelectionNodeTreeSkills()
                dialPan.close()
            }
            if (textName.value.text != "" && condition) MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить",
                Modifier.padding(start = 5.dp)) {
                if (textName.value.text != "")
                    item?.let {
                        changeNode(it)
                        MainDB.avatarFun.resetSelectionNodeTreeSkills()
                        MainDB.avatarFun.setMarkerParentAndChildForNodeTreeSkills(it)
                        dialPan.close()
                    } ?: run {
                        addNode()
                        MainDB.avatarFun.resetSelectionNodeTreeSkills()
                        dialPan.close()
                    }
            }
        }
    }

    fun checkIdTreeAndTypeNode(funBD: (Long, Long) -> Unit) {
        cbSpisTypeNodeTreeSkills.getSelected()?.id?.let { id_type_node ->
            funBD(itemTree.id.toLong(), id_type_node)
        }
    }

    @Composable
    fun ColumnScope.handType() {
        mainOpis()
        buttPanel(
            changeNode = { changeItem ->
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    MainDB.addAvatar.updHandNodeTreeSkills(
                        item = changeItem,
                        id_tree = id_tree,
                        name = textName.value.text,
                        opis = textOpis.value.text,
                        level = levelM.value,
                        icon = iconItem.value?.id ?: -1L,
                        icon_complete = iconItemComplete.value?.id ?: -1L,
                        typeTree = typeTree,
                        parents = parentsId.value,
                        quest_id = itemTree.quest_id,
                        mustNodeForLevel = mustNodeForLevel.value
                    )
                }
            },
            addNode = {
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    MainDB.addAvatar.addHandNodeTreeSkills(
                        id_tree = id_tree,
                        name = textName.value.text,
                        opis = textOpis.value.text,
                        id_type_node = id_type_node,
                        stat = 0,
                        level = levelM.value,
                        icon = iconItem.value?.id ?: -1L,
                        icon_complete = iconItemComplete.value?.id ?: -1L,
                        typeTree = typeTree,
                        parents = parentsId.value,
                        mustNodeForLevel = mustNodeForLevel.value,
                        quest_id = itemTree.quest_id
                    )
                }
            },
            true
        )
    }

    @Composable
    fun ColumnScope.planType() {
        val parentPlan = remember {
            BoxSelectParentPlan().apply {
                item?.let { itemNP ->
                    if (itemNP is ItemPlanNodeTreeSkills) {
                        selectionPlanParent.selected = MainDB.timeSpis.spisAllPlan.getState().value?.find { itemPlan ->
                            itemPlan.id.toLong() == itemNP.privplan
                        }
                        selectionPlanStapParent.selected =
                            MainDB.timeSpis.spisAllPlanStap.getState().value?.find { itemPlanStap ->
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
                        MainDB.addAvatar.updPlanNodeTreeSkills(
                            item = changeItem,
                            id_tree = id_tree,
                            name = textName.value.text,
                            opis = textOpis.value.text,
                            level = levelM.value,
                            icon = iconItem.value?.id ?: -1L,
                            icon_complete = iconItemComplete.value?.id ?: -1L,
                            privplan = id_plan,
                            stap_prpl = parentPlan.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                            porog_hour = 0.0,
                            typeTree = typeTree,
                            parents = parentsId.value,
                            quest_id = itemTree.quest_id,
                            mustNodeForLevel = mustNodeForLevel.value
                        )
                    }
                }

            },
            addNode = {
                checkIdTreeAndTypeNode { id_tree, id_type_node ->
                    parentPlan.selectionPlanParent.selected?.id?.toLong()?.let { id_plan ->
                        MainDB.addAvatar.addPlanNodeTreeSkills(
                            id_tree = id_tree,
                            name = textName.value.text,
                            opis = textOpis.value.text,
                            id_type_node = id_type_node,
                            stat = 0,
                            level = levelM.value,
                            icon = iconItem.value?.id ?: -1L,
                            icon_complete = iconItemComplete.value?.id ?: -1L,
                            privplan = id_plan,
                            stap_prpl = parentPlan.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                            porog_hour = 0.0,
                            typeTree = typeTree,
                            parents = parentsId.value,
                            mustNodeForLevel = mustNodeForLevel.value,
                            quest_id = itemTree.quest_id
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
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA {
                    IconNode(iconItem.value, "icon_skill_color_lamp.png",
                        modifier = Modifier.clickable {
                            PanCreateIconNode(dialLayInner, icon = iconItem)
                        }
                    )
                    Text(
                        text = "❯❯❯",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MyTextStyleParam.style1.copy(fontSize = 25.sp)
                    )
                    IconNode(iconItemComplete.value ?: iconItem.value, "icon_skill_color_lamp.png",
                        complete = iconItemComplete.value?.let { TypeIconBorder.getType(it.type_ramk) != TypeIconBorder.NONE }
                            ?: true,
                        modifier = Modifier.clickable {
                            PanCreateIconNode(dialLayInner, icon = iconItemComplete)
                        }
                    )
                }
                if (item == null) cbSpisTypeNodeTreeSkills.show()
                when (typeTree) {
                    TypeTreeSkills.KIT -> Unit
                    TypeTreeSkills.LEVELS -> {
                        cbSpisLevelTreeSkills.show(Modifier.padding(top = 10.dp))
                        MyCheckbox(mustNodeForLevel, "Обязательно для выполнения")
                    }

                    TypeTreeSkills.TREE -> {
                        MyListRow(
                            MainDB.avatarSpis.spisNodeTreeSkillsSelection,
                            Modifier.padding(top = 8.dp).heightIn(0.dp, 150.dp)
                        ) { ind, nodeTreeSkills ->
                            ComItemNodeLevelTreeSkillsSelParents(nodeTreeSkills).getComposable()
                        }
                        MyTextButtStyle1("Выбрать родителей") {
                            PanSelectNodeParents(dialLayInner, id_tree = itemTree.id.toLong(), parentsId, levelM, item)
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

                    TypeNodeTreeSkills.COUNTER_ENDLESS -> Unit
                    TypeNodeTreeSkills.HOUR_END -> Unit
                    TypeNodeTreeSkills.HOUR_ENDLESS -> Unit
                    null -> Unit
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}
