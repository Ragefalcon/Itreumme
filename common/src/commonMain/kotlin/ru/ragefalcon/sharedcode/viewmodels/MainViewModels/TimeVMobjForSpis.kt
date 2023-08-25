package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.Time.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.minusTime
import ru.ragefalcon.sharedcode.extensions.unOffset
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.SpisQueryForListener
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.CommonObserveObj
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniConvertQueryAdapter
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.UniQueryAdapter

class TimeVMobjForSpis(private val mDB: Database, private val spisQueryListener: SpisQueryForListener) {

    var currentDate =
        CommonObserveObj<Long>().apply { setValue(DateTimeTz.nowLocal().minusTime().unOffset().localUnix()) }
    var textAboveSelectDenPlan = CommonObserveObj<String>().apply { setValue("") }
    var textColorAboveSelectDenPlan =
        CommonObserveObj<MyColorARGB>().apply { setValue(MyColorARGB.colorEffektShkal_Month) }

    val spisSrokPlanAndStap = UniConvertQueryAdapter<SelectSrokForPlanAndStap, ItemSrokPlanAndStap> {
        ItemSrokPlanAndStap(
            id = it.id,
            plan_id = it.plan_id,
            stap_id = it.stap_id,
            name = it.name,
            namePlan = it.name_plan ?: "",
            gotov = it.gotov,
            data1 = it.data1,
            data2 = it.data2,
            stat = it.stat,
            marker = it.vajn,
            quest = (it.quest_id ?: 0L) != 0L,
            listDate = if (it.spis_date != "") it.spis_date.split(',').map { it.toLongOrNull() ?: -1L } else listOf()
        )
    }

    val spisTimelineForPlan = UniConvertQueryAdapter<SelectTimelineForPlan, ItemSrokPlanAndStap> {
        ItemSrokPlanAndStap(
            id = it.id,
            plan_id = it.plan_id,
            stap_id = it.stap_id,
            name = it.name,
            namePlan = "",
            gotov = it.gotov,
            data1 = it.data1,
            data2 = it.data2,
            stat = it.stat,
            marker = it.vajn,
            quest = (it.quest_id ?: 0L) != 0L,
            listDate = if (it.spis_date != "") it.spis_date.split(',').map { it.toLongOrNull() ?: -1L } else listOf()
        )
    }

    var sverDenPlan = mutableMapOf<Long, Boolean>()
    val spisDenPlan = UniConvertQueryAdapter<SelectDenPlan, ItemDenPlan>() {
        ItemDenPlan(
            id = it._id.toString(),
            name = it.name,
            time1 = it.time1,
            time2 = it.time2,
            sum_hour = it.sum_hour,
            vajn = it.vajn,
            gotov = it.gotov,
            data = it.data_.unOffset(),
            opis = it.opis,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            nameprpl = it.nameprpl,
            namestap = it.namestap,
            sver = sverDenPlan.get(it._id) ?: true
        )
    }

    val spisDenPlanForCalendar = UniConvertQueryAdapter<SelectDenPlanForCalendar, ItemDenPlan>() {
        ItemDenPlan(
            id = it._id.toString(),
            name = it.name,
            time1 = it.time1,
            time2 = it.time2,
            sum_hour = it.sum_hour,
            vajn = it.vajn,
            gotov = it.gotov,
            data = it.data_.unOffset(),
            opis = it.opis,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            nameprpl = it.nameprpl,
            namestap = it.namestap
        )
    }

    val spisDenPlanForHistoryPlan = UniConvertQueryAdapter<SelectDenPlanForHistoryPlan, ItemDenPlan>() {
        ItemDenPlan(
            id = it._id.toString(),
            name = it.name,
            time1 = it.time1,
            time2 = it.time2,
            sum_hour = it.sum_hour,
            vajn = it.vajn,
            gotov = it.gotov,
            data = it.data_.unOffset(),
            opis = it.opis,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            nameprpl = it.nameprpl,
            namestap = it.namestap
        )
    }

    val spisSumHourForHistoryPlanDiag =
        UniConvertQueryAdapter<SelectSumHourForHistoryPlanDiag, ItemRectDiagWithDate>() {
            ItemRectDiagWithDate(it.year, it.month, it.dataFdM, it.sum, it.sumyear, it.proc ?: -1.0)
        }

    val spisSumHourForHistoryStapPlanDiag =
        UniConvertQueryAdapter<SelectSumHourForHistoryStapPlanDiag, ItemRectDiagWithDate>() {
            ItemRectDiagWithDate(it.year, it.month, it.dataFdM, it.sum, it.sumyear, it.proc ?: -1.0)
        }

    val spisDenPlanForHistoryStapPlan = UniConvertQueryAdapter<SelectDenPlanForHistoryStapPlan, ItemDenPlan>() {
        ItemDenPlan(
            id = it._id.toString(),
            name = it.name,
            time1 = it.time1,
            time2 = it.time2,
            sum_hour = it.sum_hour,
            vajn = it.vajn,
            gotov = it.gotov,
            data = it.data_.unOffset(),
            opis = it.opis,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            nameprpl = it.nameprpl,
            namestap = it.namestap
        )
    }

