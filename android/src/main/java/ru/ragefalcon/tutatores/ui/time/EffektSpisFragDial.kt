package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.EffektRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentSpisEffektBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class EffektSpisFragDial() : MyFragmentForDialogVM<FragmentSpisEffektBinding>(FragmentSpisEffektBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    private val callEffektDChKey = "EffektDelChange"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragDelChangeDial.setRezListener<ItemEffekt>(this, callEffektDChKey,
            fchange = {
                showMyFragDial(TimeAddEffektDial(it))
            },
            fdel = {
                    viewmodel.addTime.delEffekt(it.id.toLong())
            })
        with(binding) {
            with(rvEffektList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }

            with(viewmodel) {
                var firstLoad = true
                timeSpis.spisEffekt.observe(viewLifecycleOwner) {
                    println("Check current date and update spisEffekt count2: ${it.count()}")
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        EffektRVItem(item, longTapListener = {
                            showMyFragDial(FragDelChangeDial(it, callEffektDChKey))
                        })
                    })
//                    if (firstLoad) {
//                        selItem?.let {
//                            Log.d("MyTut", "selItem: $selItem");
//                            rvmAdapter.setSelectItem(it, PlanViewHolder::class)// UniRVItem(PlanRVItem(this)))
//                        }
//                        firstLoad = false
//                    }
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvEffektList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
        }
    }

}