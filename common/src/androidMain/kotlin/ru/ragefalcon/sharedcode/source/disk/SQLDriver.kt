package ru.ragefalcon.sharedcode.source.disk

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.DatabaseStyle
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.models.data.SverOpis
import ru.ragefalcon.sharedcode.quest.DatabaseQuest
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj


actual class DbArgs(
    var context: Context,
    var name: String = "test.db"
)

actual fun getPlatformName2(): String {
    return "Android platform"
}

actual fun getSqlDriver(dbArgs: DbArgs): SqlDriver? {
    /**
     * https://github.com/cashapp/sqldelight/issues/1241
     * https://github.com/cashapp/sqldelight/issues/2421
     * */
    val driver = AndroidSqliteDriver(
        schema = Database.Schema,
        context = dbArgs.context,
        name = dbArgs.name,
        callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                db.execSQL("PRAGMA recursive_triggers=ON;");
            }
        })
    return driver
}

actual fun getSqlQuestDriver(dbArgs: DbArgs): SqlDriver? {
    val driver = AndroidSqliteDriver(
        schema = DatabaseQuest.Schema,
        context = dbArgs.context,
        name = dbArgs.name,
        callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                db.execSQL("PRAGMA recursive_triggers=ON;");
            }
        })
    return driver
}

actual fun getSqlStyleDriver(dbArgs: DbArgs): SqlDriver? {
    val driver = AndroidSqliteDriver(
        schema = DatabaseStyle.Schema,
        context = dbArgs.context,
        name = dbArgs.name,
        callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                db.execSQL("PRAGMA recursive_triggers=ON;");
            }
        })
    return driver
}

actual fun getCorDisp(): CoroutineDispatcher {
    return Dispatchers.Main
}

actual fun checkUserVersionSqlDriver(db: SqlDriver?) {

}

actual fun getUserVersionSqlDriver(db: SqlDriver): Int {
    return -1
}

actual class ItrCommObserveMutableObj<T> actual constructor(default: T, var change: (T) -> Unit) {
    actual fun innerUpdateValue(newValue: T) {
    }

}

actual open class ItrCommObserveObj<T> actual constructor(observeObj: MyObserveObj<T>) : ItrCommObserveInt {
    private val ldObjValue = MutableLiveData<T>()
    private val firstSpisSchet by lazy {
        observeObj.getObserve {
            if (it != null) {
                ldObjValue.value = it
            }
        }
    }

    fun getLiveData(): LiveData<T> {
        firstSpisSchet
        return ldObjValue
    }

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {
        firstSpisSchet
        ldObjValue.observe(owner, observer)
    }
}

actual open class ItrCommListObserveObj<T : Id_class> actual constructor(observeObj: MyObserveObj<List<T>>) :
    ItrCommObserveInt {
    private val ldObjValue = MutableLiveData<List<T>>()
    private val firstSpisSchet by lazy {
        observeObj.getObserve {
            if (it != null) {
                ldObjValue.value = it
            }
        }
    }

    fun updateElem(old: T, new: T) {
    }

    fun <R : Comparable<R>> updateElem(old: T, new: T, sortBy: (T) -> R?) {
    }

    fun <R : Comparable<R>> sortBy(sortBy: (T) -> R?) {
    }

    fun getLiveData(): LiveData<List<T>> {
        firstSpisSchet
        return ldObjValue
    }

    fun observe(owner: LifecycleOwner, observer: (List<T>) -> Unit) {
        firstSpisSchet
        ldObjValue.observe(owner, observer)
    }
}

actual open class ItrCommListWithOpisObserveObj<T> actual constructor(
    observeObj: MyObserveObj<List<T>>,
    private val sverMap: MutableMap<Long, Boolean>
) : ItrCommListObserveObj<T>(observeObj) where T : Id_class, T : SverOpis<T> {
    fun sverOpisElem(elem: T, newSver: Boolean? = null) {
        updateElem(elem, newSver?.let { elem.sver(it) } ?: elem.sver())
        sverMap.remove(elem.id_main.toLong())
        sverMap.put(elem.id_main.toLong(), elem.sver.not())
    }

}