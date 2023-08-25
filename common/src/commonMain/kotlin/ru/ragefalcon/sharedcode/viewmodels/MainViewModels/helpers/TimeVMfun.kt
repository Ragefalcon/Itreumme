package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.*
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.ComplexOpisVMobjForSpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.TimeVMobjForSpis
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonComplexObserveObj
import kotlin.math.abs
import kotlin.properties.Delegates

class TimeVMfun(
    private val mDB: Database,
    private val spisVM: TimeVMobjForSpis,
    private val spisCO: ComplexOpisVMobjForSpis,
    private val styleSpis: StyleVMspis,
) {
    private var openStatStapPlan: List<TypeStatPlanStap> = TypeStatPlanStap.getCloseSelectList()
    private var idPlanForSpisStapPlan = -1L
    private var idPlanForSpisStapPlanForSelect = -1L

    /**
     * Текущая дата (Long) в формате UTC без времени
     * */
    private var currentDatePriv = DateTimeTz.nowLocal().minusTime().unOffset()
    private val currDate = CommonComplexObserveObj<Long>().apply {
        setValueFun {
            currentDatePriv.withOffset().localUnix()
        }
    }
    val currentDate = ItrCommObserveObj<Long>(currDate.getMyObserveObj())

    var funDateUpd: (Long) -> Unit = {}

    fun updateTextDateBetweenWithColor() {
        val deltaRange = DateTimeTz.nowLocal().minusTime().unOffset().local until dateOporTime.local
        var yearsDelta = deltaRange.span.years
        var monthDelta = deltaRange.span.months
        var daysDelta = deltaRange.span.daysIncludingWeeks

        val delta = daysDelta + monthDelta * 30 + yearsDelta * 365
        if (delta < 0) {
            yearsDelta *= -1
            monthDelta *= -1
            daysDelta *= -1
        }
        val percent = if (abs(delta) > 30) 1f else abs(delta) / 30f
        with(styleSpis.styleSett.styleParam.timeParam.denPlanTab.today) {
            spisVM.textColorAboveSelectDenPlan.setValue(
                if (delta == 0) COLOR_TODAY.getCurrentColor()
                else (if (delta < 0) COLOR_YESTERDAY.getCurrentColor() else COLOR_TOMORROW.getCurrentColor()).inColor(
                    if (delta < 0) COLOR_YESTERDAY_END.getCurrentColor()
                    else COLOR_TOMORROW_END.getCurrentColor(), percent
                )
            )
        }

        spisVM.textAboveSelectDenPlan.setValue(
            "${
                when (delta) {
                    -2 -> "Позавчера"
                    -1 -> "Вчера"
                    0 -> "Сегодня"
                    1 -> "Завтра"
                    2 -> "Послезавтра"
                    else -> {
                        val str = "${if (yearsDelta > 0) "${TimeUnits.YEAR.plural(yearsDelta)} " else ""}${
                            if (monthDelta > 0) "${
                                TimeUnits.MONTH.plural(monthDelta)
                            } " else ""
                        }${if (daysDelta > 0) "${TimeUnits.DAY.plural(daysDelta)} " else ""}"
                        if (delta > 0) "Через $str"
                        else "$str назад"
                    }
                }
            }:"
        )
    }

    private var lastDateOporTime: DateTimeTz = DateTimeTz.nowLocal().minusTime().unOffset()
    var dateOporTime: DateTimeTz by Delegates.observable(
        DateTimeTz.nowLocal().minusTime().unOffset()
    ) { prop, old, new ->
        spisVM.spisDenPlan.updateQuery(
            mDB.denPlanQueries.selectDenPlan(
                new.withOffset().localUnix(), (new + 1.days).withOffset().localUnix()
            )
        )
        updateTextDateBetweenWithColor()
        spisVM.spisNapom.updateQuery(mDB.napomQueries.selectWorkNapom(new.withOffset().localUnix()))

        spisCO.spisComplexOpisForNapom.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("napom", new.withOffset().localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("napom", new.withOffset().localUnix()),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "napom", new.withOffset().localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("napom", new.withOffset().localUnix()),
        )

        spisCO.spisComplexOpisForDenPlan.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("den_plan", new.withOffset().localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "den_plan", new.withOffset().localUnix()
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "den_plan", new.withOffset().localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("den_plan", new.withOffset().localUnix()),
        )

        funDateUpd(new.localUnix())
    }
        private set

    fun setToday() {
        if (dateOporTime != DateTimeTz.nowLocal().minusTime().unOffset()) {
            lastDateOporTime = dateOporTime
            dateOporTime = DateTimeTz.nowLocal().minusTime().unOffset()
        } else dateOporTime = lastDateOporTime
    }

    fun nextDay() {
        spisVM.sverDenPlan.clear()
        dateOporTime += 1.days
    }

    fun prevDay() {
        spisVM.sverDenPlan.clear()
        dateOporTime -= 1.days
    }

    fun setDay(time: Long) {
        spisVM.sverDenPlan.clear()
        dateOporTime = DateTimeTz.Companion.fromUnixLocal(time)
    }

    var funDateForCalendarUpd: (Long) -> Unit = {}
    fun setFunDateOporForCalendarTimeUpd(ff: (Long) -> Unit) {
        funDateForCalendarUpd = ff
    }

    private val sizeCalendar = (7 * spisCO.countCalendarWeek + 1).days
    var dateOporForCalendar: DateTimeTz by Delegates.observable(
        DateTimeTz.nowLocal().minusTime().unOffset()
    ) { prop, old, new ->
        val dayOfWeek = (new.withOffset().dayOfWeek.index0.let { if (it == 0) 7 else it } - 1)
        val startDate = new.withOffset() - (7 + dayOfWeek).days
        val endDate = startDate + sizeCalendar
        spisVM.spisDenPlanForCalendar.updateQuery(
            mDB.denPlanQueries.selectDenPlanForCalendar(
                startDate.localUnix(), endDate.localUnix()
            )
        )
        spisVM.spisNapomForCalendar.updateQuery(
            mDB.napomQueries.selectNapomForCalendar(
                startDate.localUnix(), endDate.localUnix()
            )
        )
        spisCO.spisComplexOpisForCalendar.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("calendar", startDate.localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("calendar", startDate.localUnix()),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("calendar", startDate.localUnix()),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("calendar", startDate.localUnix()),
        )
        spisCO.spisComplexOpisForCalendarNapom.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("calendar_napom", startDate.localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "calendar_napom", startDate.localUnix()
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "calendar_napom", startDate.localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("calendar_napom", startDate.localUnix()),
        )
        funDateForCalendarUpd(new.localUnix())
    }
        private set

    fun nextCalendarWeek() {
        dateOporForCalendar += 7.days
    }

    fun prevCalendarWeek() {
        dateOporForCalendar -= 7.days
    }

    fun setCalendarDate(time: Long) {
        dateOporForCalendar = DateTimeTz.Companion.fromUnixLocal(time)
    }

    fun getCalendarDay() = dateOporForCalendar.localUnix()

    fun getDay() = dateOporTime.localUnix()

    fun setFunDateOporTimeUpd(ff: (Long) -> Unit) {
        funDateUpd = ff
    }

    fun setOpenSpisPlan(open: Boolean) {
        if (open) {
            spisVM.spisPlan.updateQuery(
                mDB.spisPlanQueries.selectOpenPlan(
                    closeStap = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
                    typePlanId = TypeBindElementForSchetPlan.PLAN.id,
                    stat = listOf()
                )
            )
        } else {
            spisVM.spisPlan.updateQuery(
                mDB.spisPlanQueries.selectOpenPlan(closeStap = TypeStatPlanStap.getCloseSelectList()
                    .map { it.codValue },
                    typePlanId = TypeBindElementForSchetPlan.PLAN.id,
                    stat = TypeStatPlan.getCloseSelectList().map { it.codValue })
            )
        }
    }

    fun setOpenSpisPlanIn(open: Boolean = false, array_iskl: Collection<Long> = listOf()) {
        if (open) {
            spisVM.spisPlanIn.updateQuery(
                mDB.spisPlanQueries.selectOpenPlanIn(
                    closeStap = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
                    stat = listOf(),
                    array_iskl = array_iskl
                )
            )
        } else {
            spisVM.spisPlanIn.updateQuery(
                mDB.spisPlanQueries.selectOpenPlanIn(closeStap = TypeStatPlanStap.getCloseSelectList()
                    .map { it.codValue },
                    stat = TypeStatPlan.getCloseSelectList().map { it.codValue },
                    array_iskl = array_iskl
                )
            )
        }
    }

    private fun updateSpisStapPlan() {
        spisVM.spisPlanStap.updateQuery(
            mDB.spisStapPlanQueries.openStapPlan(
                idpl = idPlanForSpisStapPlan,
                codInvis = TypeStatPlan.INVIS.codValue,
                codBlock = TypeStatPlan.BLOCK.codValue,
                iskstat = openStatStapPlan.map { it.codValue },
                typePlanStapId = TypeBindElementForSchetPlan.PLANSTAP.id
            )
        )
        spisVM.countStapPlan.updateQuery(
            mDB.spisStapPlanQueries.countOpenStapPlan(
                iskstat = openStatStapPlan.map { it.codValue }, idpl = idPlanForSpisStapPlan
            )
        )
        spisCO.spisComplexOpisForStapPlan.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("spis_stap_plan", idPlanForSpisStapPlan),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "spis_stap_plan", idPlanForSpisStapPlan
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "spis_stap_plan", idPlanForSpisStapPlan
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("spis_stap_plan", idPlanForSpisStapPlan),
        )
    }

    fun setOpenSpisStapPlan(open: Boolean) {
        if (open) {
            openStatStapPlan = listOf()
            updateSpisStapPlan()
        } else {
            openStatStapPlan = TypeStatPlanStap.getCloseSelectList()
            updateSpisStapPlan()
        }
    }

    fun hideUnblockNowSignalPlanStap(item: ItemPlanStap) {
        mDB.spisStapPlanQueries.unlockQuestPlanStap(
            TypeStatPlan.VISIB.codValue, item.quest_id, item.quest_key_id, listOf(TypeStatPlan.UNBLOCKNOW.codValue)
        )
    }

    fun hideUnblockNowSignalPlan(item: ItemPlan) {
        mDB.spisPlanQueries.unlockQuestPlan(
            TypeStatPlan.VISIB.codValue, item.quest_id, item.quest_key_id, listOf(TypeStatPlan.UNBLOCKNOW.codValue)
        )
    }

    fun setPlanForCountStapPlan(idPlan: Long) {
        spisVM.countStapPlan.updateQuery(
            mDB.spisStapPlanQueries.countOpenStapPlan(
                iskstat = TypeStatPlanStap.getCloseSelectList().map { it.codValue }, idpl = idPlan
            )
        )
    }

    fun setPlanForTimeline(idPlan: Long) {
        spisVM.spisTimelineForPlan.updateQuery(
            mDB.spisPlanForSrokQueries.selectTimelineForPlan(idPlan)
        )
    }

    fun setPlanForSpisStapPlan(idPlan: Long) {
        if (idPlanForSpisStapPlan != idPlan) {
            spisVM.sverStapPlan.clear()
            idPlanForSpisStapPlan = idPlan
            updateSpisStapPlan()
        }
    }

    fun setPlanForSpisStapPlanForSelect(idPlan: Long, array_iskl: Collection<Long> = listOf()) {
        spisVM.spisStapPlansForSelect.updateQuery(
            mDB.spisStapPlanQueries.openStapPlanForSelect(idpl = idPlan,
                iskl = array_iskl,
                codInvis = TypeStatPlan.INVIS.codValue,
                codBlock = TypeStatPlan.BLOCK.codValue,
                iskstat = TypeStatPlanStap.getCloseSelectList().map { it.codValue })
        )
    }

    fun setExpandStapPlan(id: Long, sver: Boolean) {
        mDB.spisStapPlanQueries.expandStapPlan(id = id, sver = if (sver) "true" else "false")
    }

    fun setListenerCountStapPlan(updF: (Long) -> Unit) {
        spisVM.countStapPlan.updateFunc {
            it.firstOrNull()?.let(updF)
        }
    }

    private var firstCheckDate = false
    fun checkChangeCurrentDate() {
        if (currentDatePriv < DateTimeTz.nowLocal().minusTime().unOffset() || firstCheckDate == false) {
            spisVM.currentDate.setValue(DateTimeTz.nowLocal().minusTime().unOffset().localUnix())
            currentDatePriv = DateTimeTz.nowLocal().minusTime().unOffset()
            currDate.update()
            updateTextDateBetweenWithColor()

            spisVM.spisEffekt.updateQuery(
                mDB.effektQueries.selectEffektWithHour(
                    currentDatePriv.withOffset().localUnix()
                )
            )
            spisVM.spisSrokPlanAndStap.updateQuery(
                mDB.spisPlanForSrokQueries.selectSrokForPlanAndStap(currentDatePriv.withOffset()
                    .localUnix() + TimeUnits.DAY.milliseconds * 21,
                    closePlanStat = TypeStatPlan.getCloseSelectList().map { it.codValue },
                    closeStapStat = TypeStatPlanStap.getCloseSelectList().map { it.codValue })
            )
            spisVM.spisVajnHourPriv.updateQuery(
                mDB.denPlanQueries.selectHourFromVajn(
                    currentDatePriv.withOffset().localUnix()
                )
            )
            if (!firstCheckDate) firstCheckDate = true
        }
    }

    fun setPlanForHistory(idPlan: Long) {
        spisVM.spisDenPlanForHistoryPlan.updateQuery(mDB.denPlanQueries.selectDenPlanForHistoryPlan(idPlan))
        spisCO.spisComplexOpisForHistoryPlan.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("history_plan", idPlan),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("history_plan", idPlan),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("history_plan", idPlan),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("history_plan", idPlan)
        )
        spisVM.spisSumHourForHistoryPlanDiag.updateQuery(mDB.denPlanQueries.selectSumHourForHistoryPlanDiag(idPlan))
    }

    fun setStapPlanForHistory(idStap: Long) {
        spisVM.spisDenPlanForHistoryStapPlan.updateQuery(mDB.denPlanQueries.selectDenPlanForHistoryStapPlan(idStap))
        spisCO.spisComplexOpisForHistoryStapPlan.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("history_stap_plan", idStap),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("history_stap_plan", idStap),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("history_stap_plan", idStap),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("history_stap_plan", idStap)
        )
        spisVM.spisSumHourForHistoryStapPlanDiag.updateQuery(
            mDB.denPlanQueries.selectSumHourForHistoryStapPlanDiag(
                idStap
            )
        )
    }

    init {
        spisVM.spisDenPlan.updateQuery(
            mDB.denPlanQueries.selectDenPlan(
                dateOporTime.withOffset().localUnix(), (dateOporTime + 1.days).withOffset().localUnix()
            )
        )
        spisVM.spisNapom.updateQuery(mDB.napomQueries.selectWorkNapom(dateOporTime.withOffset().localUnix()))

        val dayOfWeek = dateOporForCalendar.withOffset().dayOfWeek.index0.let { if (it == 0) 7 else it }
        val startDate = dateOporForCalendar.withOffset() - (7 + dayOfWeek).days
        spisVM.spisDenPlanForCalendar.updateQuery(
            mDB.denPlanQueries.selectDenPlanForCalendar(
                startDate.localUnix(), (startDate + sizeCalendar).localUnix()
            )
        )
        spisVM.spisNapomForCalendar.updateQuery(
            mDB.napomQueries.selectNapomForCalendar(
                startDate.localUnix(), (startDate + sizeCalendar).localUnix()
            )
        )
        spisCO.spisComplexOpisForCalendar.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("calendar", startDate.localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId("calendar", startDate.localUnix()),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId("calendar", startDate.localUnix()),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("calendar", startDate.localUnix()),
        )
        spisCO.spisComplexOpisForCalendarNapom.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId("calendar_napom", startDate.localUnix()),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "calendar_napom", startDate.localUnix()
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "calendar_napom", startDate.localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId("calendar_napom", startDate.localUnix()),
        )

        spisCO.spisComplexOpisForNapom.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId(
                "napom", dateOporTime.withOffset().localUnix()
            ),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "napom", dateOporTime.withOffset().localUnix()
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "napom", dateOporTime.withOffset().localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId(
                "napom", dateOporTime.withOffset().localUnix()
            ),
        )

        spisCO.spisComplexOpisForDenPlan.updateQuery(
            mDB.complexOpisTextQueries.selectComplexOpisTextCommonWithId(
                "den_plan", dateOporTime.withOffset().localUnix()
            ),
            mDB.complexOpisCheckboxQueries.selectComplexOpisCheckboxCommonWithId(
                "den_plan", dateOporTime.withOffset().localUnix()
            ),
            mDB.complexOpisImageGroupQueries.selectComplexOpisImageGroupCommonWithId(
                "den_plan", dateOporTime.withOffset().localUnix()
            ),
            mDB.complexOpisLinkQueries.selectComplexOpisLinkCommonWithId(
                "den_plan", dateOporTime.withOffset().localUnix()
            ),
        )

        spisVM.spisEffekt.updateQuery(mDB.effektQueries.selectEffektWithHour(currentDatePriv.withOffset().localUnix()))
        spisVM.spisSrokPlanAndStap.updateQuery(
            mDB.spisPlanForSrokQueries.selectSrokForPlanAndStap(

                currentDatePriv.withOffset().localUnix() + TimeUnits.DAY.milliseconds * 21,
                closePlanStat = TypeStatPlan.getCloseSelectList().map { it.codValue },
                closeStapStat = TypeStatPlanStap.getCloseSelectList().map { it.codValue })
        )
        spisVM.spisVajnHourPriv.updateQuery(
            mDB.denPlanQueries.selectHourFromVajn(
                currentDatePriv.withOffset().localUnix()
            )
        )
    }
}