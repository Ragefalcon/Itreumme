package viewmodel

import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.ObserveModels.ObserveStyleBaseVM
import java.io.File

class StyleDB(file: File) {
    val arg = DbArgs(file.path)

    val dirStyle = file.parent
    val nameStyle = file.nameWithoutExtension
    val ObserFM = ObserveStyleBaseVM(arg)
}