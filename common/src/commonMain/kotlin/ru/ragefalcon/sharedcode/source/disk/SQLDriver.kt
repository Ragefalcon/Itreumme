package ru.ragefalcon.sharedcode.source.disk

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.*
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
expect open class ItrCommListObserveObj<T: Id_class>(observeObj: MyObserveObj<List<T>>)  : ItrCommObserveInt
expect open class ItrCommListWithOpisObserveObj<T>(observeObj: MyObserveObj<List<T>>, sverMap: MutableMap<Long, Boolean>)
    : ItrCommListObserveObj<T> where T : Id_class, T : SverOpis<T>

expect fun getCorDisp(): CoroutineDispatcher

object CommonName {
    const val nameAvatarFile = "avatarMain.jpg"
    const val nameDefaultAvatarResource = "iv_barash.jpg"
    const val nameMainDBfile = "mainDB.db"
    const val nameFromNetworkDBfile = "fromNetworkDB.db"
}
//expect fun<T:Any,F: Any> getPlatformLiveData(ff: MyObserveObj<T>):F

class LoadQuestFromFile(
    private val driver: SqlDriver
) : TransacterImpl(driver) {
    //    internal val selectBloknot: MutableList<Query<*>> = copyOnWriteList()
    fun loadQuestSkillTree(
        questId: Long,
        path: String,
        sqlDr: SqlDriver,
        db: Database,
        selectBloknot: MutableList<Query<*>> = copyOnWriteList()
    ) {
        var aa: String? = ""//sqlCursor.getString(1)// next()
//        DatabaseCreator.aaf ?: run { DatabaseCreator.aaf = sqlDr.newTransaction() }
//        aaf.apply {
//        sqlDr.newTransaction().apply {
//            val selectBloknot: MutableList<Query<*>> = copyOnWriteList()
        db.transaction {
            sqlDr.execute(
                null,
                "attach database '$path' AS QDB; ",
//                        "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
//                        "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;",
                0
            )
            var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM QDB.spis_tree_skill_quest", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
                println("aa: $aa")
            }

//            Query(1631510975, selectBloknot, sqlDr, "SpisBlo45knot.sq", "selectBl45oknot",
//                "attach database '$path' AS QDB; " +//,
//                        "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
//                        "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;".trimMargin()) { cursor ->
//            }.execute()


            sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
                println("spis_tree_skill: $aa")
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
                        "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_level_tree_skill_quest;", // ORDER BY num_level
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
//                        "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_binding_node_tree_skill_quest.id_tree) AS id_tree," +
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
/*          /** Нужно для начала перенести сюда также перенос планов и этапов, а для этого походу нужно добавить к ним поля quest_id и quest_key_id. А со счетчика и того сложнее, потому что нет пока никаких счетчиков...) **/
            sqlDr.execute(
                null,
                "INSERT INTO property_plan_node_ts(id_node,privplan,stap_prpl,porog_hour,quest_id,quest_key_id) SELECT " +
                        "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_binding_node_tree_skill_quest.id_tree) AS id_tree," +
                        "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_binding_node_tree_skill_quest.id_parent) AS id_parent," +
                        "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_binding_node_tree_skill_quest.id_child) AS id_child," +
                        "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_binding_node_tree_skill_quest;",
                0
            )
*/
            sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
                println("spis_tree_skill: $aa")
            }
        }
        driver.execute(
            null,
            "detach database QDB;",
            0
        )
//        aaf.enclosingTransaction()
    }

    fun detach() {
    }

}

object DatabaseCreator {
    var sqlDriver: SqlDriver? = null
    fun checkRecurTrig() {
        /**
         * данную фунцию (подобного типа) можно использовать для выполнения прямых строковых запросов к базе.
         * в частности можно попробовать установить соединение между двумя файлами баз, чтобы производить копирование
         * между ними с помощью SQL запросов.
         * */
//        sqlDriver!!.execute(null, "CREATE TABLE spis_tests ( _id     INTEGER PRIMARY KEY AUTOINCREMENT,  id_test INTEGER NOT NULL);", 0)
//        sqlDriver!!.execute(null, "PRAGMA recursive_triggers = 1;", 0)
//        var sqlCursor = sqlDriver!!.executeQuery(null, "PRAGMA recursive_triggers;", 0, null);
//            sqlCursor = sqlDriver!!.executeQuery(null, "PRAGMA recursive_triggers;", 0, null);
//        println("user_version: ${sqlCursor.use { sqlCursor.getString(0) }}") //!!.toInt()
//        println(sqlCursor.use { sqlCursor.getString(0) }) //!!.toInt()
    }

    var aaf: Transacter.Transaction? = null


