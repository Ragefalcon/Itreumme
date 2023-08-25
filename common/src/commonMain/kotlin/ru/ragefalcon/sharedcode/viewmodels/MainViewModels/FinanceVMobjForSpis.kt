package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.Finance.*
import ru.ragefalcon.sharedcode.extensions.*
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PeriodSelecter
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.setMapStatikToItemYearGraf
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.setMapStatikToItemYearGrafTwoRect
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.*


class FinanceVMobjForSpis(val mDB: Database) {

    val sumAllCapAdapter = UniConvertQueryAdapter<Double, String>() {
        "${it.roundToStringProb(2)} Руб."
    }.apply { updateQuery(mDB.schetaQueries.sumAllCapital()) }
    var sumAllCap = sumAllCapAdapter.getMyObsObjOneValue()

    /** Переменные для работы с датой */
    val selPer = PeriodSelecter()

    val spisShabRasxod = UniConvertQueryAdapter<SelectShablonRasxod, ItemShabRasxod>() {
        ItemShabRasxod(
            it._id.toString(),
            it.sort,
            it.name,
            it.namer,
            it.summar,
            it.typer,
            it.schetName ?: "",
            it.schet_id,
            it.schpl_id
        )
    }.apply {
        updateQuery(
            mDB.shabRasxodQueries.selectShablonRasxod()
        )
    }

    val spisShabDoxod = UniConvertQueryAdapter<SelectShablonDoxod, ItemShabDoxod>() {
        ItemShabDoxod(
            it._id.toString(),
            it.sort,
            it.name,
            it.named,
            it.summad,
            it.typed,
            it.schetName ?: "",
            it.schet_id
        )
    }.apply {
        updateQuery(
            mDB.shabDoxodQueries.selectShablonDoxod()
        )
    }


    val rasxodSpisPeriod = UniConvertQueryPeriodAdapter<SelectRasxodPeriod, ItemRasxod>(selPer) {
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
    }.apply {
        updateFunQuery { dtB, dtE ->
            mDB.rasxodQueries.selectRasxodPeriod(dtB, dtE)
        }
    }

    val rasxodSummaPeriod = UniConvertQueryPeriodAdapter<SumRasxPeriod, String>(selPer) {
        val summ: Double = it.sum ?: 0.0
        "${summ.roundToStringProb(2)} Руб."
    }.apply {
        updateFunQuery { dtB, dtE ->
            mDB.rasxodQueries.sumRasxPeriod(dtB, dtE)
        }
    }

    val doxodSpisPeriod = UniConvertQueryPeriodAdapter<SelectDoxodPeriod, ItemDoxod>(selPer) {
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
    }.apply {
        updateFunQuery { dtB, dtE ->
            mDB.doxodQueries.selectDoxodPeriod(dtB, dtE)
        }
    }

    val doxodSummaPeriod = UniConvertQueryPeriodAdapter<SumDoxPeriod, String>(selPer) {
        val summ: Double = it.sum ?: 0.0
        "${summ.roundToStringProb(2)} Руб."

    }.apply {
        updateFunQuery { dtB, dtE ->
            mDB.doxodQueries.sumDoxPeriod(dtB, dtE)
        }
    }

    val schetSpisPeriod = UniConvertQueryPeriodAdapter<SelectSchet, ItemCommonFinOper>(selPer) {
        ItemCommonFinOper(
            it._id.toString(),
            it.nameoper ?: "Error",
            "-1",
            it.podtype.toString(),
            it.summaoper ?: -1.0,
            it.summa2 ?: -1.0,
            it.data_?.unOffset() ?: 1L,
            it.schetidd.toString(),
            it.typeoper ?: "",
            it.second_schet_open == "True" || it.second_schet_open == "true"
        )
    }

    val schetPlanSpisPeriod = UniConvertQueryPeriodAdapter<SelectSchetPlan, ItemCommonFinOper>(selPer) {
        ItemCommonFinOper(
            it._id.toString(),
            it.nameoper ?: "Error",
            "-1",
            it.podtype.toString(),
            it.summaoper ?: -1.0,
            it.summaoper ?: -1.0,
            it.data_?.unOffset() ?: 1L,
            it.schetidd.toString(),
            it.typeoper,
            it.second_schet_open == 1L
        )
    }

