package ru.ragefalcon.sharedcode.viewmodels.UniAdapters

import com.squareup.sqldelight.Query
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.startMy
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PeriodSelecter
import kotlin.coroutines.CoroutineContext

open class UniQueryPeriodAdapter<T : Any>(private val selPer: PeriodSelecter) {

    init {
        selPer.addUpdate(::update)
    }

    /** Переменная для хранения функций обновления данных которые задаются в каждой среде отдельно */
    private var updSpisF: ((List<T>) -> Unit)? = null
    private var query: ((Long, Long) -> Query<T>)? = null

    private var qSeries = 0

    fun setListner(quer: (Long, Long) -> Query<T>, upd: (List<T>) -> Unit) {
        updSpisF = upd
        query = quer
        update()
    }

    fun updateFunQuery(quer: (Long, Long) -> Query<T>) {
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
        ctx = Job()
        ctx?.let { coroutineContext ->
            query?.let { it(selPer.dateBegin.localUnix(), selPer.dateEnd.localUnix()) }
                ?.startMy(coroutineContext) {
                    updSpisF?.invoke(it)
                }
        }
    }
}