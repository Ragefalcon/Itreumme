package ru.ragefalcon.sharedcode.viewmodels.MainViewModels

import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj

class JournalVMspis(private val objForSpis: JournalVMobjForSpis) {
    val spisBloknot = ItrCommObserveObj(objForSpis.spisBloknot.getMyObserveObj())
    val spisIdea = ItrCommObserveObj(objForSpis.spisIdea.getMyObserveObj())
    val spisStapIdea = ItrCommObserveObj(objForSpis.spisIdeaStap.getMyObserveObj())
    val spisIdeaForSelect = ItrCommObserveObj(objForSpis.spisIdeaForSelect.getMyObserveObj())
}