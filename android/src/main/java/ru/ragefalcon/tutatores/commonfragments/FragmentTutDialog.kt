package ru.ragefalcon.tutatores.commonfragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentTutDialogBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.story.VoiceOver

class FragmentTutDialog(body: Int? = null) :
    MyFragmentForDialogVM<FragmentTutDialogBinding>(FragmentTutDialogBinding::inflate) {

    var body: Int? by instanceState(body)

    private var rvmAdapter = UniRVAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvDialogBody) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
//            collapse(rvDialogBody,duration = 0)
            BodyTutDialog(this@FragmentTutDialog).apply {
                VoiceOver.getFunBody(body ?: -1).invoke(this) // VoiceOver.spisVODialog.valueOf(body).body(this)
            }.observe {
                rvmAdapter.updateData(it)
//                expand(rvDialogBody)
            }
            (rvDialogBody.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}