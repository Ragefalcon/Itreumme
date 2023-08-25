package ru.ragefalcon.sharedcode.viewmodels.UniAdapters


import com.squareup.sqldelight.Query
import kotlinx.coroutines.cancel
import ru.ragefalcon.sharedcode.extensions.startMy
import kotlin.coroutines.CoroutineContext

open class UniConvertQueryAdapter<T: Any, K: Any> (private var query: Query<T>? = null, private val adaptF:(T)->K){

    /** Переменная для хранения функций обновления данных которые задаются в каждой среде отдельно */
    private var updSpisF: ((List<K>) -> Unit)? = null

    private var qSeries = 0

    fun setListner(quer: Query<T>, upd: (List<K>) -> Unit) {
        updSpisF = upd
        query=quer
        update()
    }
    fun updateQuery(quer: Query<T>) {
        query=quer
        update()
    }
    fun updateFunc(upd: (List<K>) -> Unit) {
        updSpisF = upd
        update()
    }

    /**
     * Повторный вызов функций ниже(getMyObserveObj или getMyObsObjOneValue),
     * затрет всех слушателей которые были назначены на объект предыдущего вызова. Поэтому для каждого запроса
     * такой объект должен создаваться только один.
     * */
    fun getMyObserveObj() = getMyObsObj(this)
    fun getMyObsObjOneValue() = getMyObsObjOneValue(this)

    fun oneZapros(rezFF: (List<K>) -> Unit){
        query?.executeAsList()?.let {
            rezFF(it.map { type ->
                adaptF(type)
            })
        }
    }

    private var ctx: CoroutineContext? = null
    private fun update() {
        qSeries++
        ctx?.cancel()
        query?.startMy ({qSeries}, { ctx = it}){
            updSpisF?.invoke(it.map { type ->
                adaptF(type)
            })
        }

    }
}