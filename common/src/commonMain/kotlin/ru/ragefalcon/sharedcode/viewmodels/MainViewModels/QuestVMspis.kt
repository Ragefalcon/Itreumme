package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

class QuestVMspis(private val objForSpis: QuestVMobjForSpis) {
    val spisGovorun = ItrCommObserveObj(objForSpis.spisGovorun.getMyObserveObj())
    val spisDialog = ItrCommObserveObj(objForSpis.spisDialog.getMyObserveObj())
    val spisOtvetDialog = ItrCommObserveObj(objForSpis.spisOtvetDialog.getMyObserveObj())

    val spisTrigger = ItrCommObserveObj(objForSpis.spisTrigger.getMyObserveObj())

    val spisPlan = ItrCommObserveObj(objForSpis.spisPlan.getMyObserveObj())
//    val spisStapPlan = ItrCommObserveObj(objForSpis.spisStapPlan.getMyObserveObj())
    val spisOpenStapPlan = ItrCommObserveObj(objForSpis.spisOpenStapPlan.getMyObserveObj())
    val spisStapPlanForSelect = ItrCommObserveObj(objForSpis.spisStapPlanForSelect.getMyObserveObj())
    val spisAllStapPlan = ItrCommObserveObj(objForSpis.spisAllStapPlan.getMyObserveObj())
    val countStapPlan = ItrCommObserveObj(objForSpis.countStapPlan.getMyObsObjOneValue())

    val spisMainParam = ItrCommObserveObj(objForSpis.spisMainParam.getMyObserveObj())

    val spisTreeSkills = ItrCommObserveObj(objForSpis.spisTreeSkills.getMyObserveObj())
    val spisLevelTreeSkills = ItrCommObserveObj(objForSpis.spisLevelTreeSkills.getMyObserveObj())
    val spisLevelTreeSkillsForSelect = ItrCommObserveObj(objForSpis.spisLevelTreeSkillsForSelect.getMyObserveObj())
//    val spisAllNodeTreeSkillsForSelectForTrigger = ItrCommObserveObj(objForSpis.spisAllNodeTreeSkillsForSelectForTrigger.getMyObserveObj())
//    val spisHandNodeTreeSkills = ItrCommObserveObj(objForSpis.spisHandNodeTreeSkills.getMyObserveObj())
//    val spisPlanNodeTreeSkills = ItrCommObserveObj(objForSpis.spisPlanNodeTreeSkills.getMyObserveObj())
//    val spisWholeBranchParentNodeTreeSkills = ItrCommObserveObj(objForSpis.spisWholeBranchParentNodeTreeSkills.getMyObserveObj())
//    val spisWholeBranchChildNodeTreeSkills = ItrCommObserveObj(objForSpis.spisWholeBranchChildNodeTreeSkills.getMyObserveObj())
    val spisNodeTreeSkills = ItrCommObserveObj(objForSpis.spisNodeTreeSkills.getMyObserveObj())
    val spisNodeTreeSkillsForSelectionForTrigger = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsForSelectionForTrigger.getMyObserveObj())
    val spisNodeTreeSkillsForSelection = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsForSelection.getMyObserveObj())
    val spisNodeTreeSkillsSelection = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsSelection.getMyObserveObj())
    val spisNodeTreeSkillsForInfo = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsForInfo.getMyObserveObj())
//    val spisParentBranchNodeTreeSkills = ItrCommObserveObj(objForSpis.spisParentBranchNodeTreeSkills.getMyObserveObj())
//    val spisChildBranchNodeTreeSkills = ItrCommObserveObj(objForSpis.spisChildBranchNodeTreeSkills.getMyObserveObj())
    val spisIconNodeTree = ItrCommObserveObj(objForSpis.spisIconNodeTree.getMyObserveObj())

}