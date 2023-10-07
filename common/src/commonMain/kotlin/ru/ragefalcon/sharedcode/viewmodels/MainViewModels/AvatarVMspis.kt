package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

class AvatarVMspis (private val objForSpis: AvatarVMobjForSpis) {
    val spisAvatarStat = ItrCommObserveObj(objForSpis.spisAvatarStat)
    val dreamStat = ItrCommObserveObj(objForSpis.dreamStat)
    val goalStat = ItrCommObserveObj(objForSpis.goalStat)
    val diagramStatikHourDream = ItrCommObserveObj(objForSpis.diagramStatikHourDream)
    val diagramStatikHourGoal = ItrCommObserveObj(objForSpis.diagramStatikHourGoal)

    val spisTreeSkills = ItrCommObserveObj(objForSpis.spisTreeSkills.getMyObserveObj())
    val spisBestDays = ItrCommObserveObj(objForSpis.spisBestDays.getMyObserveObj())

    val spisPlanStapOfGoal = ItrCommObserveObj(objForSpis.spisPrivsGoal.getMyObserveObj())
    val spisPlanStapOfDream = ItrCommObserveObj(objForSpis.spisPrivsDream.getMyObserveObj())
    val spisPlanStapOfCharacteristic = ItrCommObserveObj(objForSpis.spisPrivsCharacteristic.getMyObserveObj())
    val spisProgressCharacteristicForMessage = ItrCommObserveObj(objForSpis.spisProgressCharacteristicForMessage.getMyObserveObj())
    val spisSumWeekHourOfCharacteristic = ItrCommObserveObj(objForSpis.spisSumWeekHourOfCharacteristic)
    val spisCharacteristics = ItrCommObserveObj(objForSpis.spisCharacteristics.getMyObserveObj())
    val spisGoals = ItrCommObserveObj(objForSpis.spisGoals.getMyObserveObj())
    val spisDreams = ItrCommObserveObj(objForSpis.spisDreams.getMyObserveObj())
    val spisDenPlanInBestDays = ItrCommObserveObj(objForSpis.spisDenPlanInBestDays.getMyObserveObj())
    val spisMainParam = ItrCommObserveObj(objForSpis.spisMainParam.getMyObserveObj())
    val spisLevelTreeSkills = ItrCommObserveObj(objForSpis.spisLevelTreeSkills.getMyObserveObj())
    val spisNodeTreeSkills = ItrCommObserveObj(objForSpis.spisNodeTreeSkills.getMyObserveObj())
    val spisNodeTreeSkillsForSelection = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsForSelection.getMyObserveObj())
    val spisNodeTreeSkillsSelection = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsSelection.getMyObserveObj())
    val spisNodeTreeSkillsForInfo = ItrCommObserveObj(objForSpis.spisNodeTreeSkillsForInfo.getMyObserveObj())
    val spisIconNodeTree = ItrCommObserveObj(objForSpis.spisIconNodeTree.getMyObserveObj())
}