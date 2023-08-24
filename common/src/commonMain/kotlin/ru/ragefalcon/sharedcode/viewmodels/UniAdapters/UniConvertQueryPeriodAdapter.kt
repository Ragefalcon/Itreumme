package ru.ragefalcon.sharedcode.viewmodels.UniAdapters

import com.squareup.sqldelight.Query
import kotlinx.coroutines.cancel
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.extensions.startMy
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.PeriodSelecter
import kotlin.coroutines.CoroutineContext

class UniConvertQueryPeriodAdapter<T: Any, K: Any> (private  val selPer: PeriodSelecter, private val adaptF:(T)->K){

    init {
        selPer.addUpdate(::update)
    }
    /** Переменная для хранения функций обновления данных которые задаются в каждой среде отдельно */
    private var updSpisF: ((List<K>) -> Unit)? = null
    private var query: ((Long,Long)-> Query<T>)? = null

    /**
     * Переменная для отслеживания количества запущенных слушателей по типовым запросам
     * с целью автоматического закрытия устаревших,
     * актуально для запросов у которых в процессе работы меняются параметры,
     * например, дата или номер счета
     * */
    private var qSeries = 0

    fun setListner(quer: (Long,Long)-> Query<T>, upd: (List<K>) -> Unit) {
        updSpisF = upd
        query=quer
        update()
    }
    fun updateFunQuery(quer: (Long,Long)-> Query<T>) {
        query=quer
        update()
    }
    fun updateFunc(upd: (List<K>) -> Unit) {
        updSpisF = upd
        update()
    }
    fun getMyObserveObj() = getMyObsObj(this)
    fun getMyObsObjOneValue() = getMyObsObjOneValue(this)

    private var ctx: CoroutineContext? = null
    private fun update() {
        qSeries++
        ctx?.cancel()
        query?.let{ it(selPer.dateBegin.localUnix(),selPer.dateEnd.localUnix()) }?.startMy ({qSeries}, { ctx = it}){
            updSpisF?.invoke(it.map { type ->
                adaptF(type)
            })
        }

    }
}