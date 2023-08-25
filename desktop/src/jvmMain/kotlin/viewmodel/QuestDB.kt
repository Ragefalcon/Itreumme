package viewmodel

import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.ObserveModels.ObserveQuestBaseVM
import java.io.File

class QuestDB(file: File) {

    constructor(nameDB: String) : this(File(File(StateVM.dirQuest, nameDB).path, "$nameDB.db"))

    val dirQuest = file.parent
    val nameQuest = file.nameWithoutExtension

    val arg = DbArgs(file.path)

    val ObserFM = ObserveQuestBaseVM(arg)

    val spisQuest = ObserFM.questSpis
    val addQuest = ObserFM.addQuest
    val questFun = ObserFM.questFun

    companion object {
        fun getPathQuest(name: String): String {
            return File(File(StateVM.dirQuest, name).path, "$name.db").path
        }
    }

}