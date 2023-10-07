package ru.ragefalcon.tutatores.commonfragments;

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.databinding.FragmentTutDialogBinding
import ru.ragefalcon.tutatores.story.VoiceOver
import java.lang.ref.WeakReference

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
            BodyTutDialog(WeakReference(this@FragmentTutDialog)).apply {
                VoiceOver.getFunBody(body ?: -1).invoke(this)
            }.observe {
                rvmAdapter.updateData(it)
            }
            (rvDialogBody.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}