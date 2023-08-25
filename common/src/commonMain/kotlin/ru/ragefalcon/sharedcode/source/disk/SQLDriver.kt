package ru.ragefalcon.sharedcode.source.disk

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.AfterVersion
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.migrateWithCallbacks
import com.squareup.sqldelight.internal.copyOnWriteList
import kotlinx.coroutines.CoroutineDispatcher
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.DatabaseStyle
import ru.ragefalcon.sharedcode.Journal.UpdateFromVers3to4Select
import ru.ragefalcon.sharedcode.migration.SelectComplexOpisTextMig
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.models.data.SverOpis
import ru.ragefalcon.sharedcode.quest.DatabaseQuest
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj

expect class DbArgs

expect fun getPlatformName2(): String

expect fun getSqlDriver(dbArgs: DbArgs): SqlDriver?
expect fun getUserVersionSqlDriver(db: SqlDriver): Int
expect fun checkUserVersionSqlDriver(db: SqlDriver?)
expect fun getSqlQuestDriver(dbArgs: DbArgs): SqlDriver?

expect fun getSqlStyleDriver(dbArgs: DbArgs): SqlDriver?

expect class ItrCommObserveMutableObj<T>(default: T, change: (T) -> Unit) {
    fun innerUpdateValue(newValue: T)
}

interface ItrCommObserveInt

expect open class ItrCommObserveObj<T>(observeObj: MyObserveObj<T>) : ItrCommObserveInt
expect open class ItrCommListObserveObj<T : Id_class>(observeObj: MyObserveObj<List<T>>) : ItrCommObserveInt
expect open class ItrCommListWithOpisObserveObj<T>(
    observeObj: MyObserveObj<List<T>>,
    sverMap: MutableMap<Long, Boolean>
) : ItrCommListObserveObj<T> where T : Id_class, T : SverOpis<T>

expect fun getCorDisp(): CoroutineDispatcher

object CommonName {
    const val nameAvatarFile = "avatarMain.jpg"
    const val nameDefaultAvatarResource = "iv_barash.jpg"
    const val nameMainDBfile = "mainDB.db"
    const val nameFromNetworkDBfile = "fromNetworkDB.db"
}


class LoadQuestFromFile(
    private val driver: SqlDriver
) : TransacterImpl(driver) {

    fun loadQuestSkillTree(
        questId: Long,
        path: String,
        sqlDr: SqlDriver,
        db: Database,
        selectBloknot: MutableList<Query<*>> = copyOnWriteList()
    ) {
        var aa: String? = ""
        db.transaction {
            sqlDr.execute(
                null,
                "attach database '$path' AS QDB; ",
                0
            )
            var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM QDB.spis_tree_skill_quest", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
            }
            sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
            }
            sqlDr.execute(
                -1173528573,
                "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
                        "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;",
                0
            )
            /**
             * Команда ниже отправляет оповещение слушателям запросов указанных вторым параметром,
             * в него должны передаваться конкретно те объекты на которые вешались слушатели,
             * после (как я понимаю) выполнеия запроса с соответствующим идентификатором.
             * */
            notifyQueries(-1173528573, { selectBloknot })
            /**
             * Во время такого добавления также работают триггеры поэтому уровни необходимо добавлять до добавления узлов.
             * Иначе уровень узлов при добавлении уровней будет пересчитан, и в итоге узлы будут числиться в итоге за
             * несуществующими уровнями.
             * Теоретически можно было бы отключить триггеры на время такого добавления, но тогда также не будут
             * добавляться записи для системы реплицирования, которую я планирую использовать для синхронизации.
             * */
            sqlDr.execute(
                null,
                "INSERT INTO spis_level_tree_skill(id_tree, name, num_level, opis, proc_porog, open_level, quest_id, quest_key_id) SELECT " +
                        "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_level_tree_skill_quest.id_tree) AS id_tree," +
                        "name, num_level, opis, proc_porog, visible_stat AS open_level," +
                        "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_level_tree_skill_quest;",
                0
            )
            sqlDr.execute(
                null,
                "INSERT INTO spis_node_tree_skills(id_tree,name,opis,id_type_node,complete,level,icon,icon_complete,quest_id,quest_key_id) " +
                        "SELECT (SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_node_tree_skills_quest.id_tree) AS id_tree," +
                        "name,opis,id_type_node,visible_stat AS complete,level,icon,icon_complete, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_node_tree_skills_quest;",
                0
            )
            sqlDr.execute(
                null,
                "INSERT INTO spis_binding_node_tree_skill(id_tree,id_parent,id_child,quest_id,quest_key_id) SELECT " +

                        "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = cc.id_tree) AS id_tree," +
                        "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = cc.id_parent) AS id_parent," +
                        "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = cc.id_child) AS id_child," +
                        "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_binding_node_tree_skill_quest AS cc;",
                0
            )
            sqlDr.execute(
                null,
                "INSERT INTO spis_must_complete_node_for_level(id_tree, num_level, id_node, quest_id, quest_key_id) SELECT " +
                        "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_must_complete_node_for_level_quest.id_tree) AS id_tree," +
                        "num_level, " +
                        "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_must_complete_node_for_level_quest.id_node) AS id_node," +
                        "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_must_complete_node_for_level_quest;",
                0
            )
            sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
            }
        }
        driver.execute(
            null,
            "detach database QDB;",
            0
        )
    }
}

