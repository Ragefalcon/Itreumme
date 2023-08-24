package ru.ragefalcon.sharedcode.viewmodels.ObserveModels

import ru.ragefalcon.sharedcode.quest.DatabaseQuest
import ru.ragefalcon.sharedcode.source.disk.DatabaseQuestCreator
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.QuestVMobjForSpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.QuestVMspis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.AddQuestHandler
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.QuestVMfun

class ObserveQuestBaseVM (
        dbArgs: DbArgs
    ) {

        private var qDatabase: DatabaseQuest? = DatabaseQuestCreator.getDataBase(dbArgs)

        private val questObjForSpis = QuestVMobjForSpis(qDatabase!!)
        val questSpis = QuestVMspis(questObjForSpis)
        val questFun = QuestVMfun(qDatabase!!,questObjForSpis)
        val addQuest = AddQuestHandler(qDatabase!!)
}