package ru.ragefalcon.tutatores.ui.journal

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.transition.MaterialElevationScale
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.IdeaRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.sravItemIdType
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.CommonAddChangeObj
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentSpisIdeaBinding
import ru.ragefalcon.tutatores.extensions.KurokOneShot
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel


class SpisIdeaFragment() : BaseFragmentVM<FragmentSpisIdeaBinding>(FragmentSpisIdeaBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemIdea? by instanceState()

    var freshFun = KurokOneShot()

    fun toSpisStapIdea(extras: FragmentNavigator.Extras){

        exitTransition = MaterialElevationScale(false).apply {
            duration = 400
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 400
        }
        val tmp =                         FragmentNavigatorExtras(
            binding.clBloknotItemFrcl to "cl_bloknot_item_frideastap",
            binding.tvIdeaNameFrsp to "tv_bloknot_name_frideastap",
            extras.sharedElements.map { Pair(it.key,it.value) }[0],
            extras.sharedElements.map { Pair(it.key,it.value) }[1]
               )
        val tmp2 = tmp.sharedElements.toMutableMap()
            tmp2.putAll(extras.sharedElements)
        ViewGroupCompat.setTransitionGroup(binding.rvIdeaList,true)
        val directions = SpisIdeaFragmentDirections.actionIdeaToStapidea()
        findNavController().navigate(directions, tmp)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =  getMyTransition(end = {
            stateViewModel.sel_jornal_nav.value = MyStateViewModel.journal_nav.idea
        })
        sharedElementReturnTransition = getMyTransition(end = {
            stateViewModel.sel_jornal_nav.value = MyStateViewModel.journal_nav.bloknot
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        with(binding) {
        rvIdeaList.doOnPreDraw { startPostponedEnterTransition() }
            stateViewModel.sel_jornal_nav.observe(viewLifecycleOwner) {
                if (it == MyStateViewModel.journal_nav.idea) rvIdeaList.requestLayout()
            }

            testButtBack.setOnClickListener {
                findNavController().navigateUp()
            }
            with(rvIdeaList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            val panelAddChangeIdea = CommonAddChangeObj<ItemIdea>(this@SpisIdeaFragment, "callAddIdea") {
                freshFun.setFire {
                    rvmAdapter.removeInsertItem(
                        it,
                        IdeaRVItem::class
                    )
                }
            }
            val menuPopupIdea = MyPopupMenuItem<ItemIdea>(this@SpisIdeaFragment, "IdeaDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                        viewmodel.addJournal.delIdea(it.id.toLong())
                }
                addButton(MenuPopupButton.CHANGE) {
                    panelAddChangeIdea.showDial(dial = { callbkKey ->
                        JournalAddIdeaPanelFragment(
                        item = it,
                        parBloknot = stateViewModel.selectItemBloknot.value,
                        parIdea = rvmAdapter.getItems().find { findItem ->
                            sravItemIdType(findItem,it.parent_id.toString(), IdeaRVItem::class)
                        }?.let { it.getData() as ItemIdea},
                        callbackKey = callbkKey
                    )})
                }
            }

            with(viewmodel) {
                stateViewModel.selectItemBloknot.value?.let {
                    viewmodel.journalFun.setBloknotForSpisIdea(it.id.toLong())
                }
                journalSpis.spisIdea.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        IdeaRVItem(item, selectListener = {
                            selItem = it
                        }, tapListener = {
                            stateViewModel.selectItemIdea.value = item
                        }, longTapListener = {
                            menuPopupIdea.showMenu(it,name = "${it.name}",)
                        }, funForTransition = ::toSpisStapIdea)
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, IdeaRVItem::class)
                    }
                    freshFun.fire()
                    (rvIdeaList as? ViewGroup)?.doOnPreDraw {
                    }
                }
                buttAddIdea.setOnClickListener {
                    showAddChangeFragDial(JournalAddIdeaPanelFragment(null,stateViewModel.selectItemBloknot.value))
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvIdeaList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            stateViewModel.selectItemBloknot.observe(viewLifecycleOwner) { plan ->
                plan?.let {
                    tvIdeaNameFrsp.text = it.name
                }
            }
        }
    }
}