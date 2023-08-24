package viewmodel

import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.ObserveModels.ObserveQuestBaseVM
import java.io.File

class QuestDB(file: File) {

    constructor(nameDB: String) : this(File(File(StateVM.dirQuest, nameDB).path,"$nameDB.db"))

    val dirQuest = file.parent
    val nameQuest = file.nameWithoutExtension

    val arg = DbArgs( file.path )
//    val arg = DbArgs(File(System.getProperty("user.dir"), nameDB).path)

    //    val arg = DbArgs(System.getProperty("user.dir") + "${File.separator}databasefff.db")
    val ObserFM = ObserveQuestBaseVM(arg)  //${getPlatformName()}


//    val finFun = ObserFM.financeFun
    val spisQuest = ObserFM.questSpis
    val addQuest = ObserFM.addQuest
    val questFun = ObserFM.questFun

//    val spisGovorun = MyStateObj(ObserFM.questObjForSpis.spisGovorun.getMyObserveObj())
//    val spisOtvetDialog = MyStateObj(ObserFM.questObjForSpis.spisOtvetDialog.getMyObserveObj())
//    val spisDialog = MyStateObj(ObserFM.questObjForSpis.spisDialog.getMyObserveObj())
//    val spisTrigger = MyStateObj(ObserFM.questObjForSpis.spisTrigger.getMyObserveObj())
//    val spisPlans = MyStateObj(ObserFM.questObjForSpis.spisPlan.getMyObserveObj())
//    val spisOpenStapPlan = MyStateObj(ObserFM.questObjForSpis.spisOpenStapPlan.getMyObserveObj())
//    val spisStapPlanForSelect = MyStateObj(ObserFM.questObjForSpis.spisStapPlanForSelect.getMyObserveObj())
//    val spisAllStapPlan = MyStateObj(ObserFM.questObjForSpis.spisAllStapPlan.getMyObserveObj())
//    val countStapPlan = MyStateObj(ObserFM.questObjForSpis.countStapPlan.getMyObsObjOneValue())
//    val spisMainParam = MyStateObj(ObserFM.questObjForSpis.spisMainParam.getMyObserveObj())

//    val spisTreeSkills = MyStateObj(ObserFM.questObjForSpis.spisTreeSkills.getMyObserveObj())
//    val spisNodeTreeSkills = MyStateObj(ObserFM.questObjForSpis.spisNodeTreeSkills.getMyObserveObj())
//    val spisNodeTreeSkillsForSelection = MyStateObj(ObserFM.questObjForSpis.spisNodeTreeSkillsForSelection.getMyObserveObj())
//    val spisNodeTreeSkillsForSelectionForTrigger = MyStateObj(ObserFM.questObjForSpis.spisNodeTreeSkillsForSelectionForTrigger.getMyObserveObj())
//    val spisNodeTreeSkillsSelection = MyStateObj(ObserFM.questObjForSpis.spisNodeTreeSkillsSelection.getMyObserveObj())
//    val spisLevelTreeSkills = MyStateObj(ObserFM.questObjForSpis.spisLevelTreeSkills.getMyObserveObj())
//    val spisLevelTreeSkillsForSelect = MyStateObj(ObserFM.questObjForSpis.spisLevelTreeSkillsForSelect.getMyObserveObj())
//    val spisNodeTreeSkillsForInfo = MyStateObj(ObserFM.questObjForSpis.spisNodeTreeSkillsForInfo.getMyObserveObj())
//    val spisIconNodeTree = MyStateObj(ObserFM.questObjForSpis.spisIconNodeTree.getMyObserveObj())

    companion object{
        fun getPathQuest(name: String): String{
            return File(File(StateVM.dirQuest, name).path,"$name.db").path
        }
    }

}