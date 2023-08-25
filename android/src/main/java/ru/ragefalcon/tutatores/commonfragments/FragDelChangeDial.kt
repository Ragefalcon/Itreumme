package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.databinding.FragmentDelChangeDialogBinding
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener


class FragDelChangeDial(itemThis: Id_class? = null, callbackKey: String? = null, nabor: Int? = null) :
    MyFragmentForDialog<FragmentDelChangeDialogBinding>(FragmentDelChangeDialogBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var nabor: Int by instanceStateDef(0, nabor)
    var item: Id_class? by instanceState(itemThis)
    var callback_Key: String? by instanceState(callbackKey)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            when (nabor) {
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
                dismissDial()
            }
            buttChange.setOnClickListener {
                callback_Key?.let {
                    getSFM().setFragmentResult(it, bundleOf("answer" to "change", "item" to item))
                }
                dismissDial()
            }
        }
    }

    companion object {
        fun <T : Id_class> setRezListener(
            fragment: Fragment,
            requestKey: String,
            fchange: ((T) -> Unit)? = null,
            fdel: ((T) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { _, bundle ->
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