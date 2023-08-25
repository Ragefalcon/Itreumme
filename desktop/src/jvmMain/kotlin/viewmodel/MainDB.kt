package viewmodel

import MyDialog.InnerFinishBirthdayAction
import adapters.MyComboBox
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.useResource
import extensions.add
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.models.data.ItemYearGraf
import ru.ragefalcon.sharedcode.source.disk.CommonName
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.QUEST_ID_INNER_DIALOG
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PeriodSelecter
import ru.ragefalcon.sharedcode.viewmodels.ObserveModels.ObserveBaseViewModel
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


object MainDB {

    val arg = DbArgs(File(StateVM.dirMain, CommonName.nameMainDBfile).path)

    private val ObserFM = ObserveBaseViewModel(arg)

    fun loadQuestFromFileAttach(
        quest: File,
        questId: Long?,
    ) = ObserFM.loadQuestFromFileAttach(
        quest.path, questId, quest.parent, StateVM.dirLoadedQuestFiles, StateVM.dirIconNodeTree
    )

    val interfaceSpis = ObserFM.interfaceSpis.intSett
    val styleParam = ObserFM.styleSpis.styleSett.styleParam

    val selPer = ObserFM.selPer

    val sincFun = ObserFM.sincFun

    val addQuest = ObserFM.addQuest

    val finSpis = ObserFM.financeSpis
    val finFun = ObserFM.financeFun
    val addFinFun = ObserFM.addFin

    val dateFin = mutableStateOf(Date(ObserFM.selPer.dateOporLong))
    val dateFinBegin = mutableStateOf(Date(ObserFM.selPer.dateOporLong))
    val dateFinEnd = mutableStateOf(Date(ObserFM.selPer.dateOporLong))
    val selectTwoDate = mutableStateOf(selPer.VybPeriod == PeriodSelecter.FinPeriod.SelectDates)

    val enableFilter = mutableStateOf(ObserFM.financeFun.filter_enable)

    val CB_spisSchet by lazy {
        MyComboBox(finSpis.spisSchet, nameItem = { it.name }) {
            finFun.setPosMainSchet(it.id to it.name)
        }
    }
    val CB_spisSchetPlan by lazy {
        MyComboBox(finSpis.spisSchetPlan, nameItem = { it.name }) {
            finFun.setPosMainSchetPlan(it.id to it.name)
        }
    }

    val CB_spisTypeRasx by lazy {
        MyComboBox(finSpis.spisTypeRasx, nameItem = { it.second }) {
            finFun.setPosFilterRasx(it)
        }
    }
    val CB_spisTypeDox by lazy {
        MyComboBox(finSpis.spisTypeDox, nameItem = { it.second }) {
            finFun.setPosFilterDox(it)
        }
    }


    /*******************************************************/

    val loadQuestSpis = ObserFM.loadQuestSpis
    val avatarSpis = ObserFM.avatarSpis
    val avatarFun = ObserFM.avatarFun
    val addAvatar = ObserFM.addAvatar

    val goalYearStatistik = mutableStateOf(listOf<ItemYearGraf>())
    val dreamYearStatistik = mutableStateOf(listOf<ItemYearGraf>())
    val goalHourWeek = mutableStateOf("0")
    val goalHourMonth = mutableStateOf("0")
    val goalHourYear = mutableStateOf("0")
    val goalHourAll = mutableStateOf("0")
    val goalCountPlan = mutableStateOf("0")
    val dreamHourWeek = mutableStateOf("0")
    val dreamHourMonth = mutableStateOf("0")
    val dreamHourYear = mutableStateOf("0")
    val dreamHourAll = mutableStateOf("0")
    val dreamCountPlan = mutableStateOf("0")


    /*******************************************************/

    val editStyleSpis = ObserFM.editStyleSpis
    val editStyleFun = ObserFM.editStyleFun
    val addEditStyle = ObserFM.addEditStyle


    /*******************************************************/

    val complexOpisSpis = ObserFM.complexOpisSpis
    val addComplexOpis = ObserFM.addComplexOpis

    /*******************************************************/

    val journalSpis = ObserFM.journalSpis
    val journalFun = ObserFM.journalFun
    val addJournal = ObserFM.addJournal


    /*******************************************************/

    val timeSpis = ObserFM.timeSpis
    val timeFun = ObserFM.timeFun
    val addTime = ObserFM.addTime

    val denPlanDate = mutableStateOf(Date(ObserFM.timeFun.getDay()))
    val denPlanDateForCalendar = mutableStateOf(Date(ObserFM.timeFun.getDay()))

    /*******************************************************/

