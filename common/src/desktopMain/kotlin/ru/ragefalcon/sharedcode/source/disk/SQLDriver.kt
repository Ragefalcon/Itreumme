package ru.ragefalcon.sharedcode.source.disk

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.DatabaseStyle
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.models.data.SverOpis
import ru.ragefalcon.sharedcode.quest.DatabaseQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.BooleanItrComm
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.MyTypeCorner
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj
import java.util.*


actual class DbArgs(
    val path: String = "/database.db"
)

actual fun getPlatformName2(): String {
    return "Not yet implemented"
}

actual fun getSqlDriver(dbArgs: DbArgs): SqlDriver? {
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbArgs.path}",
        Properties(1).apply {
            put("recursive_triggers", "true")
        })
    try {
        Database.Schema.create(driver)
    } catch (e: Exception) {
    }
    return driver
}

actual fun getSqlQuestDriver(dbArgs: DbArgs): SqlDriver? {
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbArgs.path}",
        Properties(1).apply {
            put("recursive_triggers", "true")
        })
    try {
        DatabaseQuest.Schema.create(driver)
    } catch (e: Exception) {
    }
    return driver
}

actual fun getSqlStyleDriver(dbArgs: DbArgs): SqlDriver? {
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbArgs.path}",
        Properties(1).apply {
            put("recursive_triggers", "true")
        })
    try {
        DatabaseStyle.Schema.create(driver)
    } catch (e: Exception) {
    }
    return driver
}

actual fun getCorDisp(): CoroutineDispatcher {
    return Dispatchers.Default
}

actual fun checkUserVersionSqlDriver(db: SqlDriver?) {
    db?.execute(
        null,
        "PRAGMA user_version = ${Database.Schema.version};",
        0
    )
}

actual fun getUserVersionSqlDriver(db: SqlDriver): Int {
    val sqlCursor = db.executeQuery(null, "PRAGMA user_version;", 0, null);
    return sqlCursor.getString(0)?.toInt() ?: -1
}

actual class ItrCommObserveMutableObj<T> actual constructor(default: T, var change: (T) -> Unit) : MutableState<T> {
    private var lastValue: T = default
    private val mutState: MutableState<T> = mutableStateOf(default)

    actual fun innerUpdateValue(newValue: T) {
        if (mutState.value != newValue) {
            lastValue = newValue
            mutState.value = newValue
        }
    }

    override var value: T
        get() = mutState.value
        set(value) {
            if (lastValue != value) {
                mutState.value = value
                lastValue = value
                change(mutState.value)
            }
        }

    override fun component1(): T = mutState.value

    override fun component2(): (T) -> Unit = { value ->
        if (lastValue != value) {
            mutState.value = value
            lastValue = value
            change(mutState.value)
        }
    }
}

actual open class ItrCommObserveObj<T> actual constructor(observeObj: MyObserveObj<T>) : ItrCommObserveInt {
    private val ldObjValue: MutableState<T?> = mutableStateOf(observeObj.getValue())
    private val firstSpisSchet by lazy {
        observeObj.getObserve {
            ldObjValue.value = it
        }
    }

    fun getState(): MutableState<T?> {
        firstSpisSchet
        return ldObjValue
    }
}


actual open class ItrCommListObserveObj<T : Id_class> actual constructor(observeObj: MyObserveObj<List<T>>) :
    ItrCommObserveInt {

    private val ldObjValue: SnapshotStateList<T> = mutableStateListOf<T>().apply {
        addAll(observeObj.getValue() ?: listOf())
    }
    private val firstSpisSchet by lazy {
        observeObj.getObserve {
            if (it?.equals(ldObjValue)?.not() ?: true) {
                ldObjValue.clear()
                ldObjValue.addAll(it ?: listOf())
            }
        }
    }

    fun updateElem(old: T, new: T) {
        ldObjValue.indexOf(old).let {
            if (it >= 0) {
                ldObjValue.set(it, new)
            }
        }
    }

    fun <R : Comparable<R>> updateElem(old: T, new: T, sortBy: (T) -> R?) {
        ldObjValue.indexOf(old).let {
            if (it >= 0) {
                ldObjValue.set(it, new)
                ldObjValue.sortedBy(sortBy)
            }
        }
    }

    fun <R : Comparable<R>> sortBy(sortBy: (T) -> R?) {
        ldObjValue.sortedBy(sortBy)
    }

    fun getState(): SnapshotStateList<T> {
        firstSpisSchet
        return ldObjValue
    }
}


actual open class ItrCommListWithOpisObserveObj<T> actual constructor(
    observeObj: MyObserveObj<List<T>>,
    private val sverMap: MutableMap<Long, Boolean>
) :
    ItrCommListObserveObj<T>(observeObj) where T : Id_class, T : SverOpis<T> {

    fun sverOpisElem(elem: T, newSver: Boolean = elem.sver.not()) {
        updateElem(elem, elem.sver(newSver))
        sverMap.remove(elem.id_main.toLong())
        sverMap.put(elem.id_main.toLong(), elem.sver.not())
    }
}


private fun MyColorARGB.toColor(): Color {
    return Color(this.R, this.G, this.B, this.A)
}

fun BooleanItrComm.getState(): MutableState<Boolean?> = this.itrCOO.getState()

fun CommonInterfaceSetting.InterfaceSettingsBoolean.getValue(): Boolean = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsMyColor.getValue(): MyColorARGB = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsMyColorGradient.getValue(): List<MyColorARGB> = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsDoublePozitive.getValue(): Double = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsDouble.getValue(): Double = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsLong.getValue(): Long = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsFontWeight.getValue(): Long = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsAngle.getValue(): Long = this.itrObj.value
fun CommonInterfaceSetting.InterfaceSettingsTypeCorner.getValue(): MyTypeCorner = this.itrObj.value

fun CommonInterfaceSetting.InterfaceSettingsString.getValue(): String = this.itrObj.value

fun CommonInterfaceSetting.MySettings.TextStyleSetting.getValue(): TextStyle = TextStyle(
    color = this.TEXT_COLOR.getValue().toColor(),
    fontSize = this.FONT_SIZE.getValue().sp,
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight(this.FONT_WEIGHT.getValue().toInt() * 100),
    fontStyle = if (ITALIC.getValue()) FontStyle(1) else null,
    shadow = Shadow(
        this.SHADOW_COLOR.getValue().toColor(),
        Offset(
            this.SHADOW_OFFSET_X.getValue().toFloat(),
            this.SHADOW_OFFSET_Y.getValue().toFloat()
        ),
        SHADOW_BLUR_RADIUS.getValue().toFloat()
    )
)

fun CommonInterfaceSetting.MySettings.ShadowStyleSetting.getValue(): Shadow = Shadow(
    this.SHADOW_COLOR.getValue().toColor(),
    Offset(
        this.SHADOW_OFFSET_X.getValue().toFloat(),
        this.SHADOW_OFFSET_Y.getValue().toFloat()
    ),
    SHADOW_BLUR_RADIUS.getValue().toFloat()
)