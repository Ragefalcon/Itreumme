package ru.ragefalcon.tutatores.ui.avatar;

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.commonfun.unOffsetCorrFromBase
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.StatAvatarRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentAvatarBinding
import ru.ragefalcon.tutatores.story.VoiceOver

class AvatarAvatarFragment() : BaseFragmentVM<FragmentAvatarBinding>(FragmentAvatarBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    VoiceOver(this@AvatarAvatarFragment).showDialog(VoiceOver.Companion.SpisVODialog.VO_BIRTHDAY_UPDATE)
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