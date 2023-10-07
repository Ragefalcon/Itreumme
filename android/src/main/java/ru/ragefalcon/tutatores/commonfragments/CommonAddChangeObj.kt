package ru.ragefalcon.tutatores.commonfragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import java.lang.ref.WeakReference

class CommonAddChangeObj<T : Id_class> (
    private val fragment: WeakReference<Fragment>,
    val callbackKey: String,
    listenerAdd: (() -> Unit)? = null,
    listenerChange: ((item: T) -> Unit)? = null
) {

    init {
        listenerAdd?.let { listener ->
            fragment.get()?.let { FragAddChangeDialHelper.setRezListenerAdd(it, "${callbackKey}_add", listener) }
        }
        listenerChange?.let { listener ->
            fragment.get()?.let { FragAddChangeDialHelper.setRezListenerChange(it, "${callbackKey}_change", listener) }
        }
    }

    fun showDial(
        dial: (String)->FragAddChangeDialHelper<T , *>,
//        manager: FragmentManager? = fragment?.get()?.getSFM(),
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.get()?.let {  it.showAddChangeFragDial(dial(callbackKey), it.getSFM(), callbackKey, bound) }
    }

}