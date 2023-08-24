package ru.ragefalcon.tutatores.ui.avatar;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.models.data.ItemBestDays
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.BestDaysRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.StatAvatarRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentHistoryBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.avatar.dream.AvatarAddDreamFragDial

class AvatarHistoryFragment() : BaseFragmentVM<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvBestdays) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                val menuPopupBestDay = MyPopupMenuItem<ItemBestDays>(this@AvatarHistoryFragment, "BestDayDel").apply {
                    addButton(MenuPopupButton.DELETE) {
                        viewmodel.addAvatar.delBestDay(it.id.toLong())
                    }
                }
                avatarSpis.spisBestDays.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        BestDaysRVItem(item,listener = {
                            showMyFragDial(FragmentBestDayOpenPanel(it.name,it.data))
                        },longTapListener = {itemBD ->
                            menuPopupBestDay.showMenu(itemBD,name = "${itemBD.name}",)
                        })
                    })
                }
            }
            (rvBestdays.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}