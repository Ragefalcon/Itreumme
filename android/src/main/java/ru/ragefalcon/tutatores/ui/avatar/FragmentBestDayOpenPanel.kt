package ru.ragefalcon.tutatores.ui.avatar;

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.extensions.localUnix
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.DenPlanRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentBestDayOpenPanelBinding
import ru.ragefalcon.tutatores.extensions.TimeUnits
import ru.ragefalcon.tutatores.extensions.add
import ru.ragefalcon.tutatores.extensions.daysBetween
import java.util.*

class FragmentBestDayOpenPanel(name: String? = null, date: Long? = null) :
    MyFragmentForDialogVM<FragmentBestDayOpenPanelBinding>(FragmentBestDayOpenPanelBinding::inflate) {

    private var date: Long by instanceStateDef(Date().time, date)
    private var name: String by instanceStateDef("", name)
    private var delta: Int by instanceStateDef(0)

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemDenPlan? by instanceState()

    fun getDelta() = if (delta == 0) "" else if (delta > 0) "+$delta" else delta.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvBestdayList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                avatarFun.setPlanBestDay(date)
                tvNameBestdays.text = name
                tvAddDays.text = getDelta()

                avatarSpis.spisDenPlanInBestDays.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        DenPlanRVItem(item, rvBestdayList, listener = {
                            selItem = it
                        })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, DenPlanRVItem::class) //DenPlanViewHolder
                    }
                }

                etDateDp.apply {

                    setPattern("dd MMM yyyy (EEE)")
                    setDate(avatarFun.getDateLong())
                    observe(viewLifecycleOwner) {
                        delta = Date(date).daysBetween(Date(it)).toInt()
                        avatarFun.setPlanBestDay(it)
                        tvAddDays.text = getDelta()
                    }
                }

//                avatarFun.setFunPlanBestDayDateOporTimeUpd {
//                    etDateDp.setDate(it)
//                }

                buttDateDpLeft.setOnClickListener {
                    delta--
                    etDateDp.setDate(Date(date).add(delta,TimeUnits.DAY))
                }
                buttDateDpRight.setOnClickListener {
                    delta++
                    etDateDp.setDate(Date(date).add(delta,TimeUnits.DAY))
                }

            }
            (rvBestdayList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
        }
    }
}