    val spisSchetForSett = UniConvertQueryAdapter<SelectSpisSchetForSett, ItemSettSchet>() {
        ItemSettSchet(
            it._id.toString(),
            it.val_id,
            it.name,
            it.summa,
            it.cod ?: "RUB",
            it.open_ == "true",
            countoper = it.countoper.toLong()
        )
    }.apply {
        updateQuery(
            mDB.schetaQueries.selectSpisSchetForSett("false")
        )
    }

    val spisSchetPlanForSett = UniConvertQueryAdapter<SelectSpisSchetPlanForSett, ItemSettSchetPlan>() {
        ItemSettSchetPlan(
            id = it._id.toString(),
            name = it.namesp,
            summa = it.summa,
            min_aim = it.min_aim,
            max_aim = it.max_aim,
            summaRasxod = it.sumRasxod,
            open_ = it.open_,
            countoper = it.countoper.toLong()
        )
    }.apply {
        updateQuery(
            mDB.spisSchetPlQueries.selectSpisSchetPlanForSett(listOf(0, 2))
        )
    }

    val spisTyperasxodForSett = UniConvertQueryAdapter<SelectTypeRasxodForSett, ItemSettTyperasxod>() {
        ItemSettTyperasxod(
            it._id.toString(),
            it.typer,
            it.planschet,
            it.schpl_id,
            it.open_ == "true",
            countoper = it.countoper
        )
    }.apply {
        updateQuery(
            mDB.typeRasxodQueries.selectTypeRasxodForSett("false")
        )
    }

    val spisTyperasxodForPlan = UniConvertQueryAdapter<Typerasxod, ItemTyperasxod>() {
        ItemTyperasxod(
            it._id.toString(),
            it.typer,
            it.planschet,
            it.schpl_id,
            it.open_ == "true",
        )
    }.apply {
        updateQuery(
            mDB.typeRasxodQueries.selectAllOpenForPLan()
        )
    }

    val spisTypedoxodForSett = UniConvertQueryAdapter<SelectTypedoxodForSett, ItemSettTypedoxod>() {
        ItemSettTypedoxod(
            it._id.toString(),
            it.typed,
            it.open_ == "true",
            countoper = it.countoper
        )
    }.apply {
        updateQuery(
            mDB.typeDoxodQueries.selectTypedoxodForSett("false")
        )
    }

    val schetSumma = UniConvertQueryPeriodAdapter<Double, String>(selPer) {
        val summ: Double = it
        "${summ.roundToStringProb(2)} Руб."
    }
    val sumsOperForPeriod = UniQueryPeriodAdapter<SumsOperForPeriod>(selPer)

    val sumsOperPlForPeriod = UniQueryPeriodAdapter<SumsOperPlForPeriod>(selPer)

    val schetPlanSumma = UniConvertQueryPeriodAdapter<Double, String>(selPer) {
        val summ: Double = it
        "${summ.roundToStringProb(2)} Руб."
    }

    /**************** Работа с панелью добавления записей финансов *****************/

    val spisTypeRasxForAddPan = SpisIdNameAdapter<Typerasxod>(mDB.typeRasxodQueries.selectAllOpen()) {
        Pair(it._id.toString(), it.typer ?: "Нет такого типа расхода")
    }

    val spisTypeDoxForAddPan = SpisIdNameAdapter<Typedoxod>(mDB.typeDoxodQueries.selectAllOpen()) {
        Pair(it._id.toString(), it.typed ?: "Нет такого типа дохода")
    }

    private val spisSchetAdapter = UniConvertQueryAdapter<SelectAllSpisSchet, ItemSchet> {
        ItemSchet(
            it._id.toString(),
            "${it.name}, ${it.cod}",
            it.val_id,
            !((it.open_ == "false") || (it.open_ == "False")),
            it.cod ?: ""
        )
    }.apply {
        updateQuery(mDB.schetaQueries.selectAllSpisSchet())
    }
    val spisSchet = spisSchetAdapter.getMyObserveObj()

