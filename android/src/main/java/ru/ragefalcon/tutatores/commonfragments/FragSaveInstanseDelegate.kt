package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.SparseArray
import androidx.fragment.app.Fragment
import ru.ragefalcon.sharedcode.models.data.UniItem
import kotlin.reflect.KProperty


/**
 * https://habr.com/ru/post/336994/
 * */
abstract class FragSaveInstanseDelegate(requestKey: String = "") : Fragment() {

    abstract class InstanceStateProvider<T>(
        protected val savable: Bundle,
        protected val plusSet: ((T?, T?) -> Unit)? = null,
        protected val startValue: T? = null
    ) {

        protected var cache: T? = startValue

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (savable.containsKey(property.name)) {
                val tmpCache = savable.get(property.name) as T
                if (tmpCache != cache) cache = tmpCache
            }
            plusSet?.invoke(cache, value)
            cache = value
            putValueInBundle(savable, property.name, value)
        }
    }


    class Nullable<T>(
        savable: Bundle,
        plusSet: ((T?, T?) -> Unit)? = null,
        startValue: T? = null
    ) :
        FragSaveInstanseDelegate.InstanceStateProvider<T>(savable, plusSet, startValue) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            if (cache != null) return cache
            if (!savable.containsKey(property.name)) return null
            return savable.get(property.name) as T
        }
    }

    class NullableProvider<T>(
        protected val savable: Bundle,
        protected val plusSet: ((T?, T?) -> Unit)? = null,
        protected val startValue: T? = null
    ) {
        operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): Nullable<T> {
            putValueInBundle(savable, property.name, startValue)
            return Nullable<T>(savable, plusSet, startValue)
        }
    }

    class NotNull<T>(
        savable: Bundle,
        private val defaultValue: T,
        plusSet: ((T?, T?) -> Unit)? = null,
        startValue: T? = null
    ) :
        FragSaveInstanseDelegate.InstanceStateProvider<T>(savable, plusSet, startValue) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return cache ?: savable.get(property.name) as T ?: defaultValue
        }
    }

    class NotNullProvider<T>(
        protected val savable: Bundle,
        private val defaultValue: T,
        protected val plusSet: ((T?, T?) -> Unit)? = null,
        protected val startValue: T? = null
    ) {
        operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): NotNull<T> {
            putValueInBundle(savable, property.name, startValue)
            return NotNull<T>(savable, defaultValue, plusSet, startValue)
        }
    }

    protected fun <T> instanceState(startValue: T? = null, plusSet: ((T?, T?) -> Unit)? = null) =
        NullableProvider<T>(savable, plusSet, startValue)

    protected fun <T> instanceStateDef(defaultValue: T, startValue: T? = null, plusSet: ((T?, T?) -> Unit)? = null) =
        NotNullProvider(savable, defaultValue, plusSet, startValue)

    private val savable = Bundle()

    private var requestKey: String by instanceStateDef("", requestKey)

    private var resultBundle: Bundle by instanceStateDef(Bundle())
    private var resultUniItem: SparseArray<UniItem> by instanceStateDef(SparseArray<UniItem>()) //= SparseArray<UniItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            bundle.getBundle("_state")?.let {
                savable.putAll(it)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle("_state", savable)
        super.onSaveInstanceState(outState)
    }
}


fun <T> putValueInBundle(savable: Bundle, key: String, value: T?) {
    value?.let {
        when (it) {
            is Int -> savable.putInt(key, it)
            is Long -> savable.putLong(key, it)
            is Boolean -> savable.putBoolean(key, it)
            is Float -> savable.putFloat(key, it)
            is String -> savable.putString(key, it)
            is Bundle -> savable.putBundle(key, it)
            is Parcelable -> savable.putParcelable(key, it)
            is ArrayList<*> -> savable.putParcelableArrayList(key, it as? ArrayList<Parcelable> ?: arrayListOf())
            is LongArray -> savable.putLongArray(key, it)
            is Enum<*> -> savable.putString(key, it.name)
            /**  ?????  **/
        }
    } ?: run {
        savable.remove(key)
    }
}
