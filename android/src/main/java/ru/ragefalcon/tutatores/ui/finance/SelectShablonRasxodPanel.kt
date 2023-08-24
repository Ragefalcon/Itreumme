package ru.ragefalcon.tutatores.ui.finance

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemShabRasxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.ShabRasxodRVItem
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyFragDial
import ru.ragefalcon.tutatores.commonfragments.MyFragmentForDialogVM
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showMyFragDial

class SelectShablonRasxodPanel(
    private val fragment: Fragment,
    val callbackKey: String,
    listener: ((item: ItemShabRasxod) -> Unit)?
) {

    init {
        SelectShablonRasxodPanelFragment.setRezListener(fragment, callbackKey, listener)
    }

    fun showMenu(
        privPlan: ItemShabRasxod? = null,
        arrayIskl: List<Long>? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegMyFragDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(SelectShablonRasxodPanelFragment(privPlan, callbackKey, arrayIskl), manager, tag, bound)
    }

}

class SelectShablonRasxodPanelFragment(
    selItem: ItemShabRasxod? = null,
    callbackKey: String? = null,
    arrayIskl: List<Long>? = null
) :
    MyFragmentForDialogVM<FragmentSelectPlanPanelBinding>(FragmentSelectPlanPanelBinding::inflate) {


    var callbackKey: String? by instanceState(callbackKey)

    var selItem: ItemShabRasxod? by instanceState(selItem)
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
                val menuPopup = MyPopupMenuItem<ItemShabRasxod>(this@SelectShablonRasxodPanelFragment, "ShabRasxDelChange").apply {
                    addButton(MenuPopupButton.DELETE) {
                            viewmodel.addFin.delShabRasxod(it.id.toLong())
                    }
                }

                var firstLoad = true
//                timeFun.setOpenSpisPlanIn(false, arrayIskl ?: listOf())
                finSpis.spisShabRasxod.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        ShabRasxodRVItem(item,
                        longTapListener = { it ->
                            menuPopup.showMenu(it,name = it.name)
                        })
                    })
                    if (firstLoad) {
                        selItem?.let {
                            rvmAdapter.setSelectItem(
                                it,
                                ShabRasxodRVItem::class
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
                    (rvmAdapter.selectedItem.value?.let { it.getData() as ItemShabRasxod })?.let {
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
            listener: ((item: ItemShabRasxod) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemShabRasxod>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }
}