    val spisSchetPlan = UniConvertQueryAdapter<Spis_schet_pl, ItemSchetPlan> {
        ItemSchetPlan(
            id = it._id.toString(),
            name = it.namesp,
            min_aim = it.min_aim,
            max_aim = it.max_aim,
            open_ = it.open_
        )
    }.apply {
        updateQuery(mDB.spisSchetPlQueries.selectAllOpenSpisSchetPlan())
    }

    val spisBindForSchetPlanWithName = UniConvertQueryAdapter<SelectBindWithName, ItemBindForSchplWithName> {
        ItemBindForSchplWithName(
            id = it._id,
            name = it.name ?: "",
            type = TypeBindElementForSchetPlan.getType(it.type_element) ?: TypeBindElementForSchetPlan.PLAN,
            element_id = it.element_id,
            schet_plan_id = it.schet_plan_id,
            stat = it.stat ?: 0
        )
    }.apply {
        updateQuery(
            mDB.bindForSchetPlanQueries.selectBindWithName(
                typePlanId = TypeBindElementForSchetPlan.PLAN.id,
                typePlanStapId = TypeBindElementForSchetPlan.PLANSTAP.id,
                typeGoalId = TypeBindElementForSchetPlan.GOAL.id
            )
        )
    }

    val spisValut = UniConvertQueryAdapter<SelectSpisValut, ItemValut> {
        ItemValut(
            it._id.toString(),
            it.name,
            it.cod,
            it.kurs,
            it.countschet
        )
    }.apply {
        updateQuery(mDB.spisValutQueries.selectSpisValut())
    }

    val spisSchetPerevodWithIsklForAddPan = UniConvertQueryAdapter<SelectAllKrome, Pair<String, String>> {
        Pair(it._id.toString(), "${it.name}, ${it.cod}")
    }

    val spisSchetPlanPerevodWithIsklForAddPan = UniConvertQueryAdapter<Spis_schet_pl, ItemSchetPlan> {
        ItemSchetPlan(
            id = it._id.toString(),
            name = it.namesp,
            min_aim = it.min_aim,
            max_aim = it.max_aim,
            open_ = it.open_
        )
    }

    /**************** Работа с данными для отрисовки анализа ********************/

    /** Список счетов с суммами на них, для составления диаграммы */
    val spisSchetWithSumm =
        UniConvertQueryAdapter<SelectSumOnSchet, ItemSumOnSchet>(mDB.schetaQueries.selectSumOnSchet()) {
            ItemSumOnSchet(
                it._id,
                "${it.name}",
                it.summa,
                "${it.summa.roundToStringProb(2)} ${it.valut}",
                it.valut,
                it.proc ?: -1.0
            )
        }

    val spisSchetPlanWithSumm =
        UniConvertQueryAdapter<SelectSchetPlanWithSumForGrafItem, ItemSchetPlanWithSum>(mDB.spisSchetPlQueries.selectSchetPlanWithSumForGrafItem()) {
            ItemSchetPlanWithSum(
                it._id,
                "${it.name}",
                it.summa ?: 0.0,
                it.min_aim ?: -1.0,
                it.max_aim ?: -1.0,
                "${it.summa?.roundToStringProb(2) ?: "0"} RUB",
                it.sumRasxod,
                it.proc ?: -1.0
            )
        }

    val spisRasxodTypeByMonth = UniConvertQueryAdapter<AnalizRasxodTypeByMonth, ItemRectDiag>() {
        ItemRectDiag(it.year, it.month, it.sum, it.sumyear, it.proc ?: -1.0)
    }

    val spisRasxodTypeByMonthWithDatePriv = UniConvertQueryAdapter<AnalizRasxodTypeByMonth, ItemRectDiagWithDate>() {
        ItemRectDiagWithDate(it.year, it.month, it.dataFdM, it.sum, it.sumyear, it.proc ?: -1.0)
    }
    var spisRasxodTypeByMonthWithDate = MyObserveObj<List<ItemYearGraf>> { ff ->
        spisRasxodTypeByMonthWithDatePriv.updateFunc {
            setMapStatikToItemYearGraf(it) { ff(it) }
        }
    }

