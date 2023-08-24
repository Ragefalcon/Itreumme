package ru.ragefalcon.tutatores.ui.time

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.TransitionSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.PlanRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentPlanBinding
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.extensions.showMyMessage
import java.util.*


class PlanFragment : BaseFragmentVM<FragmentPlanBinding>(FragmentPlanBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    var selProgressBar: ProgressBar? = null
    var changeItemGotov: ((Double) -> Unit)? = null


    var scipUpd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageFadeTransition = Slide().addTarget(R.id.cl_stap_plan)
//        val textSlideTransition = Slide().addTarget(R.id.tv_test_plan)
        val textSlideTransition = ChangeBounds().addTarget(R.id.tv_plan_name)
        val rvBoundsTransition = Slide().addTarget(R.id.rv_plan_list)
        val customAnimTransition = TransitionSet().apply {
            duration = 1000L // 1 sec
            addTransition(rvBoundsTransition)
            addTransition(imageFadeTransition)
            addTransition(textSlideTransition)
        }
        sharedElementEnterTransition = customAnimTransition
        sharedElementReturnTransition = customAnimTransition
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyTut", "PlanFrag: onViewCreated")
        val menuPopupPlan = MyPopupMenuItem<ItemPlan>(this, "PlanDelChange").apply {
            addButton(MenuPopupButton.UNEXECUTE) {
                viewmodel.addTime.updStatPlan(item = it, stat = 0)
            }
            addButton(MenuPopupButton.EXECUTE) {
                viewmodel.addTime.updStatPlan(item = it, stat = 10)
            }
            addButton(MenuPopupButton.DELETE) {
                if (it.countstap > 0) {
                    showMyMessage("Вначале удалите этапы этого проекта")
                } else {
                    viewmodel.addTime.delPlan(it.id.toLong()){}
                }
            }
            addButton(MenuPopupButton.CHANGE) {
                showAddChangeFragDial(TimeAddPlanFragDialog(item = it))
            }
        }
        with(binding) {

            buttToggleOpenPlan.setOnClickListener {
                viewmodel.timeFun.setOpenSpisPlan(buttToggleOpenPlan.isChecked)
            }
            buttToggleOpenPlan.isChecked = false
            viewmodel.timeFun.setOpenSpisPlan(buttToggleOpenPlan.isChecked)

            val callbackAddEff = "timeAddEffCallback"
            TimeAddEffektDial.setRezListener(this@PlanFragment, callbackAddEff) {
                buttAddEffekt.isChecked = it
            }
            buttAddEffekt.setOnClickListener {
                if (buttAddEffekt.isChecked) {
                    stateViewModel.selectItemPlan.value?.let {
                        showMyFragDial(TimeAddEffektDial(itemPlan = it, callbackKey = callbackAddEff))
                    }
                } else {
                    buttAddEffekt.isChecked = true
                }
            }
            stateViewModel.gotovSelPlan.observe(viewLifecycleOwner) {
                sbGotovPlan.progress = it
            }

            /**
             * Без использования переменных ниже (startTrack, goodTouch), такое чувство, что через раз setOnTouchListener сохраняет координаты thumb
             * от первого касания и при перемещении за эти границы касание "сбрасывается"
             * */
            var startTrack = false
            var goodTouch = false
            sbGotovPlan.setOnTouchListener { v, event ->
                val thumb:Drawable = (v as SeekBar).thumb;

                if (!startTrack) {
                    goodTouch = true
                    (event.x <= thumb.bounds.left
                    || event.x >= thumb.bounds.right
                    || event.y <= thumb.bounds.top
                    || event.y >= thumb.bounds.bottom)
                } else false

            }
            sbGotovPlan.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    selProgressBar?.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    if (goodTouch){
                        startTrack = true
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    startTrack = false
                    goodTouch = false
                    scipUpd = true
                    selProgressBar?.progress = sbGotovPlan.progress
                    changeItemGotov?.invoke(sbGotovPlan.progress.toDouble())
                    viewmodel.addTime.updGotovPlan(
                        stateViewModel.selectItemPlan.value!!.id.toLong(),
                        sbGotovPlan.progress.toDouble()
                    )
                }

            })

            with(rvPlanList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
//            addItemDecoration(divider)
            }

            with(viewmodel) {
                timeSpis.spisPlan.observe(viewLifecycleOwner) {
                    tvCountPlan.text = it.count().toString()
                    buttToStapPlan.isEnabled = it.count()>0
                    buttToStapPlan.isVisible = it.count()>0
                    if (!scipUpd) {
                        rvmAdapter.updateData(formUniRVItemList(it) { item ->
                            PlanRVItem(item, funSelItem = { item, progress ->
                                stateViewModel.selectItemPlan.value = item
                                stateViewModel.gotovSelPlan.value = progress
                                viewmodel.timeSpis.spisEffekt.getLiveData().value?.find { it.idplan == item.id.toLong() }?.let {
                                    buttAddEffekt.isChecked = true
                                } ?: run { buttAddEffekt.isChecked = false }
                            }, listener = { item, viewItem ->
                                menuPopupPlan.showMenu(item,name = "${item.name}",{ if (item.stat == 10L) it != MenuPopupButton.EXECUTE else it != MenuPopupButton.UNEXECUTE})
                            }, getProgBar = { progBar, changeItemGot ->
                                selProgressBar = progBar
                                changeItemGotov = changeItemGot
                            })
                        })
                        stateViewModel.selectItemPlan.value?.let {
                            rvmAdapter.setSelectItem(it, PlanRVItem::class) //PlanViewHolder
                        }
                    } else {
                        scipUpd = false
                    }
                }
            }

            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvPlanList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            stateViewModel.selectItemPlan.observe(viewLifecycleOwner) { plan ->
                plan?.let {
                    viewmodel.timeFun.setPlanForCountStapPlan(it.id.toLong())
//                    lifecycleScope.launch(Dispatchers.Main) {
//                        tvPlanName.text = "${it.name}. Этапов: ${it.countstap}"
////                  tvPlanName.requestLayout()
//                    }
                }
            }
            viewmodel.timeFun.setListenerCountStapPlan { count ->
                stateViewModel.selectItemPlan.value?.let {
                    /**
                     * Без lifecycleScope.launch(Dispatchers.Main) и именно с (Dispatchers.Main)
                     * не будет обновляться высота текста в случае если она изменяется
                     * видимо это связано с тем что выделение происходит из RecyclerView и в такой
                     * последовательности пересчет высоты текста не происходит, вернее он происходит
                     * с запозданием ровно в одно выделение...
                     * а этот вариант работает даже без вызова requestLayout()
                     * */
                    lifecycleScope.launch(Dispatchers.Main) {
                        tvPlanName.text = "${it.name}. Этапов: $count(${it.countstap})"
                    }
                }
            }
            val logoScene = Scene(scenePlanTab)
            val logoScene2 = Scene(scenePlanTab)
            logoScene.setEnterAction {
                val params: ViewGroup.LayoutParams = tvPlanName.getLayoutParams()
                params.height = ViewGroup.LayoutParams.MATCH_PARENT //.dpToPx
                tvPlanName.setLayoutParams(params)
                rvPlanList.visibility = View.INVISIBLE
            }
            logoScene2.setEnterAction {
                val params: ViewGroup.LayoutParams = tvPlanName.getLayoutParams()
                params.height = 50.dpToPx
                tvPlanName.setLayoutParams(params)
                rvPlanList.visibility = View.VISIBLE
            }

            tvPlanName.setOnClickListener {
                tvPlanName.requestLayout()
            }
            buttToStapPlan.setOnClickListener {
                val directions = PlanFragmentDirections.actionPlanToPlanstap()
                findNavController().navigate(
                    directions, FragmentNavigatorExtras(
                        tvPlanName to "tvPlanName",
                        clStapPlan to "cl_stap_plan_frcl"
                    )
                )
            }
            buttAddPlan.setOnClickListener {
                stateViewModel.tmpTimeStampLong = Calendar.getInstance().time.time
                Log.d("MyTut", "timeStamp: ${Calendar.getInstance().time.time}");
                showAddChangeFragDial(TimeAddPlanFragDialog(), requireActivity().supportFragmentManager)
            }
        }
    }
}