package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*

class AddAvatarHandler(private var mdb: Database, private val commonFun: PrivateCommonFun) {
    fun checkEmptyBirthday(): Boolean {
        val mp = mdb.mainParamQueries.selectBirthday().executeAsList()
        return mp.isEmpty()
    }

    fun addBirthday(
        date: Long
    ) {
        mdb.mainParamQueries.insertBirthday(date.withOffset().toString())
    }

    fun addBestDay(
        name: String, data: Long
    ) {
        mdb.bestDaysQueries.insertBestDay(
            name = name, data_ = data.withOffset()
        )
    }

    fun delBestDay(
        id: Long
    ) {
        mdb.bestDaysQueries.deleteBestDay(id = id)
    }

    fun enableIconBestDay(
        id: Long, enable: Boolean
    ) {
        mdb.bestDaysQueries.enableIcon(if (enable) 1 else 0, id)
    }


    fun addCharacteristic(
        name: String, opis: String, startStat: Long
    ): Long {
        return mdb.commonFunQueries.transactionWithResult<Long> {
            mdb.spisCharacteristicQueries.insertOrReplace(
                name = name, opis = opis, start_value = startStat
            )
            return@transactionWithResult mdb.commonFunQueries.lastInsertRowId().executeAsOne()
        }
    }

    fun updCharacteristic(
        id: Long, name: String, opis: String, startStat: Long
    ) {
        mdb.spisCharacteristicQueries.update(
            id = id, name = name, opis = opis, start_value = startStat
        )
    }

    fun setLvlGoal(item: ItemGoal, newLvl: Long) {
        if (newLvl > item.lvl) {
            mdb.spisGoalQueries.changeLvlToUp(newsort = newLvl, id = item.id.toLong(), oldsort = item.lvl)
        } else {
            mdb.spisGoalQueries.changeLvlToDown(newsort = newLvl, id = item.id.toLong(), oldsort = item.lvl)
        }
    }

    fun setLvlDream(item: ItemDream, newLvl: Long) {
        if (newLvl > item.lvl) {
            mdb.spisDreamQueries.changeLvlToUp(newsort = newLvl, id = item.id.toLong(), oldsort = item.lvl)
        } else {
            mdb.spisDreamQueries.changeLvlToDown(newsort = newLvl, id = item.id.toLong(), oldsort = item.lvl)
        }
    }

    fun setSortCharacteristic(item: ItemCharacteristic, newSort: Long) {
        if (newSort > item.sort) {
            mdb.spisCharacteristicQueries.changeSortToUp(newsort = newSort, id = item.id, oldsort = item.sort)
        } else {
            mdb.spisCharacteristicQueries.changeSortToDown(newsort = newSort, id = item.id, oldsort = item.sort)
        }
    }

    fun delCharacteristic(
        id: Long
    ) {
        mdb.spisCharacteristicQueries.delete(id = id)
    }

    fun addGoal(
        name: String, data1: Long, data2: Long, opis: String, gotov: Double, foto: Long
    ) {
        mdb.spisGoalQueries.insertOrReplaceGoals(
            name = name, data1 = data1.withOffset(), data2 = data2.withOffset(), opis = opis, gotov = gotov, foto = foto
        )
    }

    fun updGoal(
        id: Long,

        name: String, data1: Long, data2: Long, opis: String, foto: Long
    ) {
        mdb.spisGoalQueries.updateGoals(
            id = id, name = name, data1 = data1.withOffset(), data2 = data2.withOffset(), opis = opis, foto = foto
        )
    }

    fun setOpenGoal(id: Long, open: Boolean) {
        if (open) {
            updGotovGoal(id, 100.0)
        } else {
            updGotovGoal(id, 0.0)
        }
    }

    private fun updGotovGoal(
        id: Long, gotov: Double
    ) {
        mdb.spisGoalQueries.updateGotovGoal(
            id = id, gotov = gotov
        )
    }

    fun delGoal(
        id: Long
    ) {
        mdb.spisGoalQueries.deleteGoal(id = id, TypeBindElementForSchetPlan.GOAL.id)
    }

    fun addDream(
        name: String, data1: Long, opis: String, stat: Long, foto: Long
    ) {
        mdb.spisDreamQueries.insertOrReplaceDreams(
            name = name, data1 = data1.withOffset(), opis = opis, stat = stat, foto = foto
        )
    }

    fun updDream(
        id: Long, name: String, data1: Long, opis: String, foto: Long
    ) {
        mdb.spisDreamQueries.updateDreams(
            id = id,
            name = name, data1 = data1.withOffset(), opis = opis, foto = foto
        )
    }

