package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import ru.ragefalcon.tutatores.databinding.FragmentMyMessageBinding



class FragMyMessage (var message: String = ""): MyFragmentForDialog<FragmentMyMessageBinding>(FragmentMyMessageBinding::inflate) {

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        fragDial?.javaClass?.newInstance()
        outState.putString("message",message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            message = it.getString("message") ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvMyMessage.text = message
            /**
             * для скроллинга TextView
             * https://stackoverflow.com/questions/1748977/making-textview-scrollable-on-android
             * */
            tvMyMessage.movementMethod = ScrollingMovementMethod()
            buttHideMessage.setOnClickListener {
                dismissDial()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            FragMyMessage().apply {
                arguments = Bundle().apply {
                }
            }
    }
}