package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.models.data.itemQuest.*
import ru.ragefalcon.sharedcode.quest.DatabaseQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills

class ParentOfTrigger(val typeParent: TypeParentOfTrig, val parent_id: Long)
class StartObjOfTrigger(val typeStartObjOfTrigger: TypeStartObjOfTrigger, val start_id: Long, val start_name: String, val act_code: Long = 1L)

fun ItemPlanQuest.parentOfTrig() = ParentOfTrigger(TypeParentOfTrig.PLAN, this.id.toLong())
fun ItemPlanStapQuest.parentOfTrig() = ParentOfTrigger(TypeParentOfTrig.PLANSTAP, this.id.toLong())
fun ItemOtvetDialog.parentOfTrig() = ParentOfTrigger(TypeParentOfTrig.OTVDIALOG, this.id.toLong())
fun ItemNodeTreeSkillsQuest.parentOfTrig() = ParentOfTrigger(TypeParentOfTrig.NODETREESKILLS, this.id)


fun ItemPlanQuest.startObjOfTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.STARTPLAN, this.id.toLong(), this.name)
fun ItemPlanStapQuest.startObjOfTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.STARTSTAP, this.id.toLong(), this.name)
fun ItemDialog.startObjOfTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.STARTDIALOG, this.id.toLong(), this.name)

fun ItemNodeTreeSkillsQuest.startObjOfTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.STARTNODETREE, this.id, this.name)
fun ItemTreeSkillsQuest.startObjOfTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.STARTTREE, this.id, this.name)
fun ItemLevelTreeSkillsQuest.startObjOfTrig() = StartObjOfTrigger(TypeStartObjOfTrigger.STARTLEVELTREE, this.id, this.name)

class AddQuestHandler(private var mdb: DatabaseQuest) {

//    fun addTriggerStartPlan(itemParent: ItemPlanQuest, itemStart: ItemPlanQuest) {
//        addTriggerStartPlan(itemParent.parentTrig(),itemStart.startObjTrig())
//    }
//    fun addTriggerStartPlan(itemParent: ItemPlanQuest, itemStart: ItemPlanQuest) {
//        mdb.commonTriggerQueries.insertOrReplace(
//            TypeParentTrig.PLAN.code,
//            itemParent.id.toLong(),
//            TypeTrigger.STARTPLAN.id,
//            itemStart.id.toLong(),
//            itemStart.name,
//            1L
//        )
//    }

    fun addTriggerStartPlan(parent: ParentOfTrigger, start: StartObjOfTrigger) {
        addTriggerStartPlan(
            parent.typeParent,
            parent.parent_id,
            start.typeStartObjOfTrigger,
            start.start_id,
            start.start_name,
            start.act_code
        )
    }

    private fun addTriggerStartPlan(
        typeParent: TypeParentOfTrig,
        parent_id: Long,
        typeStartObjOfTrigger: TypeStartObjOfTrigger,
        start_id: Long,
        start_name: String,
        act_code: Long = 1L
    ) {
        mdb.commonTriggerQueries.insertOrReplace(
            typeParent.code,
            parent_id,
            typeStartObjOfTrigger.id,
            start_id,
            start_name,
            act_code
        )
    }

    fun delTrigger(id: Long) {
        mdb.commonTriggerQueries.delete(id)
    }

    fun addGovorun(
        name: String,
        opis: String,
        image_file: String,
    ): Long {
        return mdb.spisGovorunQueries.transactionWithResult {
            mdb.spisGovorunQueries.insertOrReplace(
                name = name,
                opis = opis,
                image_file = image_file,
            )
            return@transactionWithResult mdb.commonTriggerQueries.lastInsertRowId().executeAsOne()
        }
    }

    fun updGovorun(
        id: Long,
        name: String,
        opis: String,
        image_file: String,
    ) {
        mdb.spisGovorunQueries.update(
            id = id,
            name = name,
            opis = opis,
            image_file = image_file,
        )
    }

    fun delGovorun(id: Long) {
        mdb.spisGovorunQueries.delete(id)
    }

