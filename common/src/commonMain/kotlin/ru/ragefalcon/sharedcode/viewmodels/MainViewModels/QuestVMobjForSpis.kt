package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree
import ru.ragefalcon.sharedcode.models.data.ItemMainParam
import ru.ragefalcon.sharedcode.models.data.ItemParentBranchNode
import ru.ragefalcon.sharedcode.models.data.itemQuest.*
import ru.ragefalcon.sharedcode.quest.*
import ru.ragefalcon.sharedcode.quest.TreeSkills.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MarkerNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonComplexObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniConvertQueryAdapter
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniQueryAdapter

class QuestVMobjForSpis(private val mDB: DatabaseQuest) {
    val spisGovorun = UniConvertQueryAdapter<Spis_govorun, ItemGovorun>() {
        ItemGovorun(
            id = it._id.toString(),
            name = it.name,
            opis = it.opis,
            image_file = it.image_file,
        )
    }.apply {
        this.updateQuery(mDB.spisGovorunQueries.selectAll())
    }
    val spisDialog = UniConvertQueryAdapter<SelectAll, ItemDialog>() {
        ItemDialog(
            id = it._id.toString(),
            name = it.name,
            maintext = it.maintext,
            govorun_name = it.govorun_name,
            govorun_id = it.govorun_id,
        )
    }.apply {
        this.updateQuery(mDB.spisDialogQueries.selectAll())
    }

    val spisOtvetDialog = UniConvertQueryAdapter<Spis_otvet_dialog, ItemOtvetDialog>() {
        ItemOtvetDialog(
            id = it._id.toString(),
            dialog_id = it.dialog_id,
            text = it.text,
            order_number = it.order_number
        )
    }.apply {
        this.updateQuery(mDB.spisOtvetDialogQueries.selectAll())
    }

    val spisTrigger = UniConvertQueryAdapter<Common_trigger, ItemTrigger>() {
        ItemTrigger(
            id = it._id.toString(),
            parent_type = it.parent_type_element,
            parent_id = it.parent_element_id,
            type_id = it.type_trig_id,
            child_id = it.child_id,
            child_name = it.child_name,
            act_code = it.act_code,
        )
    }.apply {
        this.updateQuery(mDB.commonTriggerQueries.selectAll())
    }

    val spisPlan = UniConvertQueryAdapter<SelectAllPlan, ItemPlanQuest>() {
        ItemPlanQuest(
            id = it._id.toString(),
            vajn = it.vajn,
            name = it.name,
            opis = it.opis,
            hour = it.hour,
            srok = it.srok,
            statsrok = it.statsrok,
            commstat = it.commstat,
            countstap = it.countstap
        )
    }.apply {
        this.updateQuery(mDB.spisPlanQuestQueries.selectAllPlan())
    }

    val spisStapPlan = UniConvertQueryAdapter<AllStapPlan, ItemPlanStapQuest>() {
        ItemPlanStapQuest(
            id = it._id.toString(),
            level = -1,
            name = it.name,
            opis = it.opis,
            hour = it.hour,
            srok = it.srok,
            statsrok = it.statsrok,
            commstat = it.commstat,
            parent_id = it.parent_id.toString(),
            podstapcount = -1,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            idplan = it.idplan
        )
    }.apply {
        this.updateQuery(mDB.spisStapPlanQuestQueries.allStapPlan())
    }

    val spisOpenStapPlan = UniConvertQueryAdapter<OpenStapPlan, ItemPlanStapQuest>() {
        ItemPlanStapQuest(
            id = it._id.toString(),
            level = it.level,
            name = it.name,
            opis = it.opis,
            hour = it.hour,
            srok = it.srok,
            statsrok = it.statsrok,
            commstat = it.commstat,
            parent_id = it.parent_id.toString(),
            podstapcount = it.stapcount,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            idplan = it.idplan
        )
    }.apply {
        this.updateQuery(mDB.spisStapPlanQuestQueries.openStapPlan(-1L, -1L))
    }
    val spisStapPlanForSelect = UniConvertQueryAdapter<OpenStapPlanForSelect, ItemPlanStapQuest>() {
        ItemPlanStapQuest(
            id = it._id.toString(),
            level = it.level,
            name = it.name,
            opis = it.opis,
            hour = it.hour,
            srok = it.srok,
            statsrok = it.statsrok,
            commstat = it.commstat,
            parent_id = it.parent_id.toString(),
            podstapcount = it.stapcount,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            idplan = it.idplan
        )
    }.apply {
        this.updateQuery(mDB.spisStapPlanQuestQueries.openStapPlanForSelect(-1L, -1, listOf()))
    }

