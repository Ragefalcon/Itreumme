package ru.ragefalcon.sharedcode.viewmodels.UniAdapters

import com.squareup.sqldelight.Query
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import ru.ragefalcon.sharedcode.extensions.startMy
import kotlin.coroutines.CoroutineContext

class UniQueryAdapter<T : Any>(private var query: Query<T>? = null) {

    /** Переменная для хранения функций обновления данных которые задаются в каждой среде отдельно */
    private var updSpisF: ((List<T>) -> Unit)? = null

    private var qSeries = 0

    fun setListner(quer: Query<T>, upd: (List<T>) -> Unit) {
        updSpisF = upd
        query = quer
        update()
    }

    fun updateQuery(quer: Query<T>) {
        query = quer
        update()
    }

    fun updateFunc(upd: (List<T>) -> Unit) {
        updSpisF = upd
        update()
    }

    fun getMyObserveObj() = getMyObsObj(this)
    fun getMyObsObjOneValue() = getMyObsObjOneValue(this)

    private var ctx: CoroutineContext? = null
    private fun update() {
        qSeries++
        ctx?.cancel()
        /**
         * Нужно рассмотреть ситуацию когда сюда может падать запрос сразу из нескольких потоков.
         * Может ли быть конфликт выполнения в этом месте.
         * */
        ctx = Job()
        ctx?.let { coroutineContext ->
            query?.startMy(coroutineContext) {
                updSpisF?.invoke(it)
            }
        }
    }
}