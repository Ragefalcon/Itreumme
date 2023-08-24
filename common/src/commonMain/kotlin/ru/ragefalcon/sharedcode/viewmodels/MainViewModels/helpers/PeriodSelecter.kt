package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.days
import com.soywiz.klock.months
import com.soywiz.klock.years
import ru.ragefalcon.sharedcode.extensions.*


class PeriodSelecter() {
    enum class FinPeriod { Day, Week, Month, Year, SelectDates }

    var dateOpor: DateTimeTz = DateTimeTz.nowLocal().unOffset ().minusTime() //fromUnixLocal(1309737600000)
        private set
    var dateOporLong: Long = 0
        get() = dateOpor.localUnix().unOffset()
        private set

    var dateEnd: DateTimeTz = dateOpor.firstDayOfNextMonth()
        private set
    var dateBegin: DateTimeTz = dateOpor.firstDayOfMonth()
        private set
    var VybPeriod = FinPeriod.Month
        private set

    private var ffUpDateOpor: (Long) -> Unit = {}
    fun setUpdDO(ff: (Long) -> Unit) {
        ffUpDateOpor = ff
        ff(dateOpor.localUnix())
    }
    fun setDateOpor(xx: Long){
        dateOpor = DateTimeTz.fromUnixLocal(xx.withOffset())
        updatePeriod()
    }
    private var ffUpDatePer: (Long,Long) -> Unit = {aa,bb ->}
    fun setUpdPer(ff: (Long,Long) -> Unit) {
        ffUpDatePer = ff
        ffUpDatePer(dateBegin.localUnix().unOffset(),(dateEnd-0.days).localUnix().unOffset())
    }
    fun SetPeriodDates(dtBegin: Long, dtEnd: Long) {
        dateBegin = DateTimeTz.fromUnixLocal(dtBegin.withOffset())
        dateEnd = DateTimeTz.fromUnixLocal(dtEnd.withOffset())
        updatePeriod()
    }


    private var spisUpd: MutableList<() -> Unit> = mutableListOf()

    private fun updateAll() {
        for (ff in spisUpd) {
            ff()
        }
    }

    fun addUpdate(updF: () -> Unit) {
        spisUpd.add(updF)
    }

    private fun updatePeriod() {
        when (VybPeriod) {
            FinPeriod.Day -> SetPeriodDay()
            FinPeriod.Week -> SetPeriodWeek()
            FinPeriod.Month -> SetPeriodMonth()
            FinPeriod.Year -> SetPeriodYear()
            FinPeriod.SelectDates -> SetPeriodSelectDates()
        }
    }

    fun nextDate(vybPeriod: PeriodSelecter.FinPeriod = VybPeriod) {
        when (vybPeriod) {
            FinPeriod.Day -> dateOpor += 1.days
            FinPeriod.Week -> dateOpor += 7.days
            FinPeriod.Month -> dateOpor += 1.months // dateOpor.firstDayOfNextMonth()
            FinPeriod.Year -> dateOpor += 1.years
            FinPeriod.SelectDates -> {
            }
        }
        ffUpDateOpor(dateOpor.localUnix())
        updatePeriod()
    }

    fun prevDate(vybPeriod: PeriodSelecter.FinPeriod = VybPeriod) {
        when (vybPeriod) {
            FinPeriod.Day -> dateOpor -= 1.days
            FinPeriod.Week -> dateOpor -= 7.days
            FinPeriod.Month -> dateOpor -= 1.months // dateOpor.firstDayOfNextMonth()
            FinPeriod.Year -> dateOpor -= 1.years
            FinPeriod.SelectDates -> {
            }
        }
        ffUpDateOpor(dateOpor.localUnix())
        updatePeriod()
    }

    fun SetPeriodDay() {
        VybPeriod = FinPeriod.Day
        dateBegin = dateOpor
        dateEnd = dateBegin + 1.days
        ffUpDatePer(dateBegin.localUnix().unOffset(),(dateEnd-0.days).localUnix().unOffset())
        updateAll()
    }

    fun SetPeriodWeek() {
        VybPeriod = FinPeriod.Week
        dateBegin = dateOpor.firstDayOfWeek()
        dateEnd = dateBegin + 7.days
        ffUpDatePer(dateBegin.localUnix().unOffset(),(dateEnd-0.days).localUnix().unOffset())
        updateAll()
    }

    fun SetPeriodMonth() {
        VybPeriod = FinPeriod.Month
        dateBegin = dateOpor.firstDayOfMonth()
        dateEnd = dateOpor.firstDayOfNextMonth()
        ffUpDatePer(dateBegin.localUnix().unOffset(),(dateEnd-0.days).localUnix().unOffset())
        updateAll()
    }

    fun SetPeriodYear() {
        VybPeriod = FinPeriod.Year
        dateBegin = dateOpor.minusTime() - dateOpor.dayOfYear.days + 1.days
        dateEnd = dateBegin + 1.years
        ffUpDatePer(dateBegin.localUnix().unOffset(),(dateEnd-0.days).localUnix().unOffset())
        updateAll()
    }

    fun SetPeriodSelectDates() {
        VybPeriod = FinPeriod.SelectDates
        updateAll()
    }

}
