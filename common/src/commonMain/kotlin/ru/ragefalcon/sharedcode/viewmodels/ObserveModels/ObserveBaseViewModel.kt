package ru.ragefalcon.sharedcode.viewmodels.ObserveModels

import com.squareup.sqldelight.db.SqlDriver
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.source.disk.DatabaseCreator
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.InterfaceStateTable
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.InterfaceStyleTable
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.InterfaceVMspis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.*

class ObserveBaseViewModel(
    dbArgs: DbArgs,
//    dbArgs2: DbArgs? = null,
) {

//    private var mDatabase2: Database? = dbArgs2?.let {
//        println("Wait Callback")
//        DatabaseCreator.getDataBase(it)
//    }
    private var pairDBandDriver = DatabaseCreator.getDataBase(dbArgs)
    private var mDatabase: Database? = pairDBandDriver.first
    private var sqlDriver: SqlDriver? = pairDBandDriver.second

    private val spisQueryListener = SpisQueryForListener()
    val loadQuestFromFileAttach = LoadQuestFromFileAttach(sqlDriver!!,mDatabase!!,spisQueryListener)
    val loadStyleFromFileAttach = LoadStyleFromFileAttach(sqlDriver!!,mDatabase!!)


    private val loadQuestObjForSpis = LoadQuestVMobjForSpis(mDatabase!!,spisQueryListener)
    private val privateCommonFun = PrivateCommonFun(mDatabase!!,loadQuestObjForSpis)
    val loadQuestSpis = LoadQuestVMspis(loadQuestObjForSpis)
    val addQuest = AddLoadQuestHandler(mDatabase!!,loadQuestObjForSpis,privateCommonFun,dbArgs)

    fun loadQuestFromFileAttach(
        path: String,
        questId: Long?,
        dirQuest: String,
        dirLoadedFiles: String,
        dirIconFiles: String
    ) {
        loadQuestFromFileAttach.loadQuest(
            path,
            questId,
            dirQuest,
            dirLoadedFiles,
            dirIconFiles
        )?.let { questId_add ->
            privateCommonFun.runTriggerReact(questId_add.toString(), TypeParentOfTrig.STARTQUESTDIALOG,-25)
        }
    }

    fun loadStartStyleFromFileAttach(
        path: String,
    ) {
        loadStyleFromFileAttach.loadStartStyle(
            path,
        )
    }

    private val interfaceStateTable = InterfaceStateTable(mDatabase!!)
    val interfaceSpis = InterfaceVMspis(interfaceStateTable)

    private val interfaceStyleTable = InterfaceStyleTable(mDatabase!!)
    val styleSpis = StyleVMspis(interfaceStyleTable)

    private val editStyleObjForSpis = EditStyleVMobjForSpis(mDatabase!!)
    val editStyleSpis = EditStyleVMspis(editStyleObjForSpis)
    val editStyleFun = EditStyleVMfun(mDatabase!!,editStyleObjForSpis)
    val addEditStyle = AddEditStyleHandler(mDatabase!!,styleSpis)

    private val complexOpisObjForSpis = ComplexOpisVMobjForSpis(mDatabase!!)
    val complexOpisSpis = ComplexOpisVMspis(complexOpisObjForSpis)
    val addComplexOpis = AddComplexOpisHandler(mDatabase!!)

    private val financeObjForSpis = FinanceVMobjForSpis(mDatabase!!)
    val financeSpis = FinanceVMspis(financeObjForSpis)
    val financeFun = FinanceVMfun(mDatabase!!,financeObjForSpis)
    val addFin = AddFinanceHandler(mDatabase!!)

    private val timeObjForSpis = TimeVMobjForSpis(mDatabase!!,spisQueryListener)
    val timeSpis = TimeVMspis(timeObjForSpis)
    val timeFun = TimeVMfun(mDatabase!!,timeObjForSpis,complexOpisObjForSpis,styleSpis)
    val addTime = AddTimeHandler(mDatabase!!,privateCommonFun,addComplexOpis)

    private val journalObjForSpis = JournalVMobjForSpis(mDatabase!!)
    val journalSpis = JournalVMspis(journalObjForSpis)
    val journalFun = JournalVMfun(mDatabase!!,journalObjForSpis,complexOpisObjForSpis)
    val addJournal = AddJournalHandler(mDatabase!!,addComplexOpis)


    private val avatarObjForSpis = AvatarVMobjForSpis(mDatabase!!,spisQueryListener)
    val addAvatar = AddAvatarHandler(mDatabase!!,privateCommonFun)
    val avatarSpis = AvatarVMspis(avatarObjForSpis)
    val avatarFun = AvatarVMfun(mDatabase!!,avatarObjForSpis,complexOpisObjForSpis)



    val sincFun = SincVMfun(mDatabase!!)

    val selPer = financeObjForSpis.selPer


    init {
        println("База start")
        mDatabase?.let { mDb ->
            val countStartDate = mDb.startDataQueries.chechData().executeAsOne()
            println("countStartDate = ${countStartDate}")
            if ( countStartDate<2){
                mDb.startDataQueries.startData()
            }
        }   ?: run {
            println("База еще пустая")
        }
        financeObjForSpis.sumAllCap.getObserve {
            it?.let {
                avatarObjForSpis.avatarStat.setCapital(it)
            }
        }
    }

}