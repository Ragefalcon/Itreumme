package MainTabs.Time.Elements

import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyDialog.buttDatePickerWithButton
import MyShowMessage
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.extensions.longMinusTimeLocal
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB
import java.util.*

enum class RepeatTypeEnum(override val nameTab: String) : tabElement {
    NDays("n-й день"),
    NWeeks("каждую неделю"),
    NMonths("n-й месяц");
}

fun PanAddDenPlan(
    dialPan: MyDialogLayout,
    item: ItemDenPlan? = null,
    cancelListener: () -> Unit = {},
    listOp: List<ItemComplexOpis>? = null,
    calendar: Boolean = false,
    shablonLoad: Triple<ItemShablonDenPlan, Boolean, Boolean>? = null,
    finListener: () -> Unit = {}
) {

    var finishListener: () -> Unit = finListener
    val change = item?.let { item.id.toLong() > 0 } ?: false

    val repeatSeekBar =
        EnumDiskretSeekBar(RepeatTypeEnum::class, RepeatTypeEnum.NDays)

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название плана",
            text_name,
            "Описание плана",
            item?.id?.toLong() ?: -1,
            TableNameForComplexOpis.spisDenPlan,
            MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan,
            listOp?.toMutableStateList() ?: item?.let {
                if (it.id.toLong() > 0)
                    MainDB.complexOpisSpis.run { if (calendar) spisComplexOpisForCalendar else spisComplexOpisForDenPlan }
                        .getState().value?.get(it.id.toLong())
                        ?.toMutableStateList()
                else
                    MainDB.complexOpisSpis.run { if (calendar) spisComplexOpisForCalendar else spisComplexOpisForDenPlan }
                        .getState().value?.get(-it.id.toLong())
                        ?.clearSourceList(TableNameForComplexOpis.spisDenPlan)
                        ?.toMutableStateList()
            },
        )

    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(item?.let { Date(it.data) } ?: MainDB.denPlanDate.value)
    val timeSelectSlider2 = TimeSelectSlider(
        item?.let { Date().timeFromHHmmss(it.time1) } ?: Date(),
        item?.let { Date().timeFromHHmmss(it.time2) } ?: Date().add(2, TimeUnits.HOUR)
    )
    val timeSelectSlider = TimeSelectSlider2(
        item?.let { Date().timeFromHHmmss(it.time1) } ?: Date(),
        item?.let { Date().timeFromHHmmss(it.time2) } ?: Date().add(2, TimeUnits.HOUR)
    )
    val enabledSleep = mutableStateOf(false)

    val vajn = MySelectStat(item?.vajn ?: 1L, statNabor = MySelectStat.statNaborPlan)

    item?.vajn?.let {
        if (it == -1L) enabledSleep.value = true
    }

    val selParents = BoxSelectParentPlan(label = "Выберете проект/этап для привязки").apply {
        item?.let { itemDP ->

            selectionPlanParent.selected = MainDB.timeSpis.spisAllPlan.getState().value?.find { itemPlan ->
                itemPlan.id.toLong() == itemDP.privplan
            }
            selectionPlanStapParent.selected = MainDB.timeSpis.spisAllPlanStap.getState().value?.find { itemPlanStap ->
                itemPlanStap.id.toLong() == itemDP.stap_prpl
            }
        }
    }

    val expandedPovtor = mutableStateOf(false)
    val povtorCount = mutableStateOf(TextFieldValue("2"))
    val povtorN = mutableStateOf(TextFieldValue("1"))
    val checkPovtNedel = arrayListOf<Pair<MutableState<Boolean>, String>>(
        Pair(mutableStateOf(false), "Пн"),
        Pair(mutableStateOf(false), "Вт"),
        Pair(mutableStateOf(false), "Ср"),
        Pair(mutableStateOf(false), "Чт"),
        Pair(mutableStateOf(false), "Пт"),
        Pair(mutableStateOf(false), "Сб"),
        Pair(mutableStateOf(false), "Вс"),
    )

    fun addDenPlan(date: Long) {
        var gotov = 0.0
        if (MainDB.interfaceSpis.ADD_DEN_PLAN_WITH_100_PERCENT.getValue()) {
            if (MainDB.interfaceSpis.ADD_DEN_PLAN_WITH_100_PERCENT_FROM_TIME.getValue()) {
                if (timeSelectSlider.getTimeEnd().time > timeSelectSlider.getTimeStart().time) {
                    if (Date().time > date.withOffset()
                            .longMinusTimeLocal() + timeSelectSlider.getTimeEnd().time
                    ) gotov = 100.0
                } else {
                    if (Date().time > date.withOffset()
                            .longMinusTimeLocal() + TimeUnits.DAY.milliseconds + timeSelectSlider.getTimeEnd().time
                    ) gotov = 100.0
                }
            } else {
                gotov = 100.0
            }
        }
        complexOpis.listOpis.addUpdList { opis ->
            MainDB.addTime.addDenPlan(
                vajn = vajn.value,
                name = text_name.value.text,
                gotov = gotov,
                data = date,
                time1 = timeSelectSlider.getTimeStart().format("HH:mm"),
                time2 = timeSelectSlider.getTimeEnd().format("HH:mm"),


                opis = opis,
                privplan = selParents.selectionPlanParent.selected?.id?.toLong() ?: -1L,
                stap_prpl = selParents.selectionPlanStapParent.selected?.id?.toLong()
                    ?: -1L,
            )
        }
    }

    fun povtorFun(listener: (Date) -> Unit) {
        val count = povtorCount.value.text.toIntOrNull() ?: 0
        if (count > 0) {
            val cal = java.util.Calendar.getInstance().apply {
                time = dateInner.value
            }
            val n = povtorN.value.text.toIntOrNull() ?: 0
            for (i in 1..count) {
                when (repeatSeekBar.active) {
                    RepeatTypeEnum.NDays -> {
                        listener(cal.time)
                        cal.add(java.util.Calendar.DATE, n)
                    }

                    RepeatTypeEnum.NWeeks -> {
                        val dn: Int = cal.get(java.util.Calendar.DAY_OF_WEEK).let {
                            if (it - 1 == 0) 7 else it - 1
                        }
                        val addDate = java.util.Calendar.getInstance().apply {
                            time = cal.time
                        }
                        var k = 0
                        for (j in 1..7) {
                            val dd = if (dn + k > 7) dn + k - 7 else dn + k
                            if (checkPovtNedel[dd - 1].first.value) {
                                addDate.time = cal.time
                                addDate.add(java.util.Calendar.DATE, k)
                                listener(addDate.time)
                            }
                            k++
                        }
                        cal.add(java.util.Calendar.DATE, 7)
                    }

                    RepeatTypeEnum.NMonths -> {
                        listener(cal.time)
                        cal.add(java.util.Calendar.MONTH, n)
                    }
                }
            }
        }
    }

    @Composable
    fun buttSaveShablon() {
        MyTextButtStyle3(content = {
            Image(
                painterResource("ic_baseline_save_24.xml"),
                "saveShab",
                Modifier
                    .height(40.dp)
                    .width(40.dp),
                contentScale = ContentScale.Fit,
            )

        }) {
            MyOneVopros(
                dialLayInner,
                "Впишите назавание шаблона",
                "Добавить",
                "Название",
                text_name.value.text
            ) { nameShab ->
                var povt = ""
                val count = povtorCount.value.text.toIntOrNull() ?: 0
                if ((count > 0) && (expandedPovtor.value)) {
                    povt = "$count;${povtorN.value.text.toIntOrNull() ?: 0};"
                    var rbnum = 0
                    for (i in 0..6) {
                        povt = "$povt${if (checkPovtNedel[i].first.value) "true" else "false"};"
                    }
                    when (repeatSeekBar.active) {
                        RepeatTypeEnum.NDays -> {
                            rbnum = 0
                        }

                        RepeatTypeEnum.NWeeks -> {
                            rbnum = 1
                        }

                        RepeatTypeEnum.NMonths -> {
                            rbnum = 2
                        }
                    }
                    povt = "$povt$rbnum"
                } else {
                    povt = "0"
                }

                complexOpis.listOpis.map {
                    it.myCommonCopy(
                        id = -1L,
                        table_name = TableNameForComplexOpis.spisShabDenPlan.nameTable,
                        item_id = -1L
                    )
                }.addUpdList(true) { opis ->
                    MainDB.addTime.addShablonDenPlan(
                        name = nameShab,
                        namepl = text_name.value.text,
                        opis = opis,
                        vajn = vajn.value,
                        time1 = timeSelectSlider.getTimeStart().format("HH:mm"),
                        time2 = timeSelectSlider.getTimeEnd().format("HH:mm"),
                        privplan = selParents.selectionPlanParent.selected?.id?.toLong() ?: -1L,
                        stap_prpl = selParents.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                        povtor = povt
                    )
                }
            }
        }
    }

    fun loadShablon(itShab: ItemShablonDenPlan, loadPovt: Boolean, loadTime: Boolean) {
        text_name.value = TextFieldValue(itShab.namepl)
        complexOpis.loadNewListOpis(MainDB.complexOpisSpis.spisComplexOpisForShabDenPlan.getState().value?.get(
            itShab.id.toLong()
        ) ?: getStartListComplexOpis(TableNameForComplexOpis.spisDenPlan, item?.id?.toLong()?.let {
            if (it > 0) it else -1L
        } ?: -1L))
        if (loadTime) {
            timeSelectSlider.setTimeStart(Date().timeFromHHmmss(itShab.time1))
            timeSelectSlider.setTimeEnd(Date().timeFromHHmmss(itShab.time2))
        }
        vajn.value = itShab.vajn
        if (itShab.vajn == -1L) enabledSleep.value = true
        selParents.selectionPlanParent.selected =
            MainDB.timeSpis.spisAllPlan.getState().value?.find { itemPlan ->
                itemPlan.id.toLong() == itShab.privplan
            }
        selParents.selectionPlanStapParent.selected =
            MainDB.timeSpis.spisAllPlanStap.getState().value?.find { itemPlanStap ->
                itemPlanStap.id.toLong() == itShab.stap_prpl
            }
        if (loadPovt) {
            if (itShab.povtor != "0") {
                val povtorList: List<String> = itShab.povtor.split(";")
                if (povtorList.count() == 10) {
                    expandedPovtor.value = true
                    povtorCount.value = TextFieldValue(povtorList[0])
                    povtorN.value = TextFieldValue(povtorList[1])
                    for (j in 0..6) checkPovtNedel[j].first.value =
                        povtorList[j + 2] == "true"
                    when (povtorList[9]) {
                        "0" -> repeatSeekBar.setSelection(RepeatTypeEnum.NDays)
                        "1" -> repeatSeekBar.setSelection(RepeatTypeEnum.NWeeks)
                        "2" -> repeatSeekBar.setSelection(RepeatTypeEnum.NMonths)
                    }
                } else {
                    expandedPovtor.value = false
                }
            } else {
                expandedPovtor.value = false
            }
        }

    }

    fun loadNextAction(
        nextAction: ItemNextAction,
        listOpis: List<ItemComplexOpis>,
        loadNameFromStap: Boolean,
        loadOpis: Boolean,
        finListener: () -> Unit

    ) {
        finishListener = finListener
        text_name.value = TextFieldValue(if (loadNameFromStap) nextAction.name else nextAction.namePlan)
        complexOpis.loadNewListOpis(if (listOpis.isNotEmpty() && loadOpis) listOpis
        else getStartListComplexOpis(TableNameForComplexOpis.spisDenPlan,
            item?.id?.toLong()?.let { if (it > 0) it else -1L } ?: -1L))

        vajn.value = nextAction.vajn

        selParents.selectionPlanParent.selected =
            MainDB.timeSpis.spisAllPlan.getState().value?.find { itemPlan ->
                itemPlan.id.toLong() == nextAction.privplan
            }
        selParents.selectionPlanStapParent.selected =
            MainDB.timeSpis.spisAllPlanStap.getState().value?.find { itemPlanStap ->
                itemPlanStap.id.toLong() == nextAction.stap_prpl
            }
        expandedPovtor.value = false
    }

    @Composable
    fun buttLoadShablon() {
        MyTextButtStyle3(Modifier.padding(start = 5.dp), content = {
            Image(
                painterResource("ic_baseline_cloud_upload_24.xml"),
                "loadShab",
                Modifier
                    .height(40.dp)
                    .width(40.dp),
                contentScale = ContentScale.Fit,
            )

        }) {
            PanSelectShablonDenPlan(
                dialLayInner,
                listenerShablon = ::loadShablon,
                listenerNextAction = ::loadNextAction
            )
        }
    }


    @Composable
    fun povtorSettings() {
        if (expandedPovtor.value) {
            Row(
                Modifier.padding(bottom = 5.dp).padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.animateContentSize().padding(horizontal = 15.dp).weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeatSeekBar.show(
                        Modifier.padding(bottom = 5.dp).padding(top = 0.dp),
                        MainDB.styleParam.timeParam.seekBarStyle
                    )
                    MeasureUnconstrainedViewHeight({
                        MyTextFieldInt(
                            value = povtorN,
                            label = "n-й день",
                            modifier = Modifier,
                            style = MyTextFieldStyleState(MainDB.styleParam.commonParam.commonTextField),
                        )
                    }) { textHeight ->
                        Row(Modifier.height(textHeight), verticalAlignment = Alignment.CenterVertically) {
                            when (repeatSeekBar.active) {
                                RepeatTypeEnum.NDays -> {
                                    MyTextFieldInt(
                                        value = povtorN,
                                        label = "n-й день",
                                        modifier = Modifier,
                                        style = MyTextFieldStyleState(MainDB.styleParam.commonParam.commonTextField),
                                    )
                                }

                                RepeatTypeEnum.NWeeks -> {
                                    for (check in checkPovtNedel) {
                                        MyCheckbox(check.first, check.second)
                                    }
                                }

                                RepeatTypeEnum.NMonths -> {
                                    MyTextFieldInt(
                                        value = povtorN,
                                        label = "n-й месяц",
                                        modifier = Modifier,
                                        style = MyTextFieldStyleState(MainDB.styleParam.commonParam.commonTextField),
                                    )
                                }
                            }
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MyTextFieldInt(
                        value = povtorCount,
                        label = "раз(а)",
                        modifier = Modifier.padding(bottom = 10.dp),
                        style = MyTextFieldStyleState(MainDB.styleParam.commonParam.commonTextField),
                    )
                    MyTextButtStyle1("info", Modifier.padding(start = 0.dp)) {
                        var str = "Даты на которые будет добавлен план:\n"
                        povtorFun { str += "${it.format("dd.MM.yyyy(EEE)")}\n" }
                        MyShowMessage(dialLayInner, str)
                    }
                }
            }
        }
    }

    shablonLoad?.also {
        loadShablon(it.first, it.second, it.third)
    }

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(
                Modifier.padding(15.dp).fillMaxWidth(0.8F).fillMaxHeight(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (enabledSleep.value.not()) selParents.getComposable(
                    Modifier.fillMaxWidth(0.8F).padding(bottom = 5.dp)
                )
                if (!selParents.isExpanded()) {
                    complexOpis.show(this, dialLayInner)
                    RowVA {
                        if (enabledSleep.value.not()) vajn.show()
                        buttDatePickerWithButton(dialLayInner, dateInner)
                        MyToggleButtIconStyle1(
                            "ic_baseline_nights_stay_24.xml",
                            modifier = Modifier.padding(start = 5.dp),
                            value = enabledSleep, sizeIcon = 30.dp,
                            myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.buttBestDays)
                        ) {
                            if (it) vajn.value = -1 else vajn.value = item?.vajn ?: 1L
                        }
                    }
                    timeSelectSlider.getComposable(Modifier.fillMaxWidth(0.9F).padding(5.dp))

                    povtorSettings()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.weight(1f))
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                            cancelListener()
                        }
                        Spacer(Modifier.weight(1f))
                        Spacer(Modifier.weight(1f))
                        Row {
                            buttSaveShablon()
                            MyToggleButtIconStyle1(
                                "ic_round_repeat_24.xml",
                                modifier = Modifier.padding(horizontal = 20.dp),
                                value = expandedPovtor, sizeIcon = 30.dp,
                                myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.denPlanTab.panSelectShablon.buttSort)
                            )
                            buttLoadShablon()
                        }
                        Spacer(Modifier.weight(1f))

                        MyTextButtStyle1(
                            if (change) "Изменить" else "Добавить", Modifier.padding(start = 5.dp)
                                .alpha(if (text_name.value.text != "") 1f else 0.3f)
                        ) {
                            if (text_name.value.text != "")

                                if (change) {
                                    item?.let {
                                        complexOpis.listOpis.addUpdList { opis ->
                                            val time1 = timeSelectSlider.getTimeStart().format("HH:mm")
                                            val time2 = timeSelectSlider.getTimeEnd().format("HH:mm")
                                            MainDB.addTime.updDenPlan(
                                                item = it,
                                                vajn = vajn.value,
                                                name = text_name.value.text,
                                                data = dateInner.value.time,
                                                time1 = time1,
                                                time2 = time2,
                                                opis = opis,
                                                privplan = selParents.selectionPlanParent.selected?.id?.toLong() ?: -1L,
                                                stap_prpl = selParents.selectionPlanStapParent.selected?.id?.toLong()
                                                    ?: -1L,
                                                exp = item.getExp(item.gotov, time1, time2)
                                            )
                                        }
                                    }
                                    dialPan.close()
                                    finishListener()
                                } else {
                                    if (!expandedPovtor.value) {
                                        addDenPlan(dateInner.value.time)
                                    } else {
                                        MainDB.addTime.executeDenPlanTransaction {
                                            povtorFun { addDenPlan(it.time) }
                                        }
                                    }
                                    dialPan.close()
                                    finishListener()
                                }
                        }
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}

