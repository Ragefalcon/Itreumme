package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

class FinanceVMspis(private val objForSpis: FinanceVMobjForSpis) {

    /** Основные списки операций расходов, доходов и по выбранному счету */
    val rasxodSpisPeriod = ItrCommObserveObj(objForSpis.rasxodSpisPeriod.getMyObserveObj())
    val doxodSpisPeriod = ItrCommObserveObj(objForSpis.doxodSpisPeriod.getMyObserveObj())
    val schetSpisPeriod = ItrCommObserveObj(objForSpis.schetSpisPeriod.getMyObserveObj())
    val schetPlanSpisPeriod = ItrCommObserveObj(objForSpis.schetPlanSpisPeriod.getMyObserveObj())

    /** Суммы выводимые под основными списками */
    val rasxodSummaPeriod = ItrCommObserveObj(objForSpis.rasxodSummaPeriod.getMyObsObjOneValue())
    val doxodSummaPeriod = ItrCommObserveObj(objForSpis.doxodSummaPeriod.getMyObsObjOneValue())
    val schetSumma = ItrCommObserveObj(objForSpis.schetSumma.getMyObsObjOneValue())
    val sumsOperForPeriod = ItrCommObserveObj(objForSpis.sumsOperForPeriod.getMyObsObjOneValue())
    val sumsOperPlForPeriod = ItrCommObserveObj(objForSpis.sumsOperPlForPeriod.getMyObsObjOneValue())
    val schetPlanSumma = ItrCommObserveObj(objForSpis.schetPlanSumma.getMyObsObjOneValue())

    /** Список всех счетов */
    val spisSchet = ItrCommObserveObj(objForSpis.spisSchet)

    /** Список всех счетов планов */
    val spisSchetPlan = ItrCommObserveObj(objForSpis.spisSchetPlan.getMyObserveObj())
    val spisBindForSchetPlanWithName = ItrCommObserveObj(objForSpis.spisBindForSchetPlanWithName.getMyObserveObj())

    val spisValut = ItrCommObserveObj(objForSpis.spisValut.getMyObserveObj())

    /** Список счетов не включающий выбранного в основном разделе счета */
    val spisSchetKrome = ItrCommObserveObj(objForSpis.spisSchetPerevodWithIsklForAddPan.getMyObserveObj())
    val spisSchetPlanKrome = ItrCommObserveObj(objForSpis.spisSchetPlanPerevodWithIsklForAddPan.getMyObserveObj())

    /** Список для настроек */
    val spisSchetForSett = ItrCommObserveObj(objForSpis.spisSchetForSett.getMyObserveObj())
    val spisSchetPlanForSett = ItrCommObserveObj(objForSpis.spisSchetPlanForSett.getMyObserveObj())
    val spisTyperasxodForSett = ItrCommObserveObj(objForSpis.spisTyperasxodForSett.getMyObserveObj())
    val spisTypedoxodForSett = ItrCommObserveObj(objForSpis.spisTypedoxodForSett.getMyObserveObj())

    /** Списки типов расходов и доходов */
    val spisTyperasxodForPlan = ItrCommObserveObj(objForSpis.spisTyperasxodForPlan.getMyObserveObj())
    val spisTypeRasx = ItrCommObserveObj(objForSpis.spisTypeRasxForAddPan.getMyObserveObj())
    val spisTypeDox = ItrCommObserveObj(objForSpis.spisTypeDoxForAddPan.getMyObserveObj())


    val spisShabRasxod = ItrCommObserveObj(objForSpis.spisShabRasxod.getMyObserveObj())
    val spisShabDoxod = ItrCommObserveObj(objForSpis.spisShabDoxod.getMyObserveObj())

    /** Список для круговой диаграммы расходов по типу за выбранный период */
    val spisRasxodByType = ItrCommObserveObj(objForSpis.spisRasxodByType)

    /** Список для построения диаграммы ежемесячных трат по выбранному типу расходов */
    val spisRasxodTypeByMonth = ItrCommObserveObj(objForSpis.spisRasxodTypeByMonth.getMyObserveObj())

    /** Список для построения диаграммы ежемесячных расходов-доходов за все время */
    val spisDoxodRasxodByMonth = ItrCommObserveObj(objForSpis.spisDoxodRasxodByMonth)

    /** Список для круговой диаграммы доходов по типу за выбранный период */
    val spisDoxodByType = ItrCommObserveObj(objForSpis.spisDoxodByType)

    /** Список размера капитала в каждую неделю за все время */
    val spisSumOperWeek = ItrCommObserveObj(objForSpis.spisSumOperWeek)

    /** Список счетов с суммами на них, для составления диаграммы */
    val spisSchetWithSumm = ItrCommObserveObj(objForSpis.spisSchetWithSumm.getMyObserveObj())
    val spisSchetPlanWithSumm = ItrCommObserveObj(objForSpis.spisSchetPlanWithSumm.getMyObserveObj())

    val spisRasxodTypeByMonthWithDate = ItrCommObserveObj(objForSpis.spisRasxodTypeByMonthWithDate)
    val spisDoxodRasxodByMonthOnYear = ItrCommObserveObj(objForSpis.spisDoxodRasxodByMonthOnYear)


    /** общая сумма капитала */
    val sumAllCap = ItrCommObserveObj(objForSpis.sumAllCapAdapter.getMyObsObjOneValue())
}