    fun setOpenDream(id: Long, open: Boolean) {
        if (open) {
            updStatDream(id, 10)
        } else {
            updStatDream(id, 0)
        }
    }

    private fun updStatDream(
        id: Long, stat: Long
    ) {
        mdb.spisDreamQueries.updateStatDream(
            id = id, stat = stat
        )
    }

    fun delDream(
        id: Long
    ) {
        mdb.spisDreamQueries.deleteDream(id = id)
    }

    fun addPrivsGoal(
        id_goal: Long, name: String, stap: Long, id_plan: Long, vajn: Long, date: Long
    ) {
        mdb.spis_plan_goalQueries.insertOrReplaceSpisPlanGoal(
            id_goal = id_goal,
            name = name,
            stap = stap,
            id_plan = id_plan,
            vajn = vajn,
            data_ = date.withOffset(),
        )
    }

    fun delPrivsGoal(
        id: Long
    ) {
        mdb.spis_plan_goalQueries.deleteSpisPlanGoal(id = id)
    }

    fun addPrivsCharacteristic(
        id_characteristic: Long,
        stap: Long,
        id_plan: Long,
    ) {
        mdb.spisPlanCharacteristicQueries.insertOrReplace(
            id_characteristic = id_characteristic,
            stap = stap,
            id_plan = id_plan,
        )
    }

    fun delPrivsCharacteristic(
        id: Long
    ) {
        mdb.spisPlanCharacteristicQueries.delete(id = id)
    }

    fun addTreeSkills(
        id_area: Long, name: String, id_type_tree: Long, opis: String, open_edit: Long, icon: Long
    ) {
        mdb.spisTreeSkillQueries.insertOrReplaceTreeSkill(
            id_area = id_area, name = name, id_type_tree = id_type_tree, opis = opis, open_edit = open_edit, icon = icon
        )
    }

    fun updTreeSkills(
        id: Long, id_area: Long, name: String, opis: String, icon: Long
    ) {
        mdb.spisTreeSkillQueries.updateTreeSkill(
            id = id, id_area = id_area, name = name, opis = opis, icon = icon
        )
    }

    fun setOpenEditTreeSkills(
        id: Long, stat: TypeStatTreeSkills
    ) {
        mdb.spisTreeSkillQueries.setOpenEditTreeSkill(open_edit = stat.codValue, id = id)
    }

    fun delTreeSkills(
        id: Long
    ) {
        mdb.spisTreeSkillQueries.deleteTreeSkill(id = id)
    }

