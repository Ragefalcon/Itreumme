package ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers

import com.soywiz.klock.DateTimeTz
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.ItemInterfaceSetting
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet

class AddEditStyleHandler(private val mdb: Database, private val styleSpis: StyleVMspis) {
    fun addColorToLibrary(
        idSet: Long,
        color: String
    ) {
        mdb.colorLibraryQueries.insert(
            set_id = idSet,
            color = color
        )
    }

    fun delColorFromLibrary(
        id: Long
    ) {
        mdb.colorLibraryQueries.delete(id = id)
    }

    fun addSetColorLibrary(
        name: String
    ) {
        mdb.spisSetColorLibraryQueries.insert(
            name = name
        )
    }

    fun updSetColorLibrary(
        id: Long,
        name: String
    ) {
        mdb.spisSetColorLibraryQueries.update(
            id = id,
            name = name
        )
    }

    fun delSetColorLibrary(
        id: Long
    ) {
        mdb.spisSetColorLibraryQueries.delete(id = id)
    }

    fun addSaveSetStyleFull(
        name: String
    ) {
        mdb.spisSaveSetStyleQueries.transactionWithResult<Long> {
            mdb.spisSaveSetStyleQueries.insert(
                name = name,
                type = TypeSaveStyleSet.FULL.id
            )
            return@transactionWithResult mdb.commonFunQueries.lastInsertRowId().executeAsOne()
        }.let {
            if (it > 0) mdb.saveSetStyleSettingsQueries.saveStyle(it)
        }
    }

    fun addSaveSetStyleCommon(
        name: String,
        type: TypeSaveStyleSet,
        values: List<ItemInterfaceSetting>
    ) {
        mdb.spisSaveSetStyleQueries.transactionWithResult<Long> {
            mdb.spisSaveSetStyleQueries.insert(
                name = name,
                type = type.id
            )
            return@transactionWithResult mdb.commonFunQueries.lastInsertRowId().executeAsOne()
        }.let { set_id ->
            if (set_id > 0) mdb.saveSetStyleSettingsQueries.transaction {
                values.forEach { item ->
                    mdb.saveSetStyleSettingsQueries.insert(
                        set_id = set_id,
                        codename = item.code_name,
                        intparam = item.long,
                        double = item.double,
                        stringparam = item.string
                    )
                }
            }
        }
    }

    fun updSaveSetStyle(
        id: Long,
        name: String
    ) {
        mdb.spisSaveSetStyleQueries.update(
            id = id,
            name = name
        )
    }

    // Как выяснилось запрос ниже(loadFull) очень ресурсоемкий, а в моему случае когда количество настроек сильно разрослось это стало вообще катастрофическим.
/*
    fun loadSaveSetStyleFull(
        id: Long
    ) {
        if (mdb.spisSaveSetStyleQueries.transactionWithResult {
                mdb.spisSaveSetStyleQueries.loadFull(id = id)
                true
            }) styleSpis.styleSett.startInit()
    }
*/

    fun loadSaveSetStyleCommon(
        set_id: Long,
        code_name_razdel: String
    ) {
        val timeNow3 = DateTimeTz.nowLocal().localUnix()
//        println("timeNow3 = ${timeNow3}, id = $set_id, code = $code_name_razdel")
//        mdb.spisSaveSetStyleQueries.selectShablonForApply(code_name_razdel = code_name_razdel, set_id = set_id).executeAsList().let {
        mdb.spisSaveSetStyleQueries.selectShablonForApply(set_id = set_id, code_name_razdel = code_name_razdel).executeAsList().let { listShab ->
//            println("timeNow3 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
            if (mdb.spisSaveSetStyleQueries.transactionWithResult {
                                mdb.spisSaveSetStyleQueries.loadShablon(set_id = set_id, code_name_razdel = code_name_razdel, delarray = listShab)
                            true
                    }) {
                mdb.spisSaveSetStyleQueries.selectLoadCommon(set_id = set_id, code_name_razdel = code_name_razdel)
                .executeAsList().let {
//                        println("timeNow3 2 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
                        styleSpis.styleSett.refreshValueFromBase(it)
//                        println("timeNow3 3 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
                    }
                    }
        }

/*
        val timeNow3 = DateTimeTz.nowLocal().localUnix()
        println("timeNow3 = ${timeNow3}, id = $set_id, code = $code_name_razdel")
//        mdb.spisSaveSetStyleQueries.selectShablonForApply(code_name_razdel = code_name_razdel, set_id = set_id).executeAsList().let {
        mdb.spisSaveSetStyleQueries.selectLoadCommon(set_id = set_id, code_name_razdel = code_name_razdel).executeAsList().let { listShab ->
            println("timeNow3 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
            if (mdb.spisSaveSetStyleQueries.transactionWithResult {
                            listShab.forEach {
                                mdb.styleSettingsQueries.update(intparam = it.intparam, doubleparam = it.doubleparam, stringparam = it.stringparam, codename = it.codename)
                            }
                            true
                    }) {
                        println("timeNow3 2 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
                        styleSpis.styleSett.refreshValueFromBase(listShab)
                        println("timeNow3 3 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
                    }
        }

*/

//        val timeNow3 = DateTimeTz.nowLocal().localUnix()
//        println("timeNow3 = ${timeNow3}, id = $set_id, code = $code_name_razdel")
//        if (mdb.spisSaveSetStyleQueries.transactionWithResult {
//                mdb.spisSaveSetStyleQueries.loadCommon(set_id = set_id, code_name_razdel)
//                true
//            }) {
//            println("timeNow3 1 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
//            mdb.spisSaveSetStyleQueries.selectLoadCommon(set_id = set_id, code_name_razdel = code_name_razdel)
//                .executeAsList().map {
//                ItemInterfaceSetting(it.codename, it.intparam, it.doubleparam, it.stringparam)
//            }.let {
//            println("timeNow3 2 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
//                    styleSpis.styleSett.refreshValueFromBase(it)
//            println("timeNow3 3 = ${DateTimeTz.nowLocal().localUnix() - timeNow3}")
//            }
//        }
    }

    fun delSaveSetStyle(
        id: Long
    ) {
        mdb.spisSaveSetStyleQueries.delete(id = id)
    }

    fun clearAllToDefault() {
        styleSpis.styleSett.toDefault()
    }

}