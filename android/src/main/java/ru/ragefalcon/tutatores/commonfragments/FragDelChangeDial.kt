package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.databinding.FragmentDelChangeDialogBinding
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener


class FragDelChangeDial(itemThis: Id_class? = null, callbackKey: String? = null, nabor: Int? = null) :
    MyFragmentForDialog<FragmentDelChangeDialogBinding>(FragmentDelChangeDialogBinding::inflate) { //var fchange: (() -> Unit)? = null, var fdel: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var nabor: Int by instanceStateDef(0,nabor) // 0 - del&change; 1 - only del; 2 - only change
    var item: Id_class? by instanceState(itemThis)
    var callback_Key: String? by instanceState(callbackKey)

//    fun setCDfun(fc: (() -> Unit)? = null, fd: (() -> Unit)? = null) {
//        fchange = fc
//        fdel = fc
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
//            Log.d("MyTut", "fragDelChanBundle2: ${dismissDial}");

            when(nabor){
                0 -> {
                    buttChange.visibility = View.VISIBLE
                    tvChange.visibility = View.VISIBLE
                    buttDelete.visibility = View.VISIBLE
                    tvDelete.visibility = View.VISIBLE
                }
                1 -> {
                    buttChange.visibility = View.GONE
                    tvChange.visibility = View.GONE
                    buttDelete.visibility = View.VISIBLE
                    tvDelete.visibility = View.VISIBLE
                }
                2 -> {
                    buttChange.visibility = View.VISIBLE
                    tvChange.visibility = View.VISIBLE
                    buttDelete.visibility = View.GONE
                    tvDelete.visibility = View.GONE
                }
            }
            buttBackDelChange.setOnClickListener {
                dismissDial()
            }
            buttDelete.setOnClickListener {
                callback_Key?.let {
                    getSFM().setFragmentResult(it, bundleOf("answer" to "delete", "item" to item))
                }
//                fdel?.invoke()
                dismissDial()
            }
            buttChange.setOnClickListener {
                callback_Key?.let {
                    getSFM().setFragmentResult(it, bundleOf("answer" to "change", "item" to item))
                }
//                fchange?.invoke()
                dismissDial()
            }
        }
    }

    companion object {
        fun <T: Id_class> setRezListener(fragment: Fragment, requestKey: String, fchange: ((T) -> Unit)? = null, fdel: ((T) -> Unit)? = null){
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<T>("item")
                itemRez?.let {
                    when (bundle.getString("answer")) {
                        "delete" -> {
                            fdel?.invoke(it)
                        }
                        "change" -> {
                            fchange?.invoke(it)
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun newInstance() =
            FragDelChangeDial().apply {
                arguments = Bundle().apply {
                }
            }
    }
}