    var sverShablonDenPlan = mutableMapOf<Long, Boolean>()
    val spisShablonDenPlan = UniConvertQueryAdapter<SelectShablonDenPlan, ItemShablonDenPlan>() {
        ItemShablonDenPlan(
            id = it._id.toString(),
            sort = it.sort,
            name = it.name,
            namepl = it.namepl,
            nameprpl = it.nameprpl,
            namestap = it.namestap,
            opis = it.opis,
            vajn = it.vajn,
            time1 = it.time1,
            time2 = it.time2,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            povtor = it.povtor,
            sver = sverShablonDenPlan[it._id] ?: true
        )
    }.apply {
        this.updateQuery(mDB.shabDenPlanQueries.selectShablonDenPlan())
    }

    var sverNextAction = mutableMapOf<Long, Boolean>()
    val spisNextAction = UniConvertQueryAdapter<SelectNextAction, ItemNextAction>() {
        getItemNextAction(
            id = it._id,
            sort = it.sort,
            common = it.common_id,
            name = it.name,
            namePlan = it.namePlan,
            nameStap = it.nameStap,
            vajn = it.vajn,
            privplan = it.privplan,
            stap_prpl = it.stap_prpl,
            sver = sverNextAction[it._id] ?: true
        )
    }.apply {
        this.updateQuery(mDB.spisNextActionQueries.selectNextAction())
    }

    var sverNapom = mutableMapOf<Long, Boolean>()
    val spisNapom = UniConvertQueryAdapter<Napom, ItemNapom>() {
        ItemNapom(
            id = it._id.toString(),
            idplan = it.idplan.toString(),
            idstap = it.idstap.toString(),
            name = it.name,
            opis = it.opis,
            data = it.data_.unOffset(),
            time = it.time,
            gotov = !((it.gotov == "false") || (it.gotov == "False")),
            sver = sverNapom[it._id] ?: true
        )
    }

    val spisNapomForCalendar = UniConvertQueryAdapter<Napom, ItemNapom>() {
        ItemNapom(
            id = it._id.toString(),
            idplan = it.idplan.toString(),
            idstap = it.idstap.toString(),
            name = it.name,
            opis = it.opis,
            data = it.data_.unOffset(),
            time = it.time,
            gotov = !((it.gotov == "false") || (it.gotov == "False"))
        )
    }

    val spisEffekt = UniConvertQueryAdapter<SelectEffektWithHour, ItemEffekt>() {
        ItemEffekt(
            id = it._id.toString(),
            name = it.name,
            idplan = it.idplan,
            norma = it.norma,
            sumNedel = it.sumNedel,
            sumMonth = it.sumMonth,
            sumYear = it.sumYear
        )
    }

    val spisVajnHourPriv = UniQueryAdapter<SelectHourFromVajn>().apply {
        updateFunc { spisBinding ->
            spisVajnHour.setValue(spisBinding.groupBy { it.vajn }.map { vajnMap ->
                ItemHourVajn(
                    vajnMap.key,
                    sumNedel = vajnMap.value.find { it.period == "nedel" }?.hour ?: 0.0,
                    sumMonth = vajnMap.value.find { it.period == "month" }?.hour ?: 0.0,
                    sumYear = vajnMap.value.find { it.period == "year" }?.hour ?: 0.0
                )
            })
        }
    }
    var spisVajnHour = CommonObserveObj<List<ItemHourVajn>>()

