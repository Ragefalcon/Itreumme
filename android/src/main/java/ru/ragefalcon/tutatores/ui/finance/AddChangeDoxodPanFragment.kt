package ru.ragefalcon.tutatores.ui.finance

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemDoxod
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.commonfragments.MyFragDial
import ru.ragefalcon.tutatores.commonfragments.OneVoprosStrDial
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeFinBinding
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.showMyMessage
import java.lang.ref.WeakReference

class AddChangeDoxodPanFragment(item: ItemDoxod? = null) :
    FragAddChangeDialHelper<ItemDoxod, FragmentAddChangeFinBinding>(
        FragmentAddChangeFinBinding::inflate, item
    ) {

    override fun addNote() {
        with(binding) {
            try {
                editSumma.editText?.text.toString().toDoubleOrNull()?.let {
                    val sd = spinner.selectedItem as Pair<String, String>
                    val ssch = srSchetAdd.selectedItem as Pair<String, String>
                    viewmodel.addFin.addDoxod(
                        name = editNameText.text.toString(),
                        summa = it,
                        typeid = sd.first.toLong(),
                        data = etDate.dateLong,
                        schet = ssch.first.toLong()
                    )
                }
            } catch (ignored: ClassCastException) {
            }
        }
    }

    override fun changeNote() {
        with(binding) {
            try {
                editSumma.editText?.text.toString().toDoubleOrNull()?.let { sum ->
                    val sd = spinner.selectedItem as Pair<String, String>
                    val ssch = srSchetAdd.selectedItem as Pair<String, String>
                    item?.let {
                        viewmodel.addFin.updDoxod(
                            item = it,
                            name = editNameText.text.toString(),
                            summa = sum,
                            typeid = sd.first.toLong(),
                            data = etDate.dateLong,
                            schet = ssch.first.toLong()
                        )
                    }
                }
            } catch (ignored: ClassCastException) {
            }
        }
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
                (srSchetAdd.adapter as TypeRasxodAdapter).objects?.find {
                    it.first == itRasx.schetid
                }?.let {
                    val k2 =
                        (srSchetAdd.adapter as TypeRasxodAdapter).getPosition(it)
                    srSchetAdd.setSelection(k2)
                    srSchetAdd.refreshDrawableState()
                }

            }
        }
    }

    private fun initViewModel() {
        with(viewmodel) {
            binding.spinner.adapter = TypeRasxodAdapter(requireContext(), listOf())
            binding.srSchetAdd.adapter = TypeRasxodAdapter(requireContext(), listOf())
            finSpis.spisTypeDox.observe(viewLifecycleOwner) {
                binding.spinner.adapter = TypeRasxodAdapter(requireContext(), it)
                startSpinner
            }
            finSpis.spisSchet.observe(viewLifecycleOwner) {
                binding.srSchetAdd.adapter = TypeRasxodAdapter(requireContext(), it.map { Pair(it.id, it.name) })
                startSrSchetAdd
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            if (change) {
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
                    buttReplay.visibility = View.VISIBLE
                    buttSaveShablon.visibility = View.GONE
                    buttLoadShablon.visibility = View.GONE
                } else {
                    buttReplay.visibility = View.GONE
                    buttSaveShablon.visibility = View.VISIBLE
                    buttLoadShablon.visibility = View.VISIBLE
                }
            }


            buttReplay.setOnClickListener {
                change = false
            }

            val addFinShablon = OneVoprosStrDial(
                WeakReference(this@AddChangeDoxodPanFragment), "voprosNameFinShablon", listener = {
                val sd = spinner.selectedItem as Pair<String, String>
                val ssch = srSchetAdd.selectedItem as Pair<String, String>
                viewmodel.addFin.addShabDoxod(
                    name = it,
                    nameoper = editNameText.text.toString(),
                    summa = editSumma.editText?.text.toString().toDouble() ?: 0.0,
                    type = sd.second,
                    schet_id = ssch.first.toLong()
                )
            }, listener_cancel = {

            })
            buttSaveShablon.setOnClickListener {
                if ((editNameText.text.toString() != "") && (editSummaText.text.toString() != "")) {
                    addFinShablon.showVopros(
                        "Введите название нового шаблона:",
                        "Название шаблона",
                        "Сохранить",
                        editNameText.text.toString(),
                        bound = MyFragDial.BoundSlide.right
                    )
                } else {
                    showMyMessage("Введите вначале имя и сумму расхода", getSFM())
                }
            }

            val selShabPan = SelectShablonDoxodPanel(this@AddChangeDoxodPanFragment, "selShablonDoxod") {
                editNameText.setText(it.nameoper)
                editSummaText.setText(it.summa.roundToString(2))

                (spinner.adapter as TypeRasxodAdapter).objects?.find { itF ->
                    itF.second == it.type
                }?.let { itPair ->
                    val k = (spinner.adapter as TypeRasxodAdapter).getPosition(itPair)
                    spinner.setSelection(k)
                    spinner.refreshDrawableState()
                }

                (srSchetAdd.adapter as TypeRasxodAdapter).objects?.find { itF ->
                    itF.first == it.schet_id.toString()
                }?.let { itPair ->
                    val k2 = (srSchetAdd.adapter as TypeRasxodAdapter).getPosition(itPair)
                    srSchetAdd.setSelection(k2)
                    srSchetAdd.refreshDrawableState()
                }
            }

            buttLoadShablon.setOnClickListener {
                selShabPan.showMenu()
            }

            etDate.setPattern("dd MMM yyyy (EEE)")
            etDate.setDate(viewmodel.dateOpor.value!!)
            editSummaZach.visibility = View.GONE
            srSchetAdd.visibility = View.VISIBLE
            editName.hint = "Название дохода"
            editSumma.hint = "Сумма дохода"

            initViewModel()
            item?.let { itRasx ->
                editNameText.setText(itRasx.name)
                editSummaText.setText(itRasx.summa.roundToString(2))
                etDate.setDate(itRasx.data)
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