    val spisAllStapPlan = UniConvertQueryAdapter<AllStapPlan, ItemPlanStapQuest>() {
        ItemPlanStapQuest(
            id = it._id.toString(),
            level = -1,
            name = it.name,
            opis = it.opis,
            hour = it.hour,
            srok = it.srok,
            statsrok = it.statsrok,
            commstat = it.commstat,
            parent_id = it.parent_id.toString(),
            podstapcount = it.stapcount,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            idplan = it.idplan
        )
    }.apply {
        this.updateQuery(mDB.spisStapPlanQuestQueries.allStapPlan())
    }

    val countStapPlan = UniQueryAdapter<Long>()
        .apply { updateQuery(mDB.spisStapPlanQuestQueries.countOpenStapPlan(iskstat = 10, idpl = -1)) }

    val spisMainParam = UniConvertQueryAdapter<Mainparam, ItemMainParam>() {
        ItemMainParam(
            id = it._id.toString(),
            name = it.name,
            intparam = it.intparam,
            stringparam = it.stringparam
        )
    }.apply {
        updateQuery(mDB.mainParamQueries.selectMainParam())
    }

    val spisTreeSkills = UniConvertQueryAdapter<SelectTreeSkillQuest, ItemTreeSkillsQuest>() {
        ItemTreeSkillsQuest(
            id = it._id,
            id_area = it.id_area,
            name = it.name,
            id_type_tree = it.id_type_tree,
            opis = it.opis,
            icon = it.icon,
            countNode = it.count_node,
            visibleStat = it.visible_stat
        )
    }.apply {
        this.updateQuery(mDB.spisTreeSkillsQuestQueries.selectTreeSkillQuest())
    }


    /*******************************************************************************/
    /**************************  Start node block  *********************************/
    /*******************************************************************************/

    private var listHandNodeTreeSkills = listOf<ItemHandNodeTreeSkillsQuest>()
    private var listPlanNodeTreeSkills = listOf<ItemPlanNodeTreeSkillsQuest>()

    private fun updateSpisNode(): Map<Long, List<ItemNodeTreeSkillsQuest>> = mutableListOf<ItemNodeTreeSkillsQuest>()
        .apply {
            addAll(listHandNodeTreeSkills)
            addAll(listPlanNodeTreeSkills)
        }
        .sortedBy { it.id }.groupBy { it.level }

    fun updateInfoSpisNode(branchParent: ItemParentBranchNode?) {
        branchParent?.let {
            val directNode = mutableListOf<ItemNodeTreeSkillsQuest>()
                .apply {
                    addAll(listHandNodeTreeSkills)
                    addAll(listPlanNodeTreeSkills)
                }
                .filterCopy { branchParent.direct.contains(it.id) }
                .onEach { it.marker = MarkerNodeTreeSkills.DIRECTPARENT }
            val indirectNode = mutableListOf<ItemNodeTreeSkillsQuest>()
                .apply {
                    addAll(listHandNodeTreeSkills)
                    addAll(listPlanNodeTreeSkills)
                }
                .filterCopy { branchParent.indirect.contains(it.id) }
                .onEach { it.marker = MarkerNodeTreeSkills.INDIRECTPARENT }
            val commonListNode = mutableListOf<ItemNodeTreeSkillsQuest>()
                .apply {
                    addAll(directNode)
                    addAll(indirectNode)
                }
            spisNodeTreeSkillsForInfo.setValue(commonListNode)
        } ?: let {
            spisNodeTreeSkillsForInfo.setValue(listOf())
        }
    }


    fun getListSelectNodeTreeSkills(selected: Array<Long>): List<ItemNodeTreeSkillsQuest> =
        mutableListOf<ItemNodeTreeSkillsQuest>()
            .apply {
                addAll(listHandNodeTreeSkills.filterCopy { selected.contains(it.id) })
                addAll(listPlanNodeTreeSkills.filterCopy { selected.contains(it.id) })
            }


