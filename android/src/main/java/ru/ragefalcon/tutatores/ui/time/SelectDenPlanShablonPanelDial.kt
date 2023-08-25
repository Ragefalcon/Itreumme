package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemShablonDenPlan
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.ShablonDenPlanRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSelectDenShablonBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM

class SelectDenPlanShablonPanelDial(callbackKey: String? = null) : MyFragmentForDialogVM<FragmentSelectDenShablonBinding>(FragmentSelectDenShablonBinding::inflate) {

    var callback_Key: String? by instanceState(callbackKey)

    var selItem: ItemShablonDenPlan? by instanceState()

    private var rvmAdapter = UniRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMyTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvDenPlanShabList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }

            val menuPopup = MyPopupMenuItem<ItemShablonDenPlan>(this@SelectDenPlanShablonPanelDial, "ShablonDenPlanDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                    viewmodel.addTime.delShablonDenPlan(it.id.toLong())
                }
            }

            with(viewmodel) {
                var firstLoad = true
                timeSpis.spisShablonDenPlan.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        ShablonDenPlanRVItem(item,
                            rvDenPlanShabList,
                            listener = {
                            selItem = it
                        },
                        longTapListener = {
                            menuPopup.showMenu(it,name = it.name)
                        })
                    })
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvDenPlanShabList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
            buttSelDenplanShabOnpanel.setOnClickListener {
                callback_Key?.let { key->
                    rvmAdapter.selectedItem.value?.let { item->
                        getSFM().setFragmentResult(key, bundleOf("povtor" to cbPovtors.isChecked,"hours" to cbHours.isChecked, "item" to item.getData()))
                    }
                }
                dismissDial()
            }
        }
    }

    companion object {
        fun setRezListener(manager: FragmentManager, requestKey: String, lifecycleOwner: LifecycleOwner, listener: ((item: ItemShablonDenPlan,povtor: Boolean,hours: Boolean) -> Unit)? = null){
            manager.setFragmentResultListener(requestKey, lifecycleOwner) { key, bundle ->
                val itemRez = bundle.getParcelable<ItemShablonDenPlan>("item")
                val povtor = bundle.getBoolean("povtor")
                val hours = bundle.getBoolean("hours")
                itemRez?.let {
                    listener?.invoke(it,povtor,hours)
                }
            }
        }
        @JvmStatic
        fun newInstance() =
            SelectDenPlanShablonPanelDial().apply {
                arguments = Bundle().apply {
                }
            }
    }

}