object DatabaseCreator {
    var sqlDriver: SqlDriver? = null

    var aaf: Transacter.Transaction? = null

    fun attachDatabase(path: String, sqlDr: SqlDriver) {
        var aa: String? = ""
        aaf = sqlDr.newTransaction().apply {
            sqlDr.execute(
                null,
                "attach database '$path' AS QDB;",
                0
            )
            var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM QDB.sqlite_master", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
            }
        }
    }

    fun loadQuestSkillTree(
        questId: Long,
        path: String,
        sqlDr: SqlDriver,
        db: Database,
        selectBloknot: MutableList<Query<*>> = copyOnWriteList()
    ) {
        var aa: String? = ""
        db.transaction {
            sqlDr.execute(
                null,
                "attach database '$path' AS QDB; ",
                0
            )
        }
        var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM QDB.spis_tree_skill_quest", 0, null);
        while (sqlCursor.next()) {
            aa = sqlCursor.getString(0)
        }

        sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
        while (sqlCursor.next()) {
            aa = sqlCursor.getString(0)
        }
        sqlDr.execute(
            -1173528573,
            "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
                    "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;",
            0
        )

        sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
        while (sqlCursor.next()) {
            aa = sqlCursor.getString(0)
        }

        sqlDr.execute(
            null,
            "detach database QDB;",
            0
        )

    }

    fun selectDialog(sqlDr: SqlDriver) {
        var aa: String? = ""
        sqlDr.currentTransaction()?.apply {

            var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM sqlite_master", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
            }
        }
    }

    fun getDataBase(dbArgs: DbArgs): Pair<Database?, SqlDriver?> {
        sqlDriver = getSqlDriver(dbArgs)
        sqlDriver?.let { sqlDr ->
            val db = Database(sqlDr)

            val pragmaUserVersion = getUserVersionSqlDriver(sqlDr)

            if (pragmaUserVersion > 0) {
                Database.Schema.migrateWithCallbacks(
                    driver = sqlDr,
                    oldVersion = pragmaUserVersion,
                    newVersion = Database.Schema.version,
                    AfterVersion(4) {
                        with(db.migrFunQueries) {
                            if (updateFromVers3to4Check().executeAsOne() != 0L) {
                                updateFromVers3to4Select() { id, name, opis ->
                                    UpdateFromVers3to4Select(id, name.uppercase(), opis.uppercase())
                                }.executeAsList().let { listIdea ->
                                    transaction {
                                        listIdea.forEach {
                                            updateFromVers3to4(it._id, it.name, it.opis)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    AfterVersion(30) {
                        with(db.migrFunQueries) {
                            selectComplexOpisTextMig() { id, opis ->
                                SelectComplexOpisTextMig(id, opis.uppercase())
                            }.executeAsList().let { listIdea ->
                                transaction {
                                    listIdea.forEach {
                                        updateComplexOpisTextMig(it.opis_id, it.text)
                                    }
                                }
                            }
                        }
                    },
                    AfterVersion(31) {
                        with(db.migrFunQueries) {
                            selectComplexOpisTextMig() { id, opis ->
                                SelectComplexOpisTextMig(id, opis.uppercase())
                            }.executeAsList().let { listIdea ->
                                transaction {
                                    listIdea.forEach {
                                        updateComplexOpisTextMig(it.opis_id, it.text)
                                    }
                                }
                            }
                        }
                    },
                    AfterVersion(32) {
                        with(db.migrFunQueries) {
                            selectComplexOpisTextMig() { id, opis ->
                                SelectComplexOpisTextMig(id, opis.uppercase())
                            }.executeAsList().let { listIdea ->
                                transaction {
                                    listIdea.forEach {
                                        updateComplexOpisTextMig(it.opis_id, it.text)
                                    }
                                }
                            }
                        }
                    },
                )
            }
            if (Database.Schema.version > pragmaUserVersion) checkUserVersionSqlDriver(sqlDr)
            return db to sqlDriver
        }
        return null to sqlDriver
    }
}

object DatabaseQuestCreator {
    fun getDataBase(dbArgs: DbArgs): DatabaseQuest? {
        val sqlDriver = getSqlQuestDriver(dbArgs)
        if (sqlDriver != null) {

            val pragmaUserVersion = getUserVersionSqlDriver(sqlDriver)
            DatabaseQuest.Schema.migrate(sqlDriver, 1, DatabaseQuest.Schema.version)
            if (Database.Schema.version > pragmaUserVersion) checkUserVersionSqlDriver(sqlDriver)
            return DatabaseQuest(sqlDriver)
        } else {
            return null
        }
    }
}

object DatabaseStyleCreator {
    fun getDataBase(dbArgs: DbArgs): Pair<DatabaseStyle?, SqlDriver?> {
        val sqlDriver = getSqlStyleDriver(dbArgs)
        if (sqlDriver != null) {
            val pragmaUserVersion = getUserVersionSqlDriver(sqlDriver)
            DatabaseStyle.Schema.migrate(sqlDriver, 1, DatabaseStyle.Schema.version)
            if (Database.Schema.version > pragmaUserVersion) checkUserVersionSqlDriver(sqlDriver)
            return DatabaseStyle(sqlDriver) to sqlDriver
        } else {
            return null to null
        }
    }
}