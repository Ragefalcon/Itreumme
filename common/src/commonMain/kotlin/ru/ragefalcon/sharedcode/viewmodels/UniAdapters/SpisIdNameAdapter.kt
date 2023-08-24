package ru.ragefalcon.sharedcode.viewmodels.UniAdapters

import com.squareup.sqldelight.Query

class SpisIdNameAdapter<T: Any> (private val query1: Query<T>, private val adaptF:(T)->Pair<String, String>):
UniConvertQueryAdapter<T,Pair<String, String>>(query1, adaptF){

//    init {
//        updateQuery(query)
//    }
//    /** Переменная для хранения функций обновления данных которые задаются в каждой среде отдельно */
//    private var updSpisF: ((List<Pair<String, String>>) -> Unit)? = null
//
//    fun setListner(upd: (List<Pair<String, String>>) -> Unit) {
////        println("MyKotlin: setListenerSpisId")
//        updSpisF = upd
//        update()
//    }
//
//    private fun update() {
////        println("MyKotlin: setListenerSpisId before startMy")
//        query.startMy {
////            println("MyKotlin: setListenerSpisId startMy")
//            updSpisF?.invoke(it.map { type ->
//                adaptF(type)
//            })
//        }
//
//    }
}