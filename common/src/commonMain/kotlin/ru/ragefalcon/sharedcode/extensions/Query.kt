package ru.ragefalcon.sharedcode.extensions

//import kotlinx.coroutines.*
//import kotlinx.coroutines.*
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import ru.ragefalcon.sharedcode.source.disk.getCorDisp
import kotlin.coroutines.CoroutineContext

fun <T : Any> Query<T>.startMy(
    keyF: () -> Int = { 1 },
    ctxSave: (CoroutineContext) -> Unit,
    stopF: (() -> Unit)? = null,
    upf: (List<T>) -> Unit
) {
    val keyStop = keyF()
    CoroutineScope(getCorDisp()).async {
        ctxSave(coroutineContext)
        Result.success(
            this@startMy.asFlow().mapToList().collect { list ->
                if (keyF() != keyStop) {
                    stopF?.invoke()
                    coroutineContext.cancel()
                } else {
                    upf(list)
                }
            }
        )
    }
}

