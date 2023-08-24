package ru.ragefalcon.tutatores.commonfragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

class CommonAddChangeObj<T : Id_class> (
    private val fragment: Fragment,
    val callbackKey: String,
    listenerAdd: (() -> Unit)? = null,
    listenerChange: ((item: T) -> Unit)? = null
) {

    init {
        listenerAdd?.let { listener ->
            FragAddChangeDialHelper.setRezListenerAdd(fragment, "${callbackKey}_add", listener)
        }
        listenerChange?.let { listener ->
            FragAddChangeDialHelper.setRezListenerChange(fragment, "${callbackKey}_change", listener)
        }
    }

    fun showDial(
        dial: (String)->FragAddChangeDialHelper<T , *>,
        manager: FragmentManager = fragment.getSFM(),
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showAddChangeFragDial(dial(callbackKey), manager, callbackKey, bound)
    }

}