    fun attachDatabase(path: String, sqlDr: SqlDriver) {
        var aa: String? = ""//sqlCursor.getString(1)// next()
        aaf = sqlDr.newTransaction().apply {
            sqlDr.execute(
                null,
                "attach database '$path' AS QDB;",
                0
            )
            var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM QDB.sqlite_master", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
                println("aa: $aa")
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
        var aa: String? = ""//sqlCursor.getString(1)// next()
//        DatabaseCreator.aaf ?: run { DatabaseCreator.aaf = sqlDr.newTransaction() }
//        aaf.apply {
//        sqlDr.newTransaction().apply {
//            val selectBloknot: MutableList<Query<*>> = copyOnWriteList()
        db.transaction {
            sqlDr.execute(
                null,
                "attach database '$path' AS QDB; ",
//                        "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
//                        "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;",
                0
            )
        }
        var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM QDB.spis_tree_skill_quest", 0, null);
        while (sqlCursor.next()) {
            aa = sqlCursor.getString(0)
            println("aa: $aa")
        }

//            Query(1631510975, selectBloknot, sqlDr, "SpisBlo45knot.sq", "selectBl45oknot",
//                "attach database '$path' AS QDB; " +//,
//                        "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
//                        "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;".trimMargin()) { cursor ->
//            }.execute()


        sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
        while (sqlCursor.next()) {
            aa = sqlCursor.getString(0)
            println("spis_tree_skill: $aa")
        }
        sqlDr.execute(
            -1173528573,
            "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
                    "SELECT id_area, name, id_type_tree, opis , 1 AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;",
            0
        )
//            notifyQueries(-1173528573, { selectBloknot })
        sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM spis_tree_skill", 0, null);
        while (sqlCursor.next()) {
            aa = sqlCursor.getString(0)
            println("spis_tree_skill: $aa")
        }
//        }
        sqlDr.execute(
            null,
            "detach database QDB;",
            0
        )
//        aaf.enclosingTransaction()
    }

    fun selectDialog(sqlDr: SqlDriver) {
        var aa: String? = ""//sqlCursor.getString(1)// next()
        sqlDr.currentTransaction()?.apply {

            var sqlCursor = sqlDr.executeQuery(null, "SELECT COUNT(*) FROM sqlite_master", 0, null);
            while (sqlCursor.next()) {
                aa = sqlCursor.getString(0)
                println("aa: $aa")
            }
        }
    }


    fun getDataBase(dbArgs: DbArgs): Pair<Database?, SqlDriver?> {
        sqlDriver = getSqlDriver(dbArgs)
        sqlDriver?.let { sqlDr ->
            val db = Database(sqlDr)

            /**
             * https://github.com/cashapp/sqldelight/issues/1106
             * https://github.com/cashapp/sqldelight/issues/1089
             * https://github.com/cashapp/sqldelight/issues/1241
             * https://github.com/cashapp/sqldelight/issues/2421
             *
             * val driver = JdbcSqliteDriver(
             * "jdbc:sqlite:${tempFile.absolutePath}",
             * Properties(1).apply { put("foreign_keys", "true") }
             * )
             *
             * Вариант ниже не рабочий, но сами запросы таким образом выполняются, думаю можно попробовать с помощью этого
             * соединить две базы и использовать для копирования квестов в базу и наоборот структур из
             * val driver = JdbcSqliteDriver("jdbc:sqlite:temp.db")
             * driver.execute(null, "PRAGMA user_version = 2;", 0, null)
             * val sqlCursor = driver.executeQuery(null, "PRAGMA user_version;", 0, null)
             * val userVersion = sqlCursor.getLong(0)!!
             * */
            val pragmaUserVersion = getUserVersionSqlDriver(sqlDr)

            println("Schema.version: ${Database.Schema.version}") //!!.toInt()
            println("PRAGMA user_version: $pragmaUserVersion")

            if (pragmaUserVersion > 0) {
                Database.Schema.migrateWithCallbacks(
                    driver = sqlDr,
                    oldVersion = pragmaUserVersion,
                    newVersion = Database.Schema.version, //Database.Schema.version,
                    /**
                     * Похоже в этом куске все логично и кусок в AfterVersion применяется именно после файла с номером
                     * который передается параметром. Т.е. если последний файл миграции 30, и AfterVersion(30) то вначале применяется он,
                     * потом блок кода внутри и потом уже версия увеличивается на 1 до 31.
                     * */
                    //AfterVersionWithDriver это похоже только в версии 1.5.3, но она пока не работает на андроиде, не формирует пустую базу
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
/*
                    AfterVersion(33) {
                        println("AfterVersion(33)")
                        sqlDr.execute(
                            null, """
                PRAGMA writable_schema = ON; 
            """, 0 )
                        sqlDr.execute(
                            null, """
                PRAGMA foreign_keys = OFF; 
            """, 0 )
                        sqlDr.execute(
                            null, """
                PRAGMA legacy_alter_table = ON;
            """, 0 )
                        sqlDr.execute(
                            null, """

CREATE TABLE IF NOT EXISTS spis_plan_2 (
    _id   INTEGER PRIMARY KEY AUTOINCREMENT,
    vajn  INTEGER NOT NULL,
    name  TEXT NOT NULL,
    gotov REAL NOT NULL,
    data1 INTEGER NOT NULL,
    data2 INTEGER NOT NULL,
    opis  TEXT NOT NULL,
    stat  INTEGER NOT NULL,
    direction  INTEGER NOT NULL,
    sort  INTEGER NOT NULL,
    quest_id  INTEGER NOT NULL DEFAULT 0,
    quest_key_id  INTEGER NOT NULL DEFAULT 0
);
            """, 0 )
                        sqlDr.execute(
                            null, """

INSERT INTO spis_plan_2 (_id,vajn,name,gotov,data1,data2,opis,stat,direction,sort,quest_id,quest_key_id)
   SELECT _id,vajn,name,gotov,data1,data2,opis,stat, 0 AS direction, _id AS sort,quest_id,quest_key_id FROM spis_plan;
            """, 0 )
                        sqlDr.execute(
                            null, """
DROP TABLE IF EXISTS spis_plan;
            """, 0 )
                        sqlDr.execute(
                            null, """
ALTER TABLE spis_plan_2 RENAME TO spis_plan;
            """, 0 )
                        sqlDr.execute(
                            null, """
CREATE TRIGGER IF NOT EXISTS sort_insert_spis_plan_trig
         AFTER INSERT
            ON spis_plan
      FOR EACH ROW
BEGIN
    UPDATE spis_plan SET sort = new._id WHERE _id = new._id;
END;
            """, 0 )
                        sqlDr.execute(
                            null, """


CREATE TABLE IF NOT EXISTS spis_stap_plan_2 (
    _id       INTEGER PRIMARY KEY AUTOINCREMENT,
    parent_id INTEGER NOT NULL,
    name      TEXT NOT NULL,
    gotov     REAL NOT NULL,
    data1     INTEGER NOT NULL,
    data2     INTEGER NOT NULL,
    opis      TEXT NOT NULL,
    stat      INTEGER NOT NULL,
    svernut   TEXT NOT NULL, --INTEGER AS Boolean NOT NULL, -- DEFAULT 0 NOT NULL
    idplan    INTEGER NOT NULL,
    marker  INTEGER NOT NULL,
    sort  INTEGER NOT NULL,
    quest_id  INTEGER NOT NULL DEFAULT 0,
    quest_key_id  INTEGER NOT NULL DEFAULT 0
);

            """, 0 )
                        sqlDr.execute(
                            null, """
INSERT INTO spis_stap_plan_2 (_id,parent_id,name,gotov,data1,data2,opis,stat,svernut,idplan,marker,sort,quest_id,quest_key_id)
   SELECT _id,parent_id,name,gotov,data1,data2,opis,stat,svernut,idplan, 0 AS marker, _id AS sort,quest_id,quest_key_id FROM spis_stap_plan;
            """, 0 )
                        sqlDr.execute(
                            null, """
DROP TABLE IF EXISTS spis_stap_plan;
            """, 0 )
                        sqlDr.execute(
                            null, """
ALTER TABLE spis_stap_plan_2 RENAME TO spis_stap_plan;

            """, 0 )
                        sqlDr.execute(
                            null, """

CREATE TRIGGER IF NOT EXISTS sort_insert_spis_stap_plan_trig
         AFTER INSERT
            ON spis_stap_plan
      FOR EACH ROW
BEGIN
    UPDATE spis_stap_plan SET sort = new._id WHERE _id = new._id;
END;
                
            """, 0 )
                        sqlDr.execute(
                            null, """
                PRAGMA writable_schema = OFF;
            """, 0 )
                        sqlDr.execute(
                            null, """
                PRAGMA foreign_keys = ON; 
            """, 0 )
                        sqlDr.execute(
                            null, """
                PRAGMA legacy_alter_table = OFF;
            """, 0 )
                    },
*/
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
//            sqlDriver.execute(null, "PRAGMA recursive_triggers = 1;", 0, null)
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
//            sqlDriver.execute(null, "PRAGMA recursive_triggers = 1;", 0, null)
            val pragmaUserVersion = getUserVersionSqlDriver(sqlDriver)
            DatabaseStyle.Schema.migrate(sqlDriver, 1, DatabaseStyle.Schema.version)
            if (Database.Schema.version > pragmaUserVersion) checkUserVersionSqlDriver(sqlDriver)
            return DatabaseStyle(sqlDriver) to sqlDriver
        } else {
            return null to null
        }
    }
}