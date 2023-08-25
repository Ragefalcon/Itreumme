package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.minusTime
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan

class AddFinanceHandler(private var mdb: Database) {

    fun addShabRasxod(
        name: String,
        nameoper: String,
        summa: Double,
        type: String,
        schet_id: Long,
        schpl_id: Long? = null
    ) {
        mdb.shabRasxodQueries.insertOrReplaceShablonRasxod(
            name = name,
            namer = nameoper,
            summar = summa,
            typer = type,
            schet_id = schet_id,
            schpl_id = schpl_id
        )
    }

    fun updShabRasxod(
        item: ItemShabRasxod,
        name: String,
        nameoper: String,
        summa: Double,
        type: String,
        schet_id: Long,
        schpl_id: Long? = null
    ) {
        mdb.shabRasxodQueries.updateShablonRasxod(
            id = item.id.toLong(),
            name = name,
            namer = nameoper,
            summar = summa,
            typer = type,
            schet_id = schet_id,
            schpl_id = schpl_id
        )
    }

    fun setSortShabRasxod(item: ItemShabRasxod, newSort: Long) {
        if (newSort > item.sort) {
            mdb.shabRasxodQueries.changeSortToUp(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        } else {
            mdb.shabRasxodQueries.changeSortToDown(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        }
    }

    fun delShabRasxod(
        id: Long
    ) {
        mdb.shabRasxodQueries.deleteShablonRasxod(id = id)
    }

    fun addShabDoxod(
        name: String,
        nameoper: String,
        summa: Double,
        type: String,
        schet_id: Long
    ) {
        mdb.shabDoxodQueries.insertOrReplaceShablonDoxod(
            name = name,
            named = nameoper,
            summad = summa,
            typed = type,
            schet_id = schet_id
        )
    }

    fun updShabDoxod(
        item: ItemShabDoxod,
        name: String,
        nameoper: String,
        summa: Double,
        type: String,
        schet_id: Long
    ) {
        mdb.shabDoxodQueries.updateShablonDoxod(
            id = item.id.toLong(),
            name = name,
            named = nameoper,
            summad = summa,
            typed = type,
            schet_id = schet_id
        )
    }


    fun setSortShabDoxod(item: ItemShabDoxod, newSort: Long) {
        if (newSort > item.sort) {
            mdb.shabDoxodQueries.changeSortToUp(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        } else {
            mdb.shabDoxodQueries.changeSortToDown(newsort = newSort, id = item.id.toLong(), oldsort = item.sort)
        }
    }

    fun delShabDoxod(
        id: Long
    ) {
        mdb.shabDoxodQueries.deleteShablonDoxod(id = id)
    }

    fun addRasxod(
        name: String,
        summa: Double,
        typeid: Long,
        data: Long,
        schet: Long,
        schpl_id: Long? = null
    ) {
        mdb.rasxodQueries.insertOrReplace(
            name = name,
            summa = summa,
            type_id = typeid,
            data = data.withOffset(),
            schet_id = schet,
            schpl_id = schpl_id
        )
    }

    fun updRasxod(
        item: ItemRasxod,
        name: String,
        summa: Double,
        typeid: Long,
        data: Long,
        schet: Long,
        schpl_id: Long? = null
    ): Boolean {
        if (item.typerasxod_open && item.schet_open && item.schpl_open) {
            mdb.rasxodQueries.updateRasxod(
                id = item.id.toLong(),
                name = name,
                summa = summa,
                type_id = typeid,
                data = data.withOffset(),
                schet_id = schet,
                schpl_id = schpl_id
            )
            return true
        }
        return false
    }

    fun delRasxod(
        item: ItemRasxod
    ): Boolean {
        if (item.typerasxod_open && item.schet_open && item.schpl_open) {
            mdb.rasxodQueries.deleteRasxod(id = item.id.toLong())
            return true
        }
        return false
    }


    fun addDoxod(
        name: String,
        summa: Double,
        typeid: Long,
        data: Long,
        schet: Long
    ) {
        mdb.doxodQueries.insertOrReplace(
            name = name,
            summ = summa,
            type_id = typeid,
            data_ = data.withOffset(),
            sch_id = schet
        )
    }

    fun updDoxod(
        item: ItemDoxod,
        name: String,
        summa: Double,
        typeid: Long,
        data: Long,
        schet: Long
    ): Boolean {
        if (item.typedoxod_open && item.schet_open) {
            mdb.doxodQueries.updateDoxod(
                id = item.id.toLong(),
                name = name,
                summa = summa,
                type_id = typeid,
                data = data.withOffset(),
                schet_id = schet
            )
            return true
        }
        return false
    }

    fun delDoxod(
        item: ItemDoxod
    ): Boolean {
        if (item.typedoxod_open && item.schet_open) {
            mdb.doxodQueries.deleteDoxod(id = item.id.toLong())
            return true
        }
        return false
    }

    fun addPerevod(
        name: String,
        schsp_id: Long,
        sumsp: Double,
        schz_id: Long,
        sumz: Double,
        data: Long
    ) {
        mdb.schetaQueries.insertPerevod(
            name = name,
            schsp_id = schsp_id,
            sumsp = sumsp,
            schz_id = schz_id,
            sumz = sumz,
            data_ = data.withOffset()
        )
    }

    fun updPerevod(
        item: ItemCommonFinOper,
        name: String,
        schsp_id: Long,
        sumsp: Double,
        schz_id: Long,
        sumz: Double,
        data: Long
    ) {
        mdb.schetaQueries.updatePerevod(
            id = item.id.toLong(),
            name = name,
            schsp_id = schsp_id,
            sumsp = sumsp,
            schz_id = schz_id,
            sumz = sumz,
            data = data.withOffset()
        )
    }

    fun addPerevodPlan(
        name: String,
        schsp_id: Long,
        summa: Double,
        schz_id: Long,
        data: Long
    ) {
        mdb.spisSchetPlQueries.insertPerevod(
            name = name,
            schsp_id = schsp_id,
            summa = summa,
            schz_id = schz_id,
            data_ = data.withOffset()
        )
    }

    fun updPerevodPlan(
        item: ItemCommonFinOper,
        name: String,
        schsp_id: Long,
        summa: Double,
        schz_id: Long,
        data: Long
    ) {
        mdb.spisSchetPlQueries.updatePerevod(
            id = item.id.toLong(),
            name = name,
            schsp_id = schsp_id,
            summa = summa,
            schz_id = schz_id,
            data = data.withOffset()
        )
    }

    fun delPerevod(
        item: ItemCommonFinOper
    ): Boolean {
        if (item.second_schet_open) {
            mdb.schetaQueries.deletePerevod(id = item.id.toLong())
            return true
        }
        return false
    }

    fun delPerevodPlan(
        item: ItemCommonFinOper
    ): Boolean {
        if (item.second_schet_open) {
            mdb.spisSchetPlQueries.deletePerevod(id = item.id.toLong())
            return true
        }
        return false
    }

    fun addSchet(
        name: String,
        val_id: Long
    ) {
        mdb.schetaQueries.insertOrReplace(
            name = name,
            val_id = val_id
        )
    }

    fun updSchetName(
        id: Long,
        name: String
    ) {
        mdb.schetaQueries.updateSchetName(
            id = id,
            name = name
        )
    }

    fun updSchetOpen(
        id: Long,
        open: Boolean
    ) {
        mdb.schetaQueries.updateSchetOpen(
            id = id,
            openn = if (open) "true" else "false"
        )
    }

    fun delSchet(
        id: Long
    ) {
        mdb.schetaQueries.deleteSchet(id = id)
    }


    fun addSchetPlan(
        name: String,
        min_aim: Double,
        max_aim: Double,
        open: Long,
        bind_element: Pair<TypeBindElementForSchetPlan, Long>? = null
    ) {
        mdb.spisSchetPlQueries.transactionWithResult<Long> {
            mdb.spisSchetPlQueries.insertOrReplace(
                namesp = name,
                min_aim = min_aim,
                max_aim = max_aim,
                open_ = open
            )
            return@transactionWithResult mdb.bindForSchetPlanQueries.lastInsertRowId().executeAsOne()
        }?.let { schet_id ->
            bind_element?.let {
                mdb.bindForSchetPlanQueries.insertOrReplaceBind(it.first.id, it.second, schet_id)
            }
        }
    }

    fun addBindForSchetPlan(
        typeBindElementForSchetPlan: TypeBindElementForSchetPlan,
        element_id: Long,
        schpl_id: Long
    ) {
        mdb.bindForSchetPlanQueries.insertOrReplaceBind(typeBindElementForSchetPlan.id, element_id, schpl_id)
    }

    fun deleteBindForSchetPlan(
        typeBindElementForSchetPlan: TypeBindElementForSchetPlan,
        element_id: Long
    ) {
        mdb.bindForSchetPlanQueries.deleteBind(typeBindElementForSchetPlan.id, element_id)
    }

    fun delBindWithDelOrCloseSchetPlan(
        typeBindElementForSchetPlan: TypeBindElementForSchetPlan,
        element_id: Long
    ) {
        mdb.bindForSchetPlanQueries.getSchetPlan(typeBindElementForSchetPlan.id, element_id).executeAsOne()
            .let { schpl_id ->
                mdb.bindForSchetPlanQueries.transaction {
                    delOrCloseSchetPlan(schpl_id)
                    mdb.bindForSchetPlanQueries.deleteBind(typeBindElementForSchetPlan.id, element_id)
                }
            }
    }

    fun updSchetPLan(
        id: Long,
        name: String,
        min_aim: Double,
        max_aim: Double,
    ) {
        mdb.spisSchetPlQueries.updateSchetPlan(
            id = id,
            name = name,
            min_aim = min_aim,
            max_aim = max_aim,
        )
    }

    fun updSchetPlanOpen(
        id: Long,
        open: Long
    ) {
        mdb.spisSchetPlQueries.updateSchetPlanOpen(
            id = id,
            openn = open
        )
    }

    fun delSchetPlan(
        id: Long
    ) {
        mdb.spisSchetPlQueries.deleteSchetPlan(id = id)
    }

    fun closeSchetPlan(
        id: Long
    ) {
        mdb.spisSchetPlQueries.testForCloseOrDelSpisSchetPlan(id).executeAsOne().let { test ->
            mdb.spisSchetPlQueries.transaction {
                if (test.summa != 0.0) {
                    if (test.summa > 0) {
                        addPerevodPlan(
                            "в связи с закрытием счета",
                            id,
                            test.summa,
                            1,
                            DateTimeTz.nowLocal().minusTime().localUnix()
                        )
                    } else {
                        addPerevodPlan(
                            "в связи с закрытием счета",
                            1,
                            -test.summa,
                            id,
                            DateTimeTz.nowLocal().minusTime().localUnix()
                        )
                    }
                }
                updSchetPlanOpen(id, 0)
            }
        }
    }

    fun delOrCloseSchetPlan(
        id: Long
    ) {
        mdb.spisSchetPlQueries.testForCloseOrDelSpisSchetPlan(id).executeAsOne().let { test ->
            if (test.countoper.toInt() == 0) {
                mdb.spisSchetPlQueries.deleteSchetPlan(id = id)
            } else {
                mdb.spisSchetPlQueries.transaction {
                    if (test.summa != 0.0) {
                        if (test.summa > 0) {
                            addPerevodPlan(
                                "в связи с закрытием счета",
                                id,
                                test.summa,
                                1,
                                DateTimeTz.nowLocal().minusTime().localUnix()
                            )
                        } else {
                            addPerevodPlan(
                                "в связи с закрытием счета",
                                1,
                                -test.summa,
                                id,
                                DateTimeTz.nowLocal().minusTime().localUnix()
                            )
                        }
                    }
                    updSchetPlanOpen(id, 0)
                }
            }
        }
    }


    fun addTyperasxod(
        name: String
    ) {
        mdb.typeRasxodQueries.insertOrReplace(
            typer = name
        )
    }

    fun updTyperasxodName(
        id: Long,
        name: String
    ) {
        mdb.typeRasxodQueries.updateTyperasxodName(
            id = id,
            name = name
        )
    }

    fun updTyperasxodSchetPlan(
        id: Long,
        planschet: String,
        schpl_id: Long
    ) {
        mdb.typeRasxodQueries.updateTyperasxodSchetPlan(
            id = id,
            planschet = planschet,
            schpl_id = schpl_id
        )
    }

    fun updTyperasxodOpen(
        id: Long,
        open: Boolean
    ) {
        mdb.typeRasxodQueries.updateTyperasxodOpen(
            id = id,
            openn = if (open) "true" else "false"
        )
    }

    fun delTyperasxod(
        id: Long
    ) {
        mdb.typeRasxodQueries.deleteTyperasxod(id = id)
    }

    fun addTypedoxod(
        name: String
    ) {
        mdb.typeDoxodQueries.insertOrReplace(
            typed = name
        )
    }

    fun updTypedoxodName(
        id: Long,
        name: String
    ) {
        mdb.typeDoxodQueries.updateTypedoxodName(
            id = id,
            name = name
        )
    }

    fun updTypedoxodOpen(
        id: Long,
        open: Boolean
    ) {
        mdb.typeDoxodQueries.updateTypedoxodOpen(
            id = id,
            openn = if (open) "true" else "false"
        )
    }

    fun delTypedoxod(
        id: Long
    ) {
        mdb.typeDoxodQueries.deleteTypedoxod(id = id)
    }

    fun addValut(
        name: String,
        cod: String,
        kurs: Double
    ) {
        mdb.spisValutQueries.insertOrReplace(
            name = name,
            cod = cod,
            kurs = kurs
        )
    }

    fun updValut(
        id: Long,
        name: String,
        cod: String,
        kurs: Double
    ) {
        mdb.spisValutQueries.updateValut(
            id = id,
            name = name,
            cod = cod,
            kurs = kurs
        )
    }

    fun delValut(
        id: Long
    ) {
        mdb.spisValutQueries.deleteValut(id = id)
    }

}