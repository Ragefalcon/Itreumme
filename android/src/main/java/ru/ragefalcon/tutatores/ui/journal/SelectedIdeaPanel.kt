package ru.ragefalcon.tutatores.ui.journal

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.IdeaRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanStapPanelBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showMyFragDial

class SelectedIdeaPanel(
    private val fragment: Fragment,
    val callbackKey: String,
    listener: ((item: ItemIdea) -> Unit)?
) {

    init {
        SelectedIdeaPanelFragment.setRezListener(fragment, callbackKey, listener)
    }

    fun showMenu(
        parBloknot: ItemBloknot,
        privIdea: ItemIdea? = null,
        arrayIskl: ArrayList<Long>? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegMyFragDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(SelectedIdeaPanelFragment(parBloknot, privIdea, callbackKey, arrayIskl), manager, tag, bound)
    }

}

class SelectedIdeaPanelFragment(parBloknot: ItemBloknot? = null, selItem: ItemIdea? = null, callbackKey: String? = null, arrayIskl: ArrayList<Long>? = null) :
    MyFragmentForDialogVM<FragmentSelectPlanStapPanelBinding>(FragmentSelectPlanStapPanelBinding::inflate) {


    var callbackKey: String? by instanceState(callbackKey)

    var parentBloknot: ItemBloknot? by instanceState(parBloknot)
    var selItem: ItemIdea? by instanceState(selItem)
    var arrayIskl: ArrayList<Long>? by instanceState(arrayIskl)


    private var rvmAdapter = UniRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMyTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvPlanStapList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                parentBloknot?.let {
                    tvPlanNameFrsp.text = it.name
                    journalFun.setBloknotForSpisIdeaForSelect(it.id.toLong(),arrayIskl ?: listOf())
                }
                var firstLoad = true
//                timeFun.setOpenSpisPlanIn(false, arrayIskl ?: listOf())
                journalSpis.spisIdeaForSelect.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        IdeaRVItem(item)
                    })
                    if (firstLoad) {
                        selItem?.let {
                            rvmAdapter.setSelectItem(
                                it,
                                IdeaRVItem::class
                            )// PlanViewHolder UniRVItem(PlanRVItem(this)))
                        }
                        firstLoad = false
                    }
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvPlanStapList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            with(viewmodel) {
            }
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
            buttSelectPlanStapOnpanel.setOnClickListener {
                callbackKey?.let { key ->
                    (rvmAdapter.selectedItem.value?.let { it.getData() as ItemIdea })?.let {
                        getSFM().setFragmentResult(
                            key,
                            bundleOf(
                                "item" to it
                            )
                        )
                    }
                }
                dismissDial()
            }
        }
    }

    companion object {
        fun setRezListener(
            fragment: Fragment,
            requestKey: String,
            listener: ((item: ItemIdea) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemIdea>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }
}