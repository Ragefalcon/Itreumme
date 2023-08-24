package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EditStyleVMobjForSpis

class EditStyleVMfun(private val mDB: Database, private val spisVM: EditStyleVMobjForSpis) {
    fun setSelectSetColorLibrary(idSet: Long) {
        spisVM.spisColorLibrary.updateQuery(mDB.colorLibraryQueries.selectColorLibrary(idSet))
    }
}