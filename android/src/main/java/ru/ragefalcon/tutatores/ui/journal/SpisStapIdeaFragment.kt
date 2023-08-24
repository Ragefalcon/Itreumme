package ru.ragefalcon.tutatores.ui.journal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.IdeaRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.IdeaStapRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.sravItemIdType
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.CommonAddChangeObj
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentSpisStapIdeaBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class SpisStapIdeaFragment: BaseFragmentVM<FragmentSpisStapIdeaBinding>(FragmentSpisStapIdeaBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = getMyTransition(end = {
//            stateViewModel.sel_jornal_nav.value = MyStateViewModel.journal_nav.stap_idea
            binding.clIdeaItemFrideastap.requestLayout()
        })
        sharedElementReturnTransition = getMyTransition(end = {
            stateViewModel.sel_jornal_nav.value = MyStateViewModel.journal_nav.idea
//            cl_idea_item_frideastap.requestLayout()
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        with(binding) {
              rvIdeaStapList.doOnPreDraw { startPostponedEnterTransition() }
//        view.doOnPreDraw { startPostponedEnterTransition() }


            val panelAddChangeStapIdea = CommonAddChangeObj<ItemIdeaStap>(this@SpisStapIdeaFragment, "callAddStapIdea") {
//                freshFun.setFire {
//                    rvmAdapter.removeInsertItem(
//                        it,
//                        IdeaRVItem::class //PlanStapViewHolder
//                    )
//                }
            }
            val menuPopupStapIdea = MyPopupMenuItem<ItemIdeaStap>(this@SpisStapIdeaFragment, "StapIdeaDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
//                    if (it.countidea==0L) {
                    viewmodel.addJournal.delStapIdea(it.id.toLong())
//                    }   else    {
//                        showMyMessage("Удалите вначале все заметки из этого блокнота")
//                    }
                }
                addButton(MenuPopupButton.CHANGE) {
                    panelAddChangeStapIdea.showDial(dial = { callbkKey ->
                        JournalAddStapIdeaPanelFragment(
                            item = it,
                            parIdea = stateViewModel.selectItemIdea.value,
                            callbackKey = callbkKey
                        )})
                }
            }

            buttBackToIdea.setOnClickListener {
                findNavController().navigateUp()
            }
            with(binding.rvIdeaStapList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }

            with(viewmodel) {
                stateViewModel.selectItemIdea.value?.let {
                    viewmodel.journalFun.setIdeaForSpisStapIdea(it.id.toLong())
                    Log.d("StapIdea", "IdeaId: ${it.id}")
                }
                journalSpis.spisStapIdea.observe(viewLifecycleOwner) {
                    Log.d("StapIdea", "count: ${it.count()}")
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        IdeaStapRVItem(item, selectListener = {
//                            selItem = it
                        }, tapListener = {
//                            stateViewModel.selectItemIdea.value = item
                        }, longTapListener = {
                            menuPopupStapIdea.showMenu(it,name = "${it.name}",)
                        })
                    })
                    // Start the transition once all views have been
                    // measured and laid out
//                (binding.rvIdeaStapList as? ViewGroup)?.doOnPreDraw {
//                        lifecycleScope.launch(Dispatchers.Main) {
//                            startPostponedEnterTransition()
//                        }
//                }
                }
                buttAddStapIdea.setOnClickListener {
                    panelAddChangeStapIdea.showDial(dial = { callbkKey ->
                        JournalAddStapIdeaPanelFragment(
                            item = null,
                            parIdea = stateViewModel.selectItemIdea.value,
                            callbackKey = callbkKey
                        )})
//                    showAddChangeFragDial(JournalAddIdeaPanelFragment(null,stateViewModel.selectItemBloknot.value))
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvIdeaStapList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            stateViewModel.selectItemBloknot.observe(viewLifecycleOwner) { plan ->
                plan?.let {
                    tvBloknotNameFrideastap.text = it.name
                }
            }
            stateViewModel.selectItemIdea.observe(viewLifecycleOwner) { plan ->
                plan?.let {
                    tvIdeaNameFrideastap.text = it.name
                }
            }
        }
    }
}