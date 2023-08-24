package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

class ComplexOpisVMspis(private val objForSpis: ComplexOpisVMobjForSpis)  {
    val spisComplexOpisShablon = ItrCommObserveObj(objForSpis.spisComplexOpisShablon.getMyObserveObj())
    val spisComplexOpisForVxod = ItrCommObserveObj(objForSpis.spisComplexOpisForVxod.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForNapom = ItrCommObserveObj(objForSpis.spisComplexOpisForNapom.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForDenPlan = ItrCommObserveObj(objForSpis.spisComplexOpisForDenPlan.spisComplexOpis.getMyObserveObj())
    val countCalendarWeek = objForSpis.countCalendarWeek
    val spisComplexOpisForCalendar = ItrCommObserveObj(objForSpis.spisComplexOpisForCalendar.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForCalendarNapom = ItrCommObserveObj(objForSpis.spisComplexOpisForCalendarNapom.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForDenPlanInBestDays = ItrCommObserveObj(objForSpis.spisComplexOpisForDenPlanInBestDays.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForShabDenPlan = ItrCommObserveObj(objForSpis.spisComplexOpisForShabDenPlan.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForBloknot = ItrCommObserveObj(objForSpis.spisComplexOpisForBloknot.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForIdea = ItrCommObserveObj(objForSpis.spisComplexOpisForIdea.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForIdeaStap = ItrCommObserveObj(objForSpis.spisComplexOpisForIdeaStap.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForNextActionCommon = ItrCommObserveObj(objForSpis.spisComplexOpisForNextActionCommon.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForNextActionStap = ItrCommObserveObj(objForSpis.spisComplexOpisForNextActionStap.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForPlan = ItrCommObserveObj(objForSpis.spisComplexOpisForPlan.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForStapPlan = ItrCommObserveObj(objForSpis.spisComplexOpisForStapPlan.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForHistoryPlan = ItrCommObserveObj(objForSpis.spisComplexOpisForHistoryPlan.spisComplexOpis.getMyObserveObj())
    val spisComplexOpisForHistoryStapPlan = ItrCommObserveObj(objForSpis.spisComplexOpisForHistoryStapPlan.spisComplexOpis.getMyObserveObj())
}