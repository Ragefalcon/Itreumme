package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.PlanRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showMyFragDial

class SelectedPlanPanel(
    private val fragment: Fragment,
    val callbackKey: String,
    listener: ((item: ItemPlan) -> Unit)?
) {

    init {
        SelectedPlanPanelFragment.setRezListener(fragment, callbackKey, listener)
    }

    fun showMenu(
        privPlan: ItemPlan? = null,
        arrayIskl: List<Long>? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegMyFragDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(SelectedPlanPanelFragment(privPlan, callbackKey,arrayIskl), manager, tag, bound)
    }

}

class SelectedPlanPanelFragment(selItem: ItemPlan? = null, callbackKey: String? = null, arrayIskl: List<Long>? = null) :
    MyFragmentForDialogVM<FragmentSelectPlanPanelBinding>(FragmentSelectPlanPanelBinding::inflate) {


    var callbackKey: String? by instanceState(callbackKey)

    var selItem: ItemPlan? by instanceState(selItem)
    var arrayIskl: List<Long>? by instanceState(arrayIskl)


    private var rvmAdapter = UniRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMyTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvPlanList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }

            with(viewmodel) {
                var firstLoad = true
                timeFun.setOpenSpisPlanIn(false, arrayIskl ?: listOf())
                timeSpis.spisPlanIn.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        PlanRVItem(item)
                    })
                    if (firstLoad) {
                        selItem?.let {
                            Log.d("MyTut", "selItem: $selItem");
                            rvmAdapter.setSelectItem(
                                it,
                                PlanRVItem::class
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
            (rvPlanList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
            buttSelectPlanOnpanel.setOnClickListener {
                callbackKey?.let { key ->
                    (rvmAdapter.selectedItem.value?.let { it.getData() as ItemPlan })?.let {
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
            listener: ((item: ItemPlan) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemPlan>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }
}