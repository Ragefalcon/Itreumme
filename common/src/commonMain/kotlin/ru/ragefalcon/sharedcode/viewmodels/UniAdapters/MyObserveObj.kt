package ru.ragefalcon.sharedcode.viewmodels.UniAdapters

import kotlinx.coroutines.*

/**
 * Класс MyObserveObj предназначен для прослойки в переходе с наблюдаемых тем или иным образом объектов из
 * общего модуля к прослушиваемым типам во внешних модулях. От слушателя базы данных к LifeDate, State и т.д.
 * При этом во внешнем модуле должна быть написана принимающая прослойка. Возможно эти принимающие прослойки
 * в будущем имеет смысл переместить тоже в общий модуль используя мультиплатформенную реализацию.
 *
 * В listener передается лямбда, которая получает на вход функцию от типа Т, эта лямбда по сути вызовется один раз
 * и туда при первом обращении будет передан setSpis. А setSpis содержит список функций от Т, которые в свою очередь будут передаваться и включаться
 * в общий список после, через getObserve от внешних наблюдателей. Так что верхня лямбда должна передавать функцию
 * setSpis к внешнему слушателю.
 *
 * Т.к. наблюдатели не вычищаются, то создавать этих наблюдателей имеет смысл в той же области где будет лежать
 * объект наблюдения, т.е. во вьюмодели внешнего модуля.
 * */
class MyObserveObj<T>(private val listener: ((T) -> Unit) -> Unit) {

    private var objValue: T? = null
    private var count = 0
    private var objValueObsFun: MutableList<Pair<Int, (T?) -> Unit>> = mutableListOf(Pair(0, {}))
    private val firstStart by lazy {
        listener(::setSpis)
        true
    }

    fun getValue(): T? {
        firstStart
        return objValue
    }

    private fun setSpis(value: T) {
        objValue = value
        for (ff in objValueObsFun) {
            ff.second(objValue)
        }
    }

    /**
     * Данная реализация не учитывает особенностей жизненного цикла на андроиде, так что его лучше использовать осторожно только внутри ВьюМоделей.
     * */
    fun getObserve(updF: (T?) -> Unit): Int {
        if (firstStart) {
            count++
            objValueObsFun.add(Pair(count, updF))
            for (ff in objValueObsFun) {
                ff.second(objValue)
            }
        }
        return count
    }

    fun delObserver(number: Int) {
        objValueObsFun.remove(objValueObsFun.find { it.first == number })
    }

}


class CommonObserveObj<T> {
    private var objValue: T? = null
    private var listener: (T) -> Unit = {}

    fun getValue(): T? = objValue

    fun setValue(newValue: T) {
        objValue = newValue
        listener(newValue)
    }

    private fun setListener(newListener: ((T) -> Unit)) {
        listener = newListener
    }

    fun getMyObserveObj() = MyObserveObj<T> { ff ->
        setListener(ff)
    }

}

class CommonComplexObserveObj<T> {
    private var objValue: (() -> T)? = null
    private var listener: (T) -> Unit = {}

    fun getValue(): T? = objValue?.invoke()

    fun setValueFun(newValueFun: () -> T) {
        objValue = newValueFun
        listener(newValueFun())
    }

    private var canUpdate = true

    fun update() {
        /**
         * Вроде бы работает, хотя полноценно протестировать сложно. Но один раз она вывела у меня эту надпись и результат был правильный.
         * Смысл в том, что когда значение формируется из несколько списков, то в редких случаях (но при старте программе более вероятно),
         * может произойти одновременное обновление составных частей, т.к. обновление всех отдельных списков из БД происходит в отдельных
         * потоках, и похоже на время работы потока в каждом из них замораживаются значения всех используемых переменных. При одновременном
         * обновлении двух списков в потоке каждого из них один обновится, а второй останется пустой и если составной объект рассчитывается
         * внутри таких потоков, то при одновременно обновлении он посчитается неправильно.
         * Поэтому здесь устанавливается не очень красивый цикл с паузой, и были сомнения, что переменная ключ не замораживается
         * и изменится по окончании первого потока, я не уверен, что в деталях понимаю механику, но вроде работает.
         * **/
        CoroutineScope(Dispatchers.Unconfined).launch {
            while (canUpdate.not()) {
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println("!!!!!!!!!!!!!!!!!!!!!! while (canUpdate.not()) !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                delay(100)
            }
            canUpdate = false
            objValue?.let { listener(it.invoke()) }
            canUpdate = true
        }
    }

    private fun setListener(newListener: ((T) -> Unit)) {
        listener = newListener
    }

    fun getMyObserveObj() = MyObserveObj<T> { ff ->
        setListener(ff)
    }

}


fun <T : Any> getMyObsObj(adapter: UniConvertQueryAdapter<*, T>) = MyObserveObj<List<T>> { ff ->
    adapter.updateFunc {
        ff(it)
    }
}

fun <T : Any> getMyObsObjOneValue(adapter: UniConvertQueryAdapter<*, T>) = MyObserveObj<T> { ff ->
    adapter.updateFunc {
        it.firstOrNull()?.let(ff)
    }
}

fun <T : Any> getMyObsObj(adapter: UniQueryAdapter<T>) = MyObserveObj<List<T>> { ff ->
    adapter.updateFunc {
        ff(it)
    }
}

fun <T : Any> getMyObsObjOneValue(adapter: UniQueryAdapter<T>) = MyObserveObj<T> { ff ->
    adapter.updateFunc {
        it.firstOrNull()?.let(ff)
    }
}

fun <T : Any> getMyObsObj(adapter: UniConvertQueryPeriodAdapter<*, T>) = MyObserveObj<List<T>> { ff ->
    adapter.updateFunc {
        ff(it)
    }
}

fun <T : Any> getMyObsObjOneValue(adapter: UniConvertQueryPeriodAdapter<*, T>) = MyObserveObj<T> { ff ->
    adapter.updateFunc {
        it.firstOrNull()?.let(ff)
    }
}

fun <T : Any> getMyObsObj(adapter: UniQueryPeriodAdapter<T>) = MyObserveObj<List<T>> { ff ->
    adapter.updateFunc {
        ff(it)
    }
}

fun <T : Any> getMyObsObjOneValue(adapter: UniQueryPeriodAdapter<T>) = MyObserveObj<T> { ff ->
    adapter.updateFunc {
        it.firstOrNull()?.let(ff)
    }
}