    val spisPlanIn = UniConvertQueryAdapter<SelectOpenPlanIn, ItemPlan>() {
        ItemPlan(
            id = it._id.toString(),
            name = it.name,
            vajn = it.vajn,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            stat = TypeStatPlan.getType(it.stat),
            hour = it.hour ?: 0.0,
            open_countstap = it.open_countstap,
            countstap = it.countstap,
            direction = it.direction == 1L,
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            namequest = it.namequest
        )
    }.apply {
        this.updateQuery(
            mDB.spisPlanQueries.selectOpenPlanIn(
                closeStap = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
                stat = TypeStatPlan.getIsklSelectList().map { it.codValue },
                array_iskl = listOf()
            )
        )
    }
    var sverPlan = mutableMapOf<Long, Boolean>()
    val spisPlan = UniConvertQueryAdapter<SelectOpenPlan, ItemPlan>() {
        ItemPlan(
            id = it._id.toString(),
            name = it.name,
            vajn = it.vajn,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            stat = TypeStatPlan.getType(it.stat),
            hour = it.hour ?: 0.0,
            open_countstap = it.open_countstap,
            countstap = it.countstap,
            direction = it.direction == 1L,
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            namequest = it.namequest,
            min_aim = it.min_aim,
            max_aim = it.max_aim,
            summa = it.summa,
            summaRasxod = it.sumRasxod,
            schplOpen = it.open_ == 1L,
            sver = sverPlan.get(it._id) ?: true
        )
    }.apply {
        mDB.spisPlanQueries.selectOpenPlan(
            closeStap = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
            typePlanId = TypeBindElementForSchetPlan.PLAN.id,
            stat = TypeStatPlan.getCloseSelectList().map { it.codValue }).let {
            spisQueryListener.spisPlan = it
            this.updateQuery(it)
        }
    }
    val spisPlanForSelect = UniConvertQueryAdapter<SelectOpenPlanForSelection, ItemPlan>() {
        ItemPlan(
            id = it._id.toString(),
            name = it.name,
            vajn = it.vajn,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            stat = TypeStatPlan.getType(it.stat),
            hour = it.hour ?: 0.0,
            open_countstap = it.open_countstap,
            countstap = it.countstap,
            direction = it.direction == 1L,
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            namequest = it.namequest
        )
    }.apply {
        this.updateQuery(
            mDB.spisPlanQueries.selectOpenPlanForSelection(
                closeStap = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
                stat = TypeStatPlan.getIsklSelectList().map { it.codValue })
        )
    }
    val spisAllPlan = UniConvertQueryAdapter<SelectAllPlan, ItemPlan>() {
        ItemPlan(
            id = it._id.toString(),
            name = it.name,
            vajn = it.vajn,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            stat = TypeStatPlan.getType(it.stat),
            hour = it.hour ?: 0.0,
            open_countstap = it.open_countstap,
            countstap = it.countstap,
            direction = it.direction == 1L,
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            namequest = it.namequest
        )
    }.apply {
        this.updateQuery(
            mDB.spisPlanQueries.selectAllPlan(
                closeStap = TypeStatPlanStap.getCloseSelectList().map { it.codValue })
        )
    }
    var sverStapPlan = mutableMapOf<Long, Boolean>()
    val spisPlanStap = UniConvertQueryAdapter<OpenStapPlan, ItemPlanStap>() {
        ItemPlanStap(
            level = it.level,
            id = it._id.toString(),
            parent_id = it.parent_id.toString(),
            name = it.name,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            stat = TypeStatPlanStap.getType(it.stat),
            hour = it.hour ?: 0.0,
            marker = it.marker,
            sortCTE = it.sortCTE,
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            podstapcount = it.stapcount,
            min_aim = it.min_aim,
            max_aim = it.max_aim,
            summa = it.summa,
            summaRasxod = it.sumRasxod,
            schplOpen = it.open_ == 1L,
            sver = sverStapPlan.get(it._id) ?: true
        )
    }.apply {
        this.updateQuery(
            mDB.spisStapPlanQueries.openStapPlan(
                idpl = -1,
                codInvis = TypeStatPlanStap.INVIS.codValue,
                codBlock = TypeStatPlanStap.BLOCK.codValue,
                iskstat = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
                typePlanStapId = TypeBindElementForSchetPlan.PLANSTAP.id
            )
        )
    }

    val spisAllPlanStap = UniConvertQueryAdapter<AllStapPlan, ItemPlanStap>() {
        ItemPlanStap(
            level = -1,
            id = it._id.toString(),
            parent_id = it.parent_id.toString(),
            name = it.name,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            stat = TypeStatPlanStap.getType(it.stat),
            hour = it.hour ?: 0.0,
            marker = it.marker,
            sortCTE = "",
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            podstapcount = it.stapcount
        )
    }.apply {
        this.updateQuery(mDB.spisStapPlanQueries.allStapPlan())
    }

    val spisStapPlansForSelect = UniConvertQueryAdapter<OpenStapPlanForSelect, ItemPlanStap>() {
        ItemPlanStap(
            level = it.level,
            id = it._id.toString(),
            parent_id = it.parent_id.toString(),
            name = it.name,
            gotov = it.gotov,
            data1 = it.data1.unOffset(),
            data2 = it.data2.unOffset(),
            opis = it.opis,
            svernut = !((it.svernut == "false") || (it.svernut == "False")),
            stat = TypeStatPlanStap.getType(it.stat),
            hour = it.hour ?: 0.0,
            marker = it.marker,
            sortCTE = it.sortCTE,
            sort = it.sort,
            quest_id = it.quest_id,
            quest_key_id = it.quest_key_id,
            podstapcount = it.stapcount
        )
    }.apply {
        this.updateQuery(
            mDB.spisStapPlanQueries.openStapPlanForSelect(
                idpl = -1,
                iskl = listOf(),
                codInvis = TypeStatPlanStap.INVIS.codValue,
                codBlock = TypeStatPlanStap.BLOCK.codValue,
                iskstat = TypeStatPlanStap.getCloseSelectList().map { it.codValue },
            )
        )
    }

    val spisVxod = UniConvertQueryAdapter<Vxod, ItemVxod>() {
        ItemVxod(
            id = it._id.toString(),
            name = it.name,
            data = it.data_.unOffset(),
            opis = it.opis,
            stat = it.stat
        )
    }.apply {
        this.updateQuery(mDB.vxodQueries.selectVxod())
    }

    val countStapPlan = UniQueryAdapter<Long>()
        .apply {
            updateQuery(
                mDB.spisStapPlanQueries.countOpenStapPlan(
                    iskstat = TypeStatPlanStap.getCloseSelectList().map { it.codValue }, idpl = -1
                )
            )
        }

}

