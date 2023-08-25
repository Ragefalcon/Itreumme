package ru.ragefalcon.sharedcode.viewmodels.ObserveModels

import com.squareup.sqldelight.db.SqlDriver
import ru.ragefalcon.sharedcode.DatabaseStyle
import ru.ragefalcon.sharedcode.source.disk.DatabaseStyleCreator
import ru.ragefalcon.sharedcode.source.disk.DbArgs

class ObserveStyleBaseVM(
    dbArgs: DbArgs
) {
    private var pairDBandDriver = DatabaseStyleCreator.getDataBase(dbArgs)
    private var sDatabase: DatabaseStyle? = pairDBandDriver.first
    private var driver: SqlDriver = pairDBandDriver.second!!

    fun saveStyle(
        path: String,
    ) {
        sDatabase?.transaction {
            driver.execute(
                null,
                "attach database '$path' AS QDB; ",
                0
            )
            driver.execute(
                -64654056,
                "INSERT INTO style_settings_save(_id,codename,intparam, doubleparam,stringparam)  " +
                        "SELECT NULL AS _id, codename,intparam, doubleparam,stringparam FROM QDB.style_settings;",
                0
            )
        }
        driver.execute(
            null,
            "detach database QDB;",
            0
        )
    }
}