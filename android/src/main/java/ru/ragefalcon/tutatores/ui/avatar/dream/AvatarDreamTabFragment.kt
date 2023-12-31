package ru.ragefalcon.tutatores.ui.avatar.dream;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentDreamTabBinding

class AvatarDreamTabFragment() : BaseFragment<FragmentDreamTabBinding>(FragmentDreamTabBinding::inflate) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyTag", "!!!!!________________________--------------AvatarDreamTabFragment onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MyTag", "!!!!!________________________--------------AvatarDreamTabFragment onDetach")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
        }
    }
}