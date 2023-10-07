package ru.ragefalcon.tutatores.ui.finance

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemShabDoxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.ShabDoxodRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.*
import java.lang.ref.WeakReference

class SelectShablonDoxodPanel(
    private val fragment: Fragment,
    val callbackKey: String,
    listener: ((item: ItemShabDoxod) -> Unit)?
) {

    init {
        SelectShablonDoxodPanelFragment.setRezListener(fragment, callbackKey, listener)
    }

    fun showMenu(
        privPlan: ItemShabDoxod? = null,
        arrayIskl: List<Long>? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegMyFragDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(SelectShablonDoxodPanelFragment(privPlan, callbackKey, arrayIskl), manager, tag, bound)
    }

}

class SelectShablonDoxodPanelFragment(
    selItem: ItemShabDoxod? = null,
    callbackKey: String? = null,
    arrayIskl: List<Long>? = null
) :
    MyFragmentForDialogVM<FragmentSelectPlanPanelBinding>(FragmentSelectPlanPanelBinding::inflate) {


    var callbackKey: String? by instanceState(callbackKey)

    var selItem: ItemShabDoxod? by instanceState(selItem)
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
                val menuPopup = MyPopupMenuItem<ItemShabDoxod>(WeakReference(this@SelectShablonDoxodPanelFragment), "ShabDoxDelChange").apply {
                    addButton(MenuPopupButton.DELETE) {
                        viewmodel.addFin.delShabDoxod(it.id.toLong())
                    }
                }

                var firstLoad = true
                finSpis.spisShabDoxod.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        ShabDoxodRVItem(item,
                            longTapListener = {
                                menuPopup.showMenu(it,name = it.name)
                            })
                    })
                    if (firstLoad) {
                        selItem?.let {
                            rvmAdapter.setSelectItem(
                                it,
                                ShabDoxodRVItem::class
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
                    (rvmAdapter.selectedItem.value?.let { it.getData() as ItemShabDoxod })?.let {
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
            listener: ((item: ItemShabDoxod) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemShabDoxod>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }
}