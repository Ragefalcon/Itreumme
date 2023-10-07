package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.models.data.ItemYearGraf
import ru.ragefalcon.sharedcode.source.disk.ItrCommListObserveObj
import ru.ragefalcon.sharedcode.source.disk.ItrCommListWithOpisObserveObj
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.setMapStatikToItemYearGraf
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj

class TimeVMspis(private val objForSpis: TimeVMobjForSpis) {
    val currentDate = ItrCommObserveObj(objForSpis.currentDate.getMyObserveObj())
    val textAboveSelectDenPlan = ItrCommObserveObj(objForSpis.textAboveSelectDenPlan.getMyObserveObj())
    val textColorAboveSelectDenPlan = ItrCommObserveObj(objForSpis.textColorAboveSelectDenPlan.getMyObserveObj())
    val spisDenPlan = ItrCommListWithOpisObserveObj(objForSpis.spisDenPlan.getMyObserveObj(), objForSpis.sverDenPlan)
    val spisDenPlanForCalendar = ItrCommListObserveObj(objForSpis.spisDenPlanForCalendar.getMyObserveObj())
    val spisDenPlanForHistoryPlan = ItrCommObserveObj(objForSpis.spisDenPlanForHistoryPlan.getMyObserveObj())
    val spisDenPlanForHistoryStapPlan = ItrCommObserveObj(objForSpis.spisDenPlanForHistoryStapPlan.getMyObserveObj())
    val spisSumHourForHistoryPlanDiag = ItrCommObserveObj(MyObserveObj<List<ItemYearGraf>> { ff ->
        objForSpis.spisSumHourForHistoryPlanDiag.updateFunc {
            setMapStatikToItemYearGraf(it) { ff(it) }
        }
    })
    val spisSumHourForHistoryStapPlanDiag = ItrCommObserveObj(MyObserveObj<List<ItemYearGraf>> { ff ->
        objForSpis.spisSumHourForHistoryStapPlanDiag.updateFunc {
            setMapStatikToItemYearGraf(it) { ff(it) }
        }
    })

    val spisShablonDenPlan =
        ItrCommListWithOpisObserveObj(objForSpis.spisShablonDenPlan.getMyObserveObj(), objForSpis.sverShablonDenPlan)
    val spisNextAction =
        ItrCommListWithOpisObserveObj(objForSpis.spisNextAction.getMyObserveObj(), objForSpis.sverNextAction)
    val spisNapom = ItrCommListWithOpisObserveObj(objForSpis.spisNapom.getMyObserveObj(), objForSpis.sverNapom)
    val spisNapomForCalendar = ItrCommObserveObj(objForSpis.spisNapomForCalendar.getMyObserveObj())
    val spisEffekt = ItrCommObserveObj(objForSpis.spisEffekt.getMyObserveObj())
    val spisSrokForPlanAndStap = ItrCommObserveObj(objForSpis.spisSrokPlanAndStap.getMyObserveObj())
    val spisTimelineForPlan = ItrCommObserveObj(objForSpis.spisTimelineForPlan.getMyObserveObj())
    val spisVajnHour = ItrCommObserveObj(objForSpis.spisVajnHour.getMyObserveObj())
    val spisPlanIn = ItrCommObserveObj(objForSpis.spisPlanIn.getMyObserveObj())
    val spisPlan = ItrCommListWithOpisObserveObj(objForSpis.spisPlan.getMyObserveObj(), objForSpis.sverPlan)
    val spisPlanForSelect = ItrCommObserveObj(objForSpis.spisPlanForSelect.getMyObserveObj())
    val spisAllPlan = ItrCommObserveObj(objForSpis.spisAllPlan.getMyObserveObj())
    val spisPlanStap = ItrCommListWithOpisObserveObj(objForSpis.spisPlanStap.getMyObserveObj(), objForSpis.sverStapPlan)
    val spisAllPlanStap = ItrCommObserveObj(objForSpis.spisAllPlanStap.getMyObserveObj())
    val spisPlanStapForSelect = ItrCommListObserveObj(objForSpis.spisStapPlansForSelect.getMyObserveObj())
    val spisVxod = ItrCommObserveObj(objForSpis.spisVxod.getMyObserveObj())
    val countStapPlan = ItrCommObserveObj(objForSpis.countStapPlan.getMyObsObjOneValue())
}