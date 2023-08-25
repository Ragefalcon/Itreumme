package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlDriver
import ru.ragefalcon.sharedcode.Database

class LoadStyleFromFileAttach(
    private val driver: SqlDriver,
    private val db: Database,
) : TransacterImpl(driver) {

    fun loadStartStyle(
        path: String,
    ) {
        db.transaction {
            driver.execute(
                null,
                "attach database '$path' AS QDB; ",
                0
            )
            driver.execute(
                -64654056,
                "INSERT INTO spis_save_set_style(_id,name,type) VALUES (-6, 'Стиль по умолчанию',1) ;",
                0
            )
            driver.execute(
                -64654056,
                "INSERT INTO save_set_style_settings(_id,set_id,codename,intparam, doubleparam,stringparam)  " +
                        "SELECT NULL AS _id, -6 AS set_id, codename,intparam, doubleparam,stringparam FROM QDB.style_settings_save;",
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