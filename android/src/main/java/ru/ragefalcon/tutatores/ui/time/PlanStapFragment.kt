package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.TransitionInflater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.PlanStapRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.sravItemIdType
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentPlanStapBinding
import ru.ragefalcon.tutatores.extensions.*

class PlanStapFragment() : BaseFragmentVM<FragmentPlanStapBinding>(FragmentPlanStapBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    var selProgressBar: ProgressBar? = null
    var changeItemGotov: ((Double) -> Unit)? = null
    var selItem: ItemPlanStap? by instanceState()

    var freshFun = KurokOneShot()
    var scipUpd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.share_tv)
    }

    private val callPlanStapChangeKey = "PlanStapChange"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        val menuPopupPlanStap = MyPopupMenuItem<ItemPlanStap>(this, "PlanStapDelChange").apply {
            addButton(MenuPopupButton.UNEXECUTE) {
                viewmodel.addTime.updStatPlanStap(item = it, stat = 0)
            }
            addButton(MenuPopupButton.EXECUTE) {
                viewmodel.addTime.updStatPlanStap(item = it, stat = 10)
            }
            addButton(MenuPopupButton.DELETE) {
                if (it.podstapcount > 0) {
                    showMyMessage("Вначале удалите подэтапы этого этапа")
                } else {
                    viewmodel.addTime.delPlanStap(it.id.toLong())
                }
            }
            addButton(MenuPopupButton.CHANGE) {
                showAddChangeFragDial(
                    TimeAddPlanStapPanelFragment(
                        item = it,
                        parPlan = stateViewModel.selectItemPlan.value!!,
                        parPlanStap = rvmAdapter.getItems().find { findItem ->
                            sravItemIdType(findItem,it.parent_id,PlanStapRVItem::class)
                        }?.let { it.getData() as ItemPlanStap },
                        callbackKey = callPlanStapChangeKey
                    )
                )
            }
        }
        /**
         * https://habr.com/ru/post/515080/
         * */
        setSFMResultListener(callPlanStapChangeKey) { key, bundle ->
            val result = bundle.getParcelable<ItemPlanStap>("bundleKey")
            result?.let {
                freshFun.setFire {
                    rvmAdapter.removeInsertItem(
                        it,
                        PlanStapRVItem::class //PlanStapViewHolder
                    )
                }
            }
        }
        with(binding) {

            buttToggleOpenStapPlan.setOnClickListener {
                viewmodel.timeFun.setOpenSpisStapPlan(buttToggleOpenStapPlan.isChecked)
            }
            buttToggleOpenStapPlan.isChecked = false
            viewmodel.timeFun.setOpenSpisStapPlan(buttToggleOpenStapPlan.isChecked)

            stateViewModel.gotovSelPlanStap.observe(viewLifecycleOwner) {
                sbGotovPlanStap.progress = it
            }
            sbGotovPlanStap.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    selProgressBar?.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    scipUpd = true
                    selProgressBar?.progress = sbGotovPlanStap.progress
                    changeItemGotov?.invoke(sbGotovPlanStap.progress.toDouble())
                    viewmodel.addTime.updGotovPlanStap(
                        rvmAdapter.selectedItem.value?.getData()?.id_main?.toLong() ?: -1,
                        sbGotovPlanStap.progress.toDouble()
                    )
                }

            })

            buttAddPlanStap.setOnClickListener {
                showAddChangeFragDial(
                    TimeAddPlanStapPanelFragment(parPlan = stateViewModel.selectItemPlan.value!!),
                    getSFM()
                )
            }
            buttAddPlanPodstap.setOnClickListener {
                showAddChangeFragDial(
                    TimeAddPlanStapPanelFragment(
                        parPlan = stateViewModel.selectItemPlan.value!!,
                        parPlanStap = rvmAdapter.selectedItem.value?.let { it.getData() as ItemPlanStap }
                    ), getSFM()
                )
            }
            testButtBack.setOnClickListener {
                findNavController().navigateUp()
            }
            with(rvPlanStapList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }

            with(viewmodel) {
                stateViewModel.selectItemPlan.value?.let {
                    timeFun.setPlanForSpisStapPlan(it.id.toLong())
                }
                timeSpis.spisPlanStap.observe(viewLifecycleOwner) { it ->
                    if (!scipUpd) {
                        rvmAdapter.updateData(formUniRVItemList(it) { item ->
                            PlanStapRVItem(item,
                                rvPlanStapList,
                                viewmodel,
                                funSelItem = { itemS, progress ->
                                    selItem = itemS
                                    stateViewModel.gotovSelPlanStap.value = progress
                                },
                                listener = {
                                    menuPopupPlanStap.showMenu(item,name = "${item.name}",{ if (item.stat == 10L) it != MenuPopupButton.EXECUTE else it != MenuPopupButton.UNEXECUTE})
                                },
                                getProgBar = { progBar, changeItemGot ->
                                    selProgressBar = progBar
                                    changeItemGotov = changeItemGot
                                }
                            )
                        })
                        selItem?.let {
                            rvmAdapter.setSelectItem(it, PlanStapRVItem::class) //PlanStapViewHolder
                        }
                        freshFun.fire()
                        rvPlanStapList.invalidate()
                        rvPlanStapList.requestLayout()
                        rvPlanStapList.doOnPreDraw {
                            startPostponedEnterTransition()
                        }
                    } else {
                        scipUpd = false
                    }

                }
                timeFun.setListenerCountStapPlan {  count ->
                    stateViewModel.selectItemPlan.value?.let {
                        tvPlanNameFrsp.text = "${it.name}. Этапов: $count"
                    }
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvPlanStapList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            lifecycleScope.launch(Dispatchers.Main) {
                delay(10)
            }
        }
    }
}