    val spisLevelTreeSkills = UniConvertQueryAdapter<SelectLevelTreeSkill, ItemLevelTreeSkillsQuest>() {
        ItemLevelTreeSkillsQuest(
            id = it._id,
            id_tree = it.id_tree,
            name = it.name,
            opis = it.opis,
            num_level = it.num_level,
            proc_porog = it.proc_porog,
            mustCountNode = it.count_node_must,
            countNode = it.count_node,
            visible_stat = it.visible_stat
        )
    }.apply {
        updateQuery(mDB.spisLevelTreeSkillsQuestQueries.selectLevelTreeSkill(-1L))
    }

    val spisLevelTreeSkillsForSelect =
        UniConvertQueryAdapter<SelectLevelTreeSkillForSelect, ItemLevelTreeSkillsQuest>() {
            ItemLevelTreeSkillsQuest(
                id = it._id,
                id_tree = it.id_tree,
                name = it.name,
                opis = it.opis,
                num_level = it.num_level,
                proc_porog = it.proc_porog,
                mustCountNode = it.count_node_must,
                countNode = it.count_node,
                visible_stat = it.visible_stat
            )
        }.apply {
            updateQuery(mDB.spisLevelTreeSkillsQuestQueries.selectLevelTreeSkillForSelect(-1L))
        }

    val spisAllNodeTreeSkillsForSelectForTrigger =
        UniConvertQueryAdapter<SelectAllNodesForSelect, ItemHandNodeTreeSkillsQuest>() {
            ItemHandNodeTreeSkillsQuest(
                id = it._id,
                id_tree = it.id_tree,
                name = it.name,
                opis = it.opis,
                id_type_node = it.id_type_node,
                level = it.level,
                icon = if (it.ext1 != null && it.type1 != null) ItemIconNodeTree(it.icon, it.ext1, it.type1) else null,
                icon_complete = if (it.ext2 != null && it.type2 != null) ItemIconNodeTree(
                    it.icon_complete,
                    it.ext2,
                    it.type2
                ) else null,
                must_node = it.must_node == 1L,
                visible_stat = it.visible_stat
            )
        }.apply {
            updateFunc {
                /**
                 * похоже условие ниже не спасает вызова setValue с null параметром, не понятно в какой момент это происходит,
                 * приложение не падает и похоже при повторном опросе все разрешается, так что видно проблему только в логах,
                 * но надо бы последить и подумать над этим.
                 * Ошибка возникает когда в открытом в редакторе квесте первый раз развернуть дерево ачивок, возможно именно
                 * уровневое, а может и не только.
                 * */
                (if (it.size > 0) it.groupBy { it.level } else mapOf()).let { newValue ->
                    spisNodeTreeSkillsForSelectionForTrigger.setValue(newValue)
                }
            }
            updateQuery(mDB.spisNodeTreeSkillsQuestQueries.selectAllNodesForSelect(TypeTreeSkills.KIT.name, -1L))
        }

    val spisHandNodeTreeSkills = UniConvertQueryAdapter<SelectHandNodes, ItemHandNodeTreeSkillsQuest>() {
        ItemHandNodeTreeSkillsQuest(
            id = it._id,
            id_tree = it.id_tree,
            name = it.name,
            opis = it.opis,
            id_type_node = it.id_type_node,
            level = it.level,
            icon = if (it.ext1 != null && it.type1 != null) ItemIconNodeTree(it.icon, it.ext1, it.type1) else null,
            icon_complete = if (it.ext2 != null && it.type2 != null) ItemIconNodeTree(
                it.icon_complete,
                it.ext2,
                it.type2
            ) else null,
            must_node = it.must_node == 1L,
            visible_stat = it.visible_stat
        )
    }.apply {
        updateFunc {
            listHandNodeTreeSkills = it
            spisNodeTreeSkills.update()
            spisNodeTreeSkillsForSelection.update()
        }
        updateQuery(
            mDB.spisNodeTreeSkillsQuestQueries.selectHandNodes(
                TypeTreeSkills.KIT.name,
                -1L,
                TypeNodeTreeSkills.HAND.id
            )
        )
    }

