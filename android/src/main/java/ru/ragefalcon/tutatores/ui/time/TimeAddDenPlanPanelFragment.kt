package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddDenPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import java.util.*

class TimeAddDenPlanPanelFragment(item: ItemDenPlan? = null) :
    FragAddChangeDialHelper<ItemDenPlan, FragmentTimeAddDenPlanPanelBinding>(
        FragmentTimeAddDenPlanPanelBinding::inflate,
        item
    ) {

    val firstStart = item != null
    var privPlan: ItemPlan? by instanceState() { cache, value ->
        if (cache != value) {
            privPlanStap = null
        }
        if (value != null) {
            binding.nameParentPlan.text = value.name
            binding.buttUnselPlan.isVisible = true
            binding.clSelectedDenplanPlanStap.visibility = ViewGroup.VISIBLE
        } else {
            binding.nameParentPlan.text = "Привязать к плану"
            binding.buttUnselPlan.isVisible = false
            binding.clSelectedDenplanPlanStap.visibility = ViewGroup.GONE
        }
    }
    var privPlanStap: ItemPlanStap? by instanceState() { cache, value ->
        if (value != null) {
            binding.nameParentPlanStap.text = value.name
            binding.buttUnselStap.isVisible = true
        } else {
            binding.nameParentPlanStap.text = "Привязать к этапу"
            binding.buttUnselStap.isVisible = false
        }
    }

    private fun addDenPlan(date: Long) {
        with(binding) {
            viewmodel.addTime.addDenPlan(
                vajn = vybStatDenPlan.selStat.toLong(),
                name = editNameDenPlanText.text.toString(),
                gotov = 0.0,
                data = date,
                time1 = timeStartPlan.timeStrHHmm,
                time2 = timeEndPlan.timeStrHHmm,
                opis = editOpisDenPlanText.text.toString(),
                privplan = privPlan?.id?.toLong() ?: -1,
                stap_prpl = privPlanStap?.id?.toLong() ?: -1,
            )
        }
    }

    private fun povtorFun(listener: (Date) -> Unit) {
        with(binding) {
            val count = etCountPovtor.text.toString().toInt()
            if (count > 0) {
                val cal = Calendar.getInstance().apply {
                    time = dateDenPlan.valueDate
                }
                val n = etNPovtor.text.toString().toInt()
                val ch: Array<Boolean> = arrayOf(
                    cbPn.isChecked,
                    cbVt.isChecked,
                    cbSr.isChecked,
                    cbCht.isChecked,
                    cbPt.isChecked,
                    cbSb.isChecked,
                    cbVs.isChecked
                )
                for (i in 1..count) {
                    when (rgSelectPovtorType.checkedRadioButtonId) {
                        rbDenPovtor.id -> {
                            listener(cal.time)
                            cal.add(Calendar.DATE, n)
                        }
                        rbNedelPovtor.id -> {
                            val dn: Int = cal.get(Calendar.DAY_OF_WEEK).let {
                                if (it - 1 == 0) 7 else it - 1
                            }
                            val addDate = Calendar.getInstance().apply {
                                time = cal.time
                            }
                            var k = 0
                            for (j in 1..7) {
                                val dd = if (dn + k > 7) dn + k - 7 else dn + k
                                if (ch[dd - 1]) {
                                    addDate.time = cal.time
                                    addDate.add(Calendar.DATE, k)
                                    listener(addDate.time)
                                }
                                k++
                            }
                            cal.add(Calendar.DATE, 7)
                        }
                        rbMonthPovtor.id -> {
                            listener(cal.time)
                            cal.add(Calendar.MONTH, n)
                        }
                    }
                }
            }
        }
    }

    override fun addNote() {
        with(binding) {
            if (!cbPovtorDenPlan.isChecked) {
                addDenPlan(dateDenPlan.dateLong)
            } else {
                viewmodel.addTime.executeDenPlanTransaction {
                    povtorFun { addDenPlan(it.time) }
                }
            }
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addTime.updDenPlan(
                    item = it,
                    vajn = vybStatDenPlan.selStat.toLong(),
                    name = editNameDenPlanText.text.toString(),
                    data = dateDenPlan.dateLong,
                    time1 = timeStartPlan.timeStrHHmm,
                    time2 = timeEndPlan.timeStrHHmm,
                    opis = editOpisDenPlanText.text.toString(),
                    privplan = privPlan?.id?.toLong() ?: -1,
                    stap_prpl = privPlanStap?.id?.toLong() ?: -1
                )
            }
        }
    }

    fun setPovtorOnOff(checked: Boolean, duration: Long = 0) {
        with(binding) {
            if (checked) {
                expand(clPovtorDenPlan, duration = duration)
                setTypePovtor(0)
            } else {
                collapse(clPovtorDenPlan, duration = duration)
            }
        }
    }

    private var rbNedel: Boolean? = null
    fun setTypePovtor(duration: Long = 0) {
        with(binding) {
            if (rbDenPovtor.isChecked || rbMonthPovtor.isChecked) {
                rbNedel?.let {
                    if (it) {
                        expand(layNPovtor, duration = duration)
                        collapse(layDniNedeli, duration = duration)
                        rbNedel = false
                    }
                } ?: run {
                    expand(layNPovtor, duration = duration)
                    collapse(layDniNedeli, duration = duration)
                    rbNedel = false
                }
            } else {
                expand(layDniNedeli, duration = duration)
                collapse(layNPovtor, duration = duration)
                rbNedel = true
            }
        }
    }

    val callbackKey = "SelectDenPlanShablon"
    val callbackKeySaveShab = "SaveDenPlanShablon"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            if (change) {
                collapse(cbPovtorDenPlan, duration = 0)
                collapse(clPovtorDenPlan, duration = 0)
                buttReplay.visibility = View.VISIBLE
                buttSaveShablon.visibility = View.GONE
                buttLoadShablon.visibility = View.GONE
            } else {
                buttReplay.visibility = View.GONE
                buttSaveShablon.visibility = View.VISIBLE
                buttLoadShablon.visibility = View.VISIBLE
            }

            changeObserve(viewLifecycleOwner) {
                if (it) {
                    collapse(cbPovtorDenPlan, duration = 0)
                    collapse(clPovtorDenPlan, duration = 0)
                    buttReplay.visibility = View.VISIBLE
                    buttSaveShablon.visibility = View.GONE
                    buttLoadShablon.visibility = View.GONE
                } else {
                    expand(cbPovtorDenPlan, duration = 200)
                    buttReplay.visibility = View.GONE
                    buttSaveShablon.visibility = View.VISIBLE
                    buttLoadShablon.visibility = View.VISIBLE
                }
            }


            buttReplay.setOnClickListener {
                change = false
            }

            buttInfoPovtor.setOnClickListener {
                var str = "Даты на которые будет добавлен план:\n"
                povtorFun { str += "${it.format("dd.MM.yyyy(EEE)")}\n" }
                showMyMessage(str, getSFM())
            }

            setPovtorOnOff(cbPovtorDenPlan.isChecked, 0)
            setTypePovtor(0)
            cbPovtorDenPlan.setOnCheckedChangeListener { buttonView, isChecked ->
                setPovtorOnOff(isChecked, 200)
            }
            rgSelectPovtorType.setOnCheckedChangeListener { group, checkedId ->
                setTypePovtor(200)
            }

            dateDenPlan.setPattern("dd MMM yyyy (EEE)")
            dateDenPlan.setDate(viewmodel.dateOporDp.value!!)

            if (firstStart) {
                item?.let {
                    if ((it.privplan != -1L) && (it.nameprpl != "")) {
                        privPlan = ItemPlan(it.privplan.toString(), it.nameprpl, 0, 0.0, 0L, 0L, "", 0, 0.0, 1)
                        if ((it.stap_prpl != -1L) && (it.namestap != "")) {
                            privPlanStap = ItemPlanStap(
                                1,
                                it.stap_prpl.toString(),
                                it.privplan.toString(),
                                it.namestap,
                                0.0,
                                0L,
                                0L,
                                "",
                                false,
                                0,
                                0.0,
                                1
                            )
                        } else {
                            privPlanStap = null
                        }
                    } else {
                        privPlan = null
                    }
                    vybStatDenPlan.selectStat(it.vajn.toInt())
                    editNameDenPlanText.setText(it.name)
                    editOpisDenPlanText.setText(it.opis)
                    timeStartPlan.setTimeHH_mm(it.time1)
                    timeEndPlan.setTimeHH_mm(it.time2)
                    dateDenPlan.setDate(it.data)
                } ?: run {
                    privPlan = null
                }
            } else {
                privPlan = privPlan
                privPlanStap = privPlanStap
            }
            buttUnselPlan.setOnClickListener {
                privPlan = null
            }
            buttUnselStap.setOnClickListener {
                privPlanStap = null
            }

            vybStatDenPlan.setTimeSquare()

            timeStartPlan.setPattern("Начало: HH:mm")
            timeEndPlan.setPattern("Завершение: HH:mm")
            timeStartPlan.observe(viewLifecycleOwner) {
                if (timeEndPlan.dateLong < timeStartPlan.dateLong) {
                    timeEndPlan.setPattern("Завершение: завтра HH:mm")
                } else {
                    timeEndPlan.setPattern("Завершение: HH:mm")
                }
            }
            timeEndPlan.observe(viewLifecycleOwner) {
                if (timeEndPlan.dateLong < timeStartPlan.dateLong) {
                    timeEndPlan.setPattern("Завершение: завтра HH:mm")
                } else {
                    timeEndPlan.setPattern("Завершение: HH:mm")
                }
            }

            SelectDenPlanShablonPanelDial.setRezListener(
                getSFM(),
                callbackKey,
                viewLifecycleOwner
            ) { itemShab, povtor, hours ->
                if ((itemShab.privplan != -1L) && (itemShab.nameprpl != "")) {
                    privPlan = ItemPlan(
                        id = itemShab.privplan.toString(),
                        name = itemShab.nameprpl,
                        0,
                        0.0,
                        0L,
                        0L,
                        "",
                        0,
                        0.0,
                        1
                    )
                    if ((itemShab.stap_prpl != -1L) && (itemShab.namestap != "")) {
                        privPlanStap = ItemPlanStap(
                            1,
                            id = itemShab.stap_prpl.toString(),
                            parent_id = itemShab.privplan.toString(),
                            name = itemShab.namestap,
                            0.0,
                            0L,
                            0L,
                            "",
                            false,
                            0,
                            0.0,
                            1
                        )
                    } else {
                        privPlanStap = null
                    }
                } else {
                    privPlan = null
                }
                vybStatDenPlan.selectStat(itemShab.vajn.toInt())
                editNameDenPlanText.setText(itemShab.namepl)
                editOpisDenPlanText.setText(itemShab.opis)
                if (hours) {
                    timeStartPlan.setTimeHH_mm(itemShab.time1)
                    timeEndPlan.setTimeHH_mm(itemShab.time2)
                }
                if (povtor) {
                    if (itemShab.povtor != "0") {
                        val povtorList: List<String> = itemShab.povtor.split(";")
                        if (povtorList.count() == 10) {
                            cbPovtorDenPlan.isChecked = true
                            etCountPovtor.setText(povtorList[0])
                            etNPovtor.setText(povtorList[1])
                            cbPn.isChecked = if (povtorList[2] == "true") true else false
                            cbVt.isChecked = if (povtorList[3] == "true") true else false
                            cbSr.isChecked = if (povtorList[4] == "true") true else false
                            cbCht.isChecked = if (povtorList[5] == "true") true else false
                            cbPt.isChecked = if (povtorList[6] == "true") true else false
                            cbSb.isChecked = if (povtorList[7] == "true") true else false
                            cbVs.isChecked = if (povtorList[8] == "true") true else false
                            when (povtorList[9]) {
                                "0" -> rbDenPovtor.isChecked = true
                                "1" -> rbNedelPovtor.isChecked = true
                                "2" -> rbMonthPovtor.isChecked = true
                            }
                        } else {
                            cbPovtorDenPlan.isChecked = false
                        }
                    } else {
                        cbPovtorDenPlan.isChecked = false
                    }
                }
            }
            FragmentOneVoprosStr.setRezListener(
                this@TimeAddDenPlanPanelFragment,
                callbackKeySaveShab)
                {
                    with(binding) {
                        var povt = ""
                        val count = etCountPovtor.text.toString().toInt()
                        if ((count > 0) && (cbPovtorDenPlan.isChecked)) {
                            povt = "$count;${etNPovtor.text};"
                            var rbnum = 0
                            val ch: Array<Boolean> = arrayOf(
                                cbPn.isChecked,
                                cbVt.isChecked,
                                cbSr.isChecked,
                                cbCht.isChecked,
                                cbPt.isChecked,
                                cbSb.isChecked,
                                cbVs.isChecked
                            )
                            for (i in 0..6) {
                                povt = "$povt${if (ch[i]) "true" else "false"};"
                            }
                            when (rgSelectPovtorType.checkedRadioButtonId) {
                                rbDenPovtor.id -> {
                                    rbnum = 0
                                }
                                rbNedelPovtor.id -> {
                                    rbnum = 1
                                }
                                rbMonthPovtor.id -> {
                                    rbnum = 2
                                }
                            }
                            povt = "$povt$rbnum"
                        } else {
                            povt = "0"
                        }



                        viewmodel.addTime.addShablonDenPlan(
                            name = it,
                            namepl = editNameDenPlanText.text.toString(),
                            opis = editOpisDenPlanText.text.toString(),
                            vajn = vybStatDenPlan.selStat.toLong(),
                            time1 = timeStartPlan.timeStrHHmm,
                            time2 = timeEndPlan.timeStrHHmm,
                            privplan = privPlan?.id?.toLong() ?: -1,
                            stap_prpl = privPlanStap?.id?.toLong() ?: -1,
                            povtor = povt
                        )
                    }
                }

            buttSaveShablon.setOnClickListener {
                if (editNameDenPlanText.text.toString() != "") {
                    showMyFragDial(
                        FragmentOneVoprosStr(
                            "Введите название нового шаблона:",
                            "Название шаблона",
                            "Сохранить",
                            editNameDenPlanText.text.toString(),
                            callbackKeySaveShab
                        ), getSFM(), bound = MyFragDial.BoundSlide.right
                    )
                } else {
                    showMyMessage("Введите вначале имя плана", getSFM())
                }
            }
            buttLoadShablon.setOnClickListener {
                showMyFragDial(SelectDenPlanShablonPanelDial(callbackKey), getSFM(), bound = MyFragDial.BoundSlide.top)
            }
            val selPlanPan = SelectedPlanPanel(this@TimeAddDenPlanPanelFragment, "callbackSelPrivPlanForDenPlan") {
                privPlan = it
            }
            clSelectedDenplanPlan.setOnClickListener {
                selPlanPan.showMenu(privPlan)
            }
            val callbackSelPrivStapPlan = "callbackSelPrivStapPlanForDenPlan"
            SelectedPlanStapPanelFragment.setRezListener(
                this@TimeAddDenPlanPanelFragment,
                callbackSelPrivStapPlan
            ) { itplst ->
                privPlanStap = itplst
            }
            clSelectedDenplanPlanStap.setOnClickListener {
                privPlan?.let {
                    showMyFragDial(
                        SelectedPlanStapPanelFragment(it, privPlanStap, callbackKey = callbackSelPrivStapPlan),
                        bound = MyFragDial.BoundSlide.top
                    )
                }
            }
        }
    }

}