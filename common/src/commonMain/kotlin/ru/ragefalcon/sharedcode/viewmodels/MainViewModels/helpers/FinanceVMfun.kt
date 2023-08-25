package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.Finance.SelectDoxodPeriod
import ru.ragefalcon.sharedcode.Finance.SelectRasxodPeriod
import ru.ragefalcon.sharedcode.Finance.SumDoxPeriod
import ru.ragefalcon.sharedcode.Finance.SumRasxPeriod
import ru.ragefalcon.sharedcode.extensions.unOffset
import ru.ragefalcon.sharedcode.models.data.ItemDoxod
import ru.ragefalcon.sharedcode.models.data.ItemRasxod
import ru.ragefalcon.sharedcode.models.data.ItemSchet
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.FilterSchetOper
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.FinanceVMobjForSpis
import kotlin.properties.Delegates

class FinanceVMfun(private val mDB: Database, private val spisVM: FinanceVMobjForSpis) {

    init {
        spisVM.spisSchet.getObserve { spisSchetInner = it }
    }

    /** Минимальный размер капитала в конце недели за все время */
    fun getMinSumOperWeek(): Double {
        return spisVM.minSumOperWeek
    }

    /** Максимальный размер капитала в конце недели за все время */
    fun getMaxSumOperWeek(): Double {
        return spisVM.maxSumOperWeek
    }

    /** Функция выбора типа расхода по которому будет выведена диаграмма ежемесячных трат */
    fun selectRasxodTypeByMonth(value: Pair<String, String>) {
        spisVM.spisRasxodTypeByMonth.updateQuery(mDB.rasxodQueries.analizRasxodTypeByMonth(value.first.toLong()))
    }

    /** Функция выбора типа расхода по которому будет выведена диаграмма ежемесячных трат, New with date */
    fun selectRasxodTypeByMonthOnYear(typeid: Long) {
        spisVM.spisRasxodTypeByMonthWithDatePriv.updateQuery(mDB.rasxodQueries.analizRasxodTypeByMonth(typeid))
    }

    private var spisSchetInner: List<ItemSchet>? = null

    /** Функция сравнения типа валюты на счете с [idSch] и счете выбранном в основном разделе счетов */
    fun sravnVal(idSch: Long): String? {
        spisSchetInner?.let {
            val val1 = it.map { it.id.toLong() }.indexOf(idSch)
            val val2 = it.map { it.id.toLong() }.indexOf(pos_main_spisSch.first.toLong() ?: idSch)
            if ((val1 != -1) && (val2 != -1)) {
                if (it[val1].cod != it[val2].cod) {
                    return@sravnVal "${it[val2].cod}->${it[val1].cod}"
                }
            }
        }
        return null
    }

    fun sravnVal(idSch1: Long, idSch2: Long): String? {
        spisSchetInner?.let {
            val val1 = it.map { it.id.toLong() }.indexOf(idSch1)
            val val2 = it.map { it.id.toLong() }.indexOf(idSch2)
            if ((val1 != -1) && (val2 != -1)) {
                if (it[val1].cod != it[val2].cod) {
                    return@sravnVal "${it[val2].cod}->${it[val1].cod}"
                }
            }
        }
        return null
    }

    private var pos_main_spisSch: Pair<String, String> by Delegates.observable(Pair("-1", "")) { prop, old, new ->
        spisVM.schetSpisPeriod.updateFunQuery { dtB, dtE ->
            mDB.schetaQueries.selectSchet(
                doxodTypeTitle = FilterSchetOper.Doxod.title,
                rasxodTypeTitle = FilterSchetOper.Rasxod.title,
                perevodTypeTitle = FilterSchetOper.Perevod.title,
                dtbegin = dtB,
                dtend = dtE,
                schet = new.first.toLong() ?: -1
            )
        }
        spisVM.schetSumma.updateFunQuery { dtB, dtE ->
            mDB.schetaQueries.sumSchetPeriod(new.first.toLong())
        }
        spisVM.sumsOperForPeriod.updateFunQuery { dtB, dtE ->
            mDB.schetaQueries.sumsOperForPeriod(new.first.toLong(), dtbegin = dtB, dtend = dtE)
        }
        spisVM.spisSchetPerevodWithIsklForAddPan.updateQuery(mDB.schetaQueries.selectAllKrome(new.first.toLong()))
    }

    /** Функция выбора основного счета */
    fun setPosMainSchet(value: Pair<String, String>) {
        pos_main_spisSch = value
    }

