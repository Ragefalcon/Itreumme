package ru.ragefalcon.tutatores.ui.viewmodels

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.ragefalcon.sharedcode.viewmodels.UniAdapters.MyObserveObj

class MyLiveDataObj<T : Any>(private val observeObj: MyObserveObj<T>) {

    private val ldObjValue = MutableLiveData<T>()
    private val firstSpisSchet by lazy {
        observeObj.getObserve {
            if (it!=null) {
                ldObjValue.value = it
            }
        }
    }

    fun getLiveData(): LiveData<T> {
        firstSpisSchet
        return ldObjValue
    }

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {
        firstSpisSchet
        println("MyLiveDataObj - observe1: ${ldObjValue}")
        println("MyLiveDataObj - observe2: ${ldObjValue.value}")
        println("MyLiveDataObj - observe3: ${owner}")
        ldObjValue.observe(owner, observer)
        println("MyLiveDataObj - observe4: End")
    }
}