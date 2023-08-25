package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener


abstract class FragAddChangeDialHelper<T : Id_class, K : ViewBinding>(
    inflateBF: (LayoutInflater, ViewGroup?, Boolean) -> K,
    itemThis: T? = null,
    callback_Key: String? = "callMyFragDial"
) :
    BaseFragmentVM<K>(inflateBF) {

    var callbk_Key: String? by instanceState(callback_Key)

    var item: T?
            by instanceState(itemThis)
            { cache, value ->
                change = value != null
            }

    abstract fun addNote()
    abstract fun changeNote()
    protected fun setRezAddNote() {
        callbk_Key?.let {
            getSFM().setFragmentResult("${it}_add", bundleOf())
        }
    }

    protected fun setRezChangeNote(item: T) {
        callbk_Key?.let {
            getSFM().setFragmentResult("${it}_change", bundleOf("item" to item))
        }
    }

    private var changeLD = MutableLiveData<Boolean>().apply {
        value = itemThis != null
    }

    var change: Boolean = itemThis != null
        set(value) {
            field = value
            changeLD.value = value
        }

    fun changeObserve(owner: LifecycleOwner, listener: (Boolean) -> Unit) {
        changeLD.observe(owner, listener)
    }

    init {
    }

    var bb: String? by instanceState()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        change = item != null
    }

    companion object {
        fun <T : Id_class> setRezListenerChange(
            fragment: Fragment,
            requestKey: String,
            listener: ((item: T) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<T>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }

        fun setRezListenerAdd(
            fragment: Fragment,
            requestKey: String,
            listener: (() -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                listener?.invoke()
            }
        }
    }

}

