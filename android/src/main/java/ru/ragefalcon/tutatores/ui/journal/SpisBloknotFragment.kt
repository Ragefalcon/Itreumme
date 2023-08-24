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
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.BloknotRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentSpisBloknotBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyMessage
import ru.ragefalcon.tutatores.ui.avatar.dream.AvatarAddDreamFragDial
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class SpisBloknotFragment : BaseFragmentVM<FragmentSpisBloknotBinding>(FragmentSpisBloknotBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemBloknot? by instanceState()

    var update_item_view: View? = null

    fun toSpisIdea(extras: FragmentNavigator.Extras) { //view_name: View,view_container: View, //, updView: View

        // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
        // instead of fading out with the rest to prevent an overlapping animation of fade and move).
//        (this.exitTransition  as TransitionSet?)?.excludeTarget(itemV, true)
//        parentFragment?.postponeEnterTransition()
//        requireActivity().supportFragmentManager.commit {
////                setCustomAnimations(...)
//            setReorderingAllowed(false)
//            tranfun?.invoke(this)
////            addSharedElement(tv_plan_name, "tv_plan_name")
////            addSharedElement(cl_stap_plan, "cl_stap_plan_frcl")
//            replace(R.id.main_fragment, fragment)
////            replace(R.id.fragment_journal_container, fragment)
//            addToBackStack(null)
//        }
//        update_item_view = updView

        exitTransition = MaterialElevationScale(false).apply {
            duration = 400 //resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 400 //resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        ViewGroupCompat.setTransitionGroup(binding.rvBloknotList, true)
        val directions = SpisBloknotFragmentDirections.actionBloknotToIdea()
        findNavController().navigate(directions, extras)
//        val host: NavHostFragment = requireActivity().supportFragmentManager
//            .findFragmentById(R.id.fragment_journal_container) as NavHostFragment? ?: return
//        var navController = host.navController
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
//                startPostponedEnterTransition()
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, BloknotRVItem::class) //DenPlanViewHolder
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