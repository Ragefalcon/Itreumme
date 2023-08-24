package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.models.data.ItemParentBranchNode
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.quest.DatabaseQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MarkerNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.QuestVMobjForSpis

class QuestVMfun(private val mDB: DatabaseQuest, private val spisVM: QuestVMobjForSpis) {
    private var openStatStapPlanQuest = 10L
    private var idPlanForSpisStapPlanQuest = -1L



    private fun updateSpisStapPlan() {
        spisVM.spisOpenStapPlan.updateQuery(
            mDB.spisStapPlanQuestQueries.openStapPlan(
                idpl = idPlanForSpisStapPlanQuest,
                iskstat = openStatStapPlanQuest
            )
        )
        spisVM.countStapPlan.updateQuery(
            mDB.spisStapPlanQuestQueries.countOpenStapPlan(
                iskstat = openStatStapPlanQuest,
                idpl = idPlanForSpisStapPlanQuest
            )
        )
    }

    fun setOpenSpisStapPlan(open: Boolean) {
        if (open) {
            openStatStapPlanQuest = -1L

            updateSpisStapPlan()
        } else {
            openStatStapPlanQuest = 10L
            updateSpisStapPlan()
        }
    }

    fun setPlanForCountStapPlan(idPlan: Long) {
        spisVM.countStapPlan.updateQuery(mDB.spisStapPlanQuestQueries.countOpenStapPlan(iskstat = 10, idpl = idPlan))
    }

    fun setPlanForSpisStapPlan(idPlan: Long) {
        idPlanForSpisStapPlanQuest = idPlan
        updateSpisStapPlan()
    }

    fun setPlanForSpisStapPlanForSelect(idPlan: Long, array_iskl: Collection<Long> = listOf()) { //,iskstat: Long
        spisVM.spisStapPlanForSelect.updateQuery(
            mDB.spisStapPlanQuestQueries.openStapPlanForSelect(
                idpl = idPlan,
                iskl = array_iskl,
                iskstat = 10
            )
        )
    }

    fun setExpandStapPlan(id: Long, sver: Boolean) {
        mDB.spisStapPlanQuestQueries.expandStapPlan(id = id, sver = if (sver) "true" else "false")
    }

    fun setListenerCountStapPlan(updF: (Long) -> Unit) {
        spisVM.countStapPlan.updateFunc {
            it.firstOrNull()?.let(updF)
        }
    }


    /*******************************************************************************/
    /**************************  Start node block  *********************************/
    /*******************************************************************************/

    fun setSelectTreeSkillsForSelectForTrigger(idTree: Long, typeTree: TypeTreeSkills) {
        spisVM.spisAllNodeTreeSkillsForSelectForTrigger.updateQuery(
            mDB.spisNodeTreeSkillsQuestQueries.selectAllNodesForSelect(
                typeTree.name,
                idTree
            )
        )
        spisVM.spisLevelTreeSkillsForSelect.updateQuery(mDB.spisLevelTreeSkillsQuestQueries.selectLevelTreeSkillForSelect(idTree))
    }
    fun setSelectTreeSkills(idTree: Long, typeTree: TypeTreeSkills) {
        spisVM.spisHandNodeTreeSkills.updateQuery(
            mDB.spisNodeTreeSkillsQuestQueries.selectHandNodes(
                typeTree.name,
                idTree,
                TypeNodeTreeSkills.HAND.id
            )
        )
        spisVM.spisPlanNodeTreeSkills.updateQuery(
            mDB.propertyPlanNodeTSQuestQueries.selectPlanNodeTreeSkills(
                typeTree.name,
                idTree,
                TypeNodeTreeSkills.PLAN.id
            )
        )
        spisVM.spisLevelTreeSkills.updateQuery(mDB.spisLevelTreeSkillsQuestQueries.selectLevelTreeSkill(idTree))
//        spisVM.spisBindingNodeTreeSkills.updateQuery(mDB.spisBindingNodeTreeSkillsQueries.selectForTree(idTree))
        spisVM.spisWholeBranchParentNodeTreeSkills.updateQuery(
            mDB.spisBindingNodeTreeSkillsQuestQueries.selectWholeBranchParent(
                idTree
            )
        )
        spisVM.spisWholeBranchChildNodeTreeSkills.updateQuery(
            mDB.spisBindingNodeTreeSkillsQuestQueries.selectWholeBranchChild(
                idTree
            )
        )
    }