    fun updateInnerDialog(overwrite: Boolean) {
        val innerDialogBD = File(StateVM.dirInnerDialogs, "InnerDialogs.db")
        if (innerDialogBD.exists().not()) {
            if (!innerDialogBD.parentFile.exists()) Files.createDirectory(Paths.get(innerDialogBD.parentFile.path))
            innerDialogBD.createNewFile()
            useResource("InnerDialogs.db") {
                innerDialogBD.writeBytes(it.readBytes())
            }
            val innerDialog_1 = File(StateVM.dirInnerDialogs, "InnerDialogs_1.jpg")
            val innerDialog_2 = File(StateVM.dirInnerDialogs, "InnerDialogs_2.jpg")
            innerDialog_1.createNewFile()
            useResource("InnerDialogs_1.jpg") {
                innerDialog_1.writeBytes(it.readBytes())
            }
            innerDialog_2.createNewFile()
            useResource("InnerDialogs_2.jpg") {
                innerDialog_2.writeBytes(it.readBytes())
            }
        }
        val check = addQuest.loadCheckInnerDialog()

        if (overwrite || !check) {
            if (check) addQuest.deleteFullQuest(
                QUEST_ID_INNER_DIALOG.toString(), StateVM.dirLoadedQuestFiles, StateVM.dirIconNodeTree
            )
            loadQuestFromFileAttach(innerDialogBD, QUEST_ID_INNER_DIALOG)
            ObserFM.addQuest.completeInnerFinishTriggerAction()
        }
    }

    fun updateStartStyle(overwrite: Boolean) {
        val startStyleBD = File(StateVM.dirStyle, "StartStyle.db")
        if (startStyleBD.exists().not()) {
            if (!startStyleBD.parentFile.exists()) Files.createDirectory(Paths.get(startStyleBD.parentFile.path))
            useResource("StartStyle.db") {
                startStyleBD.createNewFile()
                startStyleBD.writeBytes(it.readBytes())
            }
        }
        val check = MainDB.editStyleSpis.getSpisSaveSetStyle(TypeSaveStyleSet.FULL).let { spis ->
            spis?.getState()?.value?.isNotEmpty() == true
        }

        if (!check) {
            if (startStyleBD.exists()) {
                ObserFM.loadStyleFromFileAttach.loadStartStyle(startStyleBD.path)
                MainDB.addEditStyle.loadSaveSetStyleCommon(
                    set_id = -6,
                    ""
                )
            }
        }
    }

    private var setAvatarListener = false
    fun setAvatarDreamGoalListener() {
        if (!setAvatarListener) {
            avatarFun.setListenerStatikHourGoal {
                goalYearStatistik.value = it
            }
            avatarFun.setListenerStatikHourDream {
                dreamYearStatistik.value = it
            }
            avatarFun.setListenerForStatistikHourGoal { week, month, year, all, countPlan ->
                goalHourWeek.value = week
                goalHourMonth.value = month
                goalHourYear.value = year
                goalHourAll.value = all
                goalCountPlan.value = countPlan
            }
            avatarFun.setListenerHourForStatistikDream { week, month, year, all, countPlan ->
                dreamHourWeek.value = week
                dreamHourMonth.value = month
                dreamHourYear.value = year
                dreamHourAll.value = all
                dreamCountPlan.value = countPlan
            }
            setAvatarListener = true
        }
    }

    init {
        updateInnerDialog(false)
        ObserFM.selPer.addUpdate {
            dateFin.value = Date(ObserFM.selPer.dateOporLong)
        }
        selPer.addUpdate {
            selectTwoDate.value = selPer.VybPeriod == PeriodSelecter.FinPeriod.SelectDates
        }
        selPer.setUpdPer { dB, dE ->
            val k = -1
            if (dateFinBegin.value != Date(dB)) dateFinBegin.value = Date(dB)
            if (dateFinEnd.value != Date(dE).add(k, TimeUnits.DAY)) dateFinEnd.value =
                Date(dE).add(k, TimeUnits.DAY)
        }
        timeFun.setFunDateOporTimeUpd {
            if (denPlanDate.value != Date(it)) denPlanDate.value = Date(it)
        }
        timeFun.setFunDateOporForCalendarTimeUpd {
            if (denPlanDateForCalendar.value != Date(it)) denPlanDateForCalendar.value = Date(it)
        }
        ObserFM.interfaceSpis.intSett.startInit()
        ObserFM.styleSpis.styleSett.startInit()
        updateStartStyle(false)
        if (addAvatar.checkEmptyBirthday()) {
            StateVM.innerFinishAction.value = InnerFinishBirthdayAction()
            addTime.startInnerTrigger(InnerStartTriggerEnum.StartTrigger)
        }
    }

}