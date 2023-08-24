package ru.ragefalcon.tutatores.ui.finance.deprecated

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.SpinnerAdapter
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.tutatores.adapter.FinanceType
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.databinding.FragmentAddpanelFinanceBinding
import ru.ragefalcon.tutatores.extensions.format
import ru.ragefalcon.tutatores.extensions.hideKeyboard
import ru.ragefalcon.tutatores.extensions.nowDateWithoutTimeCalendar
import ru.ragefalcon.tutatores.extensions.toLong
import ru.ragefalcon.tutatores.ui.finance.FinanceMainScreen
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*

/*
class FinanceAddPanelFragment() : Fragment() {

    private lateinit var spinRTAdapter: SpinnerAdapter
    private lateinit var spinDTAdapter: SpinnerAdapter
    private lateinit var spinPSAdapter: SpinnerAdapter

    var dateAndTime = nowDateWithoutTimeCalendar()

    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels()

    private var _binding: FragmentAddpanelFinanceBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddpanelFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinPSAdapter = TypeRasxodAdapter(requireContext(), objects = listOf())
        spinDTAdapter = TypeRasxodAdapter(requireContext(), objects = listOf())
        spinRTAdapter = TypeRasxodAdapter(requireContext(), objects = listOf())
        with(binding) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
//                1TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (stateViewModel.currentFinType == FinanceType.SCHET) {
                        if (viewmodel.financeFun.sravnVal((spinner.selectedItem as Pair<String, String>).first.toLong()) != null) {
//                    Log.d("MyTag","${(spinner.selectedItem as Pair<String,String>).first.toLong()}")
                            editSummaZach.visibility = View.VISIBLE
                            editSummaZach.hint =
                                "Сумма зачисления(${viewmodel.financeFun.sravnVal((spinner.selectedItem as Pair<String, String>).first.toLong())})"
                        } else {
                            editSummaZach.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            buttCancel.setOnClickListener {
                hideAddLayout(50)
            }

            buttAddRecord.setOnClickListener {
                try {
                    hideKeyboard()
                    val sd = spinner.selectedItem as Pair<String, String>
                    val ssch = srSchetAdd.selectedItem as Pair<String, String>
                    with(viewmodel) {
                        when (stateViewModel.currentFinType) {
                            FinanceType.RASXOD -> {
                                if (stateViewModel.changeItemCommonFinOper == null) {
                                    addFin.addRasxod(
                                        name = editNameText.text.toString(),
                                        summa = editSumma.editText?.text.toString().toDouble() ?: 0.0,
                                        typeid = sd.first.toLong(),
                                        data = dateAndTime.toLong(), //.toLongPlusOffset(),
                                        schet = ssch.first.toLong()
                                    )
                                } else {
                                    addFin.updRasxod(
                                        id = stateViewModel.changeItemCommonFinOper!!.id.toLong(),
                                        name = editNameText.text.toString(),
                                        summa = editSumma.editText?.text.toString().toDouble() ?: 0.0,
                                        typeid = sd.first.toLong(),
                                        data = dateAndTime.toLong(), //.toLongPlusOffset(),
                                        schet = ssch.first.toLong()
                                    )

                                }
                            }

                            FinanceType.DOXOD -> {
                                if (stateViewModel.changeItemCommonFinOper == null) {
                                    addFin.addDoxod(
                                        name = editNameText.text.toString(),
                                        summa = editSumma.editText?.text.toString().toDouble() ?: 0.0,
                                        typeid = sd.first.toLong(),
                                        data = dateAndTime.toLong(),//.toLongPlusOffset(),
                                        schet = ssch.first.toLong()
                                    )
                                } else {
                                    addFin.updDoxod(
                                        id = stateViewModel.changeItemCommonFinOper!!.id.toLong(),
                                        name = editNameText.text.toString(),
                                        summa = editSumma.editText?.text.toString().toDouble() ?: 0.0,
                                        typeid = sd.first.toLong(),
                                        data = dateAndTime.toLong(),//.toLongPlusOffset(),
                                        schet = ssch.first.toLong()
                                    )
                                }
                            }
                            FinanceType.SCHET -> {
                                addFin.addPerevod(
                                    name = editNameText.text.toString(),
                                    schsp_id = financeFun.getPosMainSchet().first.toLong(),
                                    sumsp = editSumma.editText?.text.toString().toDouble() ?: 0.0,
                                    schz_id = sd.first.toLong(),
                                    sumz = if (editSummaZach.isVisible) {
                                        editSummaZach.editText?.text.toString().toDouble() ?: 0.0
                                    } else {
                                        editSumma.editText?.text.toString().toDouble() ?: 0.0
                                    },
                                    data = dateAndTime.toLong()
                                )
//                            mDB.sumAllCap.update()
                            }
                        }
                    }
                    ivBackaddlay.setColorFilter(Color.argb(255, 0, 156, 38))
                    hideAddLayout(350)
                } catch (ignored: ClassCastException) {
                }
            }

            etDate.setOnClickListener {
                DatePickerDialog(requireContext(),
                    { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        dateAndTime.set(year, month, dayOfMonth)
                        etDate.text = dateAndTime.time.format("dd MMM yyyy (EEE)")
                    }
//          !!! вместо того что ниже
//                object : DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        dateAndTime.set(year,month,dayOfMonth)
//                        etDate.text=dateAndTime.time.format("dd MMMM yyyy  HH:mm:ss:sss")
//                    }
//                }
                    ,
                    dateAndTime.get(Calendar.YEAR),
                    dateAndTime.get(Calendar.MONTH),
                    dateAndTime.get(Calendar.DAY_OF_MONTH))
                    .show()
            }

            buttDateLeft.setOnClickListener {
                dateAndTime.add(Calendar.DAY_OF_MONTH, -1)
                etDate.text = dateAndTime.time.format("dd MMM yyyy (EEE)")
            }
            buttDateRight.setOnClickListener {
                dateAndTime.add(Calendar.DAY_OF_MONTH, 1)
                etDate.text = dateAndTime.time.format("dd MMM yyyy (EEE)")
            }
            initViewModel()
            stateViewModel.visAddFinPanel.observe(viewLifecycleOwner) { it ->
                if (it) {
                    dateAndTime.time = viewmodel.dateOpor.value
                    etDate.text = dateAndTime.time.format("dd MMM yyyy (EEE)")
                    when (stateViewModel.currentFinType) {
                        FinanceType.RASXOD -> {
                            editSummaZach.visibility = View.INVISIBLE
                            srSchetAdd.visibility = View.VISIBLE
                            editName.hint = "Название расхода"
                            editSumma.hint = "Сумма расхода"
                            spinner.adapter = spinRTAdapter
                        }
                        FinanceType.DOXOD -> {
                            editSummaZach.visibility = View.INVISIBLE
                            srSchetAdd.visibility = View.VISIBLE
                            editName.hint = "Название дохода"
                            editSumma.hint = "Сумма дохода"
                            spinner.adapter = spinDTAdapter
                        }
                        FinanceType.SCHET -> {
                            srSchetAdd.visibility = View.INVISIBLE
                            editName.hint = "Комментарий к переводу"
                            editNameText.setText("Перевод")
                            editSumma.hint = "Сумма перевода"
                            spinner.adapter = spinPSAdapter

                        }
                    }

                    addLayout.alpha = 0f
                    addLayout.visibility = android.view.View.VISIBLE

                    ivBackaddlay.clearColorFilter()
                    addLayout.let {
                        ViewCompat.animate(addLayout)
                            .alpha(1f)
                            .setDuration(300)
                            .setInterpolator(AccelerateDecelerateInterpolator())
                            .setStartDelay(50)
                    }
                    stateViewModel.changeItemCommonFinOper.let { itRasx ->
                        if (itRasx != null) {
                            buttAddRecord.text = "Изменить"
                            editNameText.setText(itRasx.name)
                            editSummaText.setText(itRasx.summa.roundToString(2))
                            dateAndTime.time = Date(itRasx.data)
                            etDate.text = dateAndTime.time.format("dd MMM yyyy (EEE)")
                            var k = (spinner.adapter as TypeRasxodAdapter).getPosition(Pair(itRasx.typeid, itRasx.type))
                            spinner.setSelection(k)
                            spinner.refreshDrawableState()
                            val k2 =
                                (srSchetAdd.adapter as TypeRasxodAdapter).getPosition((srSchetAdd.adapter as TypeRasxodAdapter).objects?.filter {
                                    it.first == itRasx.schetid
                                }?.first())
                            srSchetAdd.setSelection(k2)
                            srSchetAdd.refreshDrawableState()
                        } else {
                            buttAddRecord.text = "Добавить"
                            editNameText.setText("")
                            editSummaText.setText("")
                            if ((srSchetAdd.adapter as TypeRasxodAdapter).objects?.count() ?: -1 > 0) {
                                srSchetAdd.setSelection(0)
                            }
                        }
                    }
                }

            }
        }

    }

    private fun initViewModel() {
        with(viewmodel) {
            finSpis.spisTypeRasx.observe(viewLifecycleOwner) {
                spinRTAdapter = TypeRasxodAdapter(requireContext(), objects = it)
            }
            finSpis.spisTypeDox.observe(viewLifecycleOwner) {
                spinDTAdapter = TypeRasxodAdapter(requireContext(), objects = it)
            }
            finSpis.spisSchet.observe(viewLifecycleOwner) {
                binding.srSchetAdd.adapter = TypeRasxodAdapter(requireContext(), it.map { Pair(it.id,it.name) })
            }
            finSpis.spisSchetKrome.observe(viewLifecycleOwner) {           //this@FinanceMainScreen
                spinPSAdapter = TypeRasxodAdapter(requireContext(), it)
            }
        }
    }

    private fun hideAddLayout(delay: Long) {
        ViewCompat.animate(binding.addLayout)
            .alpha(0f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setStartDelay(delay)
            .setListener(FinanceMainScreen.MyVPAL())
        stateViewModel.visAddFinPanel.value = false
    }

}*/
