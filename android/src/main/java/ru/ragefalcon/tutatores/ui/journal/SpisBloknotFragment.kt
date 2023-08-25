package ru.ragefalcon.tutatores.ui.journal

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.transition.MaterialElevationScale
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.BloknotRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentSpisBloknotBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyMessage
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class SpisBloknotFragment : BaseFragmentVM<FragmentSpisBloknotBinding>(FragmentSpisBloknotBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemBloknot? by instanceState()

    var update_item_view: View? = null

    fun toSpisIdea(extras: FragmentNavigator.Extras) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 400
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 400
        }
        ViewGroupCompat.setTransitionGroup(binding.rvBloknotList, true)
        val directions = SpisBloknotFragmentDirections.actionBloknotToIdea()
        findNavController().navigate(directions, extras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        with(binding) {
            view.doOnPreDraw { startPostponedEnterTransition() }
            stateViewModel.sel_jornal_nav.observe(viewLifecycleOwner) {
                if (it == MyStateViewModel.journal_nav.bloknot) rvBloknotList.requestLayout()
            }
            with(rvBloknotList) {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                adapter = rvmAdapter
            }

            val menuPopupBloknot = MyPopupMenuItem<ItemBloknot>(this@SpisBloknotFragment, "BloknotDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                    if (it.countidea==0L) {
                        viewmodel.addJournal.delBloknot(it.id.toLong())
                    }   else    {
                        showMyMessage("Удалите вначале все заметки из этого блокнота")
                    }
                }
                addButton(MenuPopupButton.CHANGE) {
                    showAddChangeFragDial(JournalAddBloknotPanelFragment(it))
                }
            }

            with(viewmodel) {
                journalSpis.spisBloknot.observe(viewLifecycleOwner) { it ->
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        BloknotRVItem(item, selectListener = {
                            selItem = it
                        }, tapListener = {
                            stateViewModel.selectItemBloknot.value = item
                        }, longTapListener = {
                            menuPopupBloknot.showMenu(it,name = "${it.name}",)
                        }, funForTransition = ::toSpisIdea)
                    })

                    selItem?.let {
                        rvmAdapter.setSelectItem(it, BloknotRVItem::class)
                    }
                }
                buttAddBloknot.setOnClickListener {
                    showAddChangeFragDial(JournalAddBloknotPanelFragment())
                }
            }

            /**
             * https://stackoverflow.com/questions/42379660/how-to-prevent-recyclerview-item-from-blinking-after-notifyitemchangedpos
             * */
            (rvBloknotList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}