    fun addDialog(
        name: String,
        maintext: String,
        govorun_id: Long,
    ) {
        mdb.spisDialogQueries.insertOrReplace(
            name = name,
            maintext = maintext,
            govorun_id = govorun_id,
        )
    }

    fun updDialog(
        id: Long,
        name: String,
        maintext: String,
        govorun_id: Long,
    ) {
        mdb.spisDialogQueries.update(
            id = id,
            name = name,
            maintext = maintext,
            govorun_id = govorun_id,
            type_trig_id = listOf(TypeStartObjOfTrigger.STARTDIALOG.id)
        )
    }

    fun delDialog(id: Long) {
        mdb.spisDialogQueries.delete(
            id,
            parent_type_element_otvet = TypeParentOfTrig.OTVDIALOG.code,
            type_trig_id_dialog = TypeStartObjOfTrigger.getListIdDialogTrigger()
        )
    }

    fun setStartQuestDialog(
        item: ItemDialog
    ) {
        val startObj = item.startObjOfTrig()
        mdb.commonTriggerQueries.setStartQuestDialog(
            TypeParentOfTrig.STARTQUESTDIALOG.code,
            -25L,
            startObj.typeStartObjOfTrigger.id,
            startObj.start_id,
            startObj.start_name,
            startObj.act_code
        )
    }

    fun addOtvetDialog(
        dialog_id: Long,
        text: String,
    ) {
        mdb.spisOtvetDialogQueries.insertOrReplace(
            dialog_id = dialog_id,
            text = text,
            order_number = 0
        )
    }

    fun updOtvetDialog(
        id: Long,
        text: String,
    ) {
        mdb.spisOtvetDialogQueries.update(
            text = text,
            id = id,
        )
    }

    fun delOtvetDialog(id: Long) {
        mdb.spisOtvetDialogQueries.delete(id, parent_type_element_otvet = TypeParentOfTrig.OTVDIALOG.code)
    }

    fun addPlan(
        vajn: Long,
        name: String,
        hour: Double,
        srok: Long,
        statsrok: Long,
        opis: String,
        commstat: Long
    ) {
        mdb.spisPlanQuestQueries.insertOrReplacePlan(
            vajn = vajn,
            name = name,
            opis = opis,
            hour = hour,
            srok = srok,
            statsrok = statsrok,
            commstat = commstat
        )
    }

    fun updPlan(
        id: Long,
        vajn: Long,
        name: String,
        hour: Double,
        srok: Long,
        statsrok: Long,
        opis: String,
        commstat: Long,
    ) {
        mdb.spisPlanQuestQueries.updatePlan(
            id = id,
            vajn = vajn,
            name = name,
            opis = opis,
            hour = hour,
            srok = srok,
            statsrok = statsrok,
            commstat = commstat,
            type_trig_id = listOf(TypeStartObjOfTrigger.STARTPLAN.id)
        )
    }

    fun delPlan(id: Long) {
        mdb.spisPlanQuestQueries.deletePlan(
            id,
            parent_type_element_plan = TypeParentOfTrig.PLAN.code,
            type_trig_id_plan = TypeStartObjOfTrigger.getListIdPlanTrigger()
        )
    }

    fun addStapPlan(
        parent_id: Long,
        name: String,
        hour: Double,
        srok: Long,
        statsrok: Long,
        opis: String,
        commstat: Long,
        idplan: Long
    ) {
        mdb.spisStapPlanQuestQueries.insertOrReplaceStapPlan(
            name = name,
            opis = opis,
            hour = hour,
            srok = srok,
            statsrok = statsrok,
            commstat = commstat,
            parent_id = parent_id,
            svernut = "false",
            idplan = idplan
        )
    }

    fun updStapPlan(
        id: Long,
        parent_id: Long,
        name: String,
        hour: Double,
        srok: Long,
        statsrok: Long,
        opis: String,
        commstat: Long,
        idplan: Long
    ) {
        mdb.spisStapPlanQuestQueries.updatePlanStap(
            id = id,
            name = name,
            opis = opis,
            hour = hour,
            srok = srok,
            statsrok = statsrok,
            commstat = commstat,
            parent_id = parent_id,
            idplan = idplan,
            type_trig_id = listOf(TypeStartObjOfTrigger.STARTSTAP.id)
        )
    }