    val spisPlanNodeTreeSkills = UniConvertQueryAdapter<SelectPlanNodeTreeSkills, ItemPlanNodeTreeSkillsQuest>() {
        ItemPlanNodeTreeSkillsQuest(
            id = it._id,
            id_tree = it.id_tree,
            name = it.name,
            opis = it.opis,
            id_type_node = it.id_type_node,
            level = it.level,
            icon = if (it.ext1 != null && it.type1 != null) ItemIconNodeTree(it.icon, it.ext1, it.type1) else null,
            icon_complete = if (it.ext2 != null && it.type2 != null) ItemIconNodeTree(
                it.icon_complete,
                it.ext2,
                it.type2
            ) else null,
            must_node = it.must_node == 1L,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            porog_hour = it.porog_hour,
            visible_stat = it.visible_stat
        )
    }.apply {
        updateFunc {
            listPlanNodeTreeSkills = it
            spisNodeTreeSkills.update()
            spisNodeTreeSkillsForSelection.update()
        }
        updateQuery(mDB.propertyPlanNodeTSQuestQueries.selectPlanNodeTreeSkills("KIT", -1L, TypeNodeTreeSkills.PLAN.id))
    }

    val spisWholeBranchParentNodeTreeSkills = UniQueryAdapter<SelectWholeBranchParent>().apply {
        updateFunc { spisBinding ->
            spisParentBranchNodeTreeSkills.setValue(spisBinding.groupBy { it.id_childW }.map {
                ItemParentBranchNode(
                    it.key,
                    direct = it.value.filter { it.level == 0L }.map { it.id_parentW }.toTypedArray(),
                    indirect = it.value.filter { it.level > 0L }.map { it.id_parentW }.toTypedArray()
                )
            })
        }
        updateQuery(mDB.spisBindingNodeTreeSkillsQuestQueries.selectWholeBranchParent(-1L))
    }

    val spisWholeBranchChildNodeTreeSkills = UniQueryAdapter<SelectWholeBranchChild>().apply {
        updateFunc { spisBinding ->
            spisChildBranchNodeTreeSkills.setValue(spisBinding.groupBy { it.id_parentW }.map {
                ItemParentBranchNode(
                    it.key,
                    direct = it.value.filter { it.level == 0L }.map { it.id_childW }.toTypedArray(),
                    indirect = it.value.filter { it.level > 0L }.map { it.id_childW }.toTypedArray()
                )
            })
        }
        updateQuery(mDB.spisBindingNodeTreeSkillsQuestQueries.selectWholeBranchChild(-1L))
    }

    var spisNodeTreeSkills = CommonComplexObserveObj<Map<Long, List<ItemNodeTreeSkillsQuest>>>().apply {
        setValueFun { updateSpisNode() }
    }
    var spisNodeTreeSkillsForSelection = CommonComplexObserveObj<Map<Long, List<ItemNodeTreeSkillsQuest>>>().apply {
        setValueFun { updateSpisNode() }
    }

    var spisNodeTreeSkillsForSelectionForTrigger = CommonObserveObj<Map<Long, List<ItemNodeTreeSkillsQuest>>>()
    var spisNodeTreeSkillsSelection = CommonObserveObj<List<ItemNodeTreeSkillsQuest>>()
    var spisNodeTreeSkillsForInfo = CommonObserveObj<List<ItemNodeTreeSkillsQuest>>()

    var spisParentBranchNodeTreeSkills = CommonObserveObj<List<ItemParentBranchNode>>()
    var spisChildBranchNodeTreeSkills = CommonObserveObj<List<ItemParentBranchNode>>()

    val spisIconNodeTree = UniConvertQueryAdapter<Spis_icon_node_tree_skills_quest, ItemIconNodeTree>() {
        ItemIconNodeTree(
            id = it._id,
            extension = it.extension,
            type_ramk = it.type_ramk
        )
    }.apply {
        this.updateQuery(mDB.spisIconNodeTreeSkillsQuestQueries.select())
    }


    /*******************************************************************************/
    /**************************  End node block  ***********************************/
    /*******************************************************************************/


}