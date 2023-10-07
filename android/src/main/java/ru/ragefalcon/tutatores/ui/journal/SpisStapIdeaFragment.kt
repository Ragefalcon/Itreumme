package ru.ragefalcon.tutatores.ui.journal

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.IdeaStapRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.CommonAddChangeObj
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentSpisStapIdeaBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.lang.ref.WeakReference

class SpisStapIdeaFragment : BaseFragmentVM<FragmentSpisStapIdeaBinding>(FragmentSpisStapIdeaBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = getMyTransition(end = {
            binding.clIdeaItemFrideastap.requestLayout()
        })
        sharedElementReturnTransition = getMyTransition(end = {
            stateViewModel.sel_jornal_nav.value = MyStateViewModel.journal_nav.idea
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvmAdapter = UniRVAdapter()
        postponeEnterTransition()
        with(binding) {
            rvIdeaStapList.doOnPreDraw { startPostponedEnterTransition() }

            val panelAddChangeStapIdea =
                CommonAddChangeObj<ItemIdeaStap>(WeakReference(this@SpisStapIdeaFragment), "callAddStapIdea") {
                }
            val menuPopupStapIdea =
                MyPopupMenuItem<ItemIdeaStap>(WeakReference(this@SpisStapIdeaFragment), "StapIdeaDelChange").apply {
                    addButton(MenuPopupButton.DELETE) {
                        viewmodel.addJournal.delStapIdea(it.id.toLong()){
                            /*
                            * TODO здесь необходимо реализовать функцию удаления изображений из памяти устройства
                            * пример в Desctop версии: MainDB.complexOpisSpis.spisComplexOpisForBloknot.delAllImageForItem(it)
                            * */
                        }
                    }
                    addButton(MenuPopupButton.CHANGE) {
                        panelAddChangeStapIdea.showDial(dial = { callbkKey ->
                            JournalAddStapIdeaPanelFragment(
                                item = it,
                                parIdea = stateViewModel.selectItemIdea.value,
                                callbackKey = callbkKey
                            )
                        })
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
                }
                journalSpis.spisStapIdea.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        IdeaStapRVItem(item, selectListener = {
                        }, tapListener = {
                        }, longTapListener = {
                            menuPopupStapIdea.showMenu(it, name = "${it.name}")
                        }, recyclerView = rvIdeaStapList)
                    })
                }
                buttAddStapIdea.setOnClickListener {
                    panelAddChangeStapIdea.showDial(dial = { callbkKey ->
                        JournalAddStapIdeaPanelFragment(
                            item = null,
                            parIdea = stateViewModel.selectItemIdea.value,
                            callbackKey = callbkKey
                        )
                    })
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