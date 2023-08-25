package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTimeTz
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.TransactionWithoutReturn
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.minusTime
import ru.ragefalcon.sharedcode.source.disk.FileMP
import ru.ragefalcon.sharedcode.source.disk.getPathWithSeparator
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan

class SpisQueryForListener() {
    var spisQuest: Query<*>? = null
    var spisTreeSkills: Query<*>? = null
    var spisPlan: Query<*>? = null
    var spisStapPlan: Query<*>? = null
}

class LoadQuestFromFileAttach(
    private val driver: SqlDriver,
    private val db: Database,
    private val spisQuery: SpisQueryForListener
) : TransacterImpl(driver) {

    private fun TransactionWithoutReturn.loadSkillTree(
        questId: Long
    ) {
        driver.execute(
            -1173528573,
            "INSERT INTO spis_tree_skill(id_area,name,id_type_tree,opis,open_edit,icon,quest_id,quest_key_id) " +
                    "SELECT id_area, name, id_type_tree, opis , visible_stat AS open_edit, icon, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_tree_skill_quest;",
            0
        )
        driver.execute(
            null,
            "INSERT INTO spis_icon_node_tree_skills( extension, type_ramk, quest_id, quest_key_id) " +
                    "SELECT extension, type_ramk, '$questId' AS quest_id, _id AS quest_key_id " +
                    "FROM QDB.spis_icon_node_tree_skills_quest;",
            0
        )
        /**
         * Команда ниже отправляет оповещение слушателям запросов указанных вторым параметром,
         * в него должны передаваться конкретно те объекты на которые вешались слушатели,
         * после (как я понимаю) выполнеия запроса с соответствующим идентификатором.
         * */
        notifyQueries(-1173528573) { spisQuery.spisTreeSkills?.let { mutableListOf(it) } ?: copyOnWriteList() }
        /**
         * Во время такого добавления также работают триггеры поэтому уровни необходимо добавлять до добавления узлов.
         * Иначе уровень узлов при добавлении уровней будет пересчитан, и в итоге узлы будут числиться в итоге за
         * несуществующими уровнями.
         * Теоретически можно было бы отключить триггеры на время такого добавления, но тогда также не будут
         * добавляться записи для системы реплицирования, которую я планирую использовать для синхронизации.
         *
         * так же в запросе необходимо использовать ORDER BY num_level, иначе если уровни будут добавляться в неправильно порядке,
         * то так же будут активизироваться триггеры персчитывающие номер уровня.
         * */
        driver.execute(
            null,
            "INSERT INTO spis_level_tree_skill(id_tree, name, num_level, opis, proc_porog, open_level, quest_id, quest_key_id) SELECT " +
                    "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_level_tree_skill_quest.id_tree) AS id_tree," +
                    "name, num_level, opis, proc_porog, (CASE WHEN num_level = 1 THEN 1 ELSE visible_stat END) AS open_level," +
                    "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_level_tree_skill_quest ORDER BY num_level;", // ORDER BY num_level
            0
        )
        driver.execute(
            null,
            "INSERT INTO spis_node_tree_skills(id_tree,name,opis,id_type_node,complete,level,icon,icon_complete,quest_id,quest_key_id) " +
                    "SELECT treeNode._id AS id_tree," +
                    "qNode.name, qNode.opis, qNode.id_type_node, qNode.visible_stat AS complete, qNode.level," +
                    "IFNULL(icon_node._id,-1) AS icon," +
                    "IFNULL(icon_node_c._id,-1) AS icon_complete, " +
                    "'$questId' AS quest_id, qNode._id AS quest_key_id FROM QDB.spis_node_tree_skills_quest AS qNode " +
                    "LEFT JOIN spis_tree_skill AS treeNode ON treeNode.quest_id = '$questId' AND treeNode.quest_key_id = qNode.id_tree " +
                    "LEFT JOIN spis_icon_node_tree_skills AS icon_node ON icon_node.quest_id = '$questId' AND icon_node.quest_key_id = qNode.icon " +
                    "LEFT JOIN spis_icon_node_tree_skills AS icon_node_c ON icon_node_c.quest_id = '$questId' AND icon_node_c.quest_key_id = qNode.icon_complete " +
                    ";",
            0
        )
        driver.execute(
            null,
            "INSERT INTO spis_binding_node_tree_skill(id_tree,id_parent,id_child,quest_id,quest_key_id) SELECT " +
                    "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = cc.id_tree) AS id_tree," +
                    "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = cc.id_parent) AS id_parent," +
                    "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = cc.id_child) AS id_child," +
                    "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_binding_node_tree_skill_quest AS cc;",
            0
        )
        driver.execute(
            null,
            "INSERT INTO spis_must_complete_node_for_level(id_tree, id_node, quest_id, quest_key_id) SELECT " +
                    "(SELECT _id FROM spis_tree_skill WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_must_complete_node_for_level_quest.id_tree) AS id_tree," +
                    "(SELECT _id FROM spis_node_tree_skills WHERE quest_id = '$questId' AND quest_key_id = QDB.spis_must_complete_node_for_level_quest.id_node) AS id_node," +
                    "'$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_must_complete_node_for_level_quest;",
            0
        )
        driver.execute(
            null,
            "INSERT INTO property_plan_node_ts(id_node,privplan,stap_prpl,porog_hour,quest_id,quest_key_id) SELECT " +
                    "nodes._id AS id_node, plans._id AS privplan, IFNULL(staps._id,-1) AS stap_prpl, qprop.porog_hour, " +
                    "'$questId' AS quest_id, qprop._id AS quest_key_id " +
                    "FROM QDB.property_plan_node_ts_quest AS qprop " +
                    "LEFT JOIN spis_node_tree_skills AS nodes ON nodes.quest_id = '$questId' AND nodes.quest_key_id = qprop.id_node " +
                    "LEFT JOIN spis_plan AS plans ON plans.quest_id = '$questId' AND plans.quest_key_id = qprop.privplan " +
                    "LEFT JOIN spis_stap_plan AS staps ON staps.quest_id = '$questId' AND staps.quest_key_id = qprop.stap_prpl " +
                    ";",
            0
        )
    }

    private fun TransactionWithoutReturn.loadPlanAndStap(
        questId: Long
    ) {
        driver.execute(
            -1173528574,
            "INSERT INTO spis_plan(vajn, name, gotov, data1, data2, opis, stat, quest_id, quest_key_id) " +
                    "SELECT vajn, name, 0 AS gotov, 0 AS data1, 1 AS data2, opis, commstat AS stat, '$questId' AS quest_id, _id AS quest_key_id FROM QDB.spis_plan_quest;",
            0
        )
        notifyQueries(-1173528574) { spisQuery.spisPlan?.let { mutableListOf(it) } ?: copyOnWriteList() }
        driver.execute(
            null,
            "INSERT INTO spis_stap_plan(parent_id, name, gotov, data1, data2, opis, stat, svernut, idplan, quest_id, quest_key_id) " +
                    "SELECT qq.parent_id, qq.name, 0 AS gotov, 0 AS data1, 1 AS data2, qq.opis, qq.commstat AS stat, qq.svernut, spis_plan._id  AS idplan," +
                    "'$questId' AS quest_id, qq._id AS quest_key_id FROM spis_plan, QDB.spis_stap_plan_quest AS qq WHERE spis_plan.quest_id = '$questId' AND spis_plan.quest_key_id = qq.idplan ORDER BY quest_key_id;",
            0
        )
        /**
         * Во время запроса ниже (UPDATE spis_stap_plan) срабатывают триггеры на обновление родителя, т.к. при добавлении квеста это не актуально,
         * то в целом на выполнение этой операции их можно отключить. Хотя если время выполнения в целом будет не критичным, то может и не стоит
         * с этим заморачиваться.
         *
         * P.S.: Запрос ниже должен выполняться с привлечением таблицы QDB.spis_stap_plan_quest, т.к. перезаписываемое
         * поле parent_id не может учавствовать в запросе, он в этом случае выполняется, но как то неправильно, если я правильно
         * понимаю, то это поле в запросе участвует со значением NULL. Также необходимо использовать конструкцию spis_stap_plan.quest_key_id,
         * чтобы обратиться к обновляемой таблице, а вспомогательные таблицы использовать с псевдонимами, до конца не понял как это работает,
         * но иначе она похоже путает это поле с полями из вспомогательных, во всяком случае если там есть одноименные.
         * */
        driver.execute(
            null,
            "UPDATE spis_stap_plan SET parent_id = (SELECT cc._id FROM spis_stap_plan AS cc, QDB.spis_stap_plan_quest AS aa WHERE cc.quest_id = '$questId' AND " +
                    "cc.quest_key_id = aa.parent_id AND aa._id = spis_stap_plan.quest_key_id) " +
                    "WHERE quest_id = '$questId' AND parent_id <> -1;",
            0
        )

    }

    private fun TransactionWithoutReturn.loadDialogAndOtvet(
        questId: Long
    ) {
        driver.execute(
            null,
            "INSERT INTO spis_dialog_quest( quest_id, key_id, name, maintext, govorun_name, govorun_id) " +
                    "SELECT '$questId' AS quest_id, dial._id AS key_id, dial.name, dial.maintext, IFNULL(gov.name,'') AS govorun_name, dial.govorun_id " +
                    "FROM QDB.spis_dialog AS dial LEFT JOIN QDB.spis_govorun AS gov ON dial.govorun_id = gov._id;",
            0
        )
        driver.execute(
            null,
            "INSERT INTO spis_otvet_dialog_quest( quest_id, key_id, dialog_id, text, order_number) " +
                    "SELECT '$questId' AS quest_id, _id AS key_id, dialog_id, text, order_number " +
                    "FROM QDB.spis_otvet_dialog;",
            0
        )
        driver.execute(
            null,
            "INSERT INTO spis_govorun_quest( quest_id, key_id, name, opis, image_file) " +
                    "SELECT '$questId' AS quest_id, _id AS key_id, name, opis, CAST('Quest_${questId}_gid_' || CAST(_id AS TEXT) || '.jpg' AS TEXT) AS image_file " +
                    "FROM QDB.spis_govorun;",
            0
        )
    }

    private fun TransactionWithoutReturn.loadCommonTrigger(
        questId: Long
    ) {
        val strQuerySelect = """
                  SELECT '$questId' AS quest_id, comtr.parent_type_element, 
                      comtr.parent_element_id,
                      comtr.type_trig_id,
                      comtr.child_id,
                      comtr.child_name,
                      comtr.act_code
                      FROM QDB.common_trigger AS comtr;
                  """.trimMargin()
        driver.execute(
            null,
            "INSERT INTO quest_common_trigger (quest_id, parent_type_element, parent_element_id, type_trig_id, child_id, child_name, act_code) $strQuerySelect",
            0
        )
    }

    private fun TransactionWithoutReturn.copyGovorunFiles(
        questId: Long,
        dirQuest: String,
        dirLoadedFiles: String
    ) {
        val sqlCursor = driver.executeQuery(null, "SELECT * FROM QDB.spis_govorun;", 0, null);
        while (sqlCursor.next()) {
            var image_file = sqlCursor.getString(3) ?: ""
            var id = sqlCursor.getLong(0) ?: ""
            if (image_file != "") {
                val ava = FileMP()
                ava.openFileInput(getPathWithSeparator(listOf(dirQuest, "${image_file}.jpg")))
                ava.getFileStream()?.let { byteArray ->
                    val loadAva = FileMP()
                    loadAva.openFileOutput(
                        getPathWithSeparator(
                            listOf(
                                dirLoadedFiles,
                                "Quest_${questId}",
                                "Quest_${questId}_gid_${id}.jpg"
                            )
                        )
                    )
                    loadAva.writeFile(byteArray)
                    loadAva.closeFile()
                }
                ava.closeFile()
            }
        }
    }

    private fun TransactionWithoutReturn.copyIconTreeSkillFiles(
        questId: Long,
        dirQuest: String,
        dirIconFiles: String
    ) {
        val sqlCursor =
            driver.executeQuery(null, "SELECT * FROM spis_icon_node_tree_skills WHERE quest_id = '$questId';", 0, null);
        while (sqlCursor.next()) {
            val extension = sqlCursor.getString(1) ?: "jpg"
            val key_id = sqlCursor.getLong(4) ?: -1L
            val id = sqlCursor.getLong(0) ?: -1L
            if (id != -1L) {
                val iconFile = FileMP()
                iconFile.openFileInput(getPathWithSeparator(listOf(dirQuest, "icon_${key_id}.$extension")))
                iconFile.getFileStream()?.let { byteArray ->
                    val saveIcon = FileMP()
                    saveIcon.openFileOutput(
                        getPathWithSeparator(
                            listOf(
                                dirIconFiles,
                                "icon_${id}.$extension"
                            )
                        )
                    )
                    saveIcon.writeFile(byteArray)
                    saveIcon.closeFile()
                }
                iconFile.closeFile()
            }
        }
    }

    fun loadQuest(
        path: String,
        questId: Long?,
        dirQuest: String,
        dirLoadedFiles: String,
        dirIconFiles: String
    ): Long? {
        var qId = questId ?: 0L
        db.transaction {
            driver.execute(
                null,
                "attach database '$path' AS QDB; ",
                0
            )
            val datetime = DateTimeTz.nowLocal().minusTime().localUnix()
            questId?.let {
                driver.execute(
                    -64654056,
                    "INSERT INTO spis_quest(_id,name, dateopen,complete)  " +
                            "VALUES ($it, (SELECT stringparam FROM QDB.mainparam WHERE name = 'name'), $datetime,0);",
                    0
                )
            } ?: run {
                driver.execute(
                    -64654056,
                    "INSERT INTO spis_quest(name, dateopen, complete)  " +
                            "VALUES ((SELECT stringparam FROM QDB.mainparam WHERE name = 'name'), $datetime, 0);",
                    0
                )
                val sqlCursor = driver.executeQuery(null, "SELECT last_insert_rowid();", 0, null);
                if (sqlCursor.next()) {
                    qId = sqlCursor.getLong(0) ?: -1L
                }
            }
            driver.execute(
                null,
                "INSERT INTO quest_mainparam(quest_id, name, intparam, stringparam) " +
                        "SELECT $qId AS quest_id, name, intparam, stringparam FROM QDB.mainparam WHERE name <> 'name';",
                0
            )
            notifyQueries(-64654056) { spisQuery.spisQuest?.let { mutableListOf(it) } ?: copyOnWriteList() }

            if (qId != 0L || qId != -1L) {

                loadPlanAndStap(qId)
                /**
                 * Кусок со вставкой и удалением ниже - костыль для оповещения всех слушателей по списку проектов.
                 * Я решил, что чем собирать и следить за всеми этими слушателями вручную, проще спровоцировать на это
                 * встроенную систему. Её также было бы неплохо запускать без триггеров, возможно стоит сделать функцию,
                 * которая будет внутри себя выполнять запросы перед этим отключив триггеры, а после включив.
                 * */
                db.spisPlanQueries.insertOrReplacePlan(
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
                    db.spisPlanQueries.deletePlan(sqlCursor.getLong(0) ?: -1L, TypeBindElementForSchetPlan.PLAN.id)
                }

                loadDialogAndOtvet(qId)
                loadCommonTrigger(qId)
                loadSkillTree(qId)
            }

        }
        db.transaction {
            copyGovorunFiles(qId, dirQuest, dirLoadedFiles)
            copyIconTreeSkillFiles(qId, dirQuest, dirIconFiles)
        }
        driver.execute(
            null,
            "detach database QDB;",
            0
        )
        return if (qId != 0L || qId != -1L) qId else null
    }

}
