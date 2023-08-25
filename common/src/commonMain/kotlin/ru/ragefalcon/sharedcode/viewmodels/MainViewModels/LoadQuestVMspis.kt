package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

class LoadQuestVMspis(private val objForSpis: LoadQuestVMobjForSpis) {
    val dialogEvent = ItrCommObserveObj(objForSpis.dialogEvent)
    val spisOtvetDialogForEvent = ItrCommObserveObj(objForSpis.spisOtvetDialogForEvent.getMyObserveObj())
    val dialogForEvent = ItrCommObserveObj(objForSpis.dialogForEvent.getMyObsObjOneValue())
    val spisQuest = ItrCommObserveObj(objForSpis.spisQuest.getMyObserveObj())
    val innerFinishTriggerAction = ItrCommObserveObj(objForSpis.innerFinishTriggerAction.getMyObserveObj())
}