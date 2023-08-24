package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.longMinusTimeLocal
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.QUEST_ID_INNER_DIALOG

class AddTimeHandler(
    private var mdb: Database,
    private val commonFun: PrivateCommonFun,
    private val addComplexOpis: AddComplexOpisHandler
) {
    fun addEffekt(
        name: String,
        idplan: Long,
        norma: Double
    ) {
        mdb.effektQueries.insertOrReplaceEffekt(
            name = name,
            idplan = idplan,
            norma = norma
        )
    }

    fun updEffekt(
        name: String,
        norma: Double,
        id: Long
    ) {
        mdb.effektQueries.updateEffekt(
            name = name,
            norma = norma,
            id = id
        )
    }

    fun delEffekt(
        id: Long
    ) {
        mdb.effektQueries.deleteEffekt(id = id)
    }

    fun addVxod(
        name: String,
        opis: List<ItemComplexOpis>,
        data: Long,
        stat: Long
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis) {
        mdb.vxodQueries.insertOrReplaceVxod(
            name = name,
            opis = "opis",
            data_ = data.withOffset().longMinusTimeLocal(),
            stat = stat
        )
    }

    fun updVxod(
        id: Long,
        name: String,
        opis: List<ItemComplexOpis>,
        data: Long,
        stat: Long
    ): List<PairId> {
        mdb.vxodQueries.updateVxod(
            id = id,
            name = name,
            opis = "opis",
            data = data.withOffset().longMinusTimeLocal(),
            stat = stat
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun delVxod(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.vxodQueries.deleteVxod(id = id)
        delImageComplexOpis(id)
    }

    fun addNapom(
        idplan: Long,
        idstap: Long,
        name: String,
        opis: List<ItemComplexOpis>,
        data: Long,
        time: String,
        gotov: Boolean
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis) {
        mdb.napomQueries.insertOrReplaceNapom(
            idplan = idplan,
            idstap = idstap,
            name = name,
            opis = "opis",
            data_ = data.withOffset().longMinusTimeLocal(),
            time = time,
            gotov = if (gotov) "true" else "false"
        )
    }

    fun updNapom(
        id: Long,
        name: String,
        opis: List<ItemComplexOpis>,
        data: Long,
        time: String,
    ): List<PairId> {
        mdb.napomQueries.updateNapom(
            id = id,
            name = name,
            opis = "opis",
            data = data.withOffset().longMinusTimeLocal(),
            time = time
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun setVypNapom(gotov: Boolean, data: Long, id: Long) {
        mdb.napomQueries.updateVypNapom(
            gotov = if (gotov) "true" else "false",
            data = data.withOffset().longMinusTimeLocal(),
            id = id
        )
//        println("data1 add = ${data.withOffset().minusTime()}")
    }

    fun delNapom(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.napomQueries.deleteNapom(id = id)
        delImageComplexOpis(id)
    }

    private fun addNextAction(
        name: String,
        vajn: Long,
        opis: List<ItemComplexOpis>,
        privplan: Long,
        stap_prpl: Long
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis, withId = {
        mdb.spisNextActionQueries.insertCommonIntoNextAction(it)
    }) {
        mdb.spisNextActionQueries.insertNextActionCommon(
            name = name,
            vajn = vajn,
            privplan = privplan,
            stap_prpl = stap_prpl
        )
    }

    private fun updNextAction(
        id: Long,
        name: String,
        vajn: Long,
        opis: List<ItemComplexOpis>,
        privplan: Long,
        stap_prpl: Long
    ): List<PairId> {
        mdb.spisNextActionQueries.updateNextActionCommon(
            id = id,
            name = name,
            vajn = vajn,
            privplan = privplan,
            stap_prpl = stap_prpl
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun updOrAddNextAction(
        id: Long?,
        name: String,
        vajn: Long,
        opis: List<ItemComplexOpis>,
        privplan: Long,
        stap_prpl: Long
    ): List<PairId> = id?.let {
        updNextAction(id, name, vajn, opis, privplan, stap_prpl)
    } ?: addNextAction(name, vajn, opis, privplan, stap_prpl)

    fun setSortNextAction(item: ItemNextAction, newSort: Long) {
        if (newSort > item.sort) {
            mdb.spisNextActionQueries.changeSortToUp(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        } else {
            mdb.spisNextActionQueries.changeSortToDown(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        }
    }

    fun delNextAction(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.napomQueries.transaction {
            mdb.spisNextActionQueries.deleteNextActionCommon(id = id)
            mdb.complexOpisQueries.deleteForItem(TableNameForComplexOpis.spisNextAction.nameTable, id)
        }
        delImageComplexOpis(id)
    }

    fun addShablonDenPlan(
        name: String,
        namepl: String,
        opis: List<ItemComplexOpis>,
        vajn: Long,
        time1: String,
        time2: String,
        privplan: Long,
        stap_prpl: Long,
        povtor: String
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis) {
        mdb.shabDenPlanQueries.insertOrReplaceShablonDenPlan(
            name = name,
            namepl = namepl,
            opis = "opis",
            vajn = vajn,
            time1 = time1,
            time2 = time2,
            privplan = privplan,
            stap_prpl = stap_prpl,
            povtor = povtor
        )
    }

    fun setSortShabDenPlan(item: ItemShablonDenPlan, newSort: Long) {
        if (newSort > item.sort) {
            mdb.shabDenPlanQueries.changeSortToUp(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        } else {
            mdb.shabDenPlanQueries.changeSortToDown(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        }
    }


    fun delShablonDenPlan(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.shabDenPlanQueries.deleteShablonDenPlan(id = id)
        delImageComplexOpis(id)
    }

    fun executeDenPlanTransaction(trans: () -> Unit) {
        mdb.denPlanQueries.transaction {
            trans()
        }
    }

    fun addDenPlan(
        vajn: Long,
        name: String,
        gotov: Double,
        data: Long,
        time1: String,
        time2: String,
        opis: List<ItemComplexOpis>,
        privplan: Long,
        stap_prpl: Long,
    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis, withId = {
        if (gotov != 0.0 && it > 0L) {
            val tmpPlan = ItemDenPlan(
                it.toString(),
                vajn = vajn,
                name = name,
                gotov = gotov,
                data = data.withOffset().longMinusTimeLocal(),
                time1 = time1,
                time2 = time2,
                opis = "opis",
                privplan = privplan,
                stap_prpl = stap_prpl,
                sum_hour = 0.0,
                nameprpl = "",
                namestap = ""
            )
            updGotovDenPlan(tmpPlan, gotov)
        }
    }) {
        mdb.denPlanQueries.insertOrReplaceDenPlan(
            vajn = vajn,
            name = name,
            gotov = 0.0,
            data_ = data.withOffset().longMinusTimeLocal(),
            time1 = time1,
            time2 = time2,
            opis = "opis",
            privplan = privplan,
            stap_prpl = stap_prpl,
            sum_hour = 0.0,
            exp = 0.0
        )
    }

    fun updDenPlan(
        item: ItemDenPlan,
        name: String,
        opis: List<ItemComplexOpis>,
        vajn: Long,
        data: Long,
        time1: String,
        time2: String,
        privplan: Long,
        stap_prpl: Long,
        exp: Double
    ): List<PairId> {

        mdb.denPlanQueries.updateDenPlan(
            id = item.id.toLong(),
            name = name,
            opis = "opis",
            vajn = vajn,
            data = data.withOffset().longMinusTimeLocal(),
            time1 = time1,
            time2 = time2,
            privplan = privplan,
            stap_prpl = stap_prpl,
            sum_hour = item.sum_hour,
            exp = exp
        )
        updGotovDenPlan(item.copy(time1 = time1, time2 = time2, vajn = vajn), item.gotov)
        return addComplexOpis.updComplexOpis(item.id.toLong(), opis)
    }

    fun updGotovDenPlan(
        id: Long,
        gotov: Double,
        sum_hour: Double,
        exp: Double
    ) {
        mdb.denPlanQueries.updateGotovDenPlan(gotov = gotov, sum_hour = sum_hour, exp = exp, id = id)
    }

    fun updGotovDenPlan(
        item: ItemDenPlan,
        gotov: Double,
    ) {
//        val time1 = timeFromHHmmss(item.time1)
//        var time2 = timeFromHHmmss(item.time2)
//        if (time2<time1) time2 += 24*60*60*1000
//        val hourMls =
//            (time2 - time1) * gotov / 100.0
//        val hour = hourMls.toDouble() / 1000F / 60F / 60F
//        val exp: Double = when (item.vajn) {
//            0L -> hour * 1.25
//            1L -> hour //* 1.0
//            2L -> hour * 0.5
//            3L -> hour * 0.25
//            else -> 0.0
//        }
//        item.gotov = gotov.toDouble()
        item.setGotov(gotov) { hour, gotov1, exp ->
            mdb.denPlanQueries.updateGotovDenPlan(
                gotov = gotov1,
                sum_hour = hour,
                exp = exp,
                id = item.id.toLong()
            )
        }
    }

    fun delDenPlan(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.denPlanQueries.deleteDenPlan(id = id)
        delImageComplexOpis(id)
    }

    /**
     * data1 = 0 data2 = 1 если сроки не указываются
     * */
    fun addPlan(
        vajn: Long,
        name: String,
        gotov: Double,
        data1: Long,
        data2: Long,
        opis: List<ItemComplexOpis>,
        stat: TypeStatPlan,
        direction: Boolean

    ): List<PairId> = addComplexOpis.addComplexOpisAfterOtherItem(opis) {
        mdb.spisPlanQueries.insertOrReplacePlan(
            vajn = vajn,
            name = name,
            gotov = gotov,
            data1 = data1.withOffset().longMinusTimeLocal(),
            data2 = data2.withOffset().longMinusTimeLocal(),
            opis = "opis",
            stat = stat.codValue,
            direction = if (direction) 1L else 0L
        )
    }

    fun updPlan(
        id: Long,
        vajn: Long,
        name: String,
        data1: Long,
        data2: Long,
        opis: List<ItemComplexOpis>,
        direction: Boolean
//        opis: String
    ): List<PairId> {
        mdb.spisPlanQueries.updatePlan(
            id = id,
            vajn = vajn,
            name = name,
            data1 = data1.withOffset().longMinusTimeLocal(),
            data2 = data2.withOffset().longMinusTimeLocal(),
            opis = "opis",
            direction = if (direction) 1L else 0L
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun setSortPlan(item: ItemPlan, newSort: Long) {
        if (newSort > item.sort) {
            mdb.spisPlanQueries.changeSortToUp(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        } else {
            mdb.spisPlanQueries.changeSortToDown(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        }
    }


    fun updGotovPlan(
        id: Long,
        gotov: Double
    ) {
        mdb.spisPlanQueries.updateGotovPlan(gotov, id)
    }

    fun updStatPlan(
        item: ItemPlan,
//        id: Long,
//        quest_id: Long,
        stat: TypeStatPlan
    ) {
        mdb.spisPlanQueries.transaction {
            mdb.spisPlanQueries.updateStatPlan(stat = stat.codValue, id = item.id.toLong())
            if (stat == TypeStatPlan.COMPLETE) {
//                mdb.spisQuestElementQueries.getQuestId(TypeQuestElement.PLAN.code, id).executeAsList().let {
//                    it.firstOrNull()?.let { quest_id ->
                commonFun.runTriggerReact(quest_id = item.quest_id.toString(), TypeParentOfTrig.PLAN, item.quest_key_id)
//                    }
//                }
                mdb.propertyPlanNodeTSQueries.completePlanUpdate(
                    TypeStatNodeTree.COMPLETE.codValue,
                    item.id.toLong(),
                    -4L
                )
                mdb.propertyPlanNodeTSQueries.selectSpisNodeForPlanOrStap(item.id.toLong(), -4L).executeAsList()
                    .let { listNode ->
                        listNode.forEach {
                            mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
//                                it.level,
                                it.id_tree,
                                cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                                cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                            )
                        }
                    }
            } else {
                if (item.stat == TypeStatPlan.COMPLETE) {
                    mdb.propertyPlanNodeTSQueries.completePlanUpdate(
                        TypeStatNodeTree.VISIB.codValue,
                        item.id.toLong(),
                        -4L
                    )
                    mdb.propertyPlanNodeTSQueries.selectSpisNodeForPlanOrStap(item.id.toLong(), -4L).executeAsList()
                        .let { listNode ->
                            listNode.forEach {
                                mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
//                                    it.level,
                                    it.id_tree,
                                    cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                                    cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                                )
                            }
                        }
                }
            }

        }
    }

    fun delPlan(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.spisPlanQueries.deletePlan(id = id, typePlanId = TypeBindElementForSchetPlan.PLAN.id)
        delImageComplexOpis(id)
    }

    fun addStapPlan(
        parent_id: Long,
        name: String,
        gotov: Double,
        data1: Long,
        data2: Long,
        opis: List<ItemComplexOpis>,
//        opis: String
        stat: TypeStatPlanStap,
        svernut: String,
        idplan: Long,
        marker: Long,
        quest_id: Long = 0
    ): List<PairId> {
        var listNewId = listOf<PairId>()
        mdb.spisStapPlanQueries.transactionWithResult<Long> {
            mdb.spisStapPlanQueries.insertOrReplaceStapPlan(
                parent_id = parent_id,
                name = name,
                gotov = gotov,
                data1 = data1.withOffset().longMinusTimeLocal(),
                data2 = data2.withOffset().longMinusTimeLocal(),
                opis = "opis",
                stat = stat.codValue,
                svernut = svernut,
                idplan = idplan,
                marker = marker,
                quest_id = quest_id
            )
            return@transactionWithResult mdb.spisPlanQueries.getLastIndex().executeAsOne()
        }.let { plan_id ->
            if (plan_id > 0) {
                listNewId = addComplexOpis.addComplexOpis(plan_id, opis)
            }
        }
        return listNewId
    }

    fun updPlanStap(
        id: Long,
        parent_id: Long,
        name: String,
        data1: Long,
        data2: Long,
        opis: List<ItemComplexOpis>,
        marker: Long,
//        opis: String
        idplan: Long
    ): List<PairId> {
        mdb.spisStapPlanQueries.updatePlanStap(
            id = id,
            parent_id = parent_id,
            name = name,
            data1 = data1.withOffset().longMinusTimeLocal(),
            data2 = data2.withOffset().longMinusTimeLocal(),
            opis = "opis",
            idplan = idplan,
            marker = marker
        )
        return addComplexOpis.updComplexOpis(id, opis)
    }

    fun setSortPlanStap(item: ItemPlanStap, newSort: Long) {
        if (newSort > item.sort) {
            mdb.spisStapPlanQueries.changeSortToUp(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        } else {
            mdb.spisStapPlanQueries.changeSortToDown(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        }
    }


    fun updGotovPlanStap(
        id: Long,
        gotov: Double
    ) {
        mdb.spisStapPlanQueries.updateGotovPlanStap(gotov, id)
    }

    fun updMarkerPlanStap(
        id: Long,
        marker: Long
    ) {
        mdb.spisStapPlanQueries.updateMarkerPlanStap(marker = marker, id = id)
    }

    fun updStatPlanStap(
        item: ItemPlanStap,
//        id: Long,
//        quest_id: Long,
        stat: TypeStatPlanStap
    ) {
        mdb.spisStapPlanQueries.transaction {
            mdb.spisStapPlanQueries.updateStatPlanStap(
                stat = stat.codValue,
                id = item.id.toLong(),
                statNext = TypeStatPlanStap.NEXTACTION.codValue
            )
            if (stat == TypeStatPlanStap.COMPLETE) {
//                mdb.spisQuestElementQueries.getQuestId(TypeQuestElement.PLANSTAP.code, id).executeAsList().let {
//                    it.firstOrNull()?.let { quest_id ->
                commonFun.runTriggerReact(
                    quest_id = item.quest_id.toString(),
                    TypeParentOfTrig.PLANSTAP,
                    item.quest_key_id
                )
//                    }
//                }
                mdb.propertyPlanNodeTSQueries.completePlanUpdate(
                    TypeStatNodeTree.COMPLETE.codValue,
                    -4L,
                    item.id.toLong()
                )
                mdb.propertyPlanNodeTSQueries.selectSpisNodeForPlanOrStap(-4L, item.id.toLong()).executeAsList()
                    .let { listNode ->
                        listNode.forEach {
                            mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
//                                it.level,
                                it.id_tree,
                                cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                                cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                            )
                        }
                    }
            } else {
                if (item.stat == TypeStatPlanStap.COMPLETE) {
                    mdb.propertyPlanNodeTSQueries.completePlanUpdate(
                        TypeStatNodeTree.VISIB.codValue,
                        -4L,
                        item.id.toLong()
                    )
                    mdb.propertyPlanNodeTSQueries.selectSpisNodeForPlanOrStap(-4L, item.id.toLong()).executeAsList()
                        .let { listNode ->
                            listNode.forEach {
                                mdb.spisLevelTreeSkillsQueries.updateCompleteLevel(
//                                    it.level,
                                    it.id_tree,
                                    cod_node_visib = TypeStatNodeTree.VISIB.codValue,
                                    cod_node_complete = TypeStatNodeTree.COMPLETE.codValue
                                )
                            }
                        }
                }
            }
            if (stat == TypeStatPlanStap.NEXTACTION) {
                mdb.spisNextActionQueries.insertStapIntoNextAction(item.id.toLong())
            }
        }
    }


    fun delPlanStap(
        id: Long,
        delImageComplexOpis: (Long) -> Unit
    ) {
        mdb.spisStapPlanQueries.deletePlanStap(id = id, typePlanStapId = TypeBindElementForSchetPlan.PLANSTAP.id)
        delImageComplexOpis(id)
    }

    fun startInnerTrigger(trig: InnerStartTriggerEnum) {
        commonFun.runTriggerReact(
            quest_id = QUEST_ID_INNER_DIALOG.toString(),
            typeParentOfTrig = TypeParentOfTrig.INNERSTART,
            elementId = trig.id
        )
    }
}

