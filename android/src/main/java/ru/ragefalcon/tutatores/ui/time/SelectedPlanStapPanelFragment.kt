package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.PlanStapRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanStapPanelBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showMyFragDial

class SelectedPlanStapPanel(
    private val fragment: Fragment,
    val callbackKey: String,
    listener: ((item: ItemPlanStap) -> Unit)?
) {

    init {
        SelectedPlanStapPanelFragment.setRezListener(fragment, callbackKey, listener)
    }

    fun showMenu(
        parPlan: ItemPlan? = null,
        selItem: ItemPlanStap? = null,
        arrayIskl: ArrayList<Long>? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegMyFragDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(SelectedPlanStapPanelFragment(parPlan,selItem, arrayIskl, callbackKey), manager, tag, bound)
    }

}

class SelectedPlanStapPanelFragment(
    parPlan: ItemPlan? = null,
    selItem: ItemPlanStap? = null,
     arrayIskl: ArrayList<Long>? = null,    //isklItem: Long? = null,
    callbackKey: String? = null
) : MyFragmentForDialogVM<FragmentSelectPlanStapPanelBinding>(FragmentSelectPlanStapPanelBinding::inflate) {

    var parPlan: ItemPlan? by instanceState(parPlan)

    var selItem: ItemPlanStap? by instanceState(selItem)

    var arrayIskl: ArrayList<Long>? by instanceState(arrayIskl)
//    var isklItem: Long by instanceStateDef(-1,isklItem)

    var callbackKey: String? by instanceState(callbackKey)

    private var rvmAdapter = UniRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMyTransition()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        with(binding) {
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
            with(rvPlanStapList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }

            with(viewmodel) {
                parPlan?.id?.let{
                    timeFun.setPlanForSpisStapPlanForSelect(it.toLong(), arrayIskl ?: listOf())
                }
                var firstLoad = true
                timeSpis.spisPlanStapForSelect.observe(viewLifecycleOwner) { it ->
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        PlanStapRVItem(item, rvPlanStapList, viewmodel)
                    })

                    if (firstLoad) {
                        Log.d("MyTut", "item: ${selItem?.name}");
                        selItem?.run {
                            rvmAdapter.setSelectItem(
                                this,
                                PlanStapRVItem::class //PlanStapViewHolder
                            )//.setSelect(UniRVItem(PlanStapRVItem(this, viewmodel)))
                        }
                        firstLoad = false

                    }

                }
            }

            tvPlanNameFrsp.text = "${parPlan?.name}. Этапов: ${parPlan?.countstap}"
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvPlanStapList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            lifecycleScope.launch(Dispatchers.Main) {
                delay(10)
                startPostponedEnterTransition()
            }
            buttSelectPlanStapOnpanel.setOnClickListener {
                callbackKey?.let { key ->
                    (rvmAdapter.selectedItem.value?.let { it.getData() as ItemPlanStap })?.let {
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
            listener: ((item: ItemPlanStap) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemPlanStap>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }
}