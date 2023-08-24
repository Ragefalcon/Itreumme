package ru.ragefalcon.tutatores.ui.journal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.BloknotRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showMyFragDial

class SelectedBloknotPanel(
    private val fragment: Fragment,
    val callbackKey: String,
    listener: ((item: ItemBloknot) -> Unit)?
) {

    init {
        SelectedBloknotPanelFragment.setRezListener(fragment, callbackKey, listener)
    }

    fun showMenu(
        privPlan: ItemBloknot? = null,
        arrayIskl: List<Long>? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegMyFragDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(SelectedBloknotPanelFragment(privPlan, callbackKey, arrayIskl), manager, tag, bound)
    }

}

class SelectedBloknotPanelFragment(
    selItem: ItemBloknot? = null,
    callbackKey: String? = null,
    arrayIskl: List<Long>? = null
) :
    MyFragmentForDialogVM<FragmentSelectPlanPanelBinding>(FragmentSelectPlanPanelBinding::inflate) {


    var callbackKey: String? by instanceState(callbackKey)

    var selItem: ItemBloknot? by instanceState(selItem)
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
//                timeFun.setOpenSpisPlanIn(false, arrayIskl ?: listOf())
                journalSpis.spisBloknot.observe(viewLifecycleOwner) { spisBl ->
                    rvmAdapter.updateData(formUniRVItemList<ItemBloknot>(spisBl) { item ->
                        BloknotRVItem(item,rvPlanList)
                    })
                    if (firstLoad) {
                        selItem?.let {
                            Log.d("MyTut", "selItem: ItemBloknot");
                            rvmAdapter.setSelectItem(
                                it,
                                BloknotRVItem::class
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
            (rvPlanList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            with(viewmodel) {
            }
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
            buttSelectPlanOnpanel.setOnClickListener {
                callbackKey?.let { key ->
                    (rvmAdapter.selectedItem.value?.let { it.getData() as ItemBloknot })?.let {
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
            listener: ((item: ItemBloknot) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemBloknot>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }
}