    /** Получает текущий выбранный в основном меню счет */
    fun getPosMainSchet(): Pair<String, String> {
        return pos_main_spisSch
    }

    private var pos_main_spisSchPl: Pair<String, String> by Delegates.observable(Pair("-1", "")) { prop, old, new ->
        spisVM.schetPlanSpisPeriod.updateFunQuery { dtB, dtE ->
            mDB.spisSchetPlQueries.selectSchetPlan(dtB, dtE, new.first.toLong() ?: -1)
        }
        spisVM.schetPlanSumma.updateFunQuery { dtB, dtE ->
            mDB.spisSchetPlQueries.sumSchetPlanPeriod(new.first.toLong())
        }
        spisVM.sumsOperPlForPeriod.updateFunQuery { dtB, dtE ->
            mDB.spisSchetPlQueries.sumsOperPlForPeriod(new.first.toLong(), dtbegin = dtB, dtend = dtE)
        }
        spisVM.spisSchetPlanPerevodWithIsklForAddPan.updateQuery(mDB.spisSchetPlQueries.selectAllSchetPlanKrome(new.first.toLong()))
    }

    /** Функция выбора основного счета */
    fun setPosMainSchetPlan(value: Pair<String, String>) {
        pos_main_spisSchPl = value
    }

    /** Получает текущий выбранный в основном меню счет */
    fun getPosMainSchetPlan(): Pair<String, String> {
        return pos_main_spisSchPl
    }

    private var enabFilter = false

    /************************/
    var pos_filter_typeRasx: Pair<String, String> by Delegates.observable(Pair("-1", "")) { prop, old, new ->
        if (enabFilter) {
            spisVM.rasxodSpisPeriod.updateFunQuery { dtB, dtE ->
                mDB.rasxodQueries.selectRasxodPeriodAndType(
                    dtB,
                    dtE,
                    new.first.toLong() ?: -1
                ) { id, name, summa, type_id, data, schet_id, schpl_id, sumrub, type, schet, schet_open, schpl_open, typerasxod_open ->
                    SelectRasxodPeriod(
                        id,
                        name,
                        summa,
                        type_id,
                        data,
                        schet_id,
                        schpl_id,
                        sumrub,
                        type,
                        schet,
                        schet_open,
                        schpl_open,
                        typerasxod_open
                    )
                }
            }
            spisVM.rasxodSummaPeriod.updateFunQuery { dtB, dtE ->
                mDB.rasxodQueries.sumRasxPeriodAndType(dtB, dtE, new.first.toLong()) {
                    SumRasxPeriod(it)
                }
            }
        }
    }

    fun getItemRasxodById(id: Long): ItemRasxod? = mDB.rasxodQueries.selectOneRasxodById(id).executeAsOneOrNull()?.let {
        ItemRasxod(
            it._id.toString(),
            it.name,
            it.type_id.toString(),
            it.type.toString(),
            it.summa,
            it.data_.unOffset(),
            it.schet_id.toString(),
            it.schet ?: "счет не найден",
            it.typerasxod_open == "true" || it.typerasxod_open == "True",
            it.schet_open == "true" || it.schet_open == "True",
            it.schpl_open == 1L,
            it.schpl_id
        )
    }

    fun getItemDoxodById(id: Long): ItemDoxod? = mDB.doxodQueries.selectOneDoxodById(id).executeAsOneOrNull()?.let {
        ItemDoxod(
            it._id.toString(),
            it.name,
            it.type_id.toString(),
            it.type.toString(),
            it.summa,
            it.data_.unOffset(),
            it.schet_id.toString(),
            it.schet ?: "",
            typedoxod_open = it.typerasxod_open == "true" || it.typerasxod_open == "True",
            schet_open = it.schet_open == "true" || it.schet_open == "True",
        )
    }

    fun setPosFilterRasx(value: Pair<String, String>) {
        pos_filter_typeRasx = value
    }

    fun getPosFilterRasx(): Pair<String, String> {
        return pos_filter_typeRasx
    }


    /************************/
    var pos_filter_typeDox: Pair<String, String> by Delegates.observable(Pair("-1", "")) { prop, old, new ->
        if (enabFilter) {
            spisVM.doxodSpisPeriod.updateFunQuery { dtB, dtE ->
                mDB.doxodQueries.selectDoxodPeriodAndType(
                    dtB,
                    dtE,
                    new.first.toLong() ?: -1
                ) { a, s, d, f, g, h, j, k, b, c, e ->
                    SelectDoxodPeriod(a, s, d, f, g, h, j, k, b, c, e)
                }
            }
            spisVM.doxodSummaPeriod.updateFunQuery { dtB, dtE ->
                mDB.doxodQueries.sumDoxPeriodAndType(dtB, dtE, new.first.toLong()) {
                    SumDoxPeriod(it)
                }
            }
        }
    }

