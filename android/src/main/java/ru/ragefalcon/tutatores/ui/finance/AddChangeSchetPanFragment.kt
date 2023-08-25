package ru.ragefalcon.tutatores.ui.finance

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeFinBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage

class AddChangeSchetPanFragment(item: ItemCommonFinOper? = null) :
    FragAddChangeDialHelper<ItemCommonFinOper, FragmentAddChangeFinBinding>(
        FragmentAddChangeFinBinding::inflate, item
    ) {

    override fun addNote() {
        with(binding) {
            try {
                editSumma.editText?.text.toString().toDoubleOrNull()?.let { sumsp ->
                    val sumz: Double?
                    if (editSummaZach.isVisible) {
                        sumz = editSummaZach.editText?.text.toString().toDoubleOrNull()
                    } else {
                        sumz = sumsp
                    }
                    sumz?.let { sumzach ->
                        val sd = spinner.selectedItem as Pair<String, String>
                        val ssch = srSchetAdd.selectedItem as Pair<String, String>
                        viewmodel.addFin.addPerevod(
                            name = editNameText.text.toString(),
                            schsp_id = viewmodel.financeFun.getPosMainSchet().first.toLong(),
                            sumsp = sumsp,
                            schz_id = sd.first.toLong(),
                            sumz = sumzach,
                            data = etDate.dateLong
                        )
                    } ?: run {
                        showMyMessage("Перевод не записан, т.к. не указана сумма зачисления")
                    }
                }
            } catch (ignored: ClassCastException) {
            }
        }
    }

    override fun changeNote() {
    }

    val startSpinner by lazy {
        with(binding) {
            item?.let { itRasx ->
                val k = (spinner.adapter as TypeRasxodAdapter).getPosition(Pair(itRasx.typeid, itRasx.type))
                spinner.setSelection(k)
                spinner.refreshDrawableState()
            }
        }
    }
    val startSrSchetAdd by lazy {
        with(binding) {
            item?.let { itRasx ->
                val k2 =
                    (srSchetAdd.adapter as TypeRasxodAdapter).getPosition((srSchetAdd.adapter as TypeRasxodAdapter).objects?.filter {
                        it.first == itRasx.schetid
                    }?.firstOrNull())
                srSchetAdd.setSelection(k2)
                srSchetAdd.refreshDrawableState()

            }
        }
    }

    private fun initViewModel() {
        with(viewmodel) {
            binding.spinner.adapter = TypeRasxodAdapter(requireContext(), listOf())
            finSpis.spisSchetKrome.observe(viewLifecycleOwner) {
                binding.spinner.adapter = TypeRasxodAdapter(requireContext(), it)
                startSpinner
            }
            binding.srSchetAdd.adapter = TypeRasxodAdapter(requireContext(), listOf())
            finSpis.spisSchet.observe(viewLifecycleOwner) {
                binding.srSchetAdd.adapter = TypeRasxodAdapter(requireContext(), it.map { Pair(it.id, it.name) })
                startSrSchetAdd
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            buttReplay.visibility = View.GONE
            buttSaveShablon.visibility = View.GONE
            buttLoadShablon.visibility = View.GONE

            etDate.setPattern("dd MMM yyyy (EEE)")
            etDate.setDate(viewmodel.dateOpor.value!!)
            srSchetAdd.visibility = View.GONE
            editName.hint = "Комментарий к переводу"
            editNameText.setText("Перевод")
            editSumma.hint = "Сумма перевода"

            initViewModel()
            item?.let { itRasx ->
                editNameText.setText(itRasx.name)
                editSummaText.setText(itRasx.summa.roundToString(2))
                etDate.setDate(itRasx.data)
            }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (viewmodel.financeFun.sravnVal((spinner.selectedItem as Pair<String, String>).first.toLong()) != null) {
                        editSummaZach.visibility = View.VISIBLE
                        editSummaZach.hint =
                            "Сумма зачисления(${viewmodel.financeFun.sravnVal((spinner.selectedItem as Pair<String, String>).first.toLong())})"
                    } else {
                        editSummaZach.visibility = View.INVISIBLE
                    }
                }
            }

            buttDateLeft.setOnClickListener {
                etDate.addDays(-1)
            }
            buttDateRight.setOnClickListener {
                etDate.addDays(1)
            }
        }
    }

}