    private val analizDoxRasxByMonth =
        UniConvertQueryAdapter<AnalizDoxodRasxodByMonth, ItemTwoRectDiag>(mDB.doxodQueries.analizDoxodRasxodByMonth()) {
            ItemTwoRectDiag(
                it.year,
                it.month,
                it.monthyear,
                it.sumdox,
                it.sumrasx,
                it.sumyeardox,
                it.sumyearrasx,
                it.procdox ?: -1.0,
                it.procrasx ?: -1.0
            )
        }
    var spisDoxodRasxodByMonth = MyObserveObj<List<ItemTwoRectDiag>> { ff ->
        analizDoxRasxByMonth.updateFunc {
            ff(it.reversed())
        }
    }
    private val analizDoxRasxByMonthWithDate =
        UniConvertQueryAdapter<AnalizDoxodRasxodByMonth, ItemTwoRectDiagWithDate>(mDB.doxodQueries.analizDoxodRasxodByMonth()) {
            ItemTwoRectDiagWithDate(
                it.year,
                it.month,
                it.monthyear,
                it.dataFdM,
                it.sumdox,
                it.sumrasx,
                it.sumyeardox,
                it.sumyearrasx,
                it.procdox ?: -1.0,
                it.procrasx ?: -1.0
            )
        }
    var spisDoxodRasxodByMonthOnYear = MyObserveObj<List<ItemYearGrafTwoRect>> { ff ->
        analizDoxRasxByMonthWithDate.updateFunc {
            setMapStatikToItemYearGrafTwoRect(it.reversed()) { ff(it) }
        }
    }

    var maxSumOperWeek = 0.0
        private set
    var minSumOperWeek = 0.0
        private set

    val sumOperWeek = UniQueryAdapter<Sum_oper_week>(mDB.schetaQueries.sumAllweek())
    var spisSumOperWeek = MyObserveObj<List<ItemOperWeek>> { ff ->
        sumOperWeek.updateFunc {
            val rez: MutableList<ItemOperWeek> = mutableListOf()
            maxSumOperWeek = 0.0
            minSumOperWeek = 0.0
            it.fold(initial = 0.0, operation = { foldValue, item ->
                val sumCap = foldValue + item.summa
                if (sumCap > maxSumOperWeek) maxSumOperWeek = sumCap
                if (sumCap < minSumOperWeek) minSumOperWeek = sumCap
                rez.add(ItemOperWeek(item.data_, item.summa, sumCap))
                sumCap
            })
            ff(rez)
        }
    }
    private val analizRasxByType = UniQueryPeriodAdapter<AnalizRasxodByTypeForPeriod>(selPer).apply {
        updateFunQuery { dtB, dtE ->
            mDB.rasxodQueries.analizRasxodByTypeForPeriod(dtB, dtE)
        }
    }

