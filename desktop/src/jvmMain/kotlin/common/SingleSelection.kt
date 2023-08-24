package common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ru.ragefalcon.sharedcode.models.data.Id_class

class SingleSelection {
    private var selectedMut: MutableState<Any?> = mutableStateOf(null)
    var selected: Any?
        get() = selectedMut.value
        set(value) {selectedMut.value = value}

    fun <T : Id_class> isActive(item: T): Boolean {
        selected?.let {
            if (it::class == item::class) {
                (selected as Id_class).let {
                    if (it.id_main == item.id_main) return true
                }
            }
        }
        return false
    }
    fun <T : Any> isActive(item: T): Boolean {
        selected?.let {
            if (it::class == item::class) {
                    if (it == item) return true
            }
        }
        return false
    }
}

class SingleSelectionType<T : Any>() {
    private var selectedMut: MutableState<T?> = mutableStateOf(null)
    var selected: T?
    get() = selectedMut.value
    set(value) {selectedMut.value = value}

    @Composable
    fun launchedEffect(listener: (T?) -> Unit){
        LaunchedEffect(selectedMut.value){
            listener(selectedMut.value)
        }
//        selectedMut.observe(listener)
    }

    fun <T : Any> isActive(item: T): Boolean {
        selected?.let {
            if (it::class == item::class) {
                if (it == item) return true
            }
        }
        return false
    }

    fun <T : Id_class> isActive(item: T): Boolean {
        selected?.let {
            if (it::class == item::class) {
                (selected as Id_class).let {
                    if (it.id_main == item.id_main) return true
                }
            }
        }
        return false
    }
}