    fun addLevelTreeSkills(
        id_tree: Long, name: String, opis: String, proc_porog: Double, level: Long?, quest_id: Long
    ) {
        mdb.spisLevelTreeSkillsQueries.transaction {
            val numLevel: Long =
                level ?: (mdb.spisLevelTreeSkillsQueries.getMaxLevelNum(id_tree = id_tree).executeAsOne() + 1)
            mdb.spisLevelTreeSkillsQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                num_level = numLevel,
                opis = opis,
                proc_porog = proc_porog,
                open_level = if (numLevel == 1L) 1L else 0L,
                quest_id = quest_id
            )
        }
        mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(

            id_tree = id_tree,
            cod_node_visib = TypeStatNodeTree.VISIB.codValue,
            cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
        )
    }

    fun updLevelTreeSkills(
        item: ItemLevelTreeSkills, name: String, opis: String, proc_porog: Double
    ) {
        mdb.spisLevelTreeSkillsQueries.update(
            name = name, opis = opis, proc_porog = proc_porog, id = item.id
        )
        mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
            id_tree = item.id_tree,
            cod_node_visib = TypeStatNodeTree.VISIB.codValue,
            cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
        )
    }

    fun delLevelTreeSkills(
        item: ItemLevelTreeSkills,

        ) {
        mdb.spisLevelTreeSkillsQueries.delete(id = item.id)
        mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
            id_tree = item.id_tree,
            cod_node_visib = TypeStatNodeTree.VISIB.codValue,
            cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
        )
    }

    private fun addTreePropertiesWhenAddNode(
        idNode: Long,
        id_tree: Long,
        quest_id: Long,
        level: Long,
        parents: Array<Long>,
        typeTree: TypeTreeSkills,
        mustNodeForLevel: Boolean = false
    ) {
        when (typeTree) {
            TypeTreeSkills.KIT -> Unit
            TypeTreeSkills.LEVELS -> {
                if (mustNodeForLevel) {
                    mdb.spisMustCompleteNodeForLevelQueries.insertOrReplace(id_tree, idNode, quest_id)
                }
                mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
                    id_tree = id_tree,
                    cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                    cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                )
            }

            TypeTreeSkills.TREE -> {
                if (parents.isNotEmpty()) {
                    mdb.spisBindingNodeTreeSkillsQueries.transaction {
                        parents.forEach { idParent ->
                            mdb.spisBindingNodeTreeSkillsQueries.insertOrReplace(id_tree, idParent, idNode, quest_id)
                        }
                    }
                }
            }
        }
    }

    private fun updateTreePropertiesWhenUpdateNode(
        item: ItemNodeTreeSkills,
        id_tree: Long,
        quest_id: Long,
        level: Long,
        parents: Array<Long>,
        typeTree: TypeTreeSkills,
        mustNodeForLevel: Boolean = false
    ) {
        when (typeTree) {
            TypeTreeSkills.KIT -> Unit
            TypeTreeSkills.LEVELS -> {
                if (mustNodeForLevel != item.must_node || level != item.level) {
                    if (mustNodeForLevel) {
                        mdb.spisMustCompleteNodeForLevelQueries.updateBind(
                            id_node = item.id, id_tree = id_tree, quest_id = quest_id
                        )
                    } else {
                        mdb.spisMustCompleteNodeForLevelQueries.deleteBind(item.id)
                    }
                    mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
                        id_tree = item.id_tree,
                        cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                        cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                    )
                    mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
                        id_tree = item.id_tree,
                        cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                        cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                    )

                }
            }

            TypeTreeSkills.TREE -> {
                mdb.spisBindingNodeTreeSkillsQueries.deleteParentBind(item.id)
                if (parents.isNotEmpty()) {
                    parents.forEach { idParent ->
                        mdb.spisBindingNodeTreeSkillsQueries.insertOrReplace(id_tree, idParent, item.id, quest_id)
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
        stat: Long,
        level: Long,
        icon: Long,
        icon_complete: Long,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false,
        quest_id: Long = 0L
    ) {
        mdb.spisNodeTreeSkillsQueries.transactionWithResult<Long> {
            mdb.spisNodeTreeSkillsQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                opis = opis,
                id_type_node = id_type_node,
                complete = stat,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                quest_id = quest_id
            )
            mdb.spisNodeTreeSkillsQueries.lastInsertRowId().executeAsOne()
        }.let { idNode ->
            if (idNode > 0) {
                addTreePropertiesWhenAddNode(idNode, id_tree, quest_id, level, parents, typeTree, mustNodeForLevel)
            }
        }
    }

    fun updHandNodeTreeSkills(
        item: ItemNodeTreeSkills,
        id_tree: Long,
        name: String,
        opis: String,
        level: Long,
        icon: Long,
        icon_complete: Long,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        quest_id: Long,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQueries.transaction {
            mdb.spisNodeTreeSkillsQueries.update(
                id = item.id, name = name, opis = opis, level = level, icon = icon, icon_complete = icon_complete
            )
            updateTreePropertiesWhenUpdateNode(item, id_tree, quest_id, level, parents, typeTree, mustNodeForLevel)
        }
    }

    fun addPlanNodeTreeSkills(
        id_tree: Long,
        name: String,
        opis: String,
        id_type_node: Long,
        stat: Long,
        level: Long,
        icon: Long,
        icon_complete: Long,
        privplan: Long,
        stap_prpl: Long,
        porog_hour: Double,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false,
        quest_id: Long = 0L
    ) {
        mdb.spisNodeTreeSkillsQueries.transactionWithResult<Long> {
            mdb.spisNodeTreeSkillsQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                opis = opis,
                id_type_node = id_type_node,
                complete = stat,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                quest_id = quest_id
            )
            mdb.spisNodeTreeSkillsQueries.lastInsertRowId().executeAsOne()
        }.let { idNode ->
            if (idNode > 0) {
                mdb.propertyPlanNodeTSQueries.insertOrReplace(
                    id_node = idNode,
                    privplan = privplan,
                    stap_prpl = stap_prpl,
                    porog_hour = porog_hour,
                    quest_id = quest_id
                )
                addTreePropertiesWhenAddNode(idNode, id_tree, quest_id, level, parents, typeTree, mustNodeForLevel)
            }
        }
    }

    fun updPlanNodeTreeSkills(
        item: ItemNodeTreeSkills,
        id_tree: Long,
        name: String,
        opis: String,
        level: Long,
        icon: Long,
        icon_complete: Long,
        privplan: Long,
        stap_prpl: Long,
        porog_hour: Double,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        quest_id: Long,
        mustNodeForLevel: Boolean = false
    ) {
        mdb.spisNodeTreeSkillsQueries.transaction {
            mdb.spisNodeTreeSkillsQueries.update(
                id = item.id, name = name, opis = opis, level = level, icon = icon, icon_complete = icon_complete
            )
            mdb.propertyPlanNodeTSQueries.update(
                id_node = item.id, privplan = privplan, stap_prpl = stap_prpl, sum_hour = porog_hour
            )
            updateTreePropertiesWhenUpdateNode(item, id_tree, quest_id, level, parents, typeTree, mustNodeForLevel)
        }
    }

    fun addCountNodeTreeSkills(
        id_tree: Long,
        name: String,
        opis: String,
        id_type_node: Long,
        stat: Long,
        level: Long,
        icon: Long,
        icon_complete: Long,
        priv_counter: Long,
        count_value: Long,
        max_value: Long,
        porog_value: Long,
        typeTree: TypeTreeSkills,
        parents: Array<Long>,
        mustNodeForLevel: Boolean = false,
        quest_id: Long = 0L

    ) {
        mdb.spisNodeTreeSkillsQueries.transactionWithResult<Long> {
            mdb.spisNodeTreeSkillsQueries.insertOrReplace(
                id_tree = id_tree,
                name = name,
                opis = opis,
                id_type_node = id_type_node,
                complete = stat,
                level = level,
                icon = icon,
                icon_complete = icon_complete,
                quest_id = quest_id
            )
            mdb.spisNodeTreeSkillsQueries.lastInsertRowId().executeAsOne()
        }.let { idNode ->
            if (idNode > 0) {
                mdb.propertyCountNodeTSQueries.insertOrReplace(
                    id_node = idNode,
                    priv_counter = priv_counter,
                    count_value = count_value,
                    max_value = max_value,
                    porog_value = porog_value,
                    quest_id = quest_id
                )
                when (typeTree) {
                    TypeTreeSkills.KIT -> Unit
                    TypeTreeSkills.LEVELS -> {
                        if (mustNodeForLevel) {
                            mdb.spisMustCompleteNodeForLevelQueries.insertOrReplace(id_tree, idNode, quest_id)
                        }
                    }

                    TypeTreeSkills.TREE -> {
                        if (parents.isNotEmpty()) {
                            mdb.spisBindingNodeTreeSkillsQueries.transaction {
                                parents.forEach { idParent ->
                                    mdb.spisBindingNodeTreeSkillsQueries.insertOrReplace(
                                        id_tree, idParent, idNode, quest_id
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun delNodeTreeSkills(
        item: ItemNodeTreeSkills
    ) {
        mdb.spisNodeTreeSkillsQueries.transaction {
            mdb.spisNodeTreeSkillsQueries.delete(id = item.id)
            mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
                id_tree = item.id_tree,
                cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
            )
        }
    }


    fun clearUnlockNowHandNode(item: ItemNodeTreeSkills) {
        mdb.spisNodeTreeSkillsQueries.completeHandNode(
            TypeStatNodeTree.VISIB.codValue, item.id
        )
    }

    fun completeHandNode(item: ItemHandNodeTreeSkills, typeTree: TypeTreeSkills) {
        mdb.spisNodeTreeSkillsQueries.transaction {
            mdb.spisNodeTreeSkillsQueries.completeHandNode(
                if (item.complete != TypeStatNodeTree.COMPLETE) TypeStatPlan.COMPLETE.codValue else TypeStatPlan.VISIB.codValue,
                item.id
            )
            if (typeTree == TypeTreeSkills.LEVELS) {
                mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
                    id_tree = item.id_tree,
                    cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                    cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                )
            }
            if (item.complete != TypeStatNodeTree.COMPLETE) commonFun.runTriggerReact(
                quest_id = item.quest_id.toString(), TypeParentOfTrig.NODETREESKILLS, item.quest_key_id
            )
        }
    }

    fun addIconNodeTree(
        extension: String, type_ramk: Long
    ): Long? {
        mdb.spisIconNodeTreeSkillsQueries.transactionWithResult<Long> {
            mdb.spisIconNodeTreeSkillsQueries.insertOrReplace(
                extension = extension, type_ramk = type_ramk
            )
            return@transactionWithResult mdb.spisIconNodeTreeSkillsQueries.lastInsertRowId().executeAsOne()
        }.let { idIcon ->
            return if (idIcon > 0) idIcon else null
        }
    }

    fun delIconNodeTree(
        id: Long
    ) {
        mdb.spisIconNodeTreeSkillsQueries.delete(id = id)
    }

}