    fun delStapPlan(id: Long) {
        mdb.spisStapPlanQuestQueries.deletePlanStap(
            id,
            parent_type_element_stap_plan = TypeParentOfTrig.PLANSTAP.code,
            type_trig_id_stap_plan = TypeStartObjOfTrigger.getListIdStapPlanTrigger()
        )
    }

    fun addMainparam(prioritet: Long, opis: String, name: String) {
        mdb.mainParamQueries.insertParam(name = name, prioritet = prioritet, opis = opis)
    }

    fun deleteMainparam(id: Long) {
        mdb.mainParamQueries.deleteParam(id = id)
    }


    fun updMainparam(int: Long, str: String, name: String, id: Long) {
        mdb.mainParamQueries.updateParam(name = name, str = str, int = int, id = id)
    }

    fun updMainparam(int: Long, id: Long) {
        mdb.mainParamQueries.updateIntParamId(int, id)
    }

    fun updMainparam(str: String, id: Long) {
        mdb.mainParamQueries.updateStrParamId(str, id)
    }

    fun updMainparam(int: Long, name: String) {
        mdb.mainParamQueries.updateIntParamName(int, name)
    }

    fun updMainparam(str: String, name: String) {
        mdb.mainParamQueries.updateStrParamName(str, name)
    }

    fun addTreeSkills(
        name: String,
        opis: String,
        idArea: Long,
        idTypeTree: Long,
        icon: Long,
        visibleStat: Long
    ) {
        mdb.spisTreeSkillsQuestQueries.insertOrReplaceTreeSkill(
            name = name,
            opis = opis,
            id_area = idArea,
            id_type_tree = idTypeTree,
            icon = icon,
            visible_stat = visibleStat
        )
    }

    fun updTreeSkills(
        id: Long,
        name: String,
        opis: String,
        idArea: Long,
        icon: Long,
        visibleStat: Long
    ) {
        mdb.spisTreeSkillsQuestQueries.updateTreeSkill(
            id = id,
            name = name,
            opis = opis,
            id_area = idArea,
            icon = icon,
            visible_stat = visibleStat,
            type_start_trig_id = listOf(TypeStartObjOfTrigger.STARTTREE.id)
        )
    }

    fun delTreeSkills(id: Long) {
        mdb.spisTreeSkillsQuestQueries.deleteTreeSkill(
            id,
            type_trig_start_tree = listOf(TypeStartObjOfTrigger.STARTTREE.id)
        )
    }

    /*******************************************************************************/
    /**************************  Start node block  *********************************/
    /*******************************************************************************/

