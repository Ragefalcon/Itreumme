package ru.ragefalcon.sharedcode.viewmodels.UniAdapters

import com.squareup.sqldelight.Query

class SpisIdNameAdapter<T : Any>(private val query1: Query<T>, private val adaptF: (T) -> Pair<String, String>) :
    UniConvertQueryAdapter<T, Pair<String, String>>(query1, adaptF) {
}