    var spisRasxodByType = MyObserveObj<List<ItemSectorDiag>> { updF ->
        analizRasxByType.updateFunc {
            val limit = 0.02
            val rez: MutableList<ItemSectorDiag> = mutableListOf()
            val dd = 1024 / (if (it.filter { it.proc > limit }.count() != 0) it.filter { it.proc > limit }
                .count() + 1 else 1)
            var last: Double = 0.0
            var lastSum: Double = 0.0
            var lastStr = ""

            val pa = it.foldRight(initial = Pair(-90F, 0), operation = { aR, pair ->
                rez.add(
                    ItemSectorDiag(
                        aR._id.toString(),
                        "${aR.type}  ( ${aR.sum.roundToStringProb(2)} - ${(aR.proc * 100).roundToString(1)}% )",
                        aR.sum,
                        aR.proc,
                        pair.first,
                        (360F * aR.proc).toFloat(),
                        if (aR.proc > limit) myColorRadugaFloat(pair.second) else MyColorFloatARGB(0F, 0F, 0F, 0F)
                    )
                )
                if (aR.proc <= limit) {
                    last += aR.proc
                    lastSum += aR.sum
                    if (lastStr != "") lastStr = "$lastStr\n"
                    lastStr = "$lastStr* ${aR.type}  ( ${aR.sum.roundToStringProb(2)} - ${
                        (aR.proc * 100).roundToString(1)
                    }% )"
                }
                if (aR.proc > limit)
                    Pair(pair.first + (360F * aR.proc).toFloat(), pair.second + dd)
                else
                    Pair(pair.first, pair.second)
            })
            if (last != 0.0) {
                rez.add(
                    ItemSectorDiag(
                        "-1",
                        "Остальное ( ${lastSum.roundToStringProb(2)} - ${(last * 100).roundToString(1)}% ):\n$lastStr",
                        lastSum,
                        last,
                        pa.first,
                        270F - pa.first,
                        myColorRadugaFloat(pa.second)
                    )
                )
            }
            updF(rez)
        }
    }

    private val analizDoxByType = UniQueryPeriodAdapter<AnalizDoxodByTypeForPeriod>(selPer).apply {
        this.updateFunQuery { dtB, dtE ->
            mDB.doxodQueries.analizDoxodByTypeForPeriod(dtB, dtE)
        }
    }

    var spisDoxodByType = MyObserveObj<List<ItemSectorDiag>> { updF ->
        analizDoxByType.updateFunc {
            val limit = 0.02
            val rez: MutableList<ItemSectorDiag> = mutableListOf()
            val dd = 1024 / (if (it.filter { it.proc > limit }.count() != 0) it.filter { it.proc > limit }
                .count() + 1 else 1)
            var last: Double = 0.0
            var lastSum: Double = 0.0
            var lastStr = ""

            val pa = it.foldRight(initial = Pair(-90F, 0), operation = { aR, pair ->
                rez.add(
                    ItemSectorDiag(
                        aR._id.toString(),
                        "${aR.type}  ( ${aR.sum.roundToStringProb(2)} - ${(aR.proc * 100).roundToString(1)}% )",
                        aR.sum,
                        aR.proc,
                        pair.first,
                        (360F * aR.proc).toFloat(),
                        if (aR.proc > limit) myColorRadugaFloat(pair.second) else MyColorFloatARGB(0F, 0F, 0F, 0F)
                    )
                )
                if (aR.proc <= limit) {
                    last += aR.proc
                    lastSum += aR.sum
                    if (lastStr != "") lastStr = "$lastStr\n"
                    lastStr = "$lastStr* ${aR.type}  ( ${aR.sum.roundToStringProb(2)} - ${
                        (aR.proc * 100).roundToString(1)
                    }% )"
                }
                if (aR.proc > limit)
                    Pair(pair.first + (360F * aR.proc).toFloat(), pair.second + dd)
                else
                    Pair(pair.first, pair.second)
            })
            if (last != 0.0) {
                rez.add(
                    ItemSectorDiag(
                        "-1",
                        "Остальное ( ${lastSum.roundToStringProb(2)} - ${(last * 100).roundToString(1)}% ):\n$lastStr",
                        lastSum,
                        last,
                        pa.first,
                        270F - pa.first,
                        myColorRadugaFloat(pa.second)
                    )
                )
            }
            updF(rez)
        }
    }


    /****************************************************************************/
    /****************************************************************************/

    private var qTypeRasxodDateOld = mDB.typeRasxodDateQueries
    private var qRasxodDataOld = mDB.rasxodDataQueries
    private var qDoxodDataOld = mDB.doxodDateQueries

    fun addOldDate(): Long {
        var rez = qTypeRasxodDateOld.testOldDate().executeAsOne()
        if (qTypeRasxodDateOld.testOldDate().executeAsOne() < 1) {
            qDoxodDataOld.addDoxodOldData()
            qDoxodDataOld.updateDoxodData()
            rez = 666
        }
        return rez
    }

}