    fun addLevelTreeSkills(
        id_tree: Long,
        name: String,
        opis: String,
        proc_porog: Double,
        level: Long?,
        visible_stat: Long
    ) {
        mdb.spisLevelTreeSkillsQuestQueries.transaction {
            val maxNum: Long =
                level ?: (mdb.spisLevelTreeSkillsQuestQueries.getMaxLevelNum(id_tree = id_tree).executeAsOne() + 1)
            mdb.spisLevelTreeSkillsQuestQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                num_level = maxNum,
                opis = opis,
                proc_porog = proc_porog,
                visible_stat = visible_stat
            )
        }
    }

    fun updLevelTreeSkills(
        item: ItemLevelTreeSkillsQuest,
        name: String,
        opis: String,
        proc_porog: Double,
        visible_stat: Long
    ) {
        mdb.spisLevelTreeSkillsQuestQueries.update(
            name = name,
            opis = opis,
            proc_porog = proc_porog,
            visible_stat = visible_stat,
            id = item.id,
            type_start_trig_id = listOf(TypeStartObjOfTrigger.STARTLEVELTREE.id)
        )
    }

    fun delLevelTreeSkills(
        id: Long
    ) {
        mdb.spisLevelTreeSkillsQuestQueries.delete(
            id = id,
            type_trig_start_node = listOf(TypeStartObjOfTrigger.STARTLEVELTREE.id)
        )
    }

    private fun addTreePropertiesWhenAddNode(
        idNode: Long,
        id_tree: Long,
        level: Long,
        parents: Array<Long>,
        typeTree: TypeTreeSkills,
        mustNodeForLevel: Boolean = false
    ) {
        when (typeTree) {
            TypeTreeSkills.KIT -> {
            }
            TypeTreeSkills.LEVELS -> {
                if (mustNodeForLevel) {
                    mdb.spisMustCompleteNodeForLevelQuestQueries.insertOrReplace(id_tree, idNode)
                }
            }
            TypeTreeSkills.TREE -> {
                if (parents.isNotEmpty()) {
                    mdb.spisBindingNodeTreeSkillsQuestQueries.transaction {
                        parents.forEach { idParent ->
                            mdb.spisBindingNodeTreeSkillsQuestQueries.insertOrReplace(id_tree, idParent, idNode)
                        }
                    }
                }
            }
        }
    }

    private fun updateTreePropertiesWhenUpdateNode(
        item: ItemNodeTreeSkillsQuest,
        id_tree: Long,
        level: Long,
        parents: Array<Long>,
        typeTree: TypeTreeSkills,
        mustNodeForLevel: Boolean = false
    ) {
        when (typeTree) {
            TypeTreeSkills.KIT -> {
            }
            TypeTreeSkills.LEVELS -> {
                if (mustNodeForLevel != item.must_node || level != item.level) {
                    if (mustNodeForLevel) {
                        mdb.spisMustCompleteNodeForLevelQuestQueries.updateBind(
                            id_node = item.id,
                            id_tree = id_tree,
                        )
                    } else {
                        mdb.spisMustCompleteNodeForLevelQuestQueries.deleteBind(item.id)
                    }
                }
            }
            TypeTreeSkills.TREE -> {
                mdb.spisBindingNodeTreeSkillsQuestQueries.deleteParentBind(item.id)
                if (parents.isNotEmpty()) {
                    parents.forEach { idParent ->
                        mdb.spisBindingNodeTreeSkillsQuestQueries.insertOrReplace(id_tree, idParent, item.id)
                    }
                }
            }
        }
    }

    fun addHandNodeTreeSkills(
        id_tree: Long,
        name: String,
        opis: String,
        id_type_node: Long,
        level: Long,
        icon: Long,
        icon_complete: Long,
        visible_stat: Long,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQuestQueries.transactionWithResult<Long> {
            mdb.spisNodeTreeSkillsQuestQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                opis = opis,
                id_type_node = id_type_node,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                visible_stat = visible_stat
            )
            mdb.spisNodeTreeSkillsQuestQueries.lastInsertRowId().executeAsOne()
        }.let { idNode ->
            if (idNode > 0) {
                addTreePropertiesWhenAddNode(idNode, id_tree, level, parents, typeTree, mustNodeForLevel)
            }
        }
    }

    fun updHandNodeTreeSkills(
        item: ItemNodeTreeSkillsQuest,
        id_tree: Long,
        name: String,
        opis: String,
        level: Long,
        icon: Long,
        icon_complete: Long,
        visible_stat: Long,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQuestQueries.transaction { //WithResult<Long>
            mdb.spisNodeTreeSkillsQuestQueries.update(
                id = item.id,
                name = name,
                opis = opis,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                visible_stat = visible_stat,
                type_start_trig_id = listOf(TypeStartObjOfTrigger.STARTNODETREE.id)
            )
            updateTreePropertiesWhenUpdateNode(item, id_tree, level, parents, typeTree, mustNodeForLevel)
        }
    }

    fun addPlanNodeTreeSkills(
        id_tree: Long,
        name: String,
        opis: String,
        id_type_node: Long,
        level: Long,
        icon: Long,
        icon_complete: Long,
        privplan: Long,
        stap_prpl: Long,
        visible_stat: Long,
        porog_hour: Double,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQuestQueries.transactionWithResult<Long> {
            mdb.spisNodeTreeSkillsQuestQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                opis = opis,
                id_type_node = id_type_node,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                visible_stat = visible_stat
            )
            mdb.spisNodeTreeSkillsQuestQueries.lastInsertRowId().executeAsOne()
        }.let { idNode ->
            if (idNode > 0) {
                mdb.propertyPlanNodeTSQuestQueries.insertOrReplace(
                    id_node = idNode,
                    privplan = privplan,
                    stap_prpl = stap_prpl,
                    porog_hour = porog_hour
                )
                addTreePropertiesWhenAddNode(idNode, id_tree, level, parents, typeTree, mustNodeForLevel)
            }
        }
    }

    fun updPlanNodeTreeSkills(
        item: ItemNodeTreeSkillsQuest,
        id_tree: Long,
        name: String,
        opis: String,
        level: Long,
        icon: Long,
        icon_complete: Long,
        visible_stat: Long,
        privplan: Long,
        stap_prpl: Long,
        porog_hour: Double,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQuestQueries.transaction { //WithResult<Long>
            mdb.spisNodeTreeSkillsQuestQueries.update(
                id = item.id,
                name = name,
                opis = opis,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                visible_stat = visible_stat,
                type_start_trig_id = listOf(TypeStartObjOfTrigger.STARTNODETREE.id)
            )
            mdb.propertyPlanNodeTSQuestQueries.update(
                id_node = item.id,
                privplan = privplan,
                stap_prpl = stap_prpl,
                sum_hour = porog_hour
            )
            updateTreePropertiesWhenUpdateNode(item, id_tree, level, parents, typeTree, mustNodeForLevel)
        }
    }

    fun addCountNodeTreeSkills(
        id_tree: Long,
        name: String,
        opis: String,
        id_type_node: Long,
        level: Long,
        icon: Long,
        icon_complete: Long,
        visible_stat: Long,
        priv_counter: Long,
        count_value: Long,
        max_value: Long,
        porog_value: Long,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQuestQueries.transactionWithResult<Long> {
            mdb.spisNodeTreeSkillsQuestQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                opis = opis,
                id_type_node = id_type_node,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                visible_stat = visible_stat
            )
            mdb.spisNodeTreeSkillsQuestQueries.lastInsertRowId().executeAsOne()
        }.let { idNode ->
            if (idNode > 0) {
                mdb.propertyCountNodeTSQuestQueries.insertOrReplace(
                    id_node = idNode,
                    priv_counter = priv_counter,
                    count_value = count_value,
                    max_value = max_value,
                    porog_value = porog_value
                )
                when (typeTree) {
                    TypeTreeSkills.KIT -> {
                    }
                    TypeTreeSkills.LEVELS -> {
                        if (mustNodeForLevel) {
                            mdb.spisMustCompleteNodeForLevelQuestQueries.insertOrReplace(id_tree, idNode)
                        }
                    }
                    TypeTreeSkills.TREE -> {
                        if (parents.isNotEmpty()) {
                            mdb.spisBindingNodeTreeSkillsQuestQueries.transaction {
                                parents.forEach { idParent ->
                                    mdb.spisBindingNodeTreeSkillsQuestQueries.insertOrReplace(id_tree, idParent, idNode)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun delNodeTreeSkills(
        item: ItemNodeTreeSkillsQuest
    ) {
        mdb.spisNodeTreeSkillsQuestQueries.delete(
            id = item.id,
            parent_type_element_node = TypeParentOfTrig.NODETREESKILLS.code,
            type_trig_start_node = listOf(TypeStartObjOfTrigger.STARTNODETREE.id)
        )
    }

    fun addIconNodeTree(
        extension: String,
        type_ramk: Long
    ): Long? {
        mdb.spisIconNodeTreeSkillsQuestQueries.transactionWithResult<Long> {
            mdb.spisIconNodeTreeSkillsQuestQueries.insertOrReplace(
                extension = extension,
                type_ramk = type_ramk
            )
            return@transactionWithResult mdb.spisIconNodeTreeSkillsQuestQueries.lastInsertRowId().executeAsOne()
        }.let { idIcon ->
            return if (idIcon > 0) idIcon else null
        }
    }

    fun delIconNodeTree(
        id: Long
    ) {
        mdb.spisIconNodeTreeSkillsQuestQueries.delete(id = id)
    }


    /*******************************************************************************/
    /**************************  End node block  ***********************************/
    /*******************************************************************************/
}