    /**
     * Получает список выделенных узлов из списка для выделения, возвращает максимальный уровень среди этих узлов,
     * массив id всех этих узлов, а также записывает все эти узлы в список для отображения их в один ряд.
     * */
    fun getSelectIdNodeTreeSkills(): Pair<Long, Array<Long>> {
        val rez = mutableListOf<Long>()
        val listNode = mutableListOf<ItemNodeTreeSkillsQuest>()
        var level = 0L
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                if (nodeTreeSkills.check && nodeTreeSkills.marker != MarkerNodeTreeSkills.UNENABLE) {
                    rez.add(nodeTreeSkills.id)
                    listNode.add(nodeTreeSkills.copy())
                    if (nodeTreeSkills.level > level) level = nodeTreeSkills.level
                }
            }
        }
        spisVM.spisNodeTreeSkillsSelection.setValue(listNode) // spisVM.getListSelectNodeTreeSkills(rez.toTypedArray()))
        return Pair(level, rez.toTypedArray())
    }

    /**
     * Сбрасывает все выделение и параметры в списке узлов для выделения.
     * */
    fun resetSelectionNodeTreeSkills() {
        spisVM.spisNodeTreeSkillsSelection.setValue(listOf())
        setSelectNodeTreeSkillsForSelection(arrayOf())
        changeItemNodeId = null
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                nodeTreeSkills.marker = MarkerNodeTreeSkills.NONE
            }
        }
    }


    fun changeCheckNode(node: ItemNodeTreeSkillsQuest) {
        node.check = node.check.not()
        if (node.check) {
            unenabelParentNodeForSelection(node.id)
        } else {
            updateCheckAndEnable()
        }
        unenableChangeItemNodeAndChildForSelection()
    }

    /**
     * Устанавливает маркер в значение -1, для всех предков узла с переданным id.
     * Маркер -1 предполагает отключение узла от возможности выбрать его.
     * */
    private fun unenabelParentNodeForSelection(id: Long) {
        spisVM.spisParentBranchNodeTreeSkills.getValue()?.find { it.id_node == id }?.let {
            setMarkerNodeTreeSkillsForSelection(it.getAllParent(), MarkerNodeTreeSkills.UNENABLE)
        }
    }

    /**
     * Обновляет маркеры, после изменения выделения. Т.к. после снятия выделения нужно убрать маркер -1 для предков
     * этого узла, но только в том случае если его предки не являются предками другого выделенного узла.
     * Для этого похоже прощее вначале убрать все маркеры, а потом пробежаться по выделенным узлам заново.
     * Не думаю, что деревья будут разрастаться до таких размеров, чтобы эта процедура выглядела слишком затратно
     * по ресурсам... но возможно в будущем это нужно будет оптимизировать.
     * */
    private fun updateCheckAndEnable() {
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                nodeTreeSkills.marker = MarkerNodeTreeSkills.NONE
            }
        }
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach { nodeTreeSkills ->
                if (nodeTreeSkills.check) {
                    unenabelParentNodeForSelection(nodeTreeSkills.id)
                }
            }
        }
    }

    fun updateInfoSpisNode(item: ItemNodeTreeSkillsQuest) {
        spisVM.updateInfoSpisNode(
            spisVM.spisParentBranchNodeTreeSkills.getValue()?.toList()?.find { it.id_node == item.id })
    }

    private fun setSelectNodeTreeSkillsForSelection(selected: Array<Long>) {
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach {
                it.check = selected.contains(it.id)
            }
        }
    }

    /**
     * Вызывается когда создается панель редактирования/добавления ачивки, когда в эту панель передана ачивка из дерева
     * общего вида для редактирования.
     * */
    fun setSelectNodeTreeSkillsForSelectionForNode(idNode: Long): Pair<Long, Array<Long>> {
        spisVM.spisParentBranchNodeTreeSkills.getValue()?.toList()?.find { it.id_node == idNode }?.let {
            setSelectNodeTreeSkillsForSelection(it.direct)
            updateCheckAndEnable()
        }
        changeItemNodeId = idNode
        unenableChangeItemNodeAndChildForSelection()
        return getSelectIdNodeTreeSkills()
    }

    /**
     * В этой переменной хранится id ачивки которая в данный момент редактируется, чтобы исключать ее и ее детей
     * из возможности выбора в качестве родителя.
     * */
    private var changeItemNodeId: Long? = null

    /**
     * Эта функция отключает возможность выбрать саму ачивку и ее детей в качестве
     * своего же родителя.
     * */
    private fun unenableChangeItemNodeAndChildForSelection() {
        changeItemNodeId?.let { changeId ->
            spisVM.spisChildBranchNodeTreeSkills.getValue()?.find { it.id_node == changeId }?.let {
                setMarkerNodeTreeSkillsForSelection(it.getNodeAndAllParent(), MarkerNodeTreeSkills.UNENABLELVL2)
            } ?: setMarkerNodeTreeSkillsForSelection(arrayOf(changeId), MarkerNodeTreeSkills.UNENABLELVL2)
        }
    }

    private fun setMarkerNodeTreeSkillsForSelection(selected: Array<Long>, marker: MarkerNodeTreeSkills = MarkerNodeTreeSkills.NONE) {
        spisVM.spisNodeTreeSkillsForSelection.getValue()?.toList()?.forEach {
            it.second.forEach {
                if (selected.contains(it.id)) it.marker = marker
            }
        }
    }

    private fun setCheckNodeTreeSkills(selected: Array<Long>) {
        spisVM.spisNodeTreeSkills.getValue()?.toList()?.forEach {
            it.second.forEach {
                it.check = selected.contains(it.id)
            }
        }
    }

    fun setMarkerParentAndChildForNodeTreeSkills(itemNodeTreeSkills: ItemNodeTreeSkillsQuest) {
        val parentBranch =
            spisVM.spisParentBranchNodeTreeSkills.getValue()?.find { it.id_node == itemNodeTreeSkills.id }
                ?: ItemParentBranchNode(itemNodeTreeSkills.id, arrayOf(), arrayOf())
        val childBranch =
            spisVM.spisChildBranchNodeTreeSkills.getValue()?.find { it.id_node == itemNodeTreeSkills.id }
                ?: ItemParentBranchNode(itemNodeTreeSkills.id, arrayOf(), arrayOf())
        spisVM.spisNodeTreeSkills.getValue()?.toList()?.forEach {
            it.second.forEach { node ->
                if (parentBranch.direct.contains(node.id)) node.marker = MarkerNodeTreeSkills.DIRECTPARENT
                else if (parentBranch.indirect.contains(node.id)) node.marker = MarkerNodeTreeSkills.INDIRECTPARENT
                else if (childBranch.direct.contains(node.id)) node.marker = MarkerNodeTreeSkills.DIRECTCHILD
                else if (childBranch.indirect.contains(node.id)) node.marker = MarkerNodeTreeSkills.INDIRECTCHILD
                else node.marker = MarkerNodeTreeSkills.NONE
            }
        }
    }


    /*******************************************************************************/
    /**************************  End node block  ***********************************/
    /*******************************************************************************/
}