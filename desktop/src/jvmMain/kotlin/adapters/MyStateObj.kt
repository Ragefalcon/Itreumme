package adapters

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj

class MyStateObj<T>(private val observeObj: MyObserveObj<T>) {

    private val ldObjValue: MutableState <T?> = mutableStateOf(observeObj.getValue())
    private val firstSpisSchet by lazy {
        observeObj.getObserve {

//            println("serveObj.getObs: ${it}")

            ldObjValue.value = it
        }
    }

//    init {
//        firstSpisSchet
//    }

    fun getState(): MutableState<T?> {
        firstSpisSchet
        return ldObjValue
    }

}