package ru.ragefalcon.sharedcode.viewmodels.ObserveModels

import com.soywiz.klock.DateTimeTz
import com.squareup.sqldelight.db.SqlDriver
import ru.ragefalcon.sharedcode.DatabaseStyle
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.minusTime
import ru.ragefalcon.sharedcode.source.disk.DatabaseStyleCreator
import ru.ragefalcon.sharedcode.source.disk.DbArgs

class ObserveStyleBaseVM (
    dbArgs: DbArgs
) {
//    private var sDatabase: DatabaseStyle? = DatabaseStyleCreator.getDataBase(dbArgs)
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
            val datetime = DateTimeTz.nowLocal().minusTime().localUnix()
                driver.execute(
                    -64654056,
                    "INSERT INTO style_settings_save(_id,codename,intparam, doubleparam,stringparam)  " +
                            "SELECT NULL AS _id, codename,intparam, doubleparam,stringparam FROM QDB.style_settings;",
                    0
                )
//            driver.execute(
//                null,
//                "INSERT INTO quest_mainparam(quest_id, name, intparam, stringparam) " +
//                        "SELECT $qId AS quest_id, name, intparam, stringparam FROM QDB.mainparam WHERE name <> 'name';",
//                0
//            )
/*            notifyQueries(-64654056) { spisQuery.spisQuest?.let { mutableListOf(it) } ?: copyOnWriteList() }

            if (qId != 0L || qId != -1L) {

                loadPlanAndStap(qId)
                *//**
                 * Кусок со вставкой и удалением ниже - костыль для оповещения всех слушателей по списку проектов.
                 * Я решил, что чем собирать и следить за всеми этими слушателями вручную, проще спровоцировать на это
                 * встроенную систему. Её также было бы неплохо запускать без триггеров, возможно стоит сделать функцию,
                 * которая будет внутри себя выполнять запросы перед этим отключив триггеры, а после включив.
                 * *//*
                sDatabase.spisPlanQueries.insertOrReplacePlan(
                    -2345,
                    "TempPlanForActivatedListener",
                    -2345.0,
                    2345,
                    2345,
                    "",
                    2345,
                    0
                )
                val sqlCursor = driver.executeQuery(null, "SELECT last_insert_rowid();", 0, null);
                if (sqlCursor.next()) {
                    sDatabase.spisPlanQueries.deletePlan(sqlCursor.getLong(0) ?: -1L, TypeBindElementForSchetPlan.PLAN.id)
                }

                loadDialogAndOtvet(qId)
                loadCommonTrigger(qId)
                loadSkillTree(qId)
                *//**
                 * todo
                 * Теперь нужно добавить:
                 *  - иконки узлов деревьев с файлами
                 * *//*
            }

        */
        }
/*
        sDatabase.transaction {
            copyGovorunFiles(qId, dirQuest, dirLoadedFiles)
            copyIconTreeSkillFiles(qId, dirQuest, dirIconFiles)
        }
*/
        driver.execute(
            null,
            "detach database QDB;",
            0
        )
//        return if (qId != 0L || qId != -1L) qId else null
    }

}