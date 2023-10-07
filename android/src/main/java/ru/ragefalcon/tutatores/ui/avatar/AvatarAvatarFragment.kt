package ru.ragefalcon.tutatores.ui.avatar;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.models.commonfun.unOffsetCorrFromBase
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.StatAvatarRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentAvatarBinding
import ru.ragefalcon.tutatores.story.VoiceOver
import java.lang.ref.WeakReference

class AvatarAvatarFragment() : BaseFragmentVM<FragmentAvatarBinding>(FragmentAvatarBinding::inflate) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyTag", "!!!!!________________________--------------AvatarAvatarFragment onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MyTag", "!!!!!________________________--------------AvatarAvatarFragment onDetach")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvmAdapter = UniRVAdapter()

        with(binding) {
            with(rvAvatarStat) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                avatarSpis.spisMainParam.observe(viewLifecycleOwner) {
                    it.find { it.name == "Birthday" }?.let {
                        avatarBirthday.setBirthday(unOffsetCorrFromBase(it.stringparam.toLong()))
                    }

                }
                avatarBirthday.setOnClickListener {
                    VoiceOver(WeakReference(this@AvatarAvatarFragment)).showDialog(VoiceOver.Companion.SpisVODialog.VO_BIRTHDAY_UPDATE)
                }
                avatarSpis.spisAvatarStat.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        StatAvatarRVItem(item)
                    })
                }
            }
            (rvAvatarStat.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}