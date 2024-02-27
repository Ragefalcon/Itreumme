package ru.ragefalcon.sharedcode.extensions

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ragefalcon.sharedcode.source.disk.getCorDisp
import kotlin.coroutines.CoroutineContext

fun <T : Any> Query<T>.startMy(
    ctxSave: CoroutineContext,
    upf: (List<T>) -> Unit
) {
    this@startMy
        .asFlow()
        .mapToList()
        .flowOn(Dispatchers.Default)
        .onEach { list ->
            upf(list)
        }
        .launchIn(CoroutineScope(getCorDisp() + ctxSave))
}

