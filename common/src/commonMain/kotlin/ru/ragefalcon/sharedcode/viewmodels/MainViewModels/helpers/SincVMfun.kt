package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import ru.ragefalcon.sharedcode.Database

class SincVMfun (private val mDB: Database) {

        fun cleanReplicateRecord() {
            mDB.systemReplicateRecordQueries.cleanReplicateRecord()
        }
}