    fun setPosFilterDox(value: Pair<String, String>) {
        pos_filter_typeDox = value
    }

    fun getPosFilterDox(): Pair<String, String> {
        return pos_filter_typeDox
    }

    /************************/
    var filter_enable: Boolean by Delegates.observable(false) { prop, old, new ->
        enabFilter = new
        when (new) {
            true -> {
                spisVM.rasxodSpisPeriod.updateFunQuery { dtB, dtE ->
                    mDB.rasxodQueries.selectRasxodPeriodAndType(
                        dtB,
                        dtE,
                        pos_filter_typeRasx.first.toLong() ?: -1
                    ) { id, name, summa, type_id, data, schet_id, schpl_id, sumrub, type, schet, schet_open, schpl_open, typerasxod_open ->
                        SelectRasxodPeriod(
                            id,
                            name,
                            summa,
                            type_id,
                            data,
                            schet_id,
                            schpl_id,
                            sumrub,
                            type,
                            schet,
                            schet_open,
                            schpl_open,
                            typerasxod_open
                        )
                    }
                }
                spisVM.rasxodSummaPeriod.updateFunQuery { dtB, dtE ->
                    mDB.rasxodQueries.sumRasxPeriodAndType(dtB, dtE, pos_filter_typeRasx.first.toLong()) {
                        SumRasxPeriod(it)
                    }
                }
                spisVM.doxodSpisPeriod.updateFunQuery { dtB, dtE ->
                    mDB.doxodQueries.selectDoxodPeriodAndType(
                        dtB,
                        dtE,
                        pos_filter_typeDox.first.toLong() ?: -1
                    ) { a, s, d, f, g, h, j, k, b, c, e ->
                        SelectDoxodPeriod(a, s, d, f, g, h, j, k, b, c, e)
                    }
                }
                spisVM.doxodSummaPeriod.updateFunQuery { dtB, dtE ->
                    mDB.doxodQueries.sumDoxPeriodAndType(dtB, dtE, pos_filter_typeDox.first.toLong()) {
                        SumDoxPeriod(it)
                    }
                }
            }

            false -> {
                spisVM.rasxodSpisPeriod.updateFunQuery { dtB, dtE ->
                    mDB.rasxodQueries.selectRasxodPeriod(dtB, dtE)
                }
                spisVM.rasxodSummaPeriod.updateFunQuery { dtB, dtE ->
                    mDB.rasxodQueries.sumRasxPeriod(dtB, dtE)
                }
                spisVM.doxodSpisPeriod.updateFunQuery { dtB, dtE ->
                    mDB.doxodQueries.selectDoxodPeriod(dtB, dtE)
                }
                spisVM.doxodSummaPeriod.updateFunQuery { dtB, dtE ->
                    mDB.doxodQueries.sumDoxPeriod(dtB, dtE)
                }
            }
        }
    }

    fun setEnableFilter(value: Boolean) {
        filter_enable = value
    }

    fun setVisibleOpenSettSchet(open: Boolean) {
        spisVM.spisSchetForSett.updateQuery(mDB.schetaQueries.selectSpisSchetForSett(if (open) "false" else "true||false"))
    }

    fun setVisibleOpenSettSchetPlan(open: Boolean) {
        spisVM.spisSchetPlanForSett.updateQuery(
            mDB.spisSchetPlQueries.selectSpisSchetPlanForSett(
                if (open) listOf(0) else listOf(
                    -1L
                )
            )
        )
    }

    fun setVisibleOpenSettTyperasxod(open: Boolean) {
        spisVM.spisTyperasxodForSett.updateQuery(mDB.typeRasxodQueries.selectTypeRasxodForSett(if (open) "false" else "true||false"))
    }

    fun setVisibleOpenSettTypedoxod(open: Boolean) {
        spisVM.spisTypedoxodForSett.updateQuery(mDB.typeDoxodQueries.selectTypedoxodForSett(if (open) "false" else "true||false"))
    }

    fun getEnableFilter(): Boolean {
        return